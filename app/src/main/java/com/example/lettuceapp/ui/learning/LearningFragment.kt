package com.example.lettuceapp.ui.learning

import com.example.lettuceapp.R
//import android.R
import android.app.FragmentManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.lettuceapp.databinding.FragmentLearningBinding
import com.google.android.material.navigation.NavigationView
import java.util.Observer


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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.cardJs.setOnClickListener{

//            val resul
//            result.putString("course", "course")
//           parentFragmentManager.setFragmentResult("courseSelected", result)
            findNavController().navigate(R.id.action_learningFragment_to_materialFragment, Bundle().apply {
                putString("course", "js")

            })
        }
        binding.cardChem.setOnClickListener{

//            val resul
//            result.putString("course", "course")
//           parentFragmentManager.setFragmentResult("courseSelected", result)
            findNavController().navigate(R.id.action_learningFragment_to_materialFragment, Bundle().apply {
                putString("course", "chem")

            })
        }
        binding.cardAcct.setOnClickListener{

//            val resul
//            result.putString("course", "course")
//           parentFragmentManager.setFragmentResult("courseSelected", result)
            findNavController().navigate(R.id.action_learningFragment_to_materialFragment, Bundle().apply {
                putString("course", "acct")

            })
        }
    }


//    fun onClick(view: View){
//        Toast.makeText(getActivity(), "eqwewq", Toast.LENGTH_LONG).show();
////        val intent = Intent(activity, LearningMaterial::class.java)
////        startActivity(intent)
//    }

}