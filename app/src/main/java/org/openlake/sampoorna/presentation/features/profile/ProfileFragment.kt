package org.openlake.sampoorna.presentation.features.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.App
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.databinding.FragmentProfileBinding


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    private val args: ProfileFragmentArgs by navArgs()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var profileViewModel: ProfileViewModel

    private val IMAGE_PICK_REQUEST = 1
    private val IMAGE_CAPTURE_REQUEST = 2

    var isEditing : Boolean = false

    private var cameraLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK && result.data != null) {
            val bitmap = result.data!!.extras?.get("data") as Bitmap
            profileViewModel.tempProfileBitmap.postValue(bitmap)
        }
    }

    private var galleryLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK && result.data != null) {
            val uri = result.data!!.data as Uri
            val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().contentResolver, uri))
            profileViewModel.tempProfileBitmap.postValue(bitmap)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)

        sharedPreferences = requireActivity().getSharedPreferences(Constants.Sampoorna, Context.MODE_PRIVATE)

        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        profileViewModel.getUser(args.uid)

        if(args.uid != auth.uid) {
            binding.profileEditFab.visibility = View.GONE
        }
        else {
            binding.userName.text = sharedPreferences.getString(Constants.Name, "")
            binding.userUsername.text = sharedPreferences.getString(Constants.Username, "")
            sharedPreferences.getInt(Constants.Age, -1).let {
                if(it != -1) {
                    binding.userAge.text = "$it years"
                }
            }
            binding.userEmail.text = sharedPreferences.getString(Constants.Email, "")
            binding.userAbout.text = sharedPreferences.getString(Constants.About, "")
        }

        profileViewModel.user.observe(viewLifecycleOwner){ user->
            binding.userName.text = user.name
            user.age?.let {
                binding.userAge.text = "$it years"
            }
            binding.userEmail.text = user.email
            binding.userAbout.text = user.about
            binding.userUsername.text = user.username
            Glide.with(requireContext())
                .load(user.photoUrl)
                .placeholder(R.drawable.womenlogo)
                .centerCrop()
                .into(binding.userImage)

            if(user.uid == auth.uid) {
                sharedPreferences.edit()
                    .putString(Constants.Name, user.name)
                    .putString(Constants.Username, user.username)
                    .putInt(Constants.Age, user.age ?: -1)
                    .putString(Constants.Email, user.email)
                    .putString(Constants.About, user.about)
                    .apply()
            }
        }

        binding.profileEditFab.setOnClickListener {
            if(!App.isOnline(requireActivity())) {
                Toast.makeText(requireContext(),"Please connect to the internet to perform any edits",Toast.LENGTH_SHORT).show()
            }
            else if(isEditing)
            {
                val name = binding.userNameEdit.text.toString()
                val age = binding.userAgeEdit.text.toString()
                val about = binding.userAboutEdit.text.toString()

                if(name.isEmpty() || age.isEmpty()) {
                    Toast.makeText(requireContext(),"One or more fields are empty",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                profileViewModel.updateUser(name, if(age.isEmpty()) null else age.toInt(), about) {
                    if(it.isSuccessful) {
                        Toast.makeText(requireContext(),"Changes saved",Toast.LENGTH_SHORT).show()
                        closeEditingViews()
                    }
                    else {
                        it.exception?.printStackTrace()
                        Toast.makeText(requireContext(),it.exception?.message ?: "",Toast.LENGTH_SHORT).show()
                    }
                }

                profileViewModel.tempProfileBitmap.observe(viewLifecycleOwner) { profileBitmap ->
                    profileBitmap?.let { bitmap ->
                        profileViewModel.uploadProfileImage(bitmap) {
                            if(it.isSuccessful) {
                                Toast.makeText(requireContext(), "Profile image updated", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                it.exception?.printStackTrace()
                            }
                            profileViewModel.tempProfileBitmap.postValue(null)
                        }
                    }
                }
            }
            else
            {
                openEditingViews()
            }
        }

        val imageDialog = AlertDialog.Builder(requireContext()).setTitle("Change profile image").setItems(arrayOf("Upload from gallery", "Take a new picture")) { dialogInterface, i ->
            val (intent, requestCode) = when (i) {
                0 -> {
                    Pair(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also { it.type = "image/*" }, IMAGE_PICK_REQUEST)
                }
                else -> {
                    Pair(Intent(MediaStore.ACTION_IMAGE_CAPTURE), IMAGE_CAPTURE_REQUEST)
                }
            }
            Log.d("reqout", requestCode.toString())
            if(requestCode == IMAGE_PICK_REQUEST) {
                galleryLauncher.launch(intent)
            }
            else {
                cameraLauncher.launch(intent)
            }
        }.create()

        profileViewModel.tempProfileBitmap.observe(viewLifecycleOwner) {
            if(it == null) {
                profileViewModel.user.observe(viewLifecycleOwner) { user ->
                    Glide.with(requireContext())
                        .load(user.photoUrl)
                        .placeholder(R.drawable.womenlogo)
                        .centerCrop()
                        .into(binding.userImage)
                }
            }
            else {
                binding.userImage.setImageBitmap(it)
            }
        }

        binding.editImage.setOnClickListener {
            imageDialog.show()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if(isEditing) {
                closeEditingViews()
                profileViewModel.tempProfileBitmap.postValue(null)
            }
            else {
                findNavController().popBackStack()
            }
        }

        binding.profileScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if(scrollY>oldScrollY) {
                binding.profileEditFab.shrink()
            } else {
                binding.profileEditFab.extend()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun closeEditingViews()
    {
        isEditing = false
        binding.profileEditFab.setIconResource(R.drawable.ic_edit)
        binding.profileEditFab.text = getString(R.string.edit)

        binding.userName.visibility = View.VISIBLE
        binding.userNameEditLayout.visibility = View.GONE

        binding.editImageCard.visibility = View.GONE

        binding.userAge.visibility = View.VISIBLE
        binding.userAgeEditLayout.visibility = View.GONE

        binding.userAbout.visibility = View.VISIBLE
        binding.userAboutEditLayout.visibility = View.GONE

        binding.userUsername.visibility = View.VISIBLE
    }

    private fun openEditingViews()
    {
        isEditing = true
        binding.profileEditFab.setIconResource(R.drawable.ic_done)
        binding.profileEditFab.text = getString(R.string.save)

        binding.userNameEdit.setText(binding.userName.text)
        binding.userName.visibility = View.GONE
        binding.userNameEditLayout.visibility = View.VISIBLE

        binding.editImageCard.visibility = View.VISIBLE

        val age = binding.userAge.text.toString().slice(0..(binding.userAge.text.toString().length-7))
        binding.userAgeEdit.setText(when(age){
            "?" -> ""
            else -> age
        })
        binding.userAge.visibility = View.GONE
        binding.userAgeEditLayout.visibility = View.VISIBLE

        binding.userAboutEdit.setText(binding.userAbout.text)
        binding.userAbout.visibility = View.GONE
        binding.userAboutEditLayout.visibility = View.VISIBLE

        binding.userUsername.visibility = View.GONE
    }

}