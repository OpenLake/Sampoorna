package org.openlake.sampoorna.presentation.features.blogs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.data.sources.entities.Blog
import org.openlake.sampoorna.data.sources.entities.Comment

class BlogViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val blogList: MutableLiveData<MutableList<Blog>> = MutableLiveData(mutableListOf())

    val searchResults: MutableLiveData<MutableList<Blog>> = MutableLiveData(mutableListOf())
    val searchQuery: MutableLiveData<String> = MutableLiveData("")
    val filterTags: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())

    val comments: MutableLiveData<MutableList<Comment>> = MutableLiveData(mutableListOf())

    val blog: MutableLiveData<Blog> = MutableLiveData()

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

    fun getBlog(blogId: String) {
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

    fun addComment(comment: Comment, onComplete: (Task<Void>) -> Unit) {
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

    fun getComments(postId: String) {
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
}