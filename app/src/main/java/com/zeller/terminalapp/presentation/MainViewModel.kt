package com.zeller.terminalapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeller.terminalapp.domain.model.Transactions
import com.zeller.terminalapp.domain.state.DomainStateResource
import com.zeller.terminalapp.data.repository.TransactionsList
import com.zeller.terminalapp.domain.usecase.DepositAmountUseCase
import com.zeller.terminalapp.domain.usecase.GetPastTransactionsUseCase
import com.zeller.terminalapp.domain.usecase.WithdrawAmountUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel : ViewModel() {
    private val transactionsListRepository: TransactionsList = TransactionsList()
    private val withdrawAmountUseCase: WithdrawAmountUseCase =
        WithdrawAmountUseCase(transactionsListRepository)
    private val depositAmountUseCase: DepositAmountUseCase =
        DepositAmountUseCase(transactionsListRepository)
    private val getPastTransactionsUseCase: GetPastTransactionsUseCase =
        GetPastTransactionsUseCase(transactionsListRepository)

    //state
    private val _depositAmountState: MutableStateFlow<AmountOperationState> = MutableStateFlow(
        AmountOperationState.Loading
    )
    val depositAmountState: StateFlow<AmountOperationState> = _depositAmountState

    fun depositAmount(amount: Float) {
        depositAmountUseCase.invoke(amount = amount).onEach {
            when (it) {
                is DomainStateResource.Loading -> {
                    _depositAmountState.value =
                        AmountOperationState.Loading
                }

                is DomainStateResource.Success -> {
                    _depositAmountState.value =
                        AmountOperationState.Success(balance = it.data)
                }

                is DomainStateResource.Error -> {
                    _depositAmountState.value =
                        AmountOperationState.Error(errorMessage = it.message.toString())
                }
            }
        }.launchIn(scope = viewModelScope)
    }


    private val _withdrawAmountState: MutableStateFlow<AmountOperationState> = MutableStateFlow(
        AmountOperationState.Loading
    )
    val withdrawAmountState: StateFlow<AmountOperationState> = _withdrawAmountState

    fun withdrawAmount(amount: Float) {
        withdrawAmountUseCase.invoke(amount = amount).onEach {
            when (it) {
                is DomainStateResource.Loading -> {
                    _depositAmountState.value =
                        AmountOperationState.Loading
                }

                is DomainStateResource.Success -> {
                    _withdrawAmountState.value =
                        AmountOperationState.Success(balance = it.data)
                }

                is DomainStateResource.Error -> {
                    _depositAmountState.value =
                        AmountOperationState.Error(errorMessage = it.message.toString())
                }
            }
        }.launchIn(scope = viewModelScope)
    }

    private val _pastTransactionsState: MutableStateFlow<PastTransactionsState> = MutableStateFlow(
        PastTransactionsState.Loading
    )
    val pastTransactionsState: StateFlow<PastTransactionsState> = _pastTransactionsState

    fun getPastTransactions() {
        resetState()
        getPastTransactionsUseCase.invoke().onEach {
            when (it) {
                is DomainStateResource.Loading -> {
                    _pastTransactionsState.value =
                        PastTransactionsState.Loading
                }

                is DomainStateResource.Success -> {
                    _pastTransactionsState.value =
                        PastTransactionsState.Success(transactions = it.data.orEmpty())
                }

                is DomainStateResource.Error -> {
                    _pastTransactionsState.value =
                        PastTransactionsState.Error(errorMessage = it.message.toString())
                }
            }
        }.launchIn(scope = viewModelScope)
    }

    private fun resetState() {
        _pastTransactionsState.value = PastTransactionsState.None
    }
}

sealed class AmountOperationState {
    data object Loading : AmountOperationState()
    data class Success(val balance: Float?) : AmountOperationState()
    data class Error(val errorMessage: String) : AmountOperationState()
}

sealed class PastTransactionsState {
    data object None : PastTransactionsState()
    data object Loading : PastTransactionsState()
    data class Success(val transactions: List<Transactions>) : PastTransactionsState()
    data class Error(val errorMessage: String) : PastTransactionsState()
}