package com.example.emailtosms.presentation.email

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.emailtosms.data.network.EmailListRepositoryImpl
import com.example.emailtosms.databinding.ListScreenBinding
import com.example.emailtosms.presentation.EmailToListApp
import com.example.emailtosms.presentation.ViewModelFactory
import com.example.emailtosms.presentation.email.adapter.EmailListAdapter
import javax.inject.Inject

class EmailListScreen: Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: EmailViewModel
    private lateinit var emailListAdapter: EmailListAdapter

    private var _binding: ListScreenBinding? = null
    private val binding: ListScreenBinding
        get() = _binding ?: throw RuntimeException("ListScreenBinding == null")

    private val component by lazy {
        (requireActivity().application as EmailToListApp).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

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
            requireActivity(),
            viewModelFactory
        ).get(EmailViewModel::class.java)

        viewModel.emailResponse.observe(viewLifecycleOwner){
            if (it.responseCode == EmailListRepositoryImpl.OK) {
                emailListAdapter.submitList(it.emailItemList)
            }
        }

        viewModel.loading.observe( viewLifecycleOwner){
            binding.swipeRefreshLayout.isRefreshing = it
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.checkEmail()
        }

    }

    fun setupRecyclerView(){
        emailListAdapter = EmailListAdapter()
        with(binding.rv1){
            binding.rv1.addItemDecoration(
                DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL)
            )
            binding.rv1.adapter = emailListAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        fun getInstance() = EmailListScreen()
    }
}