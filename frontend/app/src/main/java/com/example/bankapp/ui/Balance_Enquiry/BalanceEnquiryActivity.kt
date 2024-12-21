package com.example.bankapp.ui.balance_enquiry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bankapp.R
import com.example.bankapp.ui.operations.OperationsActivity

class BalanceEnquiryActivity : AppCompatActivity() {

    private lateinit var viewModel: BalanceEnquiryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance_enquiry)

        viewModel = ViewModelProvider(this).get(BalanceEnquiryViewModel::class.java)

        val accountNumberEditText = findViewById<EditText>(R.id.account_number_edit_text)
        val balanceTextView = findViewById<TextView>(R.id.balance_text_view)
        val enquireButton = findViewById<Button>(R.id.enquire_button)
        val backButton = findViewById<Button>(R.id.back_button)

        enquireButton.setOnClickListener {
            val accountNumber = accountNumberEditText.text.toString()
            if (accountNumber.isNotEmpty()) {
                viewModel.getBalanceEnquiry(accountNumber).observe(this) { response ->
                    if (response != null && response.responseCode == "004") {
                        val accountInfo = response.accountInfo
                        balanceTextView.text = "Name: ${accountInfo?.accountName}\n" +
                                "Balance: ${accountInfo?.accountBalance}\n" +
                                "Account Number: ${accountInfo?.accountNumber}"
                    } else {
                        balanceTextView.text = "Error: ${response?.responseMessage ?: "Unknown error"}"
                    }
                }
            } else {
                Toast.makeText(this, "Please enter an account number", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, OperationsActivity::class.java))
            finish()
        }
    }
}
