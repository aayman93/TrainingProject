package com.example.trainingproject.ui.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.trainingproject.data.models.User
import com.example.trainingproject.databinding.ItemUserBinding
import javax.inject.Inject

class UsersAdapter @Inject constructor() : ListAdapter<User, UsersAdapter.ViewHolder>(UsersDiffer) {

    private var onUserClickListener: ((User) -> Unit)? = null

    fun setOnUserClickListener(listener: (User) -> Unit) {
        onUserClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser = getItem(position)
        holder.bind(currentUser)
    }

    companion object UsersDiffer : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            with(binding) {
                tvUsername.text = user.username
                tvEmail.text = user.email
                ivProfileImage.load(user.profilePictureUrl)

                root.setOnClickListener {
                    onUserClickListener?.invoke(user)
                }
            }
        }
    }
}