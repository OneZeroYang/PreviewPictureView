package com.batchat.preview

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView


/**
 * 对外api
 * @author ZeroCode
 * @date 2021/6/29 : 10:12
 */
object PreviewTools {


    /**
     * 开始预览图片
     * @param activity
     * @param list 数据源
     * @param recyclerView 显示的列表
     * @param index 选中的项
     */
    fun startImagePreview(activity: Activity, list: ArrayList<String>, recyclerView: RecyclerView,index:Int) {

        if (list.isNullOrEmpty()) return
        val mPair: Array<androidx.core.util.Pair<View, String>?> = arrayOfNulls(recyclerView.childCount)

        for (i in 0..recyclerView.childCount){
            val view = recyclerView.getChildAt(i)
            if (view != null){
                if (index == view.tag as Int) {
                    ViewCompat.setTransitionName(view, "CONTENT")
                    mPair[i] = androidx.core.util.Pair(view, "CONTENT")
                } else {
                    ViewCompat.setTransitionName(view, "index${i}")
                    mPair[i] = androidx.core.util.Pair(view, "index${i}")
                }
            }

        }



        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity, *mPair
        )
        val intent = Intent(activity, SimpleImagePreviewActivity::class.java)
        intent.putStringArrayListExtra("data", list)
        intent.putExtra("index", index)
        // ActivityCompat是android支持库中用来适应不同android版本的
        ActivityCompat.startActivity(activity, intent, activityOptions.toBundle())

    }



    /**
     * 开始预览图片
     * @param activity
     * @param list 数据源
     * @param view 当前点击的view 用于共享元素动画
     * @param index 选中项
     */
    fun startImagePreview(activity: Activity, list: ArrayList<String>, view: View,index:Int) {

        if (list.isNullOrEmpty()) return
        val mPair: Array<androidx.core.util.Pair<View, String>?> = arrayOfNulls(1)


        ViewCompat.setTransitionName(view, "CONTENT")
        mPair[0] = androidx.core.util.Pair(view, "CONTENT")


        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity, *mPair
        )
        val intent = Intent(activity, SimpleImagePreviewActivity::class.java)
        intent.putStringArrayListExtra("data", list)
        intent.putExtra("index", index)
        // ActivityCompat是android支持库中用来适应不同android版本的
        ActivityCompat.startActivity(activity, intent, activityOptions.toBundle())
    }

}