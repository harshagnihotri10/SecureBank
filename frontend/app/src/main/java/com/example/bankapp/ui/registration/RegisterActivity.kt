package com.example.bankapp.ui.registration

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bankapp.R
import com.example.bankapp.models.RegisterRequest
import com.example.bankapp.models.RegisterResponse
import com.example.bankapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val firstNameEditText = findViewById<EditText>(R.id.first_name_edit_text)
        val lastNameEditText = findViewById<EditText>(R.id.last_name_edit_text)
        val otherNameEditText = findViewById<EditText>(R.id.other_name_edit_text)
        val genderSpinner = findViewById<Spinner>(R.id.gender_spinner)
        val addressEditText = findViewById<EditText>(R.id.address_edit_text)
        val stateOfOriginEditText = findViewById<EditText>(R.id.state_of_origin_edit_text)
        val emailEditText = findViewById<EditText>(R.id.email_edit_text)
        val passwordEditText = findViewById<EditText>(R.id.password_edit_text)
        val phoneNumberEditText = findViewById<EditText>(R.id.phone_number_edit_text)
        val alternatePhoneNumberEditText = findViewById<EditText>(R.id.alternate_phone_number_edit_text)
        val transactionLimitEditText = findViewById<EditText>(R.id.transaction_limit_edit_text)
        val requiresApprovalCheckBox = findViewById<CheckBox>(R.id.requires_approval_checkbox)
        val registerButton = findViewById<Button>(R.id.register_button)

        registerButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val otherName = otherNameEditText.text.toString()
            val gender = genderSpinner.selectedItem.toString().lowercase()
            val address = addressEditText.text.toString()
            val stateOfOrigin = stateOfOriginEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val phoneNumber = phoneNumberEditText.text.toString()
            val alternatePhoneNumber = alternatePhoneNumberEditText.text.toString()
            val transactionLimit = transactionLimitEditText.text.toString().toDoubleOrNull()
            val requiresApproval = requiresApprovalCheckBox.isChecked

            if (!validateInputs(firstName, lastName, gender, address, stateOfOrigin, email, password, phoneNumber, transactionLimit)) {
                return@setOnClickListener
            }

            val registerRequest = RegisterRequest(
                firstName = firstName,
                lastName = lastName,
                otherName = otherName,
                gender = gender,
                address = address,
                stateOfOrigin = stateOfOrigin,
                email = email,
                password = password,
                phoneNumber = phoneNumber,
                alternatePhoneNumber = alternatePhoneNumber,
                transactionLimit = transactionLimit ?: 0.0,
                requiresApproval = requiresApproval
            )

            viewModel.register(registerRequest).observe(this) { response ->
                if (response != null) {
                    if (response.responseCode == "002") {
                        Toast.makeText(this, response.responseMessage, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish() // Optional: Close the RegisterActivity
                    } else {
                        Toast.makeText(this, response.responseMessage, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateInputs(
        firstName: String,
        lastName: String,
        gender: String,
        address: String,
        stateOfOrigin: String,
        email: String,
        password: String,
        phoneNumber: String,
        transactionLimit: Double?
    ): Boolean {

        if (!password.matches("\\d{4}".toRegex())) {
            Toast.makeText(this, "Password must be exactly 4 digits.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address.", Toast.LENGTH_SHORT).show()
            return false
        }


        if (!phoneNumber.matches("\\d{10}".toRegex())) {
            Toast.makeText(this, "Phone number must be exactly 10 digits.", Toast.LENGTH_SHORT).show()
            return false
        }

        if (gender !in listOf("male", "female", "other")) {
            Toast.makeText(this, "Gender must be 'male', 'female', or 'other'.", Toast.LENGTH_SHORT).show()
            return false
        }


        if (transactionLimit == null || transactionLimit <= 0) {
            Toast.makeText(this, "Transaction limit must be a positive number.", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
