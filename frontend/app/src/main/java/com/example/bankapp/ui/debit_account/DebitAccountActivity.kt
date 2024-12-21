package com.example.bankapp.ui.debit_account

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bankapp.R
import com.example.bankapp.api.ApiClient
import com.example.bankapp.ui.operations.OperationsActivity

class DebitAccountActivity : AppCompatActivity() {

    private lateinit var viewModel: DebitAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debit_account)

        viewModel = ViewModelProvider(this).get(DebitAccountViewModel::class.java)

        // Retrieve the token from shared preferences
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", null)

        // Set the token provider for ApiClient
        ApiClient.tokenProvider = { token }

        val accountNumberEditText = findViewById<EditText>(R.id.account_number_edit_text)
        val amountEditText = findViewById<EditText>(R.id.amount_edit_text)
        val debitButton = findViewById<Button>(R.id.debit_button)

        debitButton.setOnClickListener {
            val accountNumber = accountNumberEditText.text.toString()
            val amount = amountEditText.text.toString()

            if (accountNumber.isNotEmpty() && amount.isNotEmpty()) {
                viewModel.debitAccount(accountNumber, amount).observe(this) { response ->
                    if (response != null) {
                        Toast.makeText(this, response.responseMessage, Toast.LENGTH_SHORT).show()
                        if (response.responseCode == "007") {
                            // Navigate to operations page
                            startActivity(Intent(this, OperationsActivity::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "Debit failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
