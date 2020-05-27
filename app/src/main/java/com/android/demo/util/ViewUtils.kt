package com.android.demo.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.android.demo.R
import com.androidadvance.topsnackbar.TSnackbar
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

fun Activity.showAlert(message: String?) {
    if (!isDestroyed && message?.isNotEmpty() == true)//
        MaterialAlertDialogBuilder(this)//
            .setTitle("Smytten")//
            .setMessage(message)//
            .setPositiveButton("Ok") { _, _ -> }//
            .show()
}

fun EditText.disableEmoji() {
    filters = arrayOf<InputFilter>(EmojiExcludeFilter())
}

fun TextView.setHtml(text: String) {
    setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))
}

private class EmojiExcludeFilter : InputFilter {
    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned?, dstart: Int, dend: Int): CharSequence? {
        for (i in start until end) {
            val type = Character.getType(source[i])
            if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                return ""
            }
        }
        return null
    }
}

fun Activity.showAlertExit(message: String?, positiveButton: String = "Ok", title: String = "Smytten") {
    if (!isDestroyed)//
        MaterialAlertDialogBuilder(this)//
            .setTitle(title)//
            .setMessage(message)//
            .setPositiveButton(positiveButton) { _, _ -> finish() }//
            .setCancelable(false)//
            .setOnCancelListener { finish() }//
            .show()
}

fun DialogFragment.showAlertExit(message: String?) {
    activity?.let {
        if (!it.isDestroyed)//
            MaterialAlertDialogBuilder(it)//
                .setTitle("Smytten")//
                .setMessage(message)//
                .setPositiveButton("Ok") { _, _ -> dismiss() }//
                .setCancelable(false)//
                .setOnCancelListener { dismiss() }//
                .show()
    }
}

fun String.share(context: Context?) {
    try {// buildShortDynamicLink()?.addOnSuccessListener { shortDynamicLink ->
        Logger.analytics("share", "content_type", this)
        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, this)
        sendIntent.type = "text/plain"
        context?.startActivity(Intent.createChooser(sendIntent, "share using"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

//<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
fun shareTextImage(context: Context?, imageUrl: String, desc: String) = runBlocking {
    val url = URL(imageUrl)
    withContext(Dispatchers.IO) {
        try {
            val input = url.openStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }?.let { bitmap ->
        try {
            val imgBitmapPath = MediaStore.Images.Media.insertImage(context?.contentResolver, bitmap, "share_smytten", null)
            val imgBitmapUri = Uri.parse(imgBitmapPath)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            shareIntent.putExtra(Intent.EXTRA_STREAM, imgBitmapUri)
            shareIntent.setType("image/png")
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            shareIntent.putExtra(Intent.EXTRA_TEXT, desc)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject text")
            context?.startActivity(Intent.createChooser(shareIntent, "Share this"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun String.toast(context: Context?) {
    if (context != null) Toast.makeText(context, this, Toast.LENGTH_LONG).show()
}

fun View.showHide() {
    if (visibility == View.VISIBLE) hide() else show()
}

fun View.show() {
    if (visibility != View.VISIBLE) visibility = View.VISIBLE
}

fun View.invisible() {
    if (visibility != View.INVISIBLE) visibility = View.INVISIBLE
}

fun View.hide() {
    if (visibility != View.GONE) visibility = View.GONE
}

fun MutableLiveData<Boolean>.toggle() {
    this.value = !(this.value ?: false)
}

fun View.setBackgroundInt(color: String, fraction: Float) {
    setBackgroundColor(ColorUtils.blendARGB(Color.parseColor(color), Color.parseColor("#00000000"), fraction))
}

fun View.visible(visible: Boolean?) {
    visible?.let {
        this.visibility = if (it) View.VISIBLE else View.GONE
    }
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar -> snackbar.setAction("Ok") { snackbar.dismiss() } }.show()
}

fun Fragment.addArgument(key: String, value: String): Fragment {
    val bundle = Bundle()
    bundle.putString(key, value)
    arguments = bundle
    return this
}

fun String.alert(activity: Activity?) {
    activity?.let { MaterialAlertDialogBuilder(it).setMessage(this).setNegativeButton("Ok", null).show() }
}

fun String.warn(view: View?) {
    try {
        if (this.isNotBlank() && view != null && view.context != null) {
            val snackbar = TSnackbar.make(view, this, TSnackbar.LENGTH_SHORT)
            val textView = snackbar.view.findViewById<View>(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
            textView.typeface = ResourcesCompat.getFont(view.context, R.font.proxima_nova_reg)
            if (this.contains("<body", true))
                textView.setHtml(this)
            textView.setTextColor(Color.WHITE)
            textView.textSize = 26f
            textView.gravity = Gravity.CENTER
            textView.setPaddingRelative(35, 35, 35, 35)
            textView.constraintMargin(35)
            snackbar.setActionTextColor(Color.WHITE)
            snackbar.view.setBackgroundColor(Color.parseColor("#10192a"))
            snackbar.show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun availableMB(): Float {
    val rt: Runtime = Runtime.getRuntime()
    val bytesAvailable = rt.maxMemory()
    return bytesAvailable / BYTES_IN_MB
}

fun freeMB(): Float {
    val rt: Runtime = Runtime.getRuntime()
    val bytesUsed = rt.totalMemory()
    val mbUsed = bytesUsed / BYTES_IN_MB
    return availableMB() - mbUsed
}

var BYTES_IN_KB = 1024.0f
var BYTES_IN_MB = BYTES_IN_KB * BYTES_IN_KB

fun ImageView.loadImage(activity: Context?, resId: Int?) {
    try {
        ("free memory: " + freeMB() + "/" + availableMB()).logd()
        if (resId == null || activity == null || freeMB() > BYTES_IN_KB * 50) return
        GlideApp.with(activity.applicationContext).load(resId).apply(
            RequestOptions().placeholder(loader).error(loader)
        ).thumbnail(GlideApp.with(activity.applicationContext).load(loader)).into(this)
    } catch (e: OutOfMemoryError) {
        e.printStackTrace()
    }
}

interface OnGifReadyListener {
    fun onGifReady(resource: GifDrawable?)
}

var loader = R.drawable.ic_launcher_foreground
fun ImageView.loadImage(activity: Context?, key: String?) {
//    if (BuildConfig.DEBUG) loadImageCache(activity, key) else
    loadImage1(activity, key)
}

fun ImageView.loadImage1(activity: Context?, key: String?) {
    try {
        ("free memory: " + freeMB() + "/" + availableMB()).logd()
        if (freeMB() < 10) {
            return
        }
        if (key.isNullOrBlank() || activity == null || freeMB() > BYTES_IN_MB * 150) return
        GlideApp.with(activity.applicationContext)//
            .load(key)//
            .apply(RequestOptions().placeholder(loader).error(loader))//
            .thumbnail(GlideApp.with(activity.applicationContext).load(loader))//
            .into(this)
        System.gc()
    } catch (e: OutOfMemoryError) {
        e.printStackTrace()
    }
}

fun Fragment.putInt(key: String, value: Int): Fragment {
    val bundle = if (this.arguments != null) this.arguments else Bundle()
    bundle?.putInt(key, value)
    this.arguments = bundle
    return this
}

fun Fragment.putString(key: String, value: String): Fragment {
    val bundle = if (this.arguments != null) this.arguments else Bundle()
    bundle?.putString(key, value)
    this.arguments = bundle
    return this
}

fun Any.hideKeyboard(act: Activity?) {
    if (act != null) {
        val imm = act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = act.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(act)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun View.getViewHeight(): Int {
    measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    return measuredHeight
}

fun EditText.showKeyboard(act: Activity?) {
    if (act != null) {
        this.setSelection(this.text.toString().length)
        this.requestFocus()
        val imm = act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun SpannableString.setSpan(clickListener: View.OnClickListener, start: Int, end: Int) {
    val clickableSpan1 = object : ClickableSpan() {
        override fun onClick(textView: View) {
            clickListener.onClick(textView)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = Color.parseColor("#0077bb")
//            resources.getColor(R.color.click_text)
            ds.isUnderlineText = true
        }
    }
    setSpan(clickableSpan1, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
}

fun TextView.setUnderline() {
    val spannableString = SpannableString(text.toString())
    spannableString.setSpan(UnderlineSpan(), 0, text.length, 0)
    text = spannableString
}

fun TextView.setStrikeThru() {
    setPaintFlags(getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
}

fun TextView.setSpan(ss: SpannableString) {
    setText(ss)
//    setHighlightColor(Color.TRANSPARENT)
    setMovementMethod(LinkMovementMethod.getInstance())
}

fun <T> Class<T>.start(act: Activity?) {
    try {
        act?.startActivity(Intent(act, this))
        act?.finish()
    } catch (e: ActivityNotFoundException) {
        e.message?.toast(act)
    } catch (e: Exception) {
        e.message?.toast(act)
    }
}

fun <T> Class<T>.open(act: Activity?) {
    try {
        act?.startActivity(Intent(act, this))
    } catch (e: ActivityNotFoundException) {
        e.message?.toast(act)
    } catch (e: Exception) {
        e.message?.toast(act)
    }
}

fun TextView.disableCopyPaste() {
    isLongClickable = false
    setTextIsSelectable(false)
    customSelectionActionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu): Boolean {
            return false
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {}
    }
}

fun View.constraintMargin(margin: Int) {
    if (layoutParams is ConstraintLayout.LayoutParams) {
        val param: ConstraintLayout.LayoutParams = layoutParams as ConstraintLayout.LayoutParams
        param.margin(margin)
        layoutParams = param
    }
}

fun ConstraintLayout.LayoutParams.margin(margin: Int) {
    this.setMargins(margin, margin, margin, margin)
}

fun CardView.margin(margin: Int) {
    val param = layoutParams as ViewGroup.MarginLayoutParams
    param.setMargins(margin, margin, margin, margin)
    layoutParams = param
}

fun View.padding(paddingSide: Int, padding: Int) {
    this.setPadding(paddingSide, padding, paddingSide, padding)
}

fun View.padding(padding: Int) {
    this.setPadding(padding, padding, padding, padding)
}

fun TabLayout.wrapTabIndicatorToTitle(externalMargin: Int) {
    val tabStrip = getChildAt(0)
    if (tabStrip is ViewGroup) {
        val childCount = tabStrip.childCount
        for (i in 0 until childCount) {
            val tabView = tabStrip.getChildAt(i)
            //set minimum width to 0 for instead for small texts, indicator is not wrapped as expected
            tabView.minimumWidth = 0
            // set padding to 0 for wrapping indicator as title
            tabView.setPadding(0, tabView.paddingTop, 0, tabView.paddingBottom)
            // setting custom margin between tabs
            if (tabView.layoutParams is ViewGroup.MarginLayoutParams) {
                val layoutParams = tabView.layoutParams as ViewGroup.MarginLayoutParams
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    layoutParams.setMarginStart(externalMargin)
                    layoutParams.setMarginEnd(externalMargin)
                    layoutParams.leftMargin = externalMargin
                    layoutParams.rightMargin = externalMargin
                } else {
                    layoutParams.leftMargin = externalMargin
                    layoutParams.rightMargin = externalMargin
                }
            }
        }
        requestLayout()
    }
}