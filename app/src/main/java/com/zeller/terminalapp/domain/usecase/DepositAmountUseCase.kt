package com.zeller.terminalapp.domain.usecase

import com.zeller.terminalapp.domain.state.DomainStateResource
import com.zeller.terminalapp.domain.repository.TransactionsListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class DepositAmountUseCase(
    private val transactionsListRepository: TransactionsListRepository
) {
    operator fun invoke(amount: Float): Flow<DomainStateResource<Float>> =
        flow {
            emit(DomainStateResource.Loading())
            val result = transactionsListRepository.depositAmount(amount)
            emit(DomainStateResource.Success(result))
        }.catch { e ->
            emit(DomainStateResource.Error(e.message))
        }
}