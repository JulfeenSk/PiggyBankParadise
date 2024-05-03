package com.examples.miniproject.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.examples.miniproject.HelperActivities.helperFragamentsActivities.EmptyFragment
import com.examples.miniproject.R
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var fragmentManager: FragmentManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.navigationLayout)
        fragmentManager = childFragmentManager

        // Set up ViewPager with the PagerAdapter
        viewPager.adapter = PagerAdapter(fragmentManager)

        // Connect TabLayout with ViewPager
        tabLayout.setupWithViewPager(viewPager)

        // Set the default tab to the first tab (index 0)
        viewPager.currentItem = 0

        // Set icons for each tab
        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_book)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_chart)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_accounts)

        return view
    }

    private inner class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = 3 // Number of tabs

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> TransactionsFragment() // Fragment for the first tab
                1 -> StatsFragment() // Fragment for the second tab
                2 -> AccountsFragment() // Fragment for the third tab
                else -> throw IllegalArgumentException("Invalid position")
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            // Return null to indicate no title for tabs
            return null
        }
    }
}
