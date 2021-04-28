package com.trendster.campus.ui.fragment.subjects.classwork

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentSubmissionBinding
import com.trendster.campus.viewmodels.subjectviewmodel.SubjectViewModel
import java.io.File

class SubmissionFragment : Fragment() {

    private var _binding: FragmentSubmissionBinding? = null
    private val binding get() = _binding!!
    private val args: SubmissionFragmentArgs by navArgs()
    private val REQ = 1
    private val TAG = "NISHANT"
    private var pdfData: Uri? = null
    private var pdfName = ""
    private val subjectViewModel: SubjectViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSubmissionBinding.inflate(inflater, container, false)

        val classwork = args.classromModel

        binding.txtAssignmentTitle.text = classwork.assignmentTitle
        binding.txtAssignmentDesc.text = classwork.assignmentDesc
        binding.chipAttachment.text = classwork.pdfName

        binding.chipAddFile.setOnClickListener {
            openGallery()
        }
        binding.btnSubmit.setOnClickListener {

            when {
                binding.etComment.text.isEmpty() -> {
                    binding.etComment.error = "Fill this"
                }
                pdfData == null -> {
                    Toast.makeText(requireContext(), "Please add a pdf.", Toast.LENGTH_SHORT).show()
                }
                else -> {

                    subjectViewModel.uploadSubmission(
                        requireContext(),
                        pdfName,
                        pdfData,
                        binding.txtAssignmentTitle.text.toString()
                    )
                }
            }
        }

        if (classwork.assignmentTitle != null)
            subjectViewModel.loadSubmissionStatus(classwork.assignmentTitle)
        subjectViewModel.submitStatus.observe(
            viewLifecycleOwner,
            {
                if (it == true) {
                    binding.txtSubmitStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.card_green))
                    binding.imgSubmitStatus.setColorFilter(ContextCompat.getColor(requireContext(), R.color.card_green))
                    val txt1 = "Submitted"
                    binding.txtSubmitStatus.text = txt1
                    val txt2 = "Re-submit"
                    binding.btnSubmit.text = txt2
                }
            }
        )

        return binding.root
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Pdf file"), REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ && resultCode == Activity.RESULT_OK) {
            pdfData = data?.data

            Toast.makeText(requireContext(), "" + pdfData, Toast.LENGTH_SHORT).show()
            if (pdfData.toString().startsWith("content://")) {
                val cursor: Cursor?

                try {
                    cursor = pdfData?.let {
                        this.context?.contentResolver?.query(
                            it,
                            null,
                            null,
                            null,
                            null
                        )
                    }

                    if (cursor != null && cursor.moveToFirst()) {
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    } else if (pdfData.toString().startsWith("file://")) {
                        pdfName = File(pdfData.toString()).toString()
                    }

                    binding.chipAddFile.text = pdfName
                } catch (e: Exception) {
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
