package com.zeller.terminalapp.domain.usecase

import com.zeller.terminalapp.domain.model.Transactions
import com.zeller.terminalapp.domain.state.DomainStateResource
import com.zeller.terminalapp.domain.repository.TransactionsListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetPastTransactionsUseCase(
    private val transactionsListRepository: TransactionsListRepository
) {
    operator fun invoke(): Flow<DomainStateResource<List<Transactions>>> =
        flow {
            emit(DomainStateResource.Loading())
            val result = transactionsListRepository.getAllTransactions()
            emit(DomainStateResource.Success(result))
        }.catch { e ->
            emit(DomainStateResource.Error(e.message))
        }
}