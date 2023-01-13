package org.openlake.sampoorna.presentation.features.profile

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.data.sources.entities.User
import java.io.ByteArrayOutputStream

class ProfileViewModel: ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = Firebase.storage

    val user: MutableLiveData<User> = MutableLiveData()

    val tempProfileBitmap: MutableLiveData<Bitmap?> = MutableLiveData(null)

    fun getUser(uid: String) {
        viewModelScope.launch {
            db.collection(Constants.Users)
                .document(uid)
                .addSnapshotListener { value, error ->
                    if(value != null) {
                        user.postValue(value.toObject(User::class.java))
                    }
                    else {
                        error?.printStackTrace()
                    }
                }
        }
    }

    fun updateUser(name: String, age: Int?, about: String, onComplete: (Task<Void>) -> Unit) {
        viewModelScope.launch {
            db.collection(Constants.Users)
                .document(auth.uid!!)
                .update(mapOf(
                    Constants.Name to name,
                    Constants.Age to age,
                    Constants.About to about
                ))
                .addOnCompleteListener(onComplete)
        }
    }

    fun uploadProfileImage(bitmap: Bitmap, onComplete: (Task<Void>) -> Unit) {
        viewModelScope.launch {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            storage.getReference("${auth.uid!!}/pfp.jpg")
                .putBytes(baos.toByteArray())
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        it.result.storage.downloadUrl.addOnCompleteListener { task ->
                            if(task.isSuccessful) {
                                db.collection(Constants.Users)
                                    .document(auth.uid!!)
                                    .update("photoUrl", task.result.toString())
                                    .addOnCompleteListener(onComplete)
                            }
                            else {
                                task.exception?.printStackTrace()
                            }
                        }
                    }
                    else {
                        it.exception?.printStackTrace()
                    }
                }
        }
    }

}