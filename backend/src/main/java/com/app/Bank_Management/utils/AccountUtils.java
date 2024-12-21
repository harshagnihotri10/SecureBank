package com.app.Bank_Management.utils;

import java.time.Year;

public class AccountUtils {
    public static  final String ACCOUNT_EXISTS_CODE ="001";
    public static  final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account Created!";
    public static final String ACCOUNT_CREATION_SUCCESS ="002";
    public static  final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created!";
    public static  final String ACCOUNT_NOT_EXISTS_CODE ="003";
    public static  final String ACCOUNT_NOT_EXISTS_MESSAGE = "User with the provided Account Number does not exist";
    public static final String ACCOUNT_FOUND_CODE ="004";
    public static  final String ACCOUNT_FOUND_SUCCESS = "User Account Found.";
    public static final String ACCOUNT_CREDITED_SUCCESS ="005";
    public static  final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User Account Credited successfully .";
    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";
    public static final String ACCOUNT_DEBITED_SUCCESS = "007";
    public static final String ACCOUNT_DEBITED_MESSAGE = "Account has been successfully debited";
    public static final String TRANSFER_SUCCESSFUL_CODE = "008";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Transfer successful!";

    public static final String EXCEEDS_LIMIT_CODE = "009";
    public static final String EXCEEDS_LIMIT_MESSAGE = "Transaction exceeds the account limit.";
    public static final String APPROVAL_REQUIRED_CODE = "010";
    public static final String APPROVAL_REQUIRED_MESSAGE = "Transaction requires approval.";








    public static String generateAccountNumber(){
        /**
         * 2024 + randomSixDigits
         */
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;
        String year = String.valueOf(currentYear);
        int randomNo = (int) Math.floor(Math.random() *(max - min + 1) + min);
        String randomNumber = String.valueOf(randomNo);
        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(year).append(randomNumber).toString();
    }



}
