package com.example.emailtosms.presentation.sms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.emailtosms.databinding.ListScreenBinding

class SmsListScreen: Fragment() {
    private lateinit var viewModel: SmsViewModel
    private lateinit var smsListAdapter: SmsListAdapter

    private var _binding: ListScreenBinding? = null
    private val binding: ListScreenBinding
        get() = _binding ?: throw RuntimeException("ListScreenBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(SmsViewModel::class.java)
        viewModel.smsList.observe(viewLifecycleOwner){
            smsListAdapter.smsList = it
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.checkEmail()
            binding.swipeRefreshLayout.isRefreshing = false
        }

    }

    fun setupRecyclerView(){
        smsListAdapter = SmsListAdapter()
        with(binding.rv1){
            binding.rv1.addItemDecoration(DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL))
            binding.rv1.adapter = smsListAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        fun getInstance() = SmsListScreen()
    }
}