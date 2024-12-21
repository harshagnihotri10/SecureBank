package com.example.bankapp.ui.name_enquiry

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.bankapp.R
import com.example.bankapp.api.ApiClient
import com.example.bankapp.ui.operations.OperationsActivity

class NameEnquiryActivity : AppCompatActivity() {

    private lateinit var viewModel: NameEnquiryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name_enquiry)

        viewModel = ViewModelProvider(this).get(NameEnquiryViewModel::class.java)

        // Retrieve the token from shared preferences
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", null)

        // Set the token provider for ApiClient
        ApiClient.tokenProvider = { token }

        val accountNumberEditText = findViewById<EditText>(R.id.account_number_edit_text)
        val enquireButton = findViewById<Button>(R.id.enquire_button)
        val resultTextView = findViewById<TextView>(R.id.result_text_view)
        val backButton = findViewById<Button>(R.id.back_button)

        enquireButton.setOnClickListener {
            val accountNumber = accountNumberEditText.text.toString()

            if (accountNumber.isNotEmpty()) {
                viewModel.nameEnquiry(accountNumber).observe(this) { response ->
                    if (response != null) {
                        resultTextView.text = response.responseMessage
                    } else {
                        resultTextView.text = "An error occurred."
                    }
                }
            } else {
                Toast.makeText(this, "Please enter an account number.", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            startActivity(Intent(this, OperationsActivity::class.java))
            finish()
        }
    }
}
