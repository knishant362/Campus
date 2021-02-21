package com.trendster.campus.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.trendster.campus.utils.USER_BRANCH
import com.trendster.campus.utils.USER_NAME
import com.trendster.campus.utils.USER_SEMESTER
import com.trendster.campus.utils.USER_UID
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MainViewModel(context: Context): ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _readSchedule = MutableLiveData<List<DocumentSnapshot?>>()
    val readSchedule : LiveData<List<DocumentSnapshot?>>
    get() = _readSchedule

    private val _readSubjects = MutableLiveData<List<DocumentSnapshot?>>()
    val readSubjects: LiveData<List<DocumentSnapshot?>>
    get() = _readSubjects


    /** User Activity stuff*/
    fun saveUserData(
        userUID: String,
        userName: String,
        userBranch: String,
        userSemester: String
    ){
        val data = HashMap<String, String>()
        data[USER_UID] = userUID
        data[USER_NAME] = userName
        data[USER_BRANCH] = userBranch
        data[USER_SEMESTER] = userSemester

        firestore.collection("Users").document(userUID)
            .set(data).addOnSuccessListener {
                Log.d("saveUserData", userUID)
            }.addOnFailureListener{
                Log.d("saveUserData", "failed")
            }
    }


    fun loadRequest(userUID: String, type: String, requiredDay: String){
        firestore.collection("Users").document(userUID)
            .addSnapshotListener { value, error ->
                val UID = value?.get(USER_UID)?.toString()
                val userBranch = value?.get(USER_BRANCH).toString()
                val userSemester = value?.get(USER_SEMESTER).toString()

                when(type){
                    "schedule" -> {
                        loadSchedule(userBranch, userSemester, "Monday")
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
            .collection("List").get().addOnSuccessListener { myData ->
                val docs = myData.documents
                _readSubjects.postValue(docs)
                Log.d("loadSubjects", myData.toString())
            }
    }
    private fun todayDay(): String {
        val today = Calendar.getInstance()
        val date = today.time
        val day = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)

        Log.d("TIMETABLE", day)

        return day
    }


}