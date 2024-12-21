package com.app.Bank_Management.service.impl;

import com.app.Bank_Management.config.JwtTokenProvider;
import com.app.Bank_Management.dto.*;
import com.app.Bank_Management.entity.Role;
import com.app.Bank_Management.entity.Transaction;
import com.app.Bank_Management.entity.User;
import com.app.Bank_Management.repository.TransactionRepository;
import com.app.Bank_Management.repository.UserRepository;
import com.app.Bank_Management.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    //new

    @Autowired
    TransactionRepository transactionRepository;




    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        /**
         * Creating an account - saving a new user into the db
         */
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();

        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .address(userRequest.getAddress())
                .accountBalance(BigDecimal.ZERO)
                .phoneNumber(userRequest.getPhoneNumber())
                .alternatePhoneNumber(userRequest.getAlternatePhoneNumber())
                .status("ACTIVE")
                .role(Role.valueOf("ROLE_ADMIN"))
                //new
                .transactionLimit(userRequest.getTransactionLimit()) // Set transaction limit
                .requiresApproval(userRequest.isRequiresApproval()) // Set approval requirement
                //new finish
                .build();
        User savedUser = userRepository.save(newUser);
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your Account has been successfully Created.\nYour Account details: \n" +
                        "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() + "\nAccount Number: " + savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                        .build())
                .build();


    }

    public BankResponse  login(LoginDto loginDto){
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        EmailDetails loginAlert =EmailDetails.builder()
                .subject("You're logged in !")
                .recipient(loginDto.getEmail())
                .messageBody("You logged into your account. If you did not initiate this request, please contact your bank.")
                .build();

        emailService.sendEmailAlert(loginAlert);
        return BankResponse.builder()
                .responseCode("Login Success")
                .responseMessage(jwtTokenProvider.generateToken(authentication))
                .build();

    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public BankResponse updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setOtherName(userRequest.getOtherName());
        user.setGender(userRequest.getGender());
        user.setAddress(userRequest.getAddress());
        user.setStateOfOrigin(userRequest.getStateOfOrigin());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setAlternatePhoneNumber(userRequest.getAlternatePhoneNumber());
        User updatedUser = userRepository.save(user);
        return BankResponse.builder()
                .responseCode("Update Success")
                .responseMessage("User details updated successfully")
                .accountInfo(AccountInfo.builder()
                        .accountName(updatedUser.getFirstName() + " " + updatedUser.getLastName() + " " + updatedUser.getOtherName())
                        .accountBalance(updatedUser.getAccountBalance())
                        .accountNumber(updatedUser.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
        return BankResponse.builder()
                .responseCode("Delete Success")
                .responseMessage("User deleted successfully")
                .build();
    }



    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser = userRepository.findByAccountNumber((request.getAccountNumber()));
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName())
                        .build())
                .build();

    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;

        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();

    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToCredit = userRepository.findByAccountNumber((request.getAccountNumber()));
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .build())
                .build();


    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();
        if (availableBalance.intValue() < debitAmount.intValue()) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        } else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(request.getAccountNumber())
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }
    }


    //old
//    @Override
//    public BankResponse transfer(TransferRequest request) {
//        boolean isSourceAccountExist = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
//        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
//        if(!isDestinationAccountExist){
//            return BankResponse.builder()
//                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
//                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//        }
//
//        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
//        if(request.getAmount().compareTo(sourceAccountUser.getAccountBalance())> 0){
//            return BankResponse.builder()
//                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
//                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//        }
//        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
//        String sourceUsername= sourceAccountUser.getFirstName() + " " + sourceAccountUser.getLastName();
//        userRepository.save(sourceAccountUser);
//        EmailDetails debitAlert = EmailDetails.builder()
//                .subject("ACCOUNT DEBIT ALERT")
//                .recipient(sourceAccountUser.getEmail())
//                .messageBody("The sum of "+ request.getAmount()+" has been deducted from your account.\nYour current balance is "+ sourceAccountUser.getAccountBalance() + request.getAmount())
//                .build();
//        emailService.sendEmailAlert(debitAlert);
//
//
//        User destinationAccountUser = userRepository.findByAccountNumber((request.getDestinationAccountNumber()));
//        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
////        String recipientUsername = destinationAccountUser.getFirstName() + " "+ destinationAccountUser.getLastName();
//        userRepository.save(destinationAccountUser);
//        EmailDetails creditAlert = EmailDetails.builder()
//                .subject("ACCOUNT CREDIT ALERT")
//                .recipient(sourceAccountUser.getEmail())
//                .messageBody("The sum of "+ request.getAmount()+" has been credited to your account by " + sourceUsername+ ".\nYour current balance is "+ destinationAccountUser.getAccountBalance() + request.getAmount())
//                .build();
//        emailService.sendEmailAlert(creditAlert);
//
//        return BankResponse.builder()
//                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
//                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
//                .accountInfo(null)
//                .build();
//
//
//
//
//    }

    //old finish


    //new
//    @Override
//    public BankResponse transfer(TransferRequest request) {
//        boolean isSourceAccountExist = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
//        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
//        if (!isDestinationAccountExist) {
//            return BankResponse.builder()
//                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
//                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//        }
//        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
//        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
//
//        // Check if transaction exceeds the limit
//        if (request.getAmount().compareTo(sourceAccountUser.getTransactionLimit()) > 0) {
//            return BankResponse.builder()
//                    .responseCode(AccountUtils.EXCEEDS_LIMIT_CODE)
//                    .responseMessage(AccountUtils.EXCEEDS_LIMIT_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//        }
//
//        // Check if transaction requires approval
//        if (sourceAccountUser.isRequiresApproval()) {
//            return BankResponse.builder()
//                    .responseCode(AccountUtils.APPROVAL_REQUIRED_CODE)
//                    .responseMessage(AccountUtils.APPROVAL_REQUIRED_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//        }
//
//        if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) {
//            return BankResponse.builder()
//                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
//                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//        }
//
//        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
//        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
//
//        userRepository.save(sourceAccountUser);
//        userRepository.save(destinationAccountUser);
//
//        EmailDetails debitAlert = EmailDetails.builder()
//                .subject("ACCOUNT DEBIT ALERT")
//                .recipient(sourceAccountUser.getEmail())
//                .messageBody("The sum of " + request.getAmount() + " has been deducted from your account.\nYour current balance is " + sourceAccountUser.getAccountBalance())
//                .build();
//        emailService.sendEmailAlert(debitAlert);
//
//        EmailDetails creditAlert = EmailDetails.builder()
//                .subject("ACCOUNT CREDIT ALERT")
//                .recipient(destinationAccountUser.getEmail())
//                .messageBody("The sum of " + request.getAmount() + " has been credited to your account by " + sourceAccountUser.getFirstName() + " " + sourceAccountUser.getLastName() + ".\nYour current balance is " + destinationAccountUser.getAccountBalance())
//                .build();
//        emailService.sendEmailAlert(creditAlert);
//
//        return BankResponse.builder()
//                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
//                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
//                .accountInfo(null)
//                .build();
//    }

    //new finish


//new
    @Override
    public BankResponse transfer(TransferRequest request) {
        boolean isSourceAccountExist = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());

        if (!isDestinationAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());

        if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        if (sourceAccountUser.getTransactionLimit() != null &&
                request.getAmount().compareTo(sourceAccountUser.getTransactionLimit()) > 0 &&
                sourceAccountUser.isRequiresApproval()) {

            // Create a new transaction record
            Transaction transaction = Transaction.builder()
                    .sourceAccountNumber(request.getSourceAccountNumber())
                    .destinationAccountNumber(request.getDestinationAccountNumber())
                    .amount(request.getAmount())
                    .status("PENDING")
                    .build();

            transactionRepository.save(transaction);

            return BankResponse.builder()
                    .responseCode("009") // Custom code for pending approval
                    .responseMessage("Transaction pending approval.")
                    .transactionId(transaction.getId()) // Include the transaction ID
                    .build();
        } else {
            sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
            destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
            userRepository.save(sourceAccountUser);
            userRepository.save(destinationAccountUser);

            EmailDetails debitAlert = EmailDetails.builder()
                    .subject("ACCOUNT DEBIT ALERT")
                    .recipient(sourceAccountUser.getEmail())
                    .messageBody("The sum of " + request.getAmount() + " has been deducted from your account.\nYour current balance is " + sourceAccountUser.getAccountBalance())
                    .build();
            emailService.sendEmailAlert(debitAlert);

            EmailDetails creditAlert = EmailDetails.builder()
                    .subject("ACCOUNT CREDIT ALERT")
                    .recipient(destinationAccountUser.getEmail())
                    .messageBody("The sum of " + request.getAmount() + " has been credited to your account.\nYour current balance is " + destinationAccountUser.getAccountBalance())
                    .build();
            emailService.sendEmailAlert(creditAlert);

            return BankResponse.builder()
                    .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                    .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                    .build();
        }
    }



    public BankResponse approveTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if ("PENDING".equals(transaction.getStatus())) {
            User sourceAccountUser = userRepository.findByAccountNumber(transaction.getSourceAccountNumber());
            User destinationAccountUser = userRepository.findByAccountNumber(transaction.getDestinationAccountNumber());

            sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(transaction.getAmount()));
            destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(transaction.getAmount()));
            userRepository.save(sourceAccountUser);
            userRepository.save(destinationAccountUser);

            transaction.setStatus("APPROVED");
            transactionRepository.save(transaction);

            EmailDetails debitAlert = EmailDetails.builder()
                    .subject("ACCOUNT DEBIT ALERT")
                    .recipient(sourceAccountUser.getEmail())
                    .messageBody("The sum of " + transaction.getAmount() + " has been deducted from your account.\nYour current balance is " + sourceAccountUser.getAccountBalance())
                    .build();
            emailService.sendEmailAlert(debitAlert);

            EmailDetails creditAlert = EmailDetails.builder()
                    .subject("ACCOUNT CREDIT ALERT")
                    .recipient(destinationAccountUser.getEmail())
                    .messageBody("The sum of " + transaction.getAmount() + " has been credited to your account.\nYour current balance is " + destinationAccountUser.getAccountBalance())
                    .build();
            emailService.sendEmailAlert(creditAlert);

            return BankResponse.builder()
                    .responseCode("010") // Custom code for approved transaction
                    .responseMessage("Transaction approved and processed.")
                    .build();
        }

        return BankResponse.builder()
                .responseCode("011") // Custom code for invalid status
                .responseMessage("Transaction cannot be approved.")
                .build();
    }

    public BankResponse rejectTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if ("PENDING".equals(transaction.getStatus())) {
            transaction.setStatus("REJECTED");
            transactionRepository.save(transaction);

            return BankResponse.builder()
                    .responseCode("012") // Custom code for rejected transaction
                    .responseMessage("Transaction rejected.")
                    .build();
        }

        return BankResponse.builder()
                .responseCode("013") // Custom code for invalid status
                .responseMessage("Transaction cannot be rejected.")
                .build();
    }









}