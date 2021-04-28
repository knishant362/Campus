package com.trendster.campus.ui.faculty.manageclass

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.trendster.campus.databinding.FragmentCreateClassworkBinding
import com.trendster.campus.viewmodels.facultyviewmodel.FacultyViewModel
import java.io.File
import java.util.*

class CreateClassworkFragment : Fragment() {

    private var _binding: FragmentCreateClassworkBinding? = null
    private val binding get() = _binding!!
    private val REQ = 1
    private val TAG = "NISHANT"
    private var pdfData: Uri? = null
    private var pdfName = ""
    private var dueDate = "Not Selected"
    private val facultyViewModel: FacultyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreateClassworkBinding.inflate(inflater, container, false)

        binding.txtPickDate.setOnClickListener {
            openDatePicker()
        }

        binding.txtPickFile.setOnClickListener {
            openGallery()
        }

        binding.btnCreateClasswork.setOnClickListener {
            val workTitle = binding.textFieldTitle
            val workDesc = binding.textFieldDesc

            when {
                workTitle.editText!!.text.isEmpty() -> {
                    workTitle.error = "Enter Title"
                }
                workDesc.editText!!.text.isEmpty() -> {
                    workDesc.error = "Enter Title"
                }
                pdfData == null -> {
                    Toast.makeText(requireContext(), "Please add a attachment.", Toast.LENGTH_SHORT).show()
                }
                dueDate == "Not Selected" -> {
                    Toast.makeText(requireContext(), "Please select Due Date.", Toast.LENGTH_SHORT).show()
                }
                else -> {

                    facultyViewModel.createClasswork(
                        requireContext(),
                        pdfName,
                        pdfData!!,
                        workTitle.editText!!.text.toString(),
                        workDesc.editText!!.text.toString(),
                        dueDate
                    )
                }
            }
        }

        return binding.root
    }

    private fun openDatePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select due date")
                .build()

        datePicker.show(childFragmentManager, "TAG")

        datePicker.addOnPositiveButtonClickListener {
            val selectedDate = datePicker.headerText
            binding.txtPickDate.text = selectedDate
            dueDate = selectedDate
        }
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

                    binding.txtPickFile.text = pdfName
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
