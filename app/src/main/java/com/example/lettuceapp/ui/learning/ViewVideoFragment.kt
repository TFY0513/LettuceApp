package com.example.lettuceapp.ui.learning

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import com.example.lettuceapp.databinding.FragmentMaterialBinding
import com.example.lettuceapp.databinding.FragmentVideoBinding
import com.example.lettuceapp.databinding.FragmentViewVideoBinding


class ViewVideoFragment : Fragment() {

    private var _binding: FragmentViewVideoBinding? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewVideoBinding.inflate(inflater, container, false)


        val root: View = binding.root


         binding.textViewVideoTitle.text=requireArguments().getString("title").toString()
     var uri = Uri.parse(requireArguments().getString("link").toString())
       // var uri = Uri.parse("https://www.youtube.com/watch?v=FXikAK5bbXA&list=RDMM5ErOxlofkjA&index=19")
       // binding.textViewVideoTitle.text=uri.toString()
        binding.videoView.setVideoURI(uri)
        binding.videoView.start()
       var mediaController = MediaController(this.context)
        mediaController.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)
        return root
    }


}