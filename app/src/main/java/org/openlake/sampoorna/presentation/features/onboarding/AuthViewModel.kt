package org.openlake.sampoorna.presentation.features.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openlake.sampoorna.data.auth.AuthResult
import org.openlake.sampoorna.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository : AuthRepository
) : ViewModel() {
    val authResult : MutableLiveData<AuthResult> = MutableLiveData()
    val isLoading : MutableLiveData<Boolean> = MutableLiveData(false)
    private val username : MutableLiveData<String> = MutableLiveData("")
    private val password : MutableLiveData<String> = MutableLiveData("")

    init{
        authenticate()
    }

    fun updateUsername(username: String){
        this.username.value = username
    }

    fun updatePassword(password: String){
        this.password.value = password
    }

    fun signIn() {
        viewModelScope.launch {
            isLoading.value = true
            authResult.value = authRepository.signIn(username.value!!,password.value!!)
            isLoading.value = false
        }
    }

    fun signUp() {
        viewModelScope.launch {
            isLoading.value = true
            authResult.value = authRepository.signUp(username.value!!,password.value!!)
            isLoading.value = false
        }
    }
    fun authenticate(){
        viewModelScope.launch {
            val result = authRepository.authenticate()
            if(result is AuthResult.Authorized) authResult.value = result
        }
    }
}