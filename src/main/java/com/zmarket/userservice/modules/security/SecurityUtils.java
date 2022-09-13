package com.zmarket.userservice.modules.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtils {

   private static final Logger LOG = LoggerFactory.getLogger(SecurityUtils.class);

   public static Optional<String> getCurrentUsername() {
      final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null) {
         LOG.debug("no authentication in security context found");
         return Optional.empty();
      }

      String username = null;
      if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
         username = springSecurityUser.getUsername();
      } else if (authentication.getPrincipal() instanceof String) {
         username = (String) authentication.getPrincipal();
      }

      LOG.debug("found username '{}' in security context", username);

      return Optional.ofNullable(username);
   }
}
