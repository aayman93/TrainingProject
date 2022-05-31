package com.example.trainingproject.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trainingproject.data.models.Message
import com.example.trainingproject.data.models.MessageType
import com.example.trainingproject.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val messageBody = binding.inputMessage.text.toString()
        val sender = Firebase.auth.currentUser?.email
        val messageType = MessageType.Text

        Firebase.firestore.collection("messages").add(
            Message(messageBody, sender, messageType, Date())
        ).addOnSuccessListener {
            binding.inputMessage.setText("")
            Log.d("FIRESTORE", "Message ID: ${it.id}, message: $messageBody")
        }.addOnFailureListener {
            Log.e("FIRESTORE", "Sending failed...", it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}