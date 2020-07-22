package ar.com.cristianduarte.reedettops.ui.main

import androidx.lifecycle.*
import ar.com.cristianduarte.reedettops.entity.RedditPost

class MainViewModel : ViewModel() {

    val redditPosts: LiveData<List<RedditPost>> = RedditPostsLiveData()

    // TODO Just an example before asking for posts to a repository
    class RedditPostsLiveData: MutableLiveData<List<RedditPost>>() {
        init {
            value = listOf(
                RedditPost("234324", "pepe", "news important", 324234, 5435435, "dunno"),
                RedditPost("2436545", "n23io4rnif", "good novelties", 14, 54354345, "failImage"),
                RedditPost("244326gr35g", "dever", "new presentation", 1, 54354342, "failImage"),
                RedditPost("fg35gr", "prograr", "testla giga batt unveiled", 0, 54354341, "failImage"),
                RedditPost("2134324", "pepe", "news important", 324234, 5435435, "dunno"),
                RedditPost("21436545", "n23io4rnif", "good novelties", 14, 54354345, "failImage"),
                RedditPost("2444326gr35g", "dever", "new presentation", 1, 54354342, "failImage"),
                RedditPost("fgs35gr", "prograr", "testla giga batt unveiled", 0, 54354341, "failImage")
            )
        }
    }
}
