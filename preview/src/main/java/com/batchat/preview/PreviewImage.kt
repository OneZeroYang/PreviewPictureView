package com.batchat.preview

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewConfiguration
import com.github.chrisbanes.photoview.PhotoView
import kotlin.math.abs

/**
 * 自定义图片预览控件
 * 在`PhotoView` 的基础之上 加入上下滑动
 *
 *
 *
 * @author ZeroCode
 * @date 2021/6/28 : 11:25
 */
class PreviewImage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : PhotoView(context, attrs, defStyleAttr) {


    // 手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    private var x1 = 0f
    private var x2 = 0f
    private var y1 = 0f
    private var y2 = 0f

    // 是否拦截事件
    private var intercept = false

    // 透明度回调
    private var mAlphaCallback: AlphaCallback? = null


    // 用于记录当前图片所在位置
    private var locationX = 0
    private var locationY = 0

    // 用于记录透明度
    private var mAlpha = 1.0f

    // 是否单机关闭
    var isClickClose =false


    // 最小关闭大小，用于图片缩小到多少后关闭
    var minSize = 0.5f


    init {
        // 设置最小比例
        minimumScale = 0.3f
        setOnClickListener {
            if (isClickClose) mAlphaCallback?.onChangeClose()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }


    // 这里重写onTouchEvent没有效果，因为PhotoView需要设置onTouchEvent，这样会造成冲突
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            // 按下
            MotionEvent.ACTION_DOWN -> {
                // 拦截viewpager2的事件
                parent?.requestDisallowInterceptTouchEvent(true)
                y1 = event.y
                x1 = event.x
                attacher.onTouch(this, event)
            }
            MotionEvent.ACTION_UP -> {
                parent.requestDisallowInterceptTouchEvent(true)
                y2 = event.y
                x2 = event.x

                ObjectAnimator.ofInt(this, "locationX", locationX, 0).start()
                ObjectAnimator.ofInt(this, "locationY", locationY, 0).start()

                // 如果比例小于5，并且只有一个触控得情况下，关闭预览
                if (scale < minSize && event?.pointerCount == 1) {
                    mAlphaCallback?.onChangeClose()
                    return true
                }

                // 如果是手动放大，这里不做复原
                if (scale <= 1.0f) {
                    ObjectAnimator.ofFloat(this,"scale",scale, 1.0f).start()
                }
                ObjectAnimator.ofFloat(this, "malpha", mAlpha, 1.0f).start()
                attacher.onTouch(this, event)
                intercept = false

            }
            MotionEvent.ACTION_MOVE -> {
                y2 = event.y
                x2 = event.x
                // 判断为滑动事件 并且需要在1个手指头的时候触发
                if (abs(y1 - y2) > ViewConfiguration.get(context).scaledTouchSlop && event?.pointerCount == 1 && scale <= 1.0f) {
                    intercept = true
                    moveAndScale(event, x1 - x2, y1 - y2)
                    return true
                } else if (event?.pointerCount > 1 || abs(x1 - x2) > ViewConfiguration.get(context).scaledTouchSlop) {
                    if (!intercept)
                        attacher.onTouch(this, event)
                }else if( scale > 1.0f){
                    attacher.onTouch(this, event)
                }
            }
        }

        return true
    }


    private fun setMalpha(alpha: Float) {
        mAlpha = alpha
        mAlphaCallback?.onChangeAlphaCallback(alpha)
    }

    // 用于动画设置的属性
    private fun setLocationX(x: Int) {
        locationX = x
        scrollTo(locationX, locationY)
    }

    // 用于动画设置的属性
    private fun setLocationY(y: Int) {
        locationY = y
    }


    // 这里是处理移动、放大缩小、透明度的关键代码
    private fun moveAndScale(event: MotionEvent, fl: Float, fl1: Float): Boolean {

        locationX = fl.toInt()
        locationY = fl1.toInt()
        scrollTo(locationX, locationY)

        // 如果手指向下滑动了一半，则开始改变背景透明度和图片大小
        if (event.y > height / 2) {
            // 滑动比例
            val fl2 = (height - event.y)
            // 计算出大小 越往下越小
            var cale = fl2 / (height / 2)
            if (cale <= 0.3f) cale = 0.3f
            scale = cale
            setMalpha(cale)

        }
        return false
    }


    /**
     * 设置透明度回调
     * @param mAlphaCallback
     */
    fun setAlphaCallback(mAlphaCallback: AlphaCallback) {
        this.mAlphaCallback = mAlphaCallback
    }


}


/**
 * 回调
 * @author ZeroCode
 * @date 2021/6/28 : 16:13
 */
interface AlphaCallback {
    // 透明度改变回调
    fun onChangeAlphaCallback(alpha: Float)
    // 关闭预览回调
    fun onChangeClose()
}