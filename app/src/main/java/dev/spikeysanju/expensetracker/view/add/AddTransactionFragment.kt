package dev.spikeysanju.expensetracker.view.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.spikeysanju.expensetracker.R
import dev.spikeysanju.expensetracker.databinding.FragmentAddTransactionBinding
import dev.spikeysanju.expensetracker.model.Transaction
import dev.spikeysanju.expensetracker.view.add.components.AddTransactionForm
import dev.spikeysanju.expensetracker.view.base.BaseFragment
import dev.spikeysanju.expensetracker.view.main.viewmodel.TransactionViewModel
import parseDouble
import snack

@AndroidEntryPoint
class AddTransactionFragment :
    BaseFragment<FragmentAddTransactionBinding, TransactionViewModel>() {
    override val viewModel: TransactionViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeView.setContent {
            MaterialTheme {
                AddTransactionForm { title, amount, type, tag, date, note ->
                    val transaction = Transaction(
                        title = title,
                        amount = parseDouble(amount),
                        transactionType = type,
                        tag = tag,
                        date = date,
                        note = note
                    )
                    
                    if (title.isNotEmpty() && !transaction.amount.isNaN()) {
                        viewModel.insertTransaction(transaction).run {
                            binding.root.snack(string = R.string.success_expense_saved)
                            findNavController().navigate(
                                R.id.action_addTransactionFragment_to_dashboardFragment
                            )
                        }
                    } else {
                        binding.root.snack(string = R.string.text_error_occurred)
                    }
                }
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAddTransactionBinding.inflate(inflater, container, false)
}
