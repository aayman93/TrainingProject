package com.example.trainingproject.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.trainingproject.model.Location
import com.example.trainingproject.databinding.ItemLocationBinding

class LocationAdapter(
    private val onItemClicked: (location: Location) -> Unit
) : PagingDataAdapter<Location, LocationAdapter.LocationViewHolder>(LocationComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding = ItemLocationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val currentLocation = getItem(position)
        if (currentLocation != null) {
            holder.bind(currentLocation)
        }
    }

    inner class LocationViewHolder(
        private val binding: ItemLocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: Location) {
            with(binding) {
                name.text = location.name
                address.text = location.address
                logo.load(location.photo)
                root.setOnClickListener {
                    onItemClicked(location)
                }
            }
        }
    }

    companion object LocationComparator : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }
    }
}