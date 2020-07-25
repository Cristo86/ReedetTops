package ar.com.cristianduarte.reedettops

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import ar.com.cristianduarte.reedettops.entity.RedditPost
import ar.com.cristianduarte.reedettops.ui.details.RedditPostDetailsActivity
import ar.com.cristianduarte.reedettops.ui.main.MainFragment
import ar.com.cristianduarte.reedettops.ui.main.RedditPostDetailsFragment

class MainActivity : AppCompatActivity(), MainFragment.PostItemActionsListener {

    private var mIsDualPane: Boolean = false
    private var mDetailsFragment: RedditPostDetailsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        mIsDualPane = resources.getBoolean(R.bool.dual_pane)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.list_container, MainFragment.newInstance())
                    .commitNow()
        }

        if (mIsDualPane) {
            mDetailsFragment = RedditPostDetailsFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, mDetailsFragment!!)
                .commitNow()
        } else {
            mDetailsFragment = null
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is MainFragment) {
            fragment.setPostItemActionsListener(this)
        }
    }

    override fun onPostSelected(redditPost: RedditPost) {
        if (mIsDualPane) {
            mDetailsFragment?.displayRedditPost(redditPost)
        } else {
            startActivity(RedditPostDetailsActivity.redditPostIntent(this, redditPost.id))
        }
    }
}
