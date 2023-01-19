package org.openlake.sampoorna.presentation.features.userFeatures


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.data.di.Transformer
import org.openlake.sampoorna.data.repository.ContactsRepository
import org.openlake.sampoorna.data.sources.entities.User
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(contactsRepository: ContactsRepository): ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    val user: MutableLiveData<User> = MutableLiveData()

    val allContacts = Transformations.map(contactsRepository.fetchAllContacts()) { list ->
        val contacts = list.map {
            Transformer.convertContactEntityToContactModel(it)
        }
        contacts
    }

    fun getUser() {
        viewModelScope.launch {
            db.collection(Constants.Users)
                .document(auth.uid!!)
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

}