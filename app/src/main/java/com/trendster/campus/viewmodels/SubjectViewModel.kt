package com.trendster.campus.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.trendster.campus.utils.USER_BRANCH
import com.trendster.campus.utils.USER_SEMESTER
import com.trendster.campus.utils.USER_UID

class SubjectViewModel : ViewModel(){

    private val firestore = FirebaseFirestore.getInstance()
    private var _readCollection = MutableLiveData<List<DocumentSnapshot?>>()
    val readCollection: LiveData<List<DocumentSnapshot?>>
    get() = _readCollection

    private var _readCollExtend = MutableLiveData<List<DocumentSnapshot?>>()
    val readCollExtend: LiveData<List<DocumentSnapshot?>>
    get() = _readCollExtend

    var selectedSubject = ""
    var selectedUserUID = ""
    var selectedUserBranch = ""
    var selectedUserSemester = ""

    fun userAuthdata(subject: String, uid: String){
        selectedSubject = subject
        selectedUserUID = uid
    }

    fun loadCollection(userUID : String, subjectName: String){

        firestore.collection("Users").document(userUID).addSnapshotListener { value, error ->

            val UID = value?.get(USER_UID)?.toString()
            val userBranch = value?.get(USER_BRANCH).toString()
            val userSemester = value?.get(USER_SEMESTER).toString()

            selectedUserBranch = userBranch
            selectedUserSemester = userSemester

            Log.d("loadData1",userBranch+userSemester)
            loadData(userBranch, userSemester, subjectName)
        }
    }

    private fun loadData(userBranch: String, userSemester: String, subjectName: String) {
        firestore.collection("Data").document(userBranch)
            .collection(userSemester).document("Subjects")
            .collection("List").document(subjectName)
            .collection("StudyMaterial")
            .addSnapshotListener { value, error ->

                val docs = value?.documents
                _readCollection.postValue(docs!!)
                Log.d("loadData", docs?.size.toString())

            }
    }

    fun loadCollExtend(userUID : String, collCategory: String){

        firestore.collection("Users").document(userUID).addSnapshotListener { value, error ->

//            val UID = value?.get(USER_UID)?.toString()
//            val userBranch = value?.get(USER_BRANCH).toString()
//            val userSemester = value?.get(USER_SEMESTER).toString()
//
//            Log.d("loadData1",userBranch+userSemester)
//            loadData(userBranch, userSemester, subjectName)
            firestore.collection("Data").document(selectedUserBranch)
                    .collection(selectedUserSemester).document("Subjects")
                    .collection("List").document(selectedSubject)
                    .collection("StudyMaterial").document(collCategory)
                    .collection("list").addSnapshotListener { value, error -> 
                        
                        val docs = value?.documents
                        _readCollExtend.postValue(docs!!)
                        
                    }
            Log.d("mySubje", selectedSubject)
        }
    }

}