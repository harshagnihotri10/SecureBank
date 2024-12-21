package com.example.bankapp.ui.transfer

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

class TransferActivity : AppCompatActivity() {

    private lateinit var viewModel: TransferViewModel
    //new
    private lateinit var transactionIdEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)

        viewModel = ViewModelProvider(this).get(TransferViewModel::class.java)

        // Retrieve the token from shared preferences
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", null)

        // Set the token provider for ApiClient
        ApiClient.tokenProvider = { token }

        val sourceAccountNumberEditText = findViewById<EditText>(R.id.source_account_number_edit_text)
        val destinationAccountNumberEditText = findViewById<EditText>(R.id.destination_account_number_edit_text)
        val amountEditText = findViewById<EditText>(R.id.amount_edit_text)
        val transferButton = findViewById<Button>(R.id.transfer_button)
        //new
        transactionIdEditText = findViewById(R.id.transaction_id_edit_text)


        transferButton.setOnClickListener {
            val sourceAccountNumber = sourceAccountNumberEditText.text.toString()
            val destinationAccountNumber = destinationAccountNumberEditText.text.toString()
            val amount = amountEditText.text.toString()

            if (sourceAccountNumber.isNotEmpty() && destinationAccountNumber.isNotEmpty() && amount.isNotEmpty()) {
                viewModel.transferFunds(sourceAccountNumber, destinationAccountNumber, amount).observe(this) { response ->
                    if (response != null) {
                        Toast.makeText(this, response.responseMessage, Toast.LENGTH_SHORT).show()
                        transactionIdEditText.setText(response.transactionId)
                        if (response.responseCode == "008") {
                            // Navigate to operations page
                            startActivity(Intent(this, OperationsActivity::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "Transfer failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
