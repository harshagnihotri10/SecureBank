package com.app.Bank_Management.controller;

import com.app.Bank_Management.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@AllArgsConstructor
public class TransactionController {

    private final UserServiceImpl userService;

    @PostMapping("/approve/{transactionId}")
    public ResponseEntity<?> approveTransaction(@PathVariable Long transactionId) {
        return ResponseEntity.ok(userService.approveTransaction(transactionId));
    }

    @PostMapping("/reject/{transactionId}")
    public ResponseEntity<?> rejectTransaction(@PathVariable Long transactionId) {
        return ResponseEntity.ok(userService.rejectTransaction(transactionId));
    }
}
