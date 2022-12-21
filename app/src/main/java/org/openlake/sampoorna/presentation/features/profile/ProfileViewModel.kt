package org.openlake.sampoorna.presentation.features.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.data.sources.entities.User

class ProfileViewModel: ViewModel() {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val user: MutableLiveData<User> = MutableLiveData()

    fun getUser() {
        viewModelScope.launch {
            db.collection(Constants.Users)
                .document(auth.uid!!)
                .addSnapshotListener { value, error ->
                    value?.let {
                        user.postValue(it.toObject(User::class.java))
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
}