package com.example.lettuceapp.ui.assessment

import android.os.Bundle
import android.view.*
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.lettuceapp.R
import com.example.lettuceapp.adapter.TabPagerAdapter
import com.example.lettuceapp.databinding.FragmentAssessmentTabBinding
import com.example.lettuceapp.ui.assessment.category.ActiveAssessmentFragment
import com.example.lettuceapp.ui.assessment.category.CompletedAssessmentFragment
import com.example.lettuceapp.ui.assessment.category.UpcomingAssessmentFragment
class AssessmentTabFragment : Fragment() {

    private var _binding: FragmentAssessmentTabBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var tabPagerAdapter: TabPagerAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssessmentTabBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.findItem(R.id.action_settings).isVisible = false
    }

    override fun onStart() {
        super.onStart()

        if(binding.pager.isEmpty()){
            tabPagerAdapter = TabPagerAdapter(childFragmentManager)
            viewPager = binding.pager
            /**
             * How to stop refreshing viewpager content?
             * https://stackoverflow.com/questions/29610004/how-to-stop-refreshing-viewpager-content
             */
            viewPager.offscreenPageLimit = 3
            tabPagerAdapter.addFragment(ActiveAssessmentFragment(), getString(R.string.assessment_section_one_name))
            tabPagerAdapter.addFragment(UpcomingAssessmentFragment(), getString(R.string.assessment_section_two_name))
            tabPagerAdapter.addFragment(CompletedAssessmentFragment(), getString(R.string.assessment_section_three_name))
            viewPager.adapter = tabPagerAdapter
            binding.tbLayout.setupWithViewPager(viewPager)
        }
    }

}