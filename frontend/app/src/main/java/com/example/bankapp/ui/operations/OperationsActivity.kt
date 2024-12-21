package com.example.bankapp.ui.operations

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.bankapp.databinding.ActivityOperationsBinding
import com.example.bankapp.ui.credit_account.CreditAccountActivity
import com.example.bankapp.ui.debit_account.DebitAccountActivity
import com.example.bankapp.ui.name_enquiry.NameEnquiryActivity
import com.example.bankapp.ui.transfer.TransferActivity
import com.example.bankapp.ui.update_details.UpdateDetailsActivity
import com.example.bankapp.ui.delete_account.DeleteAccountActivity
import com.example.bankapp.ui.retrieve_user_by_id.RetrieveUserByIdActivity
import com.example.bankapp.ui.approve_transaction.ApproveTransactionActivity
import com.example.bankapp.ui.balance_enquiry.BalanceEnquiryActivity
import com.example.bankapp.ui.get_all_users.GetAllUsersActivity
import com.example.bankapp.ui.home.HomeActivity
import com.example.bankapp.ui.reject_transaction.RejectTransactionActivity

class OperationsActivity : ComponentActivity() {

    private val viewModel: OperationsViewModel by viewModels()
    private lateinit var binding: ActivityOperationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOperationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnCreditAccount.setOnClickListener {
            startActivity(Intent(this, CreditAccountActivity::class.java))
        }

        binding.btnDebitAccount.setOnClickListener {
            startActivity(Intent(this, DebitAccountActivity::class.java))
        }

        binding.btnNameEnquiry.setOnClickListener {
            startActivity(Intent(this, NameEnquiryActivity::class.java))
        }

        binding.btnTransfer.setOnClickListener {
            startActivity(Intent(this, TransferActivity::class.java))
        }

        binding.btnUpdateDetails.setOnClickListener {
            startActivity(Intent(this, UpdateDetailsActivity::class.java))
        }

        binding.btnDeleteAccount.setOnClickListener {
            startActivity(Intent(this, DeleteAccountActivity::class.java))
        }

        binding.btnGetAllUsers.setOnClickListener {
            startActivity(Intent(this, GetAllUsersActivity::class.java))
        }

        binding.btnRetrieveUserById.setOnClickListener {
            startActivity(Intent(this, RetrieveUserByIdActivity::class.java))
        }

        binding.btnApproveTransaction.setOnClickListener {
            startActivity(Intent(this, ApproveTransactionActivity::class.java))
        }

        binding.btnRejectTransaction.setOnClickListener {
            startActivity(Intent(this, RejectTransactionActivity::class.java))
        }
        binding.btnBalanceEnquiry.setOnClickListener {
            startActivity(Intent(this, BalanceEnquiryActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }


    }
    private fun logout(){
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        sharedPreferences.edit().remove("jwt_token").apply()

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
