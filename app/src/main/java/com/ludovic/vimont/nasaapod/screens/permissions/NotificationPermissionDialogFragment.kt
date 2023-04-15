package com.ludovic.vimont.nasaapod.screens.permissions

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ludovic.vimont.nasaapod.R
import com.ludovic.vimont.nasaapod.databinding.DialogFragmentNotificationPermissionBinding
import com.ludovic.vimont.nasaapod.ext.requestPermission
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationPermissionDialogFragment: DialogFragment() {
    private var _binding: DialogFragmentNotificationPermissionBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val viewModel: NotificationPermissionViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentNotificationPermissionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int = R.style.FullScreenDialog

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAcceptNotificationPermission.setOnClickListener {
            requestPermission(Manifest.permission.POST_NOTIFICATIONS, requestCode = REQUEST_CODE)
        }
        binding.buttonDeniedNotificationPermission.setOnClickListener {
            viewModel.onNotificationPermissionSeen()
            dismiss()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            viewModel.onNotificationPermissionSeen()
        }
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val TAG: String = NotificationPermissionDialogFragment::class.java.simpleName
        private const val REQUEST_CODE = 12345

        fun display(supportFragmentManager: FragmentManager) = NotificationPermissionDialogFragment().show(
            supportFragmentManager, TAG
        )
    }
}