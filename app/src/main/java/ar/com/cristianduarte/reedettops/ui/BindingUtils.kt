package ar.com.cristianduarte.reedettops.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import ar.com.cristianduarte.reedettops.R
import com.bumptech.glide.Glide

@BindingAdapter("makeInvisibie")
fun makeInvisible(view: View, makeInvisibie: Boolean) {
    view.visibility = if (makeInvisibie) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("makeInvisibleIfStringBlank")
fun makeInvisibleIfStringBlank(view: View, str: String?) {
    view.visibility = if (str.isNullOrBlank()) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    if (imgUrl.isNullOrBlank()) {
        imgView.setImageResource(0)
        return
    }
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
}

@BindingAdapter("timeAgoSecs")
fun timeAgoSecs(textView: TextView, timestampSecs: Long) {
    val diff = System.currentTimeMillis() / 1000 - timestampSecs
    val resources = textView.context.resources
    if (diff <= 3600) { // less than one hour, minutes
        textView.text = resources.getString(R.string.minutes_ago, diff / 60)
    } else if (diff <= 172_800) { // less than 48 hs, hours
        textView.text = resources.getString(R.string.hours_ago, diff / 3600)
    } else { // in days
        textView.text = resources.getString(R.string.days_ago, diff / 86_400)
    }
}