package com.trendster.campus.viewmodels.mainviewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.trendster.campus.utils.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MainViewModel(context: Context): ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val storageReference = FirebaseStorage.getInstance().reference
    var pdfUploading = MutableLiveData<Boolean>()

    private val _readAccessLevel = MutableLiveData<String>()
    val readAccessLevel: LiveData<String>
        get() = _readAccessLevel

    private val _readAttendance = MutableLiveData<List<DocumentSnapshot?>>()
    val readAttendance: LiveData<List<DocumentSnapshot?>>
        get() = _readAttendance

    private val _readSchedule = MutableLiveData<List<DocumentSnapshot?>>()
    val readSchedule : LiveData<List<DocumentSnapshot?>>
    get() = _readSchedule

    private val _readSubjects = MutableLiveData<List<DocumentSnapshot?>>()
    val readSubjects: LiveData<List<DocumentSnapshot?>>
    get() = _readSubjects

    private val _readUserProfile = MutableLiveData<DocumentSnapshot?>()
    val readUserProfile: LiveData<DocumentSnapshot?>
        get() = _readUserProfile

    private val _readNotificationStatus = MutableLiveData<String>()
    val readNotificationStatus: LiveData<String>
        get() = _readNotificationStatus

    private val _readNotifications = MutableLiveData<List<DocumentSnapshot?>>()
    val readNotifications: LiveData<List<DocumentSnapshot?>>
        get() = _readNotifications



    fun loadRequest(userUID: String, type: String, requiredDay: String){
        firestore.collection("Users").document(userUID)
            .addSnapshotListener { value, error ->
                val UID = value?.get(USER_UID)?.toString()
                val userBranch = value?.get(USER_BRANCH).toString()
                val userSemester = value?.get(USER_SEMESTER).toString()
                val accessLevel = value?.get(ACCESS_LEVEL).toString()
                _readAccessLevel.postValue(accessLevel)


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
                todayDay()
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

    fun getTimeAndDate(): HashMap<String, String> {
        val today = Calendar.getInstance()
        val instantMap: HashMap<String, String> = HashMap()

        instantMap["year"] = today.get(Calendar.YEAR).toString()
        instantMap["month"] = today.get(Calendar.MONTH).toString()
        instantMap["day"] = today.get(Calendar.DAY_OF_MONTH).toString()
        instantMap["hour"] = today.get(Calendar.HOUR_OF_DAY).toString()
        instantMap["minute"] = today.get(Calendar.MINUTE).toString()

        return instantMap
    }

    /**Attendance Section*/

    fun loadAttendance(userUID: String){
        firestore.collection("Users").document(userUID)
            .collection("Attendance").addSnapshotListener { value, error ->
                val docs = value?.documents
                _readAttendance.postValue(docs!!)
            }
    }



    fun loadUserProfile(userUID: String){
        firestore.collection("Users").document(userUID)
            .get().addOnSuccessListener { myData ->
                _readUserProfile.postValue(myData)
            }

    }
    fun updateUserProfile(
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

            .update(data as Map<String, Any>).addOnSuccessListener {
                Log.d("saveUserData", userUID)
            }.addOnFailureListener{
                Log.d("saveUserData", "failed")
            }
    }

    /** Admin Functions =*/

    fun uploadSchedule(
            branchChip: String,
            semesterChip: String,
            typeChip: String,
            dayChip: String,
            lectureNoChip: String,
            tshChip: String,
            tsmChip: String,
            tehChip: String,
            temChip: String,
            subjectName: String,
            facultyName: String,
            roomNo: String) {

        val data = HashMap<String, String>()

        data[F_SUBJECT_NAME] = subjectName
        data[F_FACULTY_NAME] = facultyName
        data[F_LECTURE_TYPE] = typeChip
        data[F_TSH] = tshChip
        data[F_TSM] = tsmChip
        data[F_TEH] = tehChip
        data[F_TEM] = temChip
        data[F_ROOM_N0] = roomNo


        firestore.collection("Data").document(branchChip)
                .collection(semesterChip).document("Schedule")
                .collection(dayChip).document(lectureNoChip)
                .set(data).addOnSuccessListener {
                    Log.d("uploadSchedule", "Success")
                }.addOnFailureListener {
                    Log.d("uploadSchedule", "Failed")
                }

    }


    /** ADD Study Material*/

    fun uploadSubjectMaterial(
            fileTitle: String,
            pdfName: String,
            pdfData: Uri?,
            pdfDesc: String,
            branchChip: String,
            semesterChip: String,
            subjectName: String,
            materialCategory: String,

    ) {

        val reference1 = storageReference
                .child("pdf" + pdfName + "-" + System.currentTimeMillis() + ".pdf")
        pdfData?.let { reference1.putFile(it) }?.addOnSuccessListener { task ->

            val uriTask = task.storage.downloadUrl
            while (!uriTask.isComplete);

            val uri = uriTask.result
            uploadData(
                    fileTitle,
                    pdfName,
                    uri.toString(),
                    pdfDesc,
                    branchChip,
                    semesterChip,
                    subjectName,
                    materialCategory
            )

        }?.addOnFailureListener {
            pdfUploading.postValue(false)
        }
    }

    private fun uploadData(
            fileTitle: String,
            pdfName: String,
            pdfUrl: String,
            pdfDesc: String,
            branchChip: String,
            semesterChip: String,
            subjectName: String,
            materialCategory: String
    ) {

        val data = HashMap<String, String>()
        val dummydata = HashMap<String, String>()
        dummydata["dummy"] = "1"                          // dummy data to prevent italic problem
        data["pdfName"] = pdfName
        data["pdfUrl"] = pdfUrl
        data["pdfDesc"] = pdfDesc

        Log.d("1File",fileTitle )
        Log.d("1File",pdfName )
        Log.d("1File", pdfUrl)
        Log.d("1File",pdfDesc )
        Log.d("1File",branchChip )
        Log.d("1File",semesterChip )
        Log.d("1File",subjectName )
        Log.d("1File",materialCategory )

        val part1 = firestore.collection("Data").document(branchChip)
                .collection(semesterChip).document("Subjects")
                .collection("List").document(subjectName)
        part1.set(dummydata)                                                                  // set dummy data to prevent italic problem
        val part2 = part1.collection("StudyMaterial").document(materialCategory)  //now set our desired data
        part2.set(dummydata)                                                                 // set dummy data to prevent italic problem
        part2.collection("list").document(fileTitle)                             //now set our desired data
                .set(data)
                .addOnCompleteListener {
                    pdfUploading.postValue(true)
                }.addOnFailureListener {
                    pdfUploading.postValue(false)
                }

    }

    fun saveNotifications(title: String, message: String) {

        val myMap : HashMap<String, String> = HashMap()
        val body : HashMap<String, String> = HashMap()
        body["title"] = title
        body["message"] = message
        myMap.putAll(getTimeAndDate())
        myMap.putAll(body)

        Log.d("saveTime", message)
        firestore.collection("Notifications").document(title)
            .set(myMap).addOnSuccessListener {
                _readNotificationStatus.postValue("true")
            }.addOnFailureListener {
                _readNotificationStatus.postValue(it.message.toString())
            }
    }

    fun loadNotifications(){
        firestore.collection("Notifications")
            .addSnapshotListener { value, error -> 
                val docs = value?.documents
                _readNotifications.postValue(docs!!)
            }
    }

    fun fetchStudents(){

        firestore.collection("Users")
                .whereEqualTo("userBranch", "CSE")
                .whereEqualTo("userSemester", "6")
                .get().addOnSuccessListener {
                    Log.d("teacherData", it.documents.size.toString())
                }
    }
}