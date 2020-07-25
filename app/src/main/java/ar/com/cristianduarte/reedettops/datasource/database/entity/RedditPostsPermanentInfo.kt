package ar.com.cristianduarte.reedettops.datasource.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "reddit_post_permanent_info")
data class RedditPostPermanentInfo (
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "locally_read")
    var locallyRead: Boolean = false,

    @ColumnInfo(name = "dismissed")
    var dismissed: Boolean = false
)