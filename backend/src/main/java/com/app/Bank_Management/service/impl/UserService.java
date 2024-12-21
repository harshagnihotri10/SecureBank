package com.app.Bank_Management.service.impl;

import com.app.Bank_Management.dto.*;
import com.app.Bank_Management.entity.User;

import java.util.List;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);

    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse transfer(TransferRequest request);
    BankResponse login(LoginDto loginDto);



    List<User> getAllUsers();
    User getUserById(Long id);


    BankResponse updateUser(Long id, UserRequest userRequest);

    BankResponse deleteUserById(Long id);







}
