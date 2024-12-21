package com.example.bankapp.ui.reject_transaction

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bankapp.R
import com.example.bankapp.api.ApiClient
import com.example.bankapp.models.RejectTransactionResponse
import com.example.bankapp.ui.operations.OperationsActivity

class RejectTransactionActivity : AppCompatActivity() {

    private lateinit var viewModel: RejectTransactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reject_transaction)

        viewModel = ViewModelProvider(this).get(RejectTransactionViewModel::class.java)

        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", null)
        ApiClient.tokenProvider = { token }

        val transactionIdEditText = findViewById<EditText>(R.id.transaction_id_edit_text)
        val rejectButton = findViewById<Button>(R.id.reject_button)

        rejectButton.setOnClickListener {
            val transactionId = transactionIdEditText.text.toString()

            if (transactionId.isNotEmpty()) {
                viewModel.rejectTransaction(transactionId.toInt()).observe(this) { response ->
                    response?.let {
                        Toast.makeText(this, it.responseMessage, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, OperationsActivity::class.java))
                        finish()
                    } ?: run {
                        Toast.makeText(this, "Error rejecting transaction.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter a valid transaction ID.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
