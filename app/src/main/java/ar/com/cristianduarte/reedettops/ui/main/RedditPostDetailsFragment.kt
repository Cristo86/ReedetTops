package ar.com.cristianduarte.reedettops.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import ar.com.cristianduarte.reedettops.R
import ar.com.cristianduarte.reedettops.databinding.RedditPostDetailsFragmentBinding
import ar.com.cristianduarte.reedettops.datasource.database.RedditPostsDatabase
import ar.com.cristianduarte.reedettops.datasource.remote.service.RedditApiDatasource
import ar.com.cristianduarte.reedettops.entity.RedditPost
import ar.com.cristianduarte.reedettops.repository.RedditPostsRepository
import kotlinx.coroutines.launch

class RedditPostDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = RedditPostDetailsFragment()
    }

    // TODO delete private val viewModel: RedditPostDetailsViewModel by viewModels()
    private val viewModel: RedditPostDetailsViewModel by viewModels {
        RedditPostDetailsViewModelFactory(
            RedditPostsRepository(
                RedditApiDatasource(),
                RedditPostsDatabase.getInstance(requireNotNull(this.activity).application).redditPostsDao
            )
        )
    }

    lateinit var binding: RedditPostDetailsFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.reddit_post_details_fragment, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    fun displayRedditPost(redditPostId: String) {
        lifecycleScope.launch {
            viewModel.redditPostById(redditPostId)
            binding.viewModel = viewModel
            binding.executePendingBindings()
            viewModel.markRedditPostAsRead(redditPostId)
        }
    }

    fun displayRedditPost(redditPost: RedditPost) {
        binding.viewModel = viewModel
        viewModel.currentRedditPost = redditPost
        binding.executePendingBindings()
        lifecycleScope.launch {
            viewModel.markRedditPostAsRead(redditPost.id)
        }
    }
}

class RedditPostDetailsViewModelFactory(private val repository: RedditPostsRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = RedditPostDetailsViewModel(repository) as T
}