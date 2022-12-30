package org.openlake.sampoorna.presentation.features.blogs

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.data.sources.entities.Blog

class BlogViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()

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
}