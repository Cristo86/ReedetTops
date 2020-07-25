package ar.com.cristianduarte.reedettops.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ar.com.cristianduarte.reedettops.R
import ar.com.cristianduarte.reedettops.ui.main.RedditPostDetailsFragment

class RedditPostDetailsActivity : AppCompatActivity() {

    private lateinit var mDetailsFragment: RedditPostDetailsFragment
    private var redditPostId: String? = null

    companion object {
        val EXTRA_REDDIT_POST_ID = "reddit_post_id"

        fun redditPostIntent(context: Context, redditPostId: String): Intent {
            return Intent(context, RedditPostDetailsActivity::class.java).apply {
                putExtra(EXTRA_REDDIT_POST_ID, redditPostId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reddit_post_details)

        redditPostId = intent.getStringExtra(EXTRA_REDDIT_POST_ID)

        mDetailsFragment = RedditPostDetailsFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_container, mDetailsFragment)
            .commitNow()
    }

    override fun onStart() {
        super.onStart()
        redditPostId?.let { mDetailsFragment.displayRedditPost(it) }
    }
}
