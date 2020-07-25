package ar.com.cristianduarte.reedettops.ui

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("makeInvisibie")
fun makeInvisible(view: View, makeInvisibie: Boolean) {
    view.visibility = if (makeInvisibie) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
}