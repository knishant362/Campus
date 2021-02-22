package com.trendster.campus.ui.fragment.subjects.update

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.trendster.campus.databinding.FragmentUpdateSubjectBinding
import com.trendster.campus.viewmodels.MainViewModel
import java.io.File

class UpdateSubjectFragment : Fragment() {

    private val REQ = 1
    private val TAG = "NISHANT"
    private var pdfData : Uri? = null

    private var pdfName =""
    private lateinit var fileTitle : EditText

    private var _binding : FragmentUpdateSubjectBinding? = null
    private val binding get() = _binding!!

//    private lateinit var addPdf: ImageView
    private var branchChip = "CSE"
    private var semesterChip = "1"
    private var materialTypeChip = "Lecture Notes"
//    private lateinit var subjectName: EditText
//    private lateinit var fileTitle: EditText
//    private lateinit var fileDesc: EditText
//    private lateinit var btnUpload: Button

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateSubjectBinding.inflate(inflater, container, false)


        /**Chip Groups*/
        binding.chipGroupAddBranch.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedBranchChip = chip.text.toString()
            branchChip = selectedBranchChip
        }
        binding.chipGroupAddSemester.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedSemesterChip = chip.text.toString()
            semesterChip = selectedSemesterChip
        }
        binding.chipGroupAddMaterialType.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedMaterialTypeChip= chip.text.toString()
            materialTypeChip = selectedMaterialTypeChip
        }
        /**Chip Groups*/


        binding.pdfIcon.setOnClickListener {
            openGallery()
        }

        binding.btnUpload.setOnClickListener {
            val subjectName = binding.etSubName
            val fileTitle = binding.etFileTitle
            val fileDesc = binding.etFileDesc

            when {
                subjectName.text.isEmpty() -> {
                    subjectName.error = "Fill this"
                }
                fileTitle.text.isEmpty() -> {
                    fileTitle.error = "Fill this"
                }
                fileDesc.text.isEmpty() -> {
                    fileDesc.error = "Fill this"
                }
                pdfData == null -> {
                    Toast.makeText(requireContext(), "Please add a pdf.", Toast.LENGTH_SHORT).show()
                }
                else ->{
                    Log.d("1File",fileTitle.text.toString() )
                    Log.d("1File",pdfName )
                    Log.d("1File", pdfData.toString())
                    Log.d("1File",fileDesc.text.toString() )
                    Log.d("1File",branchChip )
                    Log.d("1File",semesterChip )
                    Log.d("1File",subjectName.text.toString() )
                    Log.d("1File",materialTypeChip )

                    mainViewModel.uploadSubjectMaterial(
                            fileTitle.text.toString(),
                            pdfName,
                            pdfData,
                            fileDesc.text.toString(),
                            branchChip,
                            semesterChip,
                            subjectName.text.toString(),
                            materialTypeChip
                    )
                }
            }
        }

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

        if (requestCode == REQ && resultCode == RESULT_OK){
            pdfData = data?.data

            Toast.makeText(requireContext(), "" + pdfData, Toast.LENGTH_SHORT).show()
            if (pdfData.toString().startsWith("content://")){
                val cursor : Cursor?

                try {
                    cursor = pdfData?.let { this.context?.contentResolver?.query(
                            it,
                            null,
                            null,
                            null,
                            null
                    )}

                    if (cursor != null && cursor.moveToFirst()){
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }else if (pdfData.toString().startsWith("file://")) {
                        pdfName = File(pdfData.toString()).toString()
                    }

                    binding.inputFileTile.text = pdfName

                }catch (e: Exception){

                }

            }

        }

    }

}