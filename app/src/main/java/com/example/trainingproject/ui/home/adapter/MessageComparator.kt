package com.example.trainingproject.ui.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.trainingproject.data.models.Message

object MessageComparator : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.messageId == newItem.messageId
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}