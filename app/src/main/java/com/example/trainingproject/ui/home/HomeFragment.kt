package com.example.trainingproject.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private lateinit var chatAdapter: ChatAdapter

    private lateinit var launchImageSelector: ActivityResultLauncher<Any?>

    private val imageSelectorContract = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return intent?.data
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchImageSelector = registerForActivityResult(imageSelectorContract) {
            it?.let {
                Log.d("ImagePicker", "Image Uri: $it")
                sendImageMessage(it)
            }
        }
    }

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
        setListeners()
    }

    private fun setListeners() {
        binding.buttonSend.setOnClickListener {
            sendTextMessage()
        }

        binding.buttonSelectImage.setOnClickListener {
            launchImageSelector.launch(null)
        }

        binding.buttonLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigateUp()
        }
    }

    private fun init() {
        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage
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
                val type = if (messageType.toString() == "IMAGE") MessageType.IMAGE else MessageType.TEXT
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

    private fun sendTextMessage() {
        val messageBody = binding.inputMessage.text.toString()
        val sender = auth.currentUser?.email
        val messageType = MessageType.TEXT

        sendMessage(messageBody, sender, messageType)
    }

    private fun sendImageMessage(imageUri: Uri) {
        storage.getReference(auth.currentUser?.uid!!).putFile(imageUri).addOnSuccessListener { uploadTask ->
            uploadTask.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                val messageBody = it.toString()
                val sender = auth.currentUser?.email
                val messageType = MessageType.IMAGE
                sendMessage(messageBody, sender, messageType)
            }?.addOnFailureListener {
                Log.e("SendImageMessage", "Error getting download link", it)
                Firebase.crashlytics.log(it.message ?: "Error getting image download link")
            }
        }.addOnFailureListener {
            Log.e("SendImageMessage", "Error uploading image", it)
            Firebase.crashlytics.log(it.message ?: "Error uploading image")
        }
    }

    private fun sendMessage(
        messageBody: String,
        sender: String?,
        messageType: MessageType
    ) {
        binding.inputMessage.setText("")
        firestore.collection("messages").add(
            Message(messageBody, sender, messageType, Date())
        ).addOnSuccessListener {
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