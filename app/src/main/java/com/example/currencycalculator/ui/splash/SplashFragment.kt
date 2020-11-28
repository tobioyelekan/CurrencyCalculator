package com.example.currencycalculator.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.currencycalculator.R
import com.example.currencycalculator.data.helper.Resource.*
import com.example.currencycalculator.util.showMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_splash.*

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewModel: SplashViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()

    }

    private fun setupObserver() {
        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    navigate()
                }
                Status.ERROR -> {
                    showMessage(it.message!!)
                    progress.visibility = View.GONE
                }
            }
        })
    }

    private fun navigate() {
        findNavController().navigate(R.id.action_splashFragment_to_convertFragment)
    }
}