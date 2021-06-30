package com.batchat.previewpictureview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Adapter(private val context: Context, private val onClick: (MyHolder,Int,ArrayList<String>) -> Unit) : RecyclerView.Adapter<MyHolder>() {


    val list= arrayListOf<String>().apply {
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn1-q.mafengwo.net%2Fs6%2FM00%2FFC%2FCC%2FwKgB4lNzI2yAK4tdAAELj6RBVtE37.jpeg%3FimageMogr2%252Fthumbnail%252F%21310x207r%252Fgravity%252FCenter%252Fcrop%252F%21310x207%252Fquality%252F90&refer=http%3A%2F%2Fn1-q.mafengwo.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1627524301&t=b2b39785c50fd1130a19c7df69aee6d9")
        add("https://img1.baidu.com/it/u=1800733240,778446138&fm=224&fmt=auto&gp=0.jpg")
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201409%2F08%2F20140908130732_kVXzh.jpeg&refer=http%3A%2F%2Fcdn.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1627524301&t=5b10bc79d75635ac2c0b4089ce8b6fa6")
        add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201307%2F03%2F20130703071814_c2Jwj.jpeg&refer=http%3A%2F%2Fcdn.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1627524301&t=cbc87d92d879cf9503cf85ccd8df19bc")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
       return MyHolder(LayoutInflater.from(context).inflate(R.layout.item_test_layout,parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.image?.tag = position
        Glide.with(holder!!.image!!).load(list[position]).into(holder!!.image!!)
        holder.image?.setOnClickListener {
            onClick.invoke(holder,position,list)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}


class MyHolder(view:View):RecyclerView.ViewHolder(view){
    var image:AppCompatImageView?=null
    init {
        image=view.findViewById(R.id.image)
    }
}