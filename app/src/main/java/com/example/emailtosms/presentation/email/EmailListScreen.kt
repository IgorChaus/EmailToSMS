package com.example.emailtosms.presentation.email

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        viewModel.emailResponse.observe(viewLifecycleOwner) {
            emailListAdapter.submitList(it?.toMutableList())
        }

        viewModel.loading.observe(viewLifecycleOwner){
            binding.swipeRefreshLayout.isRefreshing = it
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.checkEmail()
        }

    }

    private fun setupRecyclerView(){

        emailListAdapter = EmailListAdapter()

        emailListAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (viewModel.getLoadMessageNumber() > EmailListRepositoryImpl.NUMBER_DOWNLOAD_MESSAGES) {
                    binding.rv1.scrollToPosition(emailListAdapter.itemCount - 1)
                }
            }
        })

        with(binding.rv1){
            addItemDecoration(
                DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL)
            )
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount
                        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                            firstVisibleItemPosition >= 0) {
                            viewModel.getNextMessages()
                        }
                    }
                }
            })

            adapter = emailListAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}