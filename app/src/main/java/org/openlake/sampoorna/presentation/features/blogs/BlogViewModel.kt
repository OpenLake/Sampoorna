package org.openlake.sampoorna.presentation.features.blogs

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.data.sources.entities.Blog
import org.openlake.sampoorna.data.sources.entities.Comment
import org.openlake.sampoorna.data.sources.entities.User
import java.util.UUID

class BlogViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    val blogList: MutableLiveData<MutableList<Blog>> = MutableLiveData(mutableListOf())

    val searchResults: MutableLiveData<MutableList<Blog>> = MutableLiveData(mutableListOf())
    val searchQuery: MutableLiveData<String> = MutableLiveData("")
    val filterTags: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())

    val blog: MutableLiveData<Blog> = MutableLiveData()
    val savedBlogIds: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    val savedBlogs: MutableLiveData<MutableList<Blog>> = MutableLiveData(mutableListOf())

    //updated add blog to handle blog Id properly and returning boolean instead of task
    suspend fun addBlog(blog: Blog): Boolean  {
        return try {
            val blogId = UUID.randomUUID().toString()
            val blogWithId = blog.copy(blogId = blogId)

            val result = db.collection(Constants.Blogs)
                .document(blogId)
                .set(blogWithId)
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getBlogs() {
        viewModelScope.launch {
            db.collection(Constants.Blogs)
                .addSnapshotListener { value, error ->
                    if(value != null) {
                        blogList.postValue(value.toObjects(Blog::class.java))
                        if(searchQuery.value.isNullOrEmpty()) {
                            searchResults.postValue(value.toObjects(Blog::class.java))
                        }
                        Log.d("BLOGS", value.toObjects(Blog::class.java).toString())
                    }
                    else {
                        error?.printStackTrace()
                    }
                }
        }
    }

    fun getBlog(blogId: String) {
        viewModelScope.launch {
            db.collection(Constants.Blogs)
                .document(blogId)
                .addSnapshotListener { value, error ->
                    if(value != null) {
                        blog.postValue(value.toObject(Blog::class.java))
                    }
                    else {
                        error?.printStackTrace()
                    }
                }
        }
    }

    fun addComment(comment: Comment, onComplete: (Task<Void>) -> Unit) {
        viewModelScope.launch {
            db.collection(Constants.Comments)
                .add(comment)
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        it.result.update("commentId", it.result.id)
                            .addOnCompleteListener(onComplete)
                    }
                    else {
                        it.exception?.printStackTrace()
                    }
                }
        }
    }

    fun getComments(postId: String): MutableLiveData<MutableList<Comment>> {
        val comments: MutableLiveData<MutableList<Comment>> = MutableLiveData(mutableListOf())
        viewModelScope.launch {
            db.collection(Constants.Comments)
                .whereEqualTo("commentedOnId", postId)
                .addSnapshotListener { value, error ->
                    if(value != null) {
                        comments.postValue(value.toObjects(Comment::class.java))
                    }
                    else {
                        error?.printStackTrace()
                    }
                }
        }
        return comments
    }

    fun getSavedBlogIds() {
        viewModelScope.launch {
            db.collection(Constants.Users)
                .document(auth.uid!!)
                .addSnapshotListener { value, error ->
                    if(value != null) {
                        savedBlogIds.postValue(value.toObject(User::class.java)?.savedBlogs)
                    }
                    else {
                        error?.printStackTrace()
                    }
                }
        }
    }

    fun getSavedBlogs(ids: MutableList<String>) {
        if(ids.isEmpty()) {
            savedBlogs.postValue(mutableListOf())
            return
        }
        viewModelScope.launch {
            db.collection(Constants.Blogs)
                .whereIn("blogId", ids)
                .addSnapshotListener { value, error ->
                    if(value != null) {
                        savedBlogs.postValue(value.toObjects(Blog::class.java))
                    }
                    else {
                        error?.printStackTrace()
                    }
                }
        }
    }

    fun saveBlog(blogId: String, onComplete: (Task<Void>) -> Unit = {}) {
        db.collection(Constants.Users)
            .document(auth.uid!!)
            .update("savedBlogs", FieldValue.arrayUnion(blogId))
            .addOnCompleteListener(onComplete)
    }


    fun deleteSavedBlog(blogId: String, onComplete: (Task<Void>) -> Unit = {}) {
        viewModelScope.launch {
            db.collection(Constants.Users)
                .document(auth.uid!!)
                .update("savedBlogs", FieldValue.arrayRemove(blogId))
                .addOnCompleteListener(onComplete)
        }
    }

    fun getUser(uid: String): MutableLiveData<User> {
        val user: MutableLiveData<User> = MutableLiveData()
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
        return user
    }
}