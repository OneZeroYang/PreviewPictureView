package com.batchat.previewpictureview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.batchat.preview.PreviewTools
import com.batchat.preview.TestActivity


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        findViewById<RecyclerView>(R.id.mRecyclerView).apply {
            adapter =
                Adapter(this@MainActivity) { myHolder: MyHolder, i: Int, list: ArrayList<String> ->
                    strat(myHolder, i, list)
                }
            layoutManager = LinearLayoutManager(this@MainActivity)
        }


    }

    private fun strat(
        myHolder: MyHolder,
        i: Int,
        list: ArrayList<String>,

        ) {
//        // 根据当前页面上所有的图片进行适配，这里使用的是RecyclerView来显示图片
//        PreviewTools.startImagePreview(this, list, findViewById(R.id.mRecyclerView), i)

        // 根据单个图片来进行适配，推荐使用这种方法，因为在日常开发中，特别是聊天页面，RecyclerView不可能全是图片
        PreviewTools.startImagePreview(this, list, myHolder.image!!, i)

//        // 进行自定义的图片预览
//        TestActivity.start(this, i, list, myHolder.image!!)
    }


}