package com.batchat.preview

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide


/**
 * 一个简单封装图片预览的activity
 * 通过 PreviewTools启动
 * @author ZeroCode
 * @date 2021/6/28 : 9:35
 */
class SimpleImagePreviewActivity : AppCompatActivity(), AlphaCallback {


    private val viewPager by lazy { findViewById<ViewPager2>(R.id.view_pager) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(Color.TRANSPARENT,false)
//        if (Build.VERSION.SDK_INT < 16) {
//            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        }else{
//            // Hide the status bar.
//            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
//            // Remember that you should never show the action bar if the
//            // status bar is hidden, so hide that too if necessary.
//            actionBar?.hide()
//        }
        setContentView(R.layout.activity_image_preview)
        initViewPager()

    }


    override fun onBackPressed() {
        super.onBackPressed()
        if (Build.VERSION.SDK_INT >= 21) {
            finishAfterTransition()
        } else {
            finish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }


    private val data by lazy {
        intent?.getStringArrayListExtra("data")
    }

    private val index by lazy {
        intent?.getIntExtra("index",0)
    }

    private val mPagerAdapter by lazy {
        PagerAdapter<String>(
            this@SimpleImagePreviewActivity, data
        ).apply {
            onBind { holder, position, data ->
                holder.imageView?.apply {
                    setAlphaCallback(this@SimpleImagePreviewActivity)
                    if (position==index){
                        ViewCompat.setTransitionName(holder.imageView!!, "CONTENT")
                        ViewCompat.setTransitionName(viewPager, "-1")
                    }else{
                        ViewCompat.setTransitionName(holder.imageView!!, "index${position}")
                    }
                    Glide.with(context).load(data).into(holder.imageView!!)
                }
            }
        }
    }

    private fun initViewPager() {
        viewPager.setCurrentItem(index?:0, false)
        viewPager.apply {
            adapter = mPagerAdapter
        }
        viewPager.setCurrentItem(index?:0, false)

    }

    override fun onChangeAlphaCallback(alpha: Float) {
        findViewById<View>(R.id.background_view).apply {
            setAlpha(alpha)
        }
    }

    override fun onChangeClose() {
        onBackPressed()
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

}