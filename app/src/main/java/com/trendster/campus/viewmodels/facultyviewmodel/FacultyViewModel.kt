package com.trendster.campus.viewmodels.facultyviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.trendster.campus.utils.USER_BRANCH
import com.trendster.campus.utils.USER_SEMESTER
import kotlin.math.log10

class FacultyViewModel: ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val storageReference = FirebaseStorage.getInstance().reference

    private val _readFacultySubjects = MutableLiveData<List<DocumentSnapshot?>>()
    val readFacultySubjects: LiveData<List<DocumentSnapshot?>>
        get() = _readFacultySubjects

    private val _listStudents = MutableLiveData<List<DocumentSnapshot?>>()
    val listStudents: LiveData<List<DocumentSnapshot?>>
        get() = _listStudents

    private val _readAttendance = MutableLiveData<DocumentSnapshot?>()
    val readAttendance: LiveData<DocumentSnapshot?>
        get() = _readAttendance

    private val _attendanceStatus = MutableLiveData<Boolean>()
    val attendanceStatus: LiveData<Boolean>
        get() = _attendanceStatus


    fun fetchFacultySubjects(facultyUID: String ){

        firestore.collection("Faculty").document(facultyUID)
            .collection("subjectList").get().addOnSuccessListener { myData ->
                val docs = myData.documents
                _readFacultySubjects.postValue(docs)
            }

    }


    fun fetchStudents(branch: String, semester: String){
        firestore.collection("Users")
            .whereEqualTo(USER_BRANCH, branch)
            .whereEqualTo(USER_SEMESTER, "6")
            .addSnapshotListener { value, error ->
                val docs = value?.documents
                _listStudents.postValue(docs!!)
                Log.d("MYHI", docs.size.toString())
            }
    }


    fun fetchAttendance(studentUID: String, subjectName: String){
        firestore.collection("Users")
            .document(studentUID).collection("Attendance")
            .document(subjectName)
            .addSnapshotListener { value, error ->

                _readAttendance.postValue(value)

            }
    }


    fun updateAttendance(studentUID: String, subjectName: String, attendance: Map<String, Long>){
        firestore.collection("Users")
            .document(studentUID).collection("Attendance")
            .document(subjectName).update(attendance).addOnSuccessListener {
                    Log.d("DATAUPLd", "DONE")
                    _attendanceStatus.postValue(true)
            }
            .addOnFailureListener {
                Log.d("DATAUPLd", "Error")
                _attendanceStatus.postValue(false)
            }
    }

}