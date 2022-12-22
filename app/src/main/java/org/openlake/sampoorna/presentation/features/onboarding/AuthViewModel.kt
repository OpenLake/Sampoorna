package org.openlake.sampoorna.presentation.features.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.data.sources.entities.User

class AuthViewModel: ViewModel() {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    private val username : MutableLiveData<String> = MutableLiveData("")
    private val email: MutableLiveData<String> = MutableLiveData("")
    private val name: MutableLiveData<String> = MutableLiveData("")
    private val password : MutableLiveData<String> = MutableLiveData("")

    fun updateName(name: String) {
        this.name.postValue(name)
    }

    fun updateEmail(email: String) {
        this.email.postValue(email)
    }

    fun updateUsername(username: String){
        this.username.postValue(username)
    }

    fun updatePassword(password: String){
        this.password.postValue(password)
    }

    fun signIn(onComplete: (Task<AuthResult>) -> Unit) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email.value!!, password.value!!)
                .addOnCompleteListener(onComplete)
        }
    }

    fun register(onComplete: (Task<AuthResult>) -> Unit) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email.value!!, password.value!!)
                .addOnCompleteListener(onComplete)
        }
    }

    fun addUser(onComplete: (Task<Void>) -> Unit) {
        viewModelScope.launch {
            val user = User(
                uid = auth.uid!!,
                name = name.value!!,
                email = email.value!!,
                username = username.value!!
            )
            db.collection(Constants.Users)
                .document(auth.uid!!)
                .set(user)
                .addOnCompleteListener(onComplete)
        }
    }
}