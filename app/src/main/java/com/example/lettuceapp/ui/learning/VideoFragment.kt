package com.example.lettuceapp.ui.learning

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.lettuceapp.R
import com.example.lettuceapp.databinding.FragmentMaterialBinding
import com.example.lettuceapp.databinding.FragmentVideoBinding
import java.lang.Exception
import java.util.*


class VideoFragment : Fragment() {
    private var _binding: FragmentVideoBinding? = null

    private val binding get() = _binding!!

    private var permission = 0
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            permission = if (it) {
                1
            } else {
                0
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVideoBinding.inflate(inflater, container, false)

        val root: View = binding.root

        showVideo(requireArguments().getString("course").toString())

        return root
    }

    fun showVideo(course: String) {
        var video1 = ""
        var video2 = ""
        var video3 = ""
        var video4 = ""
        var video5 = ""

        if (requireArguments().getString("course").toString() == "js") {
            video1 = getResources().getString(R.string.js_videoTitle1);
            video2 = getResources().getString(R.string.js_videoTitle2);
            video3 = getResources().getString(R.string.js_videoTitle3);
            video4 = getResources().getString(R.string.js_videoTitle4);
            video5 = getResources().getString(R.string.js_videoTitle5);
        } else if (requireArguments().getString("course").toString() == "chem") {
            video1 = getResources().getString(R.string.chem_videoTitle1);
            video2 = getResources().getString(R.string.chem_videoTitle2);
            video3 = getResources().getString(R.string.chem_videoTitle3);
            video4 = getResources().getString(R.string.chem_videoTitle4);
            video5 = getResources().getString(R.string.chem_videoTitle5);
        } else {
            video1 = getResources().getString(R.string.acct_videoTitle1);
            video2 = getResources().getString(R.string.acct_videoTitle2);
            video3 = getResources().getString(R.string.acct_videoTitle3);
            video4 = getResources().getString(R.string.acct_videoTitle4);
            video5 = getResources().getString(R.string.acct_videoTitle5);
        }

        binding.textViewVideoTitle1.text = video1
        binding.textViewVideoTitle2.text = video2
        binding.textViewVideoTitle3.text = video3
        binding.textViewVideoTitle4.text = video4
        binding.textViewVideoTitle5.text = video5
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var link = ""
        binding.imageViewVideoBut.setOnClickListener {
//            requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            if (permission == 1) {
//                download()
//            } else {
//
//            } com.example.lettuceapp

            if (requireArguments().getString("course").toString() == "js") {
                link = "android.resource://" + context?.getPackageName() + "/" + R.raw.js_video1

            } else if (requireArguments().getString("course").toString() == "chem") {
                link = "android.resource://" + context?.getPackageName() + "/" + R.raw.chem_video1

            } else {
                link = "android.resource://" + context?.getPackageName() + "/" + R.raw.acct_video1

            }


            findNavController().navigate(
                R.id.action_videoFragment_to_viewVideoFragment,
                Bundle().apply {
                    putString("title", binding.textViewVideoTitle1.text.toString())
                    putString("link", link)

                })
        }
        binding.imageViewVideo2But.setOnClickListener {
            if (requireArguments().getString("course").toString() == "js") {
                link = "android.resource://" + context?.getPackageName() + "/" + R.raw.js_video2

            } else if (requireArguments().getString("course").toString() == "chem") {
                link = "android.resource://" + context?.getPackageName() + "/" + R.raw.chem_video2

            } else {
                link = "android.resource://" + context?.getPackageName() + "/" + R.raw.acct_video2

            }

            findNavController().navigate(
                R.id.action_videoFragment_to_viewVideoFragment,
                Bundle().apply {
                    putString("title", binding.textViewVideoTitle1.text.toString())
                    putString("link", link)

                })
        }
        binding.imageViewVideo3But.setOnClickListener {
            if (requireArguments().getString("course").toString() == "js") {
                link = "android.resource://" + context?.getPackageName() + "/" + R.raw.js_video3

            } else if (requireArguments().getString("course").toString() == "chem") {
                link = "android.resource://" + context?.getPackageName() + "/" + R.raw.chem_video3

            } else {
                link = "android.resource://" + context?.getPackageName() + "/" + R.raw.acct_video3

            }

            findNavController().navigate(
                R.id.action_videoFragment_to_viewVideoFragment,
                Bundle().apply {
                    putString("title", binding.textViewVideoTitle1.text.toString())
                    putString("link", link)

                })
        }
        binding.imageViewVideo4But.setOnClickListener {
            if (requireArguments().getString("course").toString() == "js") {
                link = "android.resource://" + context?.getPackageName() + "/" + R.raw.js_video4

            } else if (requireArguments().getString("course").toString() == "chem") {
                link = "android.resource://" + context?.getPackageName() + "/" + R.raw.chem_video4

            } else {
                link = "android.resource://" + context?.getPackageName() + "/" + R.raw.acct_video4

            }

            findNavController().navigate(
                R.id.action_videoFragment_to_viewVideoFragment,
                Bundle().apply {
                    putString("title", binding.textViewVideoTitle1.text.toString())
                    putString("link", link)
                })
        }
        binding.imageViewVideo5But.setOnClickListener {
            if (requireArguments().getString("course").toString() == "js") {
                link = "android.resource://" + context?.getPackageName() + "/" + R.raw.js_video5

            } else if (requireArguments().getString("course").toString() == "chem") {
                link = "android.resource://" + context?.getPackageName() + "/" + R.raw.chem_video5

            } else {
                link = "android.resource://" + context?.getPackageName() + "/" + R.raw.acct_video5

            }

            findNavController().navigate(
                R.id.action_videoFragment_to_viewVideoFragment,
                Bundle().apply {
                    putString("title", binding.textViewVideoTitle1.text.toString())
                    putString("link", link)

                })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

//    fun download() {
//
//
//        var downloadManager =
//            this.context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//
//        var link = "android.resource://com.example.lettuceapp.ui.learning/" + R.raw.video1
//
//
//        //var link =
//        // Uri.parse("https://www.youtube.com/watch?v=5ErOxlofkjA&list=RDMM5ErOxlofkjA&start_radio=1")
//        var request = DownloadManager.Request(link)
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
//            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
//            .setAllowedOverRoaming(false)
//            .setDescription("${binding.textViewVideoTitle1.text.toString()} is downloading ...")
//            .setTitle(binding.textViewVideoTitle1.text.toString() + ".mp4")
//            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Video1.mp4")
//            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//
//        downloadManager.enqueue(request)
//
//
//    }
}