package com.zmarket.userservice.modules.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "AUTHORITY")
public class Authority  {

   @Id
   @Column(name = "id")
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authority_seq")
   @SequenceGenerator(name = "authority_seq", sequenceName = "authority_seq", allocationSize = 1)
   private Long id;

   @Column(name = "name", length = 50)
   @NotNull
   @Enumerated(EnumType.STRING)
   private AuthorityName name;

   @JsonIgnore
   @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
   private List<User> users;

   @Override
   public String toString() {
      return "Authority{" +
              "id=" + id +
              ", name=" + name +
              '}';
   }
}
