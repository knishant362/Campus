package com.trendster.campus.viewmodels.mainviewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.trendster.campus.utils.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MainViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val storageReference = FirebaseStorage.getInstance().reference
    var pdfUploading = MutableLiveData<Boolean>()

//    private val _readAccessLevel = MutableLiveData<String>()
//    val readAccessLevel: LiveData<String>
//        get() = _readAccessLevel

    private val _readAttendance = MutableLiveData<List<DocumentSnapshot?>>()
    val readAttendance: LiveData<List<DocumentSnapshot?>>
        get() = _readAttendance

    private val _readSchedule = MutableLiveData<List<DocumentSnapshot?>>()
    val readSchedule: LiveData<List<DocumentSnapshot?>>
        get() = _readSchedule

    private val _readSubjects = MutableLiveData<List<DocumentSnapshot?>>()
    val readSubjects: LiveData<List<DocumentSnapshot?>>
        get() = _readSubjects

    private val _readUserProfile = MutableLiveData<DocumentSnapshot?>()
    val readUserProfile: LiveData<DocumentSnapshot?>
        get() = _readUserProfile

    private val _readNotifications = MutableLiveData<List<DocumentSnapshot?>>()
    val readNotifications: LiveData<List<DocumentSnapshot?>>
        get() = _readNotifications

    fun loadRequest(userUID: String, type: String, requiredDay: String) {
        firestore.collection("Users").document(userUID)
            .addSnapshotListener { value, error ->
                val UID = value?.get(USER_UID)?.toString()
                val userBranch = value?.get(USER_BRANCH).toString()
                val userSemester = value?.get(USER_SEMESTER).toString()
                val accessLevel = value?.get(ACCESS_LEVEL).toString()
//                _readAccessLevel.postValue(accessLevel)

                when (type) {
                    "today" -> {
                        loadSchedule(userBranch, userSemester, requiredDay)
                    }
                    "subjects" -> {
                        loadSubjects(userBranch, userSemester)
                    }
                    "week" -> {
                        // change here to today() later
                        loadSchedule(userBranch, userSemester, requiredDay)
                    }
                }
            }
    }

    private fun loadSchedule(userBranch: String, userSemester: String, requiredDay: String) {
        firestore.collection("Data").document(userBranch)
            .collection(userSemester).document("Schedule")
            .collection(requiredDay).get().addOnSuccessListener { data ->
                val docs = data.documents
                _readSchedule.postValue(docs)
                Log.d("loadSchedule", docs.size.toString())
            }
    }

    private fun loadSubjects(userBranch: String, userSemester: String) {
        firestore.collection("Data").document(userBranch)
            .collection(userSemester).document("Subjects")
            .collection("list").get().addOnSuccessListener { myData ->
                val docs = myData.documents
                _readSubjects.postValue(docs)
                Log.d("loadSubjects", myData.toString())
            }
    }
    fun todayDay(): String {
        val today = Calendar.getInstance()
        val date = today.time
        Log.d("myTime", date.toString())
        Log.d("myTime", today.toString())
        val day = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)

        /**individual item */
        val year = today.get(Calendar.YEAR)
        val month = today.get(Calendar.MONTH)
        val myday = today.get(Calendar.DAY_OF_MONTH)

        val hour = today.get(Calendar.HOUR_OF_DAY)
        val minute = today.get(Calendar.MINUTE)
        Log.d("myTime11", myday.toString())
        /**individual item */

        return day
    }

    /**Attendance Section*/

    fun loadAttendance(userUID: String) {
        firestore.collection("Users").document(userUID)
            .collection("Attendance").addSnapshotListener { value, error ->
                val docs = value?.documents
                _readAttendance.postValue(docs!!)
            }
    }

    fun loadUserProfile(userUID: String) {
        firestore.collection("Users").document(userUID)
            .get().addOnSuccessListener { myData ->
                _readUserProfile.postValue(myData)
            }
    }
    fun updateUserProfile(
        context: Context,
        userUID: String,
        userName: String,
        userBranch: String,
        userSemester: String,
        findNavController: NavController
    ) {
        val data = HashMap<String, String>()
        data[USER_UID] = userUID
        data[USER_NAME] = userName
        data[USER_BRANCH] = userBranch
        data[USER_SEMESTER] = userSemester

        firestore.collection("Users").document(userUID)

            .update(data as Map<String, Any>).addOnSuccessListener {
                Toast.makeText(context, " Update Successful", Toast.LENGTH_SHORT).show()
                Log.d("saveUserData", "Update Success")
                findNavController.popBackStack()
            }.addOnFailureListener {
                Toast.makeText(context, " Some Error Occurred", Toast.LENGTH_SHORT).show()
                Log.d("saveUserData", it.message!!)
                findNavController.popBackStack()
            }
    }

    fun loadNotifications() {
        firestore.collection("Notifications")
            .addSnapshotListener { value, error ->
                val docs = value?.documents
                _readNotifications.postValue(docs!!)
            }
    }
}
