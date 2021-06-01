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

    private val _readUserName = MutableLiveData<String>()
    val readUserName: LiveData<String>
        get() = _readUserName

    private val _readFeedbackUpload = MutableLiveData<Boolean>()
    val readFeedbackUpload: LiveData<Boolean>
        get() = _readFeedbackUpload

    val _readClassLink = MutableLiveData<String>()
    val readClassLink: LiveData<String>
        get() = _readClassLink

    val _readClassLinkWeek = MutableLiveData<String>()
    val readClassLinkWeek: LiveData<String>
        get() = _readClassLinkWeek

    private val _readAttendance = MutableLiveData<List<DocumentSnapshot?>>()
    val readAttendance: LiveData<List<DocumentSnapshot?>>
        get() = _readAttendance

    private val _readSchedule = MutableLiveData<List<DocumentSnapshot?>>()
    val readSchedule: LiveData<List<DocumentSnapshot?>>
        get() = _readSchedule

    private val _readScheduleWeek = MutableLiveData<List<DocumentSnapshot?>>()
    val readScheduleWeek: LiveData<List<DocumentSnapshot?>>
        get() = _readScheduleWeek

    private val _readSubjects = MutableLiveData<Pair<List<DocumentSnapshot?>, String>>()
    val readSubjects: LiveData<Pair<List<DocumentSnapshot?>, String>>
        get() = _readSubjects

    private val _readUserProfile = MutableLiveData<DocumentSnapshot?>()
    val readUserProfile: LiveData<DocumentSnapshot?>
        get() = _readUserProfile

    private val _readNotifications = MutableLiveData<List<DocumentSnapshot?>>()
    val readNotifications: LiveData<List<DocumentSnapshot?>>
        get() = _readNotifications

    private val _readValues = MutableLiveData<Pair<String, String>>()
    val readValues: LiveData<Pair<String, String>>
        get() = _readValues

    var selectedUserBranch = "CSE"
    var selectedUserSemester = "1"

    fun loadScheduleRequest(userUID: String, type: String, requiredDay: String) {
        firestore.collection("Users").document(userUID)
            .addSnapshotListener { value, error ->
                val userBranch = value?.get(USER_BRANCH).toString()
                val userSemester = value?.get(USER_SEMESTER).toString()
                selectedUserBranch = userBranch
                selectedUserSemester = userSemester
                loadSchedule(userBranch, userSemester, requiredDay, type)
            }
    }

    fun loadRequest(userUID: String) {
        firestore.collection("Users").document(userUID)
            .addSnapshotListener { value, error ->
                val userBranch = value?.get(TEMP_USER_BRANCH).toString()
                val userSemester = value?.get(TEMP_USER_SEMESTER).toString()
                selectedUserBranch = userBranch
                selectedUserSemester = userSemester
                loadSubjects(userBranch, userSemester)
            }
    }

    private fun loadSchedule(
        userBranch: String,
        userSemester: String,
        requiredDay: String,
        type: String
    ) {
        firestore.collection("Data").document(userBranch)
            .collection(userSemester).document("Schedule")
            .collection(requiredDay).get().addOnSuccessListener { data ->
                val docs = data.documents
                when (type) {
                    "today" -> {
                        _readSchedule.postValue(docs)
                    }
                    "week" -> {
                        _readScheduleWeek.postValue(docs)
                    }
                }

                Log.d("loadSchedule", docs.size.toString())
            }
    }

    fun loadSubjects(userBranch: String, userSemester: String) {
        Log.d("NIh", userBranch + userSemester)
        firestore.collection("Data").document(userBranch)
            .collection(userSemester).document("Subjects")
            .collection("list").get().addOnSuccessListener { myData ->
                val docs = myData.documents
                _readSubjects.postValue(Pair(docs, "$userBranch, $userSemester"))
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
        val myday = today.get(Calendar.DAY_OF_MONTH)

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
                findNavController.navigateUp()
            }.addOnFailureListener {
                Toast.makeText(context, " Some Error Occurred", Toast.LENGTH_SHORT).show()
                Log.d("saveUserData", it.message!!)
                findNavController.navigateUp()
            }
    }

    fun loadNotifications() {
        firestore.collection("Notifications")
            .addSnapshotListener { value, error ->
                val docs = value?.documents
                _readNotifications.postValue(docs!!)
            }
    }

    fun sortSubject(context: Context, userUID: String, branchChip: String, semesterChip: String) {
        _readValues.postValue(Pair("null", "null"))
        val data = HashMap<String, String>()
        data[TEMP_USER_BRANCH] = branchChip
        data[TEMP_USER_SEMESTER] = semesterChip
        firestore.collection("Users").document(userUID)
            .update(data as Map<String, Any>).addOnSuccessListener {
                Toast.makeText(context, " Update Successful", Toast.LENGTH_SHORT).show()
                Log.d("saveUserData", "Update Success")
                _readValues.postValue(Pair(branchChip, semesterChip))
            }.addOnFailureListener {
                Toast.makeText(context, " Some Error Occurred", Toast.LENGTH_SHORT).show()
                Log.d("saveUserData", it.message!!)
            }
    }

    fun fetchUserName(userUID: String) {
        firestore
            .collection("Users").document(userUID)
            .addSnapshotListener { value, error ->
                _readUserName.postValue(value?.get(USER_NAME) as String)
            }
    }

    fun submitFeedBack(userUID: String, userFeedback: String) {
        val data = HashMap<String, String>()
        data[USER_FEEDBACK] = userFeedback
        firestore.collection("Users").document(userUID)
            .update(data as Map<String, Any>).addOnSuccessListener {
                _readFeedbackUpload.postValue(true)
            }.addOnFailureListener {
                _readFeedbackUpload.postValue(false)
            }
    }

    fun fetchClassLink(subjectName: String, type: String) {
        firestore.collection("Data").document(selectedUserBranch)
            .collection(selectedUserSemester).document("Subjects")
            .collection("list").document(subjectName)
            .addSnapshotListener { value, error ->
                val data = value?.get(CLASSLINK).toString()
                if (type == "today")
                    _readClassLink.postValue(data)
                else
                    _readClassLinkWeek.postValue(data)
            }
    }
}
