package com.trendster.campus.viewmodels.userviewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.trendster.campus.utils.*

class UserViewModel: ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val storageReference = FirebaseStorage.getInstance().reference


    /** User Activity stuff*/
    fun saveUserData(
        userUID: String,
        userName: String,
        userRollNo: String,
        userBranch: String,
        userSemester: String,
        accessLevel: String
    ){
        val data = HashMap<String, String>()
        data[USER_UID] = userUID
        data[USER_NAME] = userName
        data[USER_ROLL_NO] = userRollNo
        data[USER_BRANCH] = userBranch
        data[USER_SEMESTER] = userSemester
        data[ACCESS_LEVEL] = accessLevel

        firestore.collection("Users").document(userUID)
            .set(data).addOnSuccessListener {
                Log.d("saveUserData", userUID)
            }.addOnFailureListener{
                Log.d("saveUserData", "failed")
            }

        val dummydata = HashMap<String, Long>()
        dummydata[CLASS_PRESENT] = 1
        dummydata[CLASS_TOTAL] = 1

        /** Create subjects according to Branch and Semester*/
        for (mySubject in subjectListBranch(userBranch, userSemester) ){
            firestore.collection("Users").document(userUID)
                    .collection("Attendance").document(mySubject)
                    .set(dummydata)
                    .addOnSuccessListener {
                        Log.d("SubjectCreation" , "Success")
                    }
                    .addOnFailureListener {
                        throw it
                    }
        }
    }

    private fun subjectListBranch(branch: String, semester: String): MutableList<String> {
        return when(branch){
            "CSE" -> {
                subjectCSE(semester)
            }
            else -> {
                mutableListOf()
            }
        }
    }

    private fun subjectCSE(semester: String): MutableList<String> {
        val list = mutableListOf<String>()
        when (semester) {
            "6" -> {
                list.add("Data Analytics using R")
                list.add("Theory of Automaton")
                list.add("Operating System")
                list.add(".Net")
            }
        }
        return list
    }

    fun saveFacultyData(
        userUID: String,
        userName: String
    ){
        val data = HashMap<String, String>()
        data[USER_UID] = userUID
        data[USER_NAME] = userName
        data[ACCESS_LEVEL] = "faculty"

        firestore.collection("Faculty").document(userUID)
            .set(data).addOnSuccessListener {
                Log.d("saveUserData", userUID)
            }.addOnFailureListener{
                Log.d("saveUserData", "failed")
            }
    }


}