package com.example.task.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task.R
import com.example.task.databinding.FragmentCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private lateinit var binding : FragmentCategoryBinding
    private val viewModel : CategoryViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container,false)

        setupCategoryRecyclerView()

        viewModel.category.observe(viewLifecycleOwner) {
            categoryAdapter.categories = it
        }

        viewModel.navigateToRestaurantFragment.observe(viewLifecycleOwner){ category ->
            category?.let {
                findNavController().navigate(R.id.action_categoryFragment_to_restaurantsFragment)
                viewModel.displayRestaurantFragmentComplete()
            }

        }

        return binding.root
    }

    private fun setupCategoryRecyclerView() = binding.categoryRVId.apply {
        categoryAdapter = CategoryAdapter(CategoryAdapter.OnClickListener{
            viewModel.displayRestaurantFragment(it)
        })
        adapter = categoryAdapter
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

    }


}