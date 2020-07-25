package ar.com.cristianduarte.reedettops.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ar.com.cristianduarte.reedettops.R
import ar.com.cristianduarte.reedettops.databinding.MainFragmentBinding
import ar.com.cristianduarte.reedettops.datasource.database.RedditPostsDatabase
import ar.com.cristianduarte.reedettops.datasource.remote.service.RedditApiDatasource
import ar.com.cristianduarte.reedettops.entity.RedditPost
import ar.com.cristianduarte.reedettops.repository.RedditPostsRepository

class MainFragment : Fragment(), RedditPostsAdapter.RedditPostDismissClickListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels() { MainViewModelFactory(RedditPostsRepository(
        RedditApiDatasource(), RedditPostsDatabase.getInstance(requireNotNull(this.activity).application).redditPostsDao
    )) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding: MainFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)

        // Necessary for binding and observing LiveData updates
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val adapter = RedditPostsAdapter(this)
        binding.redditPostsRecyclerView.adapter = adapter

        val swipeRefreshLayout = binding.swipeContainer
        viewModel.pagedRedditPosts.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                swipeRefreshLayout.isRefreshing = false
            }
        })

        // as we want "tops", we need to refresh when starting
        viewModel.fetchRedditPosts(true)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDismissClicked(redditPost: RedditPost) {
        viewModel.dismissPost(redditPost)
    }

}

class MainViewModelFactory(private val repository: RedditPostsRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = MainViewModel(repository) as T
}