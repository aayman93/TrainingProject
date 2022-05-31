package com.example.trainingproject.ui.home.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.trainingproject.data.models.Message
import com.example.trainingproject.data.models.MessageType
import com.example.trainingproject.databinding.ItemReceivedMessageBinding
import com.example.trainingproject.util.getReadableDateTime

class ReceivedMessageViewHolder(
    private val binding: ItemReceivedMessageBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(chatMessage: Message) {
        with(binding) {
            if (chatMessage.type == MessageType.TEXT) {
                tvMessageContent.text = chatMessage.body
                showText()
            } else {
                showImage()
                image.load(chatMessage.body)
            }
            tvTimestamp.text = chatMessage.date?.getReadableDateTime()
        }
    }

    private fun showText() {
        with(binding) {
            tvMessageContent.isVisible = true
            image.isVisible = false
        }
    }

    private fun showImage() {
        with(binding) {
            tvMessageContent.isVisible = false
            image.isVisible = true
        }
    }
}