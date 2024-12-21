package com.app.Bank_Management.service.impl;

import com.app.Bank_Management.dto.EmailDetails;

public interface EmailService {

    void sendEmailAlert(EmailDetails emailDetails);
}
