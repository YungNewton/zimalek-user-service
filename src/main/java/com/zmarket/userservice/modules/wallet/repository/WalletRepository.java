package com.zmarket.userservice.modules.wallet.repository;

import com.zmarket.userservice.modules.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

}
