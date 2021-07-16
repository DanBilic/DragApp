package com.example.dragapp.onboarding.screens

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dragapp.DashboardActivity
import com.example.dragapp.databinding.FragmentThirdScreenBinding
import com.example.dragapp.utils.Categories
import com.example.dragapp.onboarding.ChipAdapter
import kotlinx.android.synthetic.main.chip_element.view.*

class ThirdScreen : Fragment() {
    private var _binding: FragmentThirdScreenBinding? = null
    private val binding get() = _binding!!

    private val selectedList: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentThirdScreenBinding.inflate(inflater, container, false)


        binding.finishButtonTs.setOnClickListener {
            getSelectedCategories()
            // navigate to dashboard
            val intent = Intent(activity, DashboardActivity::class.java)
            activity?.startActivity(intent)
            //findNavController().navigate(R.id.action_viewPagerFragment_to_dashboardFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewChips.apply {
            layoutManager = GridLayoutManager(activity, 3)
            binding.recyclerViewChips.layoutManager = layoutManager
            adapter = ChipAdapter(Categories.categoriesList)
        }
    }

    fun getSelectedCategories() {
        var size = Categories.categoriesList.size - 1
        for (item in 0..size) {
            var chipItem = binding.recyclerViewChips.getChildAt(item).chip
            if (chipItem.isChecked) {
                selectedList.add(chipItem.text as String)
            }
        }
    }
}