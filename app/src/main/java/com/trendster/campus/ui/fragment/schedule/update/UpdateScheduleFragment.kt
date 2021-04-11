package com.trendster.campus.ui.fragment.schedule.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentUpdateScheduleBinding
import com.trendster.campus.viewmodels.mainviewmodel.MainViewModel

class UpdateScheduleFragment : Fragment() {

    private var _binding : FragmentUpdateScheduleBinding? = null
    private val binding get() = _binding!!

    private var branchChip = "CSE"
    private var semesterChip = "1"
    private var typeChip = "Theory"
    private var dayChip = "Monday"
    private var lectureNoChip = "1"
    private var tshChip = "09"
    private var tsmChip = "50"
    private var tehChip = "10"
    private var temChip = "50"
    private lateinit var subjectName: EditText
    private lateinit var facultyName: EditText
    private lateinit var roomNo: EditText
    private lateinit var btnUpload: Button
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateScheduleBinding.inflate(inflater, container, false)
        subjectName = binding.etSubName
        facultyName = binding.etFacultyName
        roomNo = binding.etRoomno
        btnUpload = binding.btnUpload


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
            val selectedTypeChip = chip.text.toString()
            typeChip = selectedTypeChip
        }

        binding.chipGroupAddDay.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedDayChip = chip.text.toString()
            dayChip = selectedDayChip
        }


        binding.chipGroupAddLecNo.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedLecNoChip = chip.text.toString()
            lectureNoChip = selectedLecNoChip
        }

        binding.cgTSH.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedTSHChip = chip.text.toString()
            tshChip = selectedTSHChip
        }

        binding.cgTSM.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedTSMChip = chip.text.toString()
            tsmChip = selectedTSMChip
        }

        binding.cgTEH.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedTEHChip = chip.text.toString()
            tehChip = selectedTEHChip
        }

        binding.cgTEM.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedTEMChip = chip.text.toString()
            temChip = selectedTEMChip
        }


        btnUpload.setOnClickListener {

            when{
                subjectName.text.isEmpty() -> {
                    subjectName.error = "Fill this"
                }
                facultyName.text.isEmpty() -> {
                    facultyName.error = "Fill this"
                }
                roomNo.text.isEmpty() -> {
                    roomNo.error = "Fill this"
                }
                else -> {
                    mainViewModel.uploadSchedule(
                            branchChip,
                            semesterChip,
                            typeChip,
                            dayChip,
                            lectureNoChip,
                            tshChip,
                            tsmChip,
                            tehChip,
                            temChip,
                            subjectName.text.toString(),
                            facultyName.text.toString(),
                            roomNo.text.toString()
                    )
                    findNavController().navigate(R.id.action_updateScheduleFragment_to_scheduleFragment)
                }

            }

        }














        return binding.root
    }

}