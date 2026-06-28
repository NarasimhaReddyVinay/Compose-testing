package dev.spikeysanju.expensetracker.utils.viewState

sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    object Empty : ViewState<Nothing>()
    data class Success<T>(val data: T) : ViewState<T>()
    data class Error(val exception: Throwable) : ViewState<Nothing>()
}
