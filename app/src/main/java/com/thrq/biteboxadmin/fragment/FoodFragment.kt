package com.thrq.biteboxadmin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.thrq.biteboxadmin.R
import com.thrq.biteboxadmin.databinding.FragmentFoodBinding

class FoodFragment : Fragment() {

    private lateinit var binding : FragmentFoodBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFoodBinding.inflate(layoutInflater)
        binding.floatingActionButton.setOnClickListener{
            Navigation.findNavController(it).navigate(R.id.action_foodFragment_to_addFoodFragment)
        }

        return binding.root

    }
}