package com.zmarket.userservice.modules.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zmarket.userservice.modules.security.enums.Gender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "USERS")
public class User implements Serializable {

   @Id
   @Column(name = "ID")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "password", length = 100)
   @JsonIgnore
   private String password;

   @Column(name = "firstname", length = 50)
   private String firstname;

   @Column(name = "lastname", length = 50)
   private String lastname;

   @Column(name = "email", length = 50)
   @Email
   private String email;

   @Column(name = "phone")
   private String phone;

   @Column(name = "gender")
   @Enumerated(EnumType.STRING)
   private Gender gender;

   @Column(name = "enabled", columnDefinition="BOOLEAN DEFAULT false")
   private boolean enabled;

   @JsonIgnore
   @Column(name = "activated", columnDefinition="BOOLEAN DEFAULT false")
   private boolean activated;

   @Column(name = "last_login_date")
   private LocalDateTime lastLoginDate;

   private LocalDateTime lastPasswordResetDate;

   @CreatedDate
   @Column(name = "created_at")
   private LocalDateTime createdAt;

   @LastModifiedDate
   @Column(name = "updated_at")
   private LocalDateTime updatedAt;


   @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
   @JoinTable(
           name = "USER_AUTHORITY",
           joinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
           inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
   @JsonIgnore
   private Set<Authority> authorities = new HashSet<>();

   public String getFullName() {
      return this.firstname + " " + this.lastname;
   }
}
