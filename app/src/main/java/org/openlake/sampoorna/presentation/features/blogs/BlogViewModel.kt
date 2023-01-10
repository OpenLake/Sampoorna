package org.openlake.sampoorna.presentation.features.blogs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.data.sources.entities.Blog
import org.openlake.sampoorna.data.sources.entities.Comment
import org.openlake.sampoorna.data.sources.entities.User

class BlogViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    val blogList: MutableLiveData<MutableList<Blog>> = MutableLiveData(mutableListOf())

    val searchResults: MutableLiveData<MutableList<Blog>> = MutableLiveData(mutableListOf())
    val searchQuery: MutableLiveData<String> = MutableLiveData("")
    val filterTags: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())

    val blog: MutableLiveData<Blog> = MutableLiveData()
    val savedBlogs: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())

    fun addBlog(blog: Blog, onComplete: (Task<Void>) -> Unit) {
        viewModelScope.launch {
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
                        savedBlogs.postValue(value.toObject(User::class.java)?.savedBlogs)
                    }
                    else {
                        error?.printStackTrace()
                    }
                }
        }
    }

    fun saveBlog(blogId: String, onComplete: (Task<Void>) -> Unit = {}) {
        viewModelScope.launch {
            db.collection(Constants.Users)
                .document(auth.uid!!)
                .update("savedBlogs", FieldValue.arrayUnion(blogId))
                .addOnCompleteListener(onComplete)
        }
    }

    fun deleteSavedBlog(blogId: String, onComplete: (Task<Void>) -> Unit = {}) {
        viewModelScope.launch {
            db.collection(Constants.Users)
                .document(auth.uid!!)
                .update("savedBlogs", FieldValue.arrayRemove(blogId))
                .addOnCompleteListener(onComplete)
        }
    }
}