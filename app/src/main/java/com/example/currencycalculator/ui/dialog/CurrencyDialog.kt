package com.example.currencycalculator.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.currencycalculator.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_fragment.*


@AndroidEntryPoint
class CurrencyDialog : DialogFragment() {

    private val viewModel: CurrencyViewModel by viewModels()

    override fun onStart() {
        super.onStart()

        val dialog2: Dialog? = dialog
        if (dialog2 != null) {
            dialog2.window
                ?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()

        cancel.setOnClickListener {
            dismiss()
        }

        searchText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable) {
                viewModel.searchCurrency(text.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {

            }

        })

    }

    private fun setupObserver() {
        viewModel.symbols.observe(viewLifecycleOwner, Observer {
            val adapter = CurrencyAdapter(viewModel)
            currencyList.adapter = adapter

            if (it.isNullOrEmpty()) notfound.visibility = View.VISIBLE
            else notfound.visibility = View.GONE

            adapter.submitList(it)
        })

        viewModel.selectedCurrency.observe(viewLifecycleOwner, Observer {
            val dialogListener = requireParentFragment() as DialogListener
            dialogListener.onSelectCurrency(requireArguments().getInt(currencyPosKey), it)
            dismiss()
        })
    }

    companion object {
        private const val currencyPosKey = "com.example.currencycalculator.id"

        fun getInstance(currencyPos: Int): CurrencyDialog {
            val args = Bundle().apply {
                putInt(currencyPosKey, currencyPos)
            }

            return CurrencyDialog().apply {
                arguments = args
            }
        }
    }

    interface DialogListener {
        fun onSelectCurrency(pos: Int, currency: String)
    }

}