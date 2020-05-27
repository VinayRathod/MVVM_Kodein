package com.android.demo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import com.android.demo.base.BaseAdapter
import com.android.demo.callbacks.BaseJsonViewModelListener
import com.android.demo.data.CommonViewModelFactory
import com.android.demo.databinding.ActivityMainBinding
import com.android.demo.util.*
import com.google.gson.JsonElement
import kotlinx.android.synthetic.main.row_trending_crops.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class CommentsActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: CommonViewModelFactory by instance<CommonViewModelFactory>()
    private val viewModel by viewModels<HomeViewModel> { factory }
    lateinit var mBinding: ActivityMainBinding
    var allPosts = ArrayList<Posts>()

    data class Posts(val userId: Int, val id: Int, val title: String, val body: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.lifecycleOwner = this
        mBinding.viewmodel = viewModel
//        setSupportActionBar(toolbar)
        mBinding.ivClose.setOnClickListener {
            if (mBinding.drawer.isDrawerOpen(GravityCompat.START)) {
                mBinding.drawer.closeDrawer(GravityCompat.START)
            }
        }
        "Comments".warn(mBinding.root)
        mBinding.rvCrops.adapter = BaseAdapter(
            layoutId = R.layout.row_trending_crops, list = allPosts, viewHolder = { holder, item ->
                holder.itemView.apply {
                    holder.itemView.tv_row_title.text = item.title
                    holder.itemView.tv_row_body.text = item.body
                }
            })
        viewModel.getComments(intent.extras?.getInt("id") ?: 0, object : BaseJsonViewModelListener {
            override fun onComplete(json: JsonElement?) {
                json?.logd()
                json?.asJsonArray?.let {
                    for (j in it) {
                        allPosts.add(Posts(j.jsonInt("postId"), j.jsonInt("id"), j.jsonString("email"), j.jsonString("body")))
                    }
                }
                mBinding.rvCrops.visible(allPosts.isNotEmpty())
                mBinding.rvCrops.adapter?.notifyDataSetChanged()
            }

            override fun onError(message: String?) {
                message?.warn(mBinding.root)
            }
        })
    }
}
