package com.example.bankapp.ui.approve_transaction

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bankapp.R

class ApproveTransactionActivity : AppCompatActivity() {

    private val viewModel: ApproveTransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approve_transaction)

        // Retrieve the token from shared preferences
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", null)

        // Set the token provider for ApiClient
        viewModel.token = token

        val transactionIdEditText = findViewById<EditText>(R.id.transaction_id_edit_text)
        val approveButton = findViewById<Button>(R.id.approve_button)

        approveButton.setOnClickListener {
            val transactionId = transactionIdEditText.text.toString().toIntOrNull()
            if (transactionId != null) {
                viewModel.approveTransaction(transactionId).observe(this) { response ->
                    response?.let {
                        Toast.makeText(this, it.responseMessage, Toast.LENGTH_SHORT).show()
                    } ?: run {
                        Toast.makeText(this, "Approval failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter a valid transaction ID.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
