package com.trendster.campus.viewmodels.subjectviewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.trendster.campus.utils.*

class SubjectViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val storageReference = FirebaseStorage.getInstance().reference

    private var _readCollection = MutableLiveData<List<DocumentSnapshot?>>()
    val readCollection: LiveData<List<DocumentSnapshot?>>
        get() = _readCollection

    private var _readClasswork = MutableLiveData<List<DocumentSnapshot?>>()
    val readClasswork: LiveData<List<DocumentSnapshot?>>
        get() = _readClasswork

    private var _submitStatus = MutableLiveData<Boolean?>()
    val submitStatus: LiveData<Boolean?>
        get() = _submitStatus

    private var _readCollExtend = MutableLiveData<List<DocumentSnapshot?>>()
    val readCollExtend: LiveData<List<DocumentSnapshot?>>
        get() = _readCollExtend

    var selectedSubjectDesc = ""
    var selectedFaculty = ""
    var selectedSubject = ""
    var selectedUserUID = ""
    var selectedUserBranch = ""
    var selectedUserSemester = ""

    fun userAuthdata(subject: String, faculty: String, subjectDesc: String, uid: String) {
        selectedSubject = subject
        selectedUserUID = uid
        selectedFaculty = faculty
        selectedSubjectDesc = subjectDesc
    }

    fun loadCollection(userUID: String, subjectName: String) {
        firestore.collection("Users").document(userUID).addSnapshotListener { value, error ->
            val userBranch = value?.get(TEMP_USER_BRANCH).toString()
            val userSemester = value?.get(TEMP_USER_SEMESTER).toString()
            selectedUserBranch = userBranch
            selectedUserSemester = userSemester
            Log.d("loadData1", userBranch + userSemester)
            loadData(userBranch, userSemester, subjectName)
        }
    }

    private fun loadData(userBranch: String, userSemester: String, subjectName: String) {
        firestore.collection("Data").document(userBranch)
            .collection(userSemester).document("Subjects")
            .collection("list").document(subjectName)
            .collection("studyMaterial")
            .addSnapshotListener { value, error ->

                val docs = value?.documents
                _readCollection.postValue(docs!!)
                Log.d("loadData", docs.size.toString())
            }
    }

    fun loadRequest(userUID: String, selectedSubject: String) {
        firestore.collection("Users").document(userUID)
            .addSnapshotListener { value, error ->
                val userBranch = value?.get(USER_BRANCH).toString()
                val userSemester = value?.get(USER_SEMESTER).toString()
                loadClasswork(userBranch, userSemester, selectedSubject)
            }
    }

    private fun loadClasswork(userBranch: String, userSemester: String, subjectName: String) {
        firestore.collection("Data").document(userBranch)
            .collection(userSemester).document("Subjects")
            .collection("list").document(subjectName)
            .collection("classwork")
            .addSnapshotListener { value, error ->

                val docs = value?.documents
                _readClasswork.postValue(docs!!)
                Log.d("loadData", docs?.size.toString())
            }
    }

    fun loadSubmissionStatus(classworkName: String) {
        firestore.collection("Data").document(selectedUserBranch)
            .collection(selectedUserSemester).document("Subjects")
            .collection("list").document(selectedSubject)
            .collection("classwork").document(classworkName)
            .collection("submissions").document(selectedUserUID)
            .addSnapshotListener { value, error ->
                val docs = value?.exists()
                _submitStatus.postValue(docs)
            }
    }

    fun uploadSubmission(
        context: Context,
        pdfName: String,
        pdfData: Uri?,
        classworkName: String

    ) {

        val reference1 = storageReference
            .child("pdf" + pdfName + "-" + System.currentTimeMillis() + ".pdf")
        pdfData?.let { reference1.putFile(it) }?.addOnSuccessListener { task ->

            val uriTask = task.storage.downloadUrl
            while (!uriTask.isComplete);

            val uri = uriTask.result
            uploadData(
                context,
                pdfName,
                uri.toString(),
                classworkName

            )
        }?.addOnFailureListener {
            showToast(context, it.message!!)
        }
    }

    private fun uploadData(context: Context, pdfName: String, pdfUrl: String, classworkName: String) {

        val data = HashMap<String, Any>()
        data["pdfUrl"] = pdfUrl
        data["pdfName"] = pdfName

        firestore.collection("Data").document(selectedUserBranch)
            .collection(selectedUserSemester).document("Subjects")
            .collection("list").document(selectedSubject)
            .collection("classwork").document(classworkName)
            .collection("submissions").document(selectedUserUID)
            .set(data).addOnSuccessListener {
                showToast(context, "Submitted Successfully")
            }
            .addOnFailureListener {
                showToast(context, it.message!!)
            }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun loadCollExtend(userUID: String, collCategory: String) {

        firestore.collection("Users").document(userUID).addSnapshotListener { value, error ->

            firestore.collection("Data").document(selectedUserBranch)
                .collection(selectedUserSemester).document("Subjects")
                .collection("list").document(selectedSubject)
                .collection("studyMaterial").document(collCategory)
                .collection("list").addSnapshotListener { value, error ->

                    val docs = value?.documents
                    _readCollExtend.postValue(docs!!)
                }
            Log.d("mySubje", selectedSubject)
        }
    }
}
