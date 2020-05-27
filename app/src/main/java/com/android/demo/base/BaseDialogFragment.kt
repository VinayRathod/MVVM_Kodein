package com.android.demo.base

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.android.demo.R

abstract class BaseDialogFragment : DialogFragment() {
    fun show(fragmentManager: FragmentManager?) {
        setStyle(STYLE_NORMAL, R.style.CustomDialogActivityTheme)
        fragmentManager?.let {
            if (!fragmentManager.isDestroyed && !fragmentManager.isStateSaved) {
                try {
                    val ft = it.beginTransaction()
                    ft.add(this, "dialog")
                    try {
                        ft.commit()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        try {
                            ft.commitAllowingStateLoss()
                        } catch (e: IllegalStateException) {
                            e.printStackTrace()
                        }
                    }
//                    show(it.beginTransaction(), "dialog")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun setWindowAttribute(isFullScreen: Boolean = false) {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.windowAnimations = if (isFullScreen) R.style.DialogAnimationSlide else R.style.DialogAnimationFade

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //            getDialog().getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
            dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            dialog?.window?.statusBarColor = resources.getColor(android.R.color.transparent)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<View>(R.id.iv_close)?.setOnClickListener {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.onDismiss()
    }

    var onDismissListener: OnDismissListener? = null

    interface OnDismissListener {
        fun onDismiss()
    }

    fun setDismissListener(onDismissListener: OnDismissListener) {
        this@BaseDialogFragment.onDismissListener = onDismissListener
    }

}