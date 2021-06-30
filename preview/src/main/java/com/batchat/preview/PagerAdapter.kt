package com.batchat.preview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList


/**
 * PagerAdapter
 * @author ZeroCode
 * @date 2021/6/28 : 11:03
 */
class PagerAdapter<E>(private val context: Context, private val list: ArrayList<E>?) :
    RecyclerView.Adapter<PagerAdapter.MyViewHolder>() {

    private var if_onBind: OnBind<E>? = null

    interface OnBind<in E> {
        fun onBind(holder: MyViewHolder, position: Int, data: E?)
    }

    fun setOnBind(if_onBind: OnBind<E>) {
        this.if_onBind = if_onBind
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_pager_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if_onBind?.onBind(holder , position, list?.get(position) as? E)

    }


    fun onBind(l: (holder: MyViewHolder, position: Int, data: E?) -> Unit) {
        setOnBind(object : OnBind<E> {
            override fun onBind(holder: MyViewHolder, position: Int, data: E?) {
                l(holder, position, data)
            }
        })
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView:PreviewImage?=null

        init {
            imageView=view.findViewById(R.id.image_content)
        }
    }
}