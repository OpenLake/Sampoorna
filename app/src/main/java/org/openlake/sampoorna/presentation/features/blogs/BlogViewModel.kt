package org.openlake.sampoorna.presentation.features.blogs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.data.sources.entities.Blog

class BlogViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val blogList: MutableLiveData<MutableList<Blog>> = MutableLiveData(mutableListOf())
    val searchResults: MutableLiveData<MutableList<Blog>> = MutableLiveData(mutableListOf())
    val searchQuery: MutableLiveData<String> = MutableLiveData("")

    fun addBlog(blog: Blog, onComplete: (Task<Void>) -> Unit) {
        db.collection(Constants.Blogs)
            .add(blog)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    db.collection(Constants.Blogs)
                        .document(it.result.id)
                        .update("blogId", it.result.id)
                        .addOnCompleteListener(onComplete)
                }
                else {
                    it.exception?.printStackTrace()
                }
            }
    }

    fun getBlogs() {
        db.collection(Constants.Blogs)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    blogList.postValue(it.result.toObjects(Blog::class.java))
                    if(searchQuery.value.isNullOrEmpty()) {
                        searchResults.postValue(it.result.toObjects(Blog::class.java))
                    }
                }
                else {
                    it.exception?.printStackTrace()
                }
            }
    }
}