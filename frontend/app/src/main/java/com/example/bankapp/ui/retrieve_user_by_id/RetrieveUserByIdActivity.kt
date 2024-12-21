package com.example.bankapp.ui.retrieve_user_by_id

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bankapp.R
import com.example.bankapp.models.User


class RetrieveUserByIdActivity: AppCompatActivity() {

    private val viewModel: RetrieveUserByIdViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrieve_user_by_id)

        val userIdEditText = findViewById<EditText>(R.id.user_id_edit_text)
        val retrieveButton = findViewById<Button>(R.id.retrieve_button)
        val userDetailsTextView = findViewById<TextView>(R.id.user_details_text_view)
        val backButton = findViewById<Button>(R.id.back_button)

        retrieveButton.setOnClickListener {
            val userId = userIdEditText.text.toString().toIntOrNull()

            if (userId != null) {
                viewModel.retrieveUserById(userId).observe(this) { user ->
                    if (user != null) {
                        userDetailsTextView.text = formatUserDetails(user)
                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter a valid user ID", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun formatUserDetails(user: User): String {
        return """
        ID: ${user.id}
        Name: ${user.firstName} ${user.lastName}
        Gender: ${user.gender}
        Address: ${user.address}
        State of Origin: ${user.stateOfOrigin}
        Account Number: ${user.accountNumber}
        Account Balance: ${user.accountBalance}
        Email: ${user.email}
        Phone Number: ${user.phoneNumber}
        Alternate Phone Number: ${user.alternatePhoneNumber}
        Status: ${user.status}
        Role: ${user.role}
        Created At: ${user.createdAt}
        Modified At: ${user.modifiedAt}
    """.trimIndent()
    }
}