package com.example.multi_functionbox.ui.distance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.multi_functionbox.databinding.FragmentDistanceBinding

class DistanceFragment : Fragment() {

    private var _binding: FragmentDistanceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(DistanceViewModel::class.java)

        _binding = FragmentDistanceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDistance
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}