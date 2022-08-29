package com.example.lettuceapp.ui.learning

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lettuceapp.databinding.FragmentLearningBinding
import com.example.lettuceapp.ui.statistics.StatisticsFragment

class LearningFragment : Fragment(){

    private var _binding: FragmentLearningBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(LearningViewModel::class.java)

        _binding = FragmentLearningBinding.inflate(inflater, container, false)
        val root: View = binding.root

//       // val bind = binding.button
//
//        root.setOnClickListener {
//            val intet = Intent (this@LearningFragment.requireContext(), StatisticsFragment::class.java)
//            startActivity(intet)
//        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    fun onClick(view: View){
//        Toast.makeText(getActivity(), "eqwewq", Toast.LENGTH_LONG).show();
////        val intent = Intent(activity, LearningMaterial::class.java)
////        startActivity(intent)
//    }

}