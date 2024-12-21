package com.example.bankapp.ui.delete_account

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bankapp.R
import com.example.bankapp.ui.operations.OperationsActivity

class DeleteAccountActivity : AppCompatActivity() {

    private val viewModel: DeleteAccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_account)

        val userIdEditText = findViewById<EditText>(R.id.user_id_edit_text)
        val deleteButton = findViewById<Button>(R.id.delete_button)
        val backButton = findViewById<Button>(R.id.back_button)

        deleteButton.setOnClickListener {
            val userId = userIdEditText.text.toString().toIntOrNull()
            if (userId != null) {
                viewModel.deleteAccount(userId).observe(this) { response ->
                    if (response != null) {
                        Toast.makeText(this, response.responseMessage, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to delete account. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter a valid user ID", Toast.LENGTH_SHORT).show()
            }
        }

        backButton.setOnClickListener {
            val intent = Intent(this, OperationsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
