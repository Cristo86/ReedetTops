package ar.com.cristianduarte.reedettops.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import ar.com.cristianduarte.imagedownload.ImageDownloader
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

        const val PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }

    private val redditPostDetailsActionsListener = RedditPostDetailsActionsListener()

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
        binding.actionsListener = redditPostDetailsActionsListener

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

    fun saveImageToGallery(imageUrl: String) {
        if (context == null) {
            return
        }

        var success = false
        lifecycleScope.launch {
            success = ImageDownloader().saveImageToGallery(
                requireContext().applicationContext, imageUrl)
            showSaveImageResult(success)
        }
    }

    private fun showSaveImageResult(success: Boolean) {
        Toast.makeText(
            requireContext(),
            if (success) R.string.successful_saving_image else R.string.error_saving_image,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun saveImageToGalleryBehindPermissions(imageUrl: String) {
        // TODO test in ANDROID Q
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray(), PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
        } else {
            saveImageToGallery(imageUrl)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission was granted.
                    viewModel.currentRedditPost.previewImageUrl?.let { saveImageToGallery(it) }
                } else {
                    // permission denied.
                    // tell the user the action is cancelled
                    Toast.makeText(requireContext(), R.string.cannot_save_image, Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    inner class RedditPostDetailsActionsListener {
        fun onSaveImageClick(imageUrl: String) {
            saveImageToGalleryBehindPermissions(imageUrl)
        }

        fun onFullScreenImageClick(imageUrl: String) {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(imageUrl)
            startActivity(i)
        }
    }
}

class RedditPostDetailsViewModelFactory(private val repository: RedditPostsRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = RedditPostDetailsViewModel(repository) as T
}