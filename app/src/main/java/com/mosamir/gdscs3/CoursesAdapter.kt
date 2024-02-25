package com.mosamir.gdscs3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CoursesAdapter () : ListAdapter<Course, CoursesAdapter.PostViewHolder>(CourseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coures_item, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
        // Set up item click listener
        holder.itemView.setOnClickListener {}
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val courseTitle: TextView = itemView.findViewById(R.id.tvCourseTitle)
        private val courseImage: ImageView = itemView.findViewById(R.id.ivCourse)

        fun bind(item: Course) {
            courseTitle.text = item.title
            courseImage.setImageResource(item.image)
        }
    }

    private class CourseDiffCallback : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }
    }
}