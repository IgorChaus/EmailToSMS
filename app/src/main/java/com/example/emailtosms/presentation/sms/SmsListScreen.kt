package com.example.emailtosms.presentation.sms

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

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
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(SmsViewModel::class.java)

        viewModel.smsList.observe(viewLifecycleOwner){
            smsListAdapter.smsList = it
        }

        viewModel.loading.observe( viewLifecycleOwner){
            binding.swipeRefreshLayout.isRefreshing = it
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.checkEmail()
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

    fun checkSmsPermission(): Boolean{
        var permission = false
        when{
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED -> {
                permission = true
            }
            shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS) -> {
                Toast.makeText(
                    requireContext(),
                    "Без этого разрешения SMS не приложение не сможет отправлять SMS",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.SEND_SMS)
            }
        }
        return permission
    }

    fun registerPermissionListener(){
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){

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