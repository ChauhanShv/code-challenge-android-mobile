package com.zeller.terminalapp.data.repository

import com.zeller.terminalapp.domain.model.Transactions
import com.zeller.terminalapp.data.common.MIN_NOT_SUPPORTED_AMOUNT
import com.zeller.terminalapp.data.common.MIN_WITHDRAW_ERR_MESSAGE
import com.zeller.terminalapp.data.common.NOT_ENOUGH_BALANCE
import com.zeller.terminalapp.domain.repository.TransactionsListRepository

class TransactionsList : TransactionsListRepository {

    private val transactionsList: MutableList<Transactions> = mutableListOf()
    private var balance = 0.0f

    fun addTransaction(transactions: Transactions) {
        transactionsList.add(transactions)
    }

    override suspend fun depositAmount(amount: Float): Float {
        balance += amount
        addTransaction(transactions = Transactions(amount = amount, isDeposit = true))
        return balance
    }

    override suspend fun withdrawAmount(amount: Float): Float {
        if (balance > amount) {
            if (amount > MIN_NOT_SUPPORTED_AMOUNT) {
                balance -= amount
                addTransaction(transactions = Transactions(amount = amount, isDeposit = false))
                return balance
            } else {
                throw Error(MIN_WITHDRAW_ERR_MESSAGE)
            }
        } else {
            throw Error(NOT_ENOUGH_BALANCE)
        }
    }

    override suspend fun getAllTransactions(): List<Transactions> {
        return transactionsList
    }
}

