package ar.com.cristianduarte.reedettops.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ar.com.cristianduarte.reedettops.databinding.ItemRedditPostBinding
import ar.com.cristianduarte.reedettops.entity.RedditPost

class RedditPostsAdapter(private val dismissClickListener: RedditPostDismissClickListener) :
    PagedListAdapter<RedditPost, RedditPostsAdapter.ViewHolder>(RedditPostDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent, dismissClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(
        private val binding: ItemRedditPostBinding,
        private val dismissClickListener: RedditPostDismissClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RedditPost?) {
            binding.redditPost = item
            binding.dismissClickListener = dismissClickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, dismissClickListener: RedditPostDismissClickListener): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRedditPostBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, dismissClickListener)
            }
        }
    }

    interface RedditPostDismissClickListener {
        fun onDismissClicked(redditPost: RedditPost);
    }
}

class RedditPostDiffCallback : DiffUtil.ItemCallback<RedditPost>() {

    override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
        return oldItem == newItem
    }
}
