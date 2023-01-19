package org.openlake.sampoorna.presentation.features.self_care

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialFadeThrough
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.FragmentSelfCareBinding

class SelfCareFragment: Fragment() {

    private var _binding: FragmentSelfCareBinding? = null
    private val binding get() = _binding!!

    private var arrayList: ArrayList<SelfCareModel> ? = null
    private var gridView: GridView? = null
    private var selfCareAdapter: SelfCareAdapter ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSelfCareBinding.inflate(inflater, container, false)

        //adding animation
        addAnimation()
        gridView = binding.selfCareGridView
        arrayList = ArrayList()
        arrayList = setDataList()
        selfCareAdapter = SelfCareAdapter(requireContext(), arrayList!!)
        gridView?.adapter = selfCareAdapter

        return binding.root
    }

    private fun setDataList() : ArrayList<SelfCareModel> {
        val arrayList = ArrayList<SelfCareModel>()

        arrayList.add(SelfCareModel(R.drawable.womenlogo, "Music"))
        arrayList.add(SelfCareModel(R.drawable.womenlogo, "Exercise"))
        arrayList.add(SelfCareModel(R.drawable.womenlogo, "Journal"))
        arrayList.add(SelfCareModel(R.drawable.womenlogo, "Remedies"))

        return arrayList
    }

    private fun addAnimation() {
        val linearLayout: LinearLayout = binding.layout
        val animationDrawable = linearLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}