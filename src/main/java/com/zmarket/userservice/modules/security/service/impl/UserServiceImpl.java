package com.zmarket.userservice.modules.security.service.impl;

import com.zmarket.userservice.dtos.BaseRequest;
import com.zmarket.userservice.exceptions.BadRequestException;
import com.zmarket.userservice.exceptions.ForbiddenException;
import com.zmarket.userservice.exceptions.NotFoundException;
import com.zmarket.userservice.exceptions.UnathorizedException;
import com.zmarket.userservice.modules.otp.enums.OTPChannel;
import com.zmarket.userservice.modules.otp.service.OTPService;
import com.zmarket.userservice.modules.security.dto.LoginRequest;
import com.zmarket.userservice.modules.security.dto.LoginResponse;
import com.zmarket.userservice.modules.security.dto.RegistrationRequest;
import com.zmarket.userservice.modules.security.dto.RegistrationResponse;
import com.zmarket.userservice.modules.security.dto.ResetPasswordRequest;
import com.zmarket.userservice.modules.security.dto.SocialRequest;
import com.zmarket.userservice.modules.security.dto.ThirdPartyUser;
import com.zmarket.userservice.modules.security.dto.UpdatePasswordRequest;
import com.zmarket.userservice.modules.security.dto.UpdateProfileRequest;
import com.zmarket.userservice.modules.security.enums.AuthProvider;
import com.zmarket.userservice.modules.security.enums.platform;
import com.zmarket.userservice.modules.security.jwt.TokenProvider;
import com.zmarket.userservice.modules.security.model.Authority;
import com.zmarket.userservice.modules.security.model.AuthorityName;
import com.zmarket.userservice.modules.security.model.User;
import com.zmarket.userservice.modules.security.repository.AuthorityRepository;
import com.zmarket.userservice.modules.security.repository.UserRepository;
import com.zmarket.userservice.modules.security.service.UserService;
import com.zmarket.userservice.util.CustomAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.zmarket.userservice.modules.security.model.AuthorityName.ROLE_ADMIN;
import static com.zmarket.userservice.modules.security.model.AuthorityName.ROLE_BRAND_SELLER;
import static com.zmarket.userservice.modules.security.model.AuthorityName.ROLE_MARKET_SELLER;
import static com.zmarket.userservice.modules.security.model.AuthorityName.ROLE_USER;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final OTPService otpService;
    private final GoogleClient googleClient;
    private final TokenProvider tokenProvider;
    private final FacebookClient facebookClient;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final AuthenticationManager authenticationManager;


    @Override
    public RegistrationResponse registerUser(RegistrationRequest dto) {
        Optional<User> optionalUser = userRepository.findFirstByEmail(dto.getEmail());
        if (optionalUser.isPresent()) {
            upgradeUserAuthority(optionalUser.get(), dto.getPlatform());
            return new RegistrationResponse(optionalUser.get().isActivated());
        }

        User user = createUser(dto, getRoles(dto.getPlatform()));
        otpService.sendOTP(user, OTPChannel.EMAIL);
        return new RegistrationResponse(false);
    }

    @Override
    public Object resendCode(BaseRequest baseRequest) {
        User user = userRepository.findFirstByEmail(baseRequest.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        otpService.sendOTP(user, OTPChannel.EMAIL);
        return new Object();
    }

    @Override
    public LoginResponse activateAccount(String otp, BaseRequest baseRequest) {
        User user = userRepository.findFirstByEmail(baseRequest.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        boolean success = otpService.verifyOTP(user.getEmail(), otp);
        if (!success) {
            throw new BadRequestException("invalid token");
        }

        user.setEnabled(true);
        user.setActivated(true);
        user = userRepository.save(user);

        CustomAuthenticationToken authenticationToken = new CustomAuthenticationToken(user.getEmail(), AuthProvider.local);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false);
        return new LoginResponse(user, jwt);
    }

    @Override
    public Object resetPassword(BaseRequest baseRequest) {
        User user = userRepository.findFirstByEmail(baseRequest.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        otpService.sendOTP(user, OTPChannel.EMAIL);
        return new Object();
    }



    @Override
    public Object completeResetPassword(ResetPasswordRequest request) {
        User user = userRepository.findFirstByEmail(request.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        boolean success = otpService.verifyOTP(user.getEmail(), request.getOtp());
        if (!success) {
            throw new BadRequestException("invalid token");
        }
        String encodePassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodePassword);

        userRepository.save(user);

        return new Object();
    }

    @Override
    public User updatePassword(UpdatePasswordRequest request) {

        User user = getCurrentUser();

        if (!Objects.equals(request.getConfirmPassword(), request.getNewPassword())) {
            throw new BadRequestException("Password does not match");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        String encodePassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodePassword);

        user = userRepository.save(user);

        return user;
    }

    @Override
    public LoginResponse login(LoginRequest dto) {

        User user = userRepository.findFirstByEmail(dto.getEmail()).orElseThrow(()->new NotFoundException("User not found"));
        Optional<Authority> authority = user.getAuthorities().stream().filter(m -> m.getName() == getRole(dto.getPlatform())).findFirst();
        if (authority.isEmpty()) {
            throw new BadRequestException("You can not login into this platform, please kindly create an account or contact support");
        }

        if (!user.isActivated()) {
            throw new UnathorizedException("Email not verified");
        }

        if (!user.isEnabled()) {
            throw new ForbiddenException("Account suspended. Please contact support");
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = dto.getRememberMe() != null && dto.getRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);

        user.setLastLoginDate(LocalDateTime.now());
        user = userRepository.save(user);

        return new LoginResponse(user, jwt);

    }

    @Override
    public User getUser() {
        return getCurrentUser();
    }

    @Override
    public User updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();

        boolean updated = false;

        if (Objects.nonNull(request.getFirstName())) {
            user.setFirstname(request.getFirstName());
            updated = true;
        }

        if (Objects.nonNull(request.getLastName())) {
            user.setLastname(request.getLastName());
            updated = true;
        }

        if (Objects.nonNull(request.getGender())) {
            user.setGender(request.getGender());
            updated = true;
        }

        if (Objects.nonNull(request.getPhoneNumber())) {
            user.setPhone(request.getPhoneNumber());
            updated = true;
        }

        if (!updated) {
            throw new BadRequestException("No new update");
        }

        user = userRepository.save(user);
        return user;
    }

    @Override
    public LoginResponse registerUserWithGoogle(SocialRequest request) {
        ThirdPartyUser thirdPartyUser = googleClient.getThirdPartyUser(request.getAccessToken());
        if (thirdPartyUser == null) {
            throw new BadRequestException("Invalid token");
        }

        Optional<User> optionalUser = userRepository.findFirstByEmail(thirdPartyUser.getEmail());
        User user;
        if (optionalUser.isPresent()) {
            upgradeUserAuthority(optionalUser.get(), request.getPlatform());
            user = optionalUser.get();
        } else {
            user = createUser(thirdPartyUser, getRoles(request.getPlatform()));
        }

        CustomAuthenticationToken authenticationToken = new CustomAuthenticationToken(request.getAccessToken(), AuthProvider.google);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false);
        return new LoginResponse(user, jwt);    }

    @Override
    public LoginResponse registerUserWithFacebook(SocialRequest request) {
        ThirdPartyUser thirdPartyUser = facebookClient.getThirdPartyUser(request.getAccessToken());
        if (thirdPartyUser == null) {
            throw new BadRequestException("Invalid token");
        }
        Optional<User> optionalUser = userRepository.findFirstByEmail(thirdPartyUser.getEmail());
        User user;
        if (optionalUser.isPresent()) {
            upgradeUserAuthority(optionalUser.get(), request.getPlatform());
            user = optionalUser.get();
        } else {
            user = createUser(thirdPartyUser, getRoles(request.getPlatform()));
        }
        CustomAuthenticationToken authenticationToken = new CustomAuthenticationToken(request.getAccessToken(), AuthProvider.facebook);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false);
        return new LoginResponse(user, jwt);        }

    @Override
    public LoginResponse loginGoogle(SocialRequest request) {
        ThirdPartyUser thirdPartyUser = googleClient.getThirdPartyUser(request.getAccessToken());
        if (thirdPartyUser == null) {
            throw new BadRequestException("Invalid token");
        }

        User user = userRepository.findFirstByEmail(thirdPartyUser.getEmail()).orElseThrow(()-> new NotFoundException("User not found"));
        Optional<Authority> authority = user.getAuthorities().stream().filter(m -> m.getName() == getRole(request.getPlatform())).findFirst();
        if (authority.isEmpty()) {
            throw new BadRequestException("You can not login into this platform, please kindly create an account or contact support");
        }

        if (!user.isActivated()) {
            throw new UnathorizedException("Email not verified");
        }

        if (!user.isEnabled()) {
            throw new ForbiddenException("Account suspended. Please contact support");
        }

        CustomAuthenticationToken authenticationToken = new CustomAuthenticationToken(request.getAccessToken(), AuthProvider.google);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false);

        user.setLastLoginDate(LocalDateTime.now());
        user = userRepository.save(user);

        return new LoginResponse(user, jwt);
    }

    @Override
    public LoginResponse loginFacebook(SocialRequest request) {
        ThirdPartyUser thirdPartyUser = facebookClient.getThirdPartyUser(request.getAccessToken());
        if (thirdPartyUser == null) {
            throw new BadRequestException("Invalid token");
        }

        User user = userRepository.findFirstByEmail(thirdPartyUser.getEmail()).orElseThrow(()-> new NotFoundException("User not found"));

        Optional<Authority> authority = user.getAuthorities().stream().filter(m -> m.getName() == getRole(request.getPlatform())).findFirst();
        if (authority.isEmpty()) {
            throw new BadRequestException("You can not login into this platform, please kindly create an account or contact support");
        }
        if (!user.isActivated()) {
            throw new UnathorizedException("Email not verified");
        }

        if (!user.isEnabled()) {
            throw new ForbiddenException("Account suspended. Please contact support");
        }

        CustomAuthenticationToken authenticationToken = new CustomAuthenticationToken(request.getAccessToken(), AuthProvider.facebook);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false);

        user.setLastLoginDate(LocalDateTime.now());
        user = userRepository.save(user);

        return new LoginResponse(user, jwt);
    }

    private void upgradeUserAuthority(User user, platform platform) {
        AuthorityName authorityName = null;

        switch (platform) {
            case BRAND_SELLER -> authorityName = ROLE_BRAND_SELLER;
            case MARKET_SELLER -> authorityName = ROLE_MARKET_SELLER;
            case ADMIN -> authorityName = ROLE_ADMIN;
        }

        AuthorityName finalAuthorityName = authorityName;
        Optional<Authority> brandAuthority = user.getAuthorities().stream().filter(m -> m.getName() == finalAuthorityName).findFirst();
        if (brandAuthority.isPresent()) {
            return;
        }

        Authority additional = authorityRepository.findByName(authorityName).orElseThrow(() -> new BadRequestException("Unable to complete Registration at the moment"));

        if (Objects.nonNull(additional)) {
            user.getAuthorities().add(additional);
            userRepository.save(user);
        }
    }

    private Set<Authority> getRoles(platform platform) {
        Set<Authority> authorities = new HashSet<>();
        Authority userAuthority = authorityRepository.findByName(AuthorityName.ROLE_USER).orElseThrow(() ->new BadRequestException("Unable to complete Registration at the moment"));
        authorities.add(userAuthority);
        Authority additionalrole = null;
        switch (platform) {

            case BRAND_SELLER -> additionalrole = authorityRepository.findByName(ROLE_BRAND_SELLER).orElseThrow(() ->new BadRequestException("Unable to complete Registration at the moment"));
            case MARKET_SELLER -> additionalrole = authorityRepository.findByName(AuthorityName.ROLE_MARKET_SELLER).orElseThrow(() ->new BadRequestException("Unable to complete Registration at the moment"));
            case ADMIN -> additionalrole = authorityRepository.findByName(AuthorityName.ROLE_ADMIN).orElseThrow(() ->new BadRequestException("Unable to complete Registration at the moment"));

        }

        if (Objects.nonNull(additionalrole)) {
            authorities.add(additionalrole);
        }

        return authorities;
    }


    private AuthorityName getRole(platform platform) {

        switch (platform) {

            case BRAND_SELLER -> {
                return ROLE_BRAND_SELLER;
            }
            case MARKET_SELLER -> {
                return ROLE_MARKET_SELLER;
            }
            case ADMIN -> {
                return ROLE_ADMIN;
            }

        }

        return ROLE_USER;
    }


    private User createUser(RegistrationRequest dto, Set<Authority> authorities) {

        User user = new User();
        user.setEmail(dto.getEmail().trim());
        user.setFirstname(dto.getFirstName().trim());
        user.setLastname(dto.getLastName().trim());
        user.setGender(dto.getGender());
        user.setEnabled(false);
        user.setAuthorities(authorities);
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        user.setPhone(dto.getPhoneNumber());
        user.setActivated(false);

        return userRepository.save(user);
    }

    private User createUser(ThirdPartyUser thirdPartyUser, Set<Authority> authorities) {

        User user = new User();
        user.setEmail(thirdPartyUser.getEmail().trim());
        user.setFirstname(thirdPartyUser.getFirstname());
        user.setLastname(thirdPartyUser.getLastname());;
        user.setEnabled(true);
        user.setAuthorities(authorities);
        user.setPassword(new BCryptPasswordEncoder().encode(UUID.randomUUID().toString()));
        user.setActivated(true);
        return userRepository.save(user);
    }

    private User getCurrentUser() {
        return userRepository.findFirstByEmail(tokenProvider.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
    }
}
