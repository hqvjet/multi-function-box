package com.example.multi_functionbox.ui.voltage

import BluetoothViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.multi_functionbox.databinding.FragmentVoltageBinding
import kotlinx.coroutines.launch
import java.io.IOException

class VoltageFragment : Fragment() {

    private var _binding: FragmentVoltageBinding? = null
    private lateinit var bluetoothViewModel: BluetoothViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(VoltageViewModel::class.java)

        bluetoothViewModel = ViewModelProvider(requireActivity()).get(BluetoothViewModel::class.java)

        _binding = FragmentVoltageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Write data to outputStream
        bluetoothViewModel.outputStream.observe(viewLifecycleOwner) { outputStream ->
            // Use outputStream as needed
            try {
                val data = "1"
                outputStream?.write(data.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        // Read data from inputStream and update UI
        lifecycleScope.launch {
            bluetoothViewModel.inputStream.observe(viewLifecycleOwner) { inputStream ->
                Thread {
                    val buffer = ByteArray(1024)
                    var bytes: Int

                    try {
                        while (true) {
                            bytes = inputStream?.read(buffer) ?: 0
                            val incomingMessage = String(buffer, 0, bytes)

                            // Update UI only when non-empty data arrives
                            if (incomingMessage.trim().isNotEmpty()) {
                                activity?.runOnUiThread {
                                    _binding?.textVoltage?.let { textView ->
                                        textView.text = incomingMessage.trim() + " V"
                                    }
                                }
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }.start()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
