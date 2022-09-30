package com.example.lettuceapp.ui.learning

import android.Manifest
import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lettuceapp.R
import com.example.lettuceapp.databinding.FragmentMaterialBinding
import java.io.File
import java.io.FileOutputStream


class MaterialFragment : Fragment() {
    private var _binding: FragmentMaterialBinding? = null

    private val binding get() = _binding!!
    private var permission = 0

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            permission = 1
        } else {
            permission = 0
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMaterialBinding.inflate(inflater, container, false)


        val root: View = binding.root

        showCourse(requireArguments().getString("course").toString())

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.quiz.setOnClickListener { quiz() }

        binding.notes.setOnClickListener {
            //   downloadNotes()
            requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permission == 1) {
                //Toast.makeText(requireContext(), "Permission Allowde " + permission, Toast.LENGTH_SHORT).show()
                downloadNotes()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission Denied " + permission,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        binding.videoLink.setOnClickListener { video() }
    }

    fun showCourse(course: String) {
        if (requireArguments().getString("course").toString() == "js") {
            binding.textViewCourse.text = "JavaScript"
        } else if (requireArguments().getString("course").toString() == "chem") {
            binding.textViewCourse.text = "Chemistry"
        } else {
            binding.textViewCourse.text = "Accounting"
        }

    }

    fun quiz() {
        findNavController().navigate(
            R.id.action_materialFragment_to_difficultyFragment,
            Bundle().apply {
                putString("course", requireArguments().getString("course").toString())
            })
    }

    fun video() {
        findNavController().navigate(R.id.action_materialFragment_to_videoFragment, Bundle().apply {
            putString("course", requireArguments().getString("course").toString())
        })
    }


    fun downloadNotes() {
        var url = ""
        var name = ""
        if (requireArguments().getString("course").toString() == "js") {
            url =
                "https://matfuvit.github.io/UVIT/predavanja/literatura/TutorialsPoint%20JavaScript.pdf"
            name = "JavaScript TutorialsPoint"
        } else if (requireArguments().getString("course").toString() == "chem") {
            url = "https://openedgroup.org/books/Chemistry.pdf"
            name = "Introduction to Chemistry"
        } else {
            url = "https://www.accountingcoach.com/pro-samples/accounting-basics-explanation.pdf"
            name = "Accounting Basics"
        }
        name += ".pdf"

        try {
            var request = DownloadManager.Request(Uri.parse(url))
                .setTitle(name)
                .setDescription("Downloading")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(false)
                .setMimeType("application/pdf")
                .setVisibleInDownloadsUi(true)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                .setDestinationInExternalFilesDir(
                    context,
                    Environment.DIRECTORY_DOWNLOADS,
                    name
                )

            var dm =  requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)

            Toast.makeText(requireContext(), "The file is downloaded", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "The file failed to download", Toast.LENGTH_SHORT)
                .show()
        }

    }

}