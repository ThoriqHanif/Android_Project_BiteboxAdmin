package com.thrq.biteboxadmin.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.thrq.biteboxadmin.R
import com.thrq.biteboxadmin.activity.AllOrdersActivity
import com.thrq.biteboxadmin.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.cvAddCategory.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_categoryFragment)
        }

        binding.cvAddFood.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_foodFragment)
        }

        binding.cvAddSlider.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_sliderFragment)
        }

        binding.cvOrder.setOnClickListener{
            startActivity(Intent(requireContext(), AllOrdersActivity::class.java))
        }

        return binding.root
    }
}
