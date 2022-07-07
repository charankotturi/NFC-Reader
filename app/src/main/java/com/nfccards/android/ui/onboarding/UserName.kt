package com.nfccards.android.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nfccards.android.MainActivity
import com.nfccards.android.R
import com.nfccards.android.databinding.FragmentUserNameBinding
import com.nfccards.android.model.User
import com.nfccards.android.resources.Resource
import java.lang.ClassCastException

class UserName : Fragment() {

    private lateinit var binding: FragmentUserNameBinding
    private val userData = MutableLiveData<Resource<User>>()
    private var userExits = false
    private var username = ""
    private var userNameChanged = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_name, container, false)

        binding.btnSend.setOnClickListener {
            if (binding.etUserName.text.toString().length < 5){
                Toast.makeText(requireActivity(), "username should contain at least 5 words!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (username != "" && username != binding.etUserName.text.toString()) {
                userNameChanged = true
            }

            if (!userExits || userNameChanged){
                val db = Firebase.firestore
                val phoneNum = "${(activity as SignInActivity).getSignInViewModel().phoneNumber}"

                val user = hashMapOf(
                    "phoneNum" to phoneNum,
                    "username" to binding.etUserName.text.toString()
                )

                db.collection("user")
                    .document(phoneNum)
                    .set(user)
                    .addOnSuccessListener { _ ->
                        userData.value = Resource.Success(data = User(
                            phoneNum = phoneNum,
                            username = binding.etUserName.text.toString()
                        ))

                        startActivity(Intent(activity, MainActivity::class.java))
                        activity?.finish()
                    }
                    .addOnFailureListener { error ->
                        userData.value = Resource.Error(message = error.message.toString())
                    }
            } else {
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }
        }

        userData.observe(viewLifecycleOwner) { it ->
            when(it){
                is Resource.Error -> {
                    Firebase.auth.signOut()
                    findNavController().navigate(R.id.action_userName_to_phoneNumber)
                    Toast.makeText(requireActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    username = it.data?.username ?: ""
                    binding.etUserName.setText(it.data?.username)


                }
            }
        }

        return binding.root
    }

    fun getUser(docId: String){
        userData.value = Resource.Loading()
        val db = Firebase.firestore

        db.collection("user")
            .document(docId)
            .get()
            .addOnSuccessListener { result ->
                if (result.exists()) {
                    userData.value = Resource.Success(data = User(
                        phoneNum = result.data?.get("phoneNum") as String,
                        username = result.data?.get("username") as String,
                    ))
                    userExits = true
                }
            }
            .addOnFailureListener { error ->
                userData.value = Resource.Error(message = error.message.toString())
            }
    }

    override fun onStart() {
        try {
            (activity as SignInActivity).setPageNumber(2)
        }catch (e: ClassCastException){e.printStackTrace()}

        super.onStart()
    }
}