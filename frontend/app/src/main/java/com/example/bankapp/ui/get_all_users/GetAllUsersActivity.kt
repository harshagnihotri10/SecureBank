package com.example.bankapp.ui.get_all_users

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bankapp.R
import com.example.bankapp.ui.operations.OperationsActivity

class GetAllUsersActivity : AppCompatActivity() {

    private val viewModel: GetAllUsersViewModel by viewModels()
    private lateinit var adapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getallusers)

        val recyclerView = findViewById<RecyclerView>(R.id.usersRecyclerView)


        adapter = UsersAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("jwt_token", null)

        if (token != null) {
            viewModel.fetchAllUsers(token)
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, OperationsActivity::class.java))
            finish()
        }

        viewModel.users.observe(this) { users ->
            adapter.submitList(users)
        }

    }
}
