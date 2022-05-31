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
import com.example.trainingproject.ui.home.adapter.ChatAdapter
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private lateinit var chatAdapter: ChatAdapter

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
        init()
        listenToMessages()

        binding.buttonSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun init() {
        auth = Firebase.auth
        firestore = Firebase.firestore
        setupRecycler()
    }

    private fun listenToMessages() {
        firestore.collection("messages").addSnapshotListener { snapshots, e ->
            if (e != null) {
                Firebase.crashlytics.log(e.message ?: "Unknown error")
                Log.e("Chat", "${e.message}", e)
                return@addSnapshotListener
            }

            val messages = mutableSetOf<Message>()

            for (document in snapshots!!.documents) {
                val messageType = document.data?.get("type")
                val type = if (messageType.toString() == "TEXT") MessageType.TEXT else MessageType.IMAGE
                val message = Message(
                    body = document.data?.get("body") as? String,
                    sender = document.data?.get("sender") as? String,
                    type = type,
                    date = (document.data?.get("date") as Timestamp).toDate(),
                    messageId = document.id
                )
                messages.add(message)
            }

            chatAdapter.submitList(messages.toList().sortedBy { it.date })
        }
    }

    private fun setupRecycler() {
        chatAdapter = ChatAdapter()
        chatAdapter.submitList(listOf())
        binding.chatRecycler.adapter = chatAdapter
    }

    private fun sendMessage() {
        val messageBody = binding.inputMessage.text.toString()
        val sender = auth.currentUser?.email
        val messageType = MessageType.TEXT

        firestore.collection("messages").add(
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