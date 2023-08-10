package com.example.emailtosms.presentation.sms

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.emailtosms.databinding.ListScreenBinding
import com.example.emailtosms.presentation.EmailToListApp
import com.example.emailtosms.presentation.ViewModelFactory
import com.example.emailtosms.presentation.sms.adapter.SmsListAdapter
import javax.inject.Inject

class SmsListScreen: Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: SmsViewModel
    private lateinit var smsListAdapter: SmsListAdapter

    private var _binding: ListScreenBinding? = null
    private val binding: ListScreenBinding
        get() = _binding ?: throw RuntimeException("ListScreenBinding == null")

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

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
        registerPermissionListener()
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(SmsViewModel::class.java)

        viewModel.smsList.observe(viewLifecycleOwner){
            smsListAdapter.submitList(it)
        }

        viewModel.loading.observe( viewLifecycleOwner){
            binding.swipeRefreshLayout.isRefreshing = it
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            checkEmail()
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

    fun checkEmail(){
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED -> {
                viewModel.checkEmail(true)
            }
            shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS) -> {
                viewModel.checkEmail(false)
                Toast.makeText(
                    requireContext(),
                    "Для отправки SMS сообщений приложению необходимо разрешение на отправку SMS",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.SEND_SMS)
            }
        }
    }

    fun registerPermissionListener(){
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                viewModel.checkEmail(true)
            }else{
                viewModel.checkEmail(false)
                Toast.makeText(
                    requireContext(),
                    "Отправка SMS выполняться не будет",
                    Toast.LENGTH_LONG
                ).show()
            }
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