package com.tuanvu.quanlichitieu.future.ultis

import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("bind:imageUrl")
fun loadImage(view: ImageView, url: Int) {
    try {
        Glide.with(view.context.applicationContext)
            .load(url)
            .into(view)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}

@BindingAdapter("bind:safeClick", "bin:timeStep")
fun safeClick(view: View, onClick: View.OnClickListener, timeStep: Long) {
    view.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < timeStep) return
            else onClick.onClick(view)
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}


@BindingAdapter("bind:isSelected")
fun isSelected(view: ImageView, isSelected: Boolean) {
    try {
        if (!isSelected) view.makeGone() else view.makeVisible()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

}

@BindingAdapter("bind:isShow")
fun isSelected(view: View, isSelected: Boolean) {
    try {
        if (!isSelected) view.makeInvisible() else view.makeVisible()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

}