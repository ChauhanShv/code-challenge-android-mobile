package com.zeller.terminalapp.domain.repository

import com.zeller.terminalapp.domain.model.Transactions

interface TransactionsListRepository {
    suspend fun depositAmount(amount: Float): Float
    suspend fun withdrawAmount(amount: Float): Float
    suspend fun getAllTransactions(): List<Transactions>
}