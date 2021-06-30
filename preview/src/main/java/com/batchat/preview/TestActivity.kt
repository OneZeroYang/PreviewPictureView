package com.batchat.preview

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat

/**
 * 测试的activity
 * @author ZeroCode
 * @date 2021/6/30 : 9:57
 */
class TestActivity : AppCompatActivity(), AlphaCallback {


    // 封装好的图片预览控件
    private var mPreviewPictureView :PreviewPictureView<String> ? =null

    // 需要传递过来的数据
    private val data by lazy {
        intent?.getStringArrayListExtra("data")
    }

    // 需要传递过来的数据
    private val index by lazy {
        intent?.getIntExtra("index",0)
    }

    companion object{
        // 启动方法，作为参考
        fun start(activity: AppCompatActivity,index:Int,list: ArrayList<String>,view: View){
            // 构建共享元素的集合，可以多个，但需要注意一一对应
            // 详情请看文档 https://developer.android.com/guide/navigation/navigation-animate-transitions?hl=zh-cn
            val mPair: Array<androidx.core.util.Pair<View, String>?> = arrayOfNulls(1)

            ViewCompat.setTransitionName(view, "CONTENT")
            mPair[0] = androidx.core.util.Pair(view, "CONTENT")


            val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, *mPair
            )
            val intent = Intent(activity, TestActivity::class.java)
            intent.putStringArrayListExtra("data", list)
            intent.putExtra("index", index)
            // ActivityCompat是android支持库中用来适应不同android版本的
            ActivityCompat.startActivity(activity, intent, activityOptions.toBundle())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置状态栏，根据需求自定义
        changeStatusBarColor(Color.TRANSPARENT,false)
        setContentView(R.layout.activity_test)
        // 初始化预览控件
        mPreviewPictureView=findViewById(R.id.mPreviewPictureView)
        // 开始预览
        mPreviewPictureView?.start(index?:0,data?: arrayListOf(),this)

    }


    // 这个是必要的，因为共享元素的返回动画
    override fun onBackPressed() {
        super.onBackPressed()
        if (Build.VERSION.SDK_INT >= 21) {
            finishAfterTransition()
        } else {
            finish()
        }
    }




    /**
     * 改变状态栏颜色
     * @param color
     * @param isCilp 是否需要padding状态栏高度，如果需要自己实现状态栏逻辑就传入false
     * @param dl 如果要兼容DrawerLayout则传入
     */
    fun changeStatusBarColor(
        @ColorInt color: Int,
        isCilp: Boolean = true,
        dl: androidx.drawerlayout.widget.DrawerLayout? = null
    ) {
        // 如果dl不为空则都使用半透明，因为dl可能拉出白色背景
        if (dl != null) {
            StatusBarUtil.setStatusBarLightMode(this, false)
            StatusBarUtil.setColorTranslucentForDrawerLayout(this, dl, color)
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 如果版本号大于等于M，则必然可以修改状态栏颜色
            StatusBarUtil.setColor(this, color, isCilp)
            StatusBarUtil.setStatusBarLightModeByColor(this, color)
            return
        }
        // 这里处理的是版本号低于M的系统
        // 判断设置的颜色是深色还是浅色，然后设置statusBar的文字颜色
        val status = StatusBarUtil.setStatusBarLightModeByColor(this, color)
        // fixme 如果手机机型不能改状态栏颜色就不允许开启沉浸式,如果业务需求请修改代码逻辑
        if (!status) {
            // 如果状态栏的文字颜色改变失败了则设置为半透明
            StatusBarUtil.setColorTranslucent(this, color, isCilp)
        } else {
            // 如果状态栏的文字颜色改变成功了则设置为全透明
            StatusBarUtil.setColor(this, color, isCilp)
            // 改变了状态栏后需要重新设置一下状态栏文字颜色
            StatusBarUtil.setStatusBarLightModeByColor(this, color)
        }

    }


    // 改变透明度的动画回调
    override fun onChangeAlphaCallback(alpha: Float) {
        findViewById<View>(R.id.view).alpha=alpha
    }

    // 触发关闭的回调
    override fun onChangeClose() {
        onBackPressed()
    }
}