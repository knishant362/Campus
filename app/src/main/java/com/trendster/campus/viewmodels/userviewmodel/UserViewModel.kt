package com.trendster.campus.viewmodels.userviewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.trendster.campus.utils.*

class UserViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
//    private val storageReference = FirebaseStorage.getInstance().reference

    private val _readUserType = MutableLiveData<Boolean?>()
    val readUserType: LiveData<Boolean?>
        get() = _readUserType

    /** User Activity stuff*/
    fun saveUserData(
        userUID: String,
        userName: String,
        userRollNo: String,
        userBranch: String,
        userSemester: String,
        accessLevel: String
    ) {
        val data = HashMap<String, String>()
        data[USER_UID] = userUID
        data[USER_NAME] = userName
        data[USER_ROLL_NO] = userRollNo
        data[USER_BRANCH] = userBranch
        data[USER_SEMESTER] = userSemester
        data[TEMP_USER_BRANCH] = userBranch
        data[TEMP_USER_SEMESTER] = userSemester
        data[ACCESS_LEVEL] = accessLevel
        data[USER_FEEDBACK] = "No Feedback submitted yet."

        firestore.collection("Users").document(userUID)
            .set(data).addOnSuccessListener {
                Log.d("saveUserData", userUID)
            }.addOnFailureListener {
                Log.d("saveUserData", "failed")
            }

        /** Create subjects according to Branch and Semester*/
        loadSubjects(userUID, userBranch, userSemester)
    }

    private fun loadSubjects(userUID: String, userBranch: String, userSemester: String) {
        val subjectArray = arrayListOf<String>()
        firestore.collection("Data").document(userBranch)
            .collection(userSemester).document("Subjects")
            .collection("list").get()
            .addOnSuccessListener { myData ->
                Log.d("HJKK", myData.documents.size.toString())
                val docs = myData.documents
                for (i in 0 until docs.size) {
                    createAttendance(userUID, docs[i].id)
                    Log.d("HJKKw11", subjectArray.toString())
                }
            }
            .addOnFailureListener {
                Log.d("HJKKw", it.message!!)
            }
    }

    private fun createAttendance(userUID: String, mySubject: String) {

        val dummyData = HashMap<String, Long>()
        dummyData[CLASS_PRESENT] = 1
        dummyData[CLASS_TOTAL] = 1

        firestore.collection("Users").document(userUID)
            .collection("Attendance").document(mySubject)
            .set(dummyData)
            .addOnSuccessListener {
                Log.d("SubjectCreation", "Success")
            }
            .addOnFailureListener {
                Log.d("SubjectCreation", it.message!!)
            }
    }

    fun saveFacultyData(
        userUID: String,
        userName: String
    ) {
        val data = HashMap<String, String>()
        data[USER_UID] = userUID
        data[USER_NAME] = userName
        data[ACCESS_LEVEL] = "faculty"

        firestore.collection("Faculty").document(userUID)
            .set(data).addOnSuccessListener {
                Log.d("saveUserData", userUID)
            }.addOnFailureListener {
                Log.d("saveUserData", "failed")
            }
    }

    fun readUserLevel(uid: String) {
        firestore.collection("Faculty").document(uid).addSnapshotListener { value, error ->
            val isFaculty = value?.exists()
            _readUserType.postValue(isFaculty)
        }
    }
}
