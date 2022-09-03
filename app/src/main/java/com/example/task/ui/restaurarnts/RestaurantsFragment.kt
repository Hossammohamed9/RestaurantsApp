package com.example.task.ui.restaurarnts

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.task.R
import com.example.task.databinding.FragmentRestaurantsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RestaurantsFragment : Fragment() {

    private lateinit var binding : FragmentRestaurantsBinding
    private val viewModel: RestaurantsViewModel by viewModels()
    private lateinit var restaurantsAdapter: RestaurantsAdapter
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var slidingImageDots: Array<ImageView?>
    private var slidingDotsCount = 0
    private var currentPage = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestaurantsBinding.inflate(inflater, container,false)


        setupRestaurantsRecyclerView()
        setupViewPager()


        viewModel.restaurants.observe(viewLifecycleOwner){
            restaurantsAdapter.restaurants = it
            binding.searchId.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

                @SuppressLint("NotifyDataSetChanged")
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    viewModel.tempList.clear()
                    val searchText = p0!!.lowercase(Locale.getDefault())
                    if (searchText.isNotEmpty()){
                        viewModel.search(searchText)
                        restaurantsAdapter.restaurants = viewModel.tempList
                        binding.restaurantRVId.adapter!!.notifyDataSetChanged()
                    }else{
                        viewModel.tempList.clear()
                        viewModel.tempList.addAll(it)
                        restaurantsAdapter.restaurants = viewModel.tempList
                        binding.restaurantRVId.adapter!!.notifyDataSetChanged()

                    }
                    return false
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onQueryTextChange(p0: String?): Boolean {
                    viewModel.tempList.clear()
                    val searchText = p0!!.lowercase(Locale.getDefault())
                    if (searchText.isNotEmpty()){
                        viewModel.search(searchText)
                        restaurantsAdapter.restaurants = viewModel.tempList
                        binding.restaurantRVId.adapter!!.notifyDataSetChanged()
                    }else{
                        viewModel.tempList.clear()
                        viewModel.tempList.addAll(it)
                        restaurantsAdapter.restaurants = viewModel.tempList
                        binding.restaurantRVId.adapter!!.notifyDataSetChanged()

                    }
                    return false
                }
            })

        }



        viewModel.ads.observe(viewLifecycleOwner){
            viewPagerAdapter.sliders = it
            slidingDotsCount = it.size
            setupViewPagerSliderDots()
            @Suppress("DEPRECATION")
            val handler = Handler()
            val update = Runnable {
                if (currentPage == it.size) {
                    currentPage = 0
                }

                binding.imageSlideId.setCurrentItem(currentPage++, true)
            }

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    handler.post(update)
                }
            }, 2500, 2500)
        }


        binding.sortBtn.setOnClickListener {

            showDialog()
        }


        return binding.root
    }



    private fun setupRestaurantsRecyclerView() = binding.restaurantRVId.apply {
        restaurantsAdapter = RestaurantsAdapter(context)
        adapter = restaurantsAdapter
    }

    private fun setupViewPager() = binding.imageSlideId.apply {
        viewPagerAdapter = ViewPagerAdapter()
        adapter = viewPagerAdapter
        registerOnPageChangeCallback(slidingCallback)

    }

    private fun setupViewPagerSliderDots(){
        slidingImageDots = arrayOfNulls(slidingDotsCount)

        for (i in 0 until slidingDotsCount) {
            slidingImageDots[i] = ImageView(requireContext())
            slidingImageDots[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.non_active_dot
                )
            )
            val params =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

            params.setMargins(8, 0, 8, 0)
            binding.sliderDots.addView(slidingImageDots[i], params)
        }

        slidingImageDots[0]?.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.active_dot
            )
        )


    }

    private val slidingCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            for (i in 0 until slidingDotsCount) {
                slidingImageDots[i]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.non_active_dot
                    )
                )
            }

            slidingImageDots[position]?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.active_dot
                )
            )
        }
    }


    @SuppressLint("NotifyDataSetChanged", "CutPasteId")
    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_layout)
        dialog.findViewById<CheckBox>(R.id.chkBox_sort_by_name).isChecked = viewModel.isName
        dialog.findViewById<CheckBox>(R.id.chkBox_sort_by_delivery_time).isChecked  = viewModel.isDelev
        dialog.findViewById<CheckBox>(R.id.chkBox_sort_by_delivery_fee).isChecked = viewModel.isFee
        dialog.findViewById<CheckBox>(R.id.chkBox_sort_by_highest_rated).isChecked = viewModel.isRate

        dialog.findViewById<Button>(R.id.sort).setOnClickListener {
            dialog.dismiss()
            viewModel.isName = dialog.findViewById<CheckBox>(R.id.chkBox_sort_by_name).isChecked
            viewModel.isDelev = dialog.findViewById<CheckBox>(R.id.chkBox_sort_by_delivery_time).isChecked
            viewModel.isFee = dialog.findViewById<CheckBox>(R.id.chkBox_sort_by_delivery_fee).isChecked
            viewModel.isRate = dialog.findViewById<CheckBox>(R.id.chkBox_sort_by_highest_rated).isChecked

            if (viewModel.isName){
                restaurantsAdapter.restaurants = restaurantsAdapter.restaurants.sortedBy { it.name.lowercase() }
            }

            if (viewModel.isDelev){
                restaurantsAdapter.restaurants = restaurantsAdapter.restaurants.sortedBy { it.delivery_time }
            }

            if (viewModel.isFee){
                restaurantsAdapter.restaurants = restaurantsAdapter.restaurants.sortedBy { it.delivery_cost }
            }

            if (viewModel.isRate){
                restaurantsAdapter.restaurants = restaurantsAdapter.restaurants.sortedBy { it.rate?:"0" }
            }

            binding.restaurantRVId.adapter!!.notifyDataSetChanged()


        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(object : ColorDrawable(Color.TRANSPARENT){})
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }



}