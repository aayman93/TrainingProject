package com.example.trainingproject.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingproject.data.models.Message
import com.example.trainingproject.databinding.ItemReceivedMessageBinding
import com.example.trainingproject.databinding.ItemSentMessageBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatAdapter : ListAdapter<Message, RecyclerView.ViewHolder>(MessageComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            val binding = ItemReceivedMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ReceivedMessageViewHolder(binding)
        } else {
            val binding = ItemSentMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            SentMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = getItem(position)
        when (getItemViewType(position)) {
            VIEW_TYPE_MESSAGE_RECEIVED -> (holder as ReceivedMessageViewHolder).bind(currentMessage)
            else -> (holder as SentMessageViewHolder).bind(currentMessage)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = getItem(position)
        return if (currentMessage.sender == Firebase.auth.currentUser?.email) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    companion object {
        const val VIEW_TYPE_MESSAGE_SENT = 1
        const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }
}