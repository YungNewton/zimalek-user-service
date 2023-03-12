package com.zmarket.userservice.modules.wallet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zmarket.userservice.modules.security.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "wallets")
public class Wallet {
    private Long id;

    @Column(nullable = false)
    @JsonIgnore
    @OneToOne
    private User user;

    private BigDecimal amount;


    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;
}
