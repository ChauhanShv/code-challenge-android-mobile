package com.zeller.terminalapp.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zeller.terminalapp.R
import com.zeller.terminalapp.domain.model.Transactions
import com.zeller.terminalapp.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickListeners()
        collectUiState()
    }

    private fun collectUiState() {
        lifecycleScope.launch {
            mainViewModel.depositAmountState.collect {
                when (it) {
                    is AmountOperationState.Success -> {
                        setBalance(it.balance)
                    }

                    is AmountOperationState.Error -> {
                        showToastMessage(it.errorMessage)
                    }

                    is AmountOperationState.Loading -> {
                        // show loading if you like
                    }
                }
            }
        }
        lifecycleScope.launch {
            mainViewModel.withdrawAmountState.collect {
                when (it) {
                    is AmountOperationState.Success -> {
                        setBalance(it.balance)
                    }

                    is AmountOperationState.Error -> {
                        showToastMessage(it.errorMessage)
                    }

                    is AmountOperationState.Loading -> {
                        // show loading if you like
                    }
                }
            }
        }
        lifecycleScope.launch {
            mainViewModel.pastTransactionsState.collectLatest {
                when (it) {
                    is PastTransactionsState.Success -> {
                        showTransactionList(it.transactions)
                    }

                    is PastTransactionsState.Error -> {
                        showToastMessage(it.errorMessage)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun showTransactionList(transactions: List<Transactions>) {
        // i am hereby making it short to show list in a single textview using string builder and new line,
        // you can show another page with recycler view xml or lazy column composable
        if (transactions.isEmpty()) {
            binding.pastTransactions.text = getString(R.string.no_transactions_info_message)
            return
        }
        val builder = StringBuilder()
        builder.append(getString(R.string.past_transactions_header)).append("\n\n")
        transactions.forEach { transaction ->
            val value = if (transaction.isDeposit) {
                "Credit: +${transaction.amount}"
            } else {
                "Debit: -${transaction.amount}"
            }
            builder.append(value).append("\n\n")
        }
        binding.pastTransactions.text = builder.toString()
    }

    private fun setBalance(balance: Float?) {
        binding.balance.text = balance.toString()
    }

    private fun showToastMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun clickListeners() {
        binding.depositButton.setOnClickListener(this)
        binding.withdrawButton.setOnClickListener(this)
        binding.pastTransactionsButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.withdrawButton) {
            if (!binding.amountInput.text.isNullOrEmpty()) {
                val amount = binding.amountInput.editableText.toString().toFloat()
                mainViewModel.withdrawAmount(amount)
            }

        } else if (view?.id == R.id.depositButton) {
            if (!binding.amountInput.text.isNullOrEmpty()) {
                val amount = binding.amountInput.editableText.toString().toFloat()
                mainViewModel.depositAmount(amount)
            }
        } else if (view?.id == R.id.pastTransactionsButton) {
            mainViewModel.getPastTransactions()
        }
    }
}