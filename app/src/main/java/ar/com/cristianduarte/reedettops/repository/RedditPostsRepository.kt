package ar.com.cristianduarte.reedettops.repository

import ar.com.cristianduarte.reedettops.entity.RedditPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RedditPostsRepository {

    fun redditPosts(): Flow<List<RedditPost>> = flow {
        // TODO Just an example before asking for posts to the network and the db
        emit(listOf(
            RedditPost("234324", "pepe", "news important", 324234, 5435435, "dunno"),
            RedditPost("2436545", "n23io4rnif", "good novelties", 14, 54354345, "failImage"),
            RedditPost("244326gr35g", "dever", "new presentation", 1, 54354342, "failImage"),
            RedditPost("fg35gr", "prograr", "testla giga batt unveiled", 0, 54354341, "failImage"),
            RedditPost("2134324", "pepe", "news important", 324234, 5435435, "dunno"),
            RedditPost("21436545", "n23io4rnif", "good novelties", 14, 54354345, "failImage"),
            RedditPost("2444326gr35g", "dever", "new presentation", 1, 54354342, "failImage"),
            RedditPost("fgs35gr", "prograr", "testla giga batt unveiled", 0, 54354341, "failImage")
        ))
    }

}