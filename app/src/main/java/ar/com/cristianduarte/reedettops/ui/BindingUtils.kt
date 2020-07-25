package ar.com.cristianduarte.reedettops.ui

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("makeInvisibie")
fun makeInvisible(view: View, makeInvisibie: Boolean) {
    view.visibility = if (makeInvisibie) View.INVISIBLE else View.VISIBLE
}