package com.example.foodies
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import classes.DietaryReq
import classes.Menu
import classes.Review
import classes.Store
import classes.StoreClickListener
import classes.storeList
import classes.storeRecyclerViewAdapter
import com.example.foodies.AccountActivity
import com.example.foodies.R
import com.example.foodies.databinding.FragmentVendorListBinding
import java.sql.Time

class VendorListFragment : Fragment() {

    private lateinit var binding: FragmentVendorListBinding

    private var imageList: List<Int> = listOf(R.drawable.cc, R.drawable.afriquezeen)
    private var campusCafeMenu: Menu = Menu()
    private var afriquezeenMenu: Menu = Menu()
    private var ccReviewList: ArrayList<Review> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVendorListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateStores()

        val mainActivity = requireActivity() as MainActivity

        binding.storeRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = storeRecyclerViewAdapter(storeList, mainActivity)
        }

        val accountButton = binding.accountButton
        accountButton.setOnClickListener {
            val intent = Intent(requireContext(), AccountActivity::class.java)
            startActivity(intent)
        }
    }

    private fun populateStores() {
        val campusCafe: Store = Store(
            "Campus Cafe", "Beverages", campusCafeMenu, 4.2,
            Time(8, 15, 0), Time(16, 15, 0), DietaryReq.VEGETARIAN, ccReviewList, imageList[0]
        )
        val afriquezeen: Store = Store(
            "Afriquezeen", "Hearty meals", afriquezeenMenu, 4.8,
            Time(8, 15, 0), Time(16, 15, 0), DietaryReq.NUT_FREE, ccReviewList, imageList[1]
        )
        storeList.add(campusCafe)
        storeList.add(afriquezeen)
    }
}
