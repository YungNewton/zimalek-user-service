package com.zmarket.userservice.modules.wallet.service;

import com.zmarket.userservice.modules.security.model.User;
import com.zmarket.userservice.modules.wallet.model.Wallet;

public interface WalletService {

    Wallet CreateWallet(User user);

}
