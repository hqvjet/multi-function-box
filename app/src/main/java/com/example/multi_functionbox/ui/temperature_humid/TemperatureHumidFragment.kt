package com.example.multi_functionbox.ui.temperature_humid

import BluetoothViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.multi_functionbox.databinding.FragmentTemperatureHumidBinding
import kotlinx.coroutines.launch
import java.io.IOException

class TemperatureHumidFragment : Fragment() {

    private var _binding: FragmentTemperatureHumidBinding? = null
    private lateinit var bluetoothViewModel: BluetoothViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(TemperatureHumidViewModel::class.java)

        bluetoothViewModel =
            ViewModelProvider(requireActivity()).get(BluetoothViewModel::class.java)

        _binding = FragmentTemperatureHumidBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Write data to outputStream
        bluetoothViewModel.outputStream.observe(viewLifecycleOwner) { outputStream ->
            // Use outputStream as needed
            try {
                val data = "4"
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
                                var arr = incomingMessage.split(" ")
                                activity?.runOnUiThread {
                                    _binding?.textTemperatureHumid?.let { textView ->
                                        textView.text = arr[0] + "°C\n" + arr[1] + "%"
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
