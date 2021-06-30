
# 图片预览组件`PreviewPictureView`

几乎还原微信的图片预览，核心使用共享元素加自定义view实现

## 架构
`PagerAdapter` viewPager2的适配器，当然也可是使用其他组件来实现
`PreviewImage` 整个图片预览的核心
`PreviewPictureView` 对图片预览进行的简单封装
`PreviewTools` 启动一个简单的图片预览页面工具（对图片预览简单封装的一个activity）
`SimpleImagePreviewActivity`对图片预览简单封装的一个activity
`TestActivity` 自定义图片预览页面构建的示例


## 集成

Add it in your root build.gradle at the end of repositories:
````
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


````
Add the dependency
[![](https://jitpack.io/v/OneZeroYang/PreviewPictureView.svg)](https://jitpack.io/#OneZeroYang/PreviewPictureView)
````

dependencies {
	        implementation 'com.github.OneZeroYang:PreviewPictureView:1.1.0'
	}
````

## 如何使用

1. 启动一个简单的图片预览页面(使用详情请看`MainActivity`)

````
        // 根据当前页面上所有的图片进行适配，这里使用的是RecyclerView来显示图片
        PreviewTools.startImagePreview(this, list, findViewById(R.id.mRecyclerView), i)

        // 根据单个图片来进行适配，推荐使用这种方法，因为在日常开发中，特别是聊天页面，RecyclerView不可能全是图片
        PreviewTools.startImagePreview(this, list, myHolder.image!!, i)

        // 进行自定义的图片预览
        TestActivity.start(this, i, list, myHolder.image!!)

````


2. 如何自定义预览页面

2.1 构建图片页面的`Activity`

创建一个`activity`，并使用指定的风格 `@style/myTransparent`

````
<activity
            android:name=".TestActivity"
            android:theme="@style/myTransparent"></activity>

````

2.2 在布局文件种加入已经封装好的支持多张图片的预览控件`PreviewPictureView`，当然也可以自己去实现单张预览，或者多种预览

2.2.1 使用多张已经封装好的预览组件

在根布局下加入

加入一个view，用于改变背景的透明度

````
  <View
        android:background="@color/black"
        android:id="@+id/view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
````
加入多图片预览组件

````
 <com.batchat.preview.PreviewPictureView
        android:id="@+id/mPreviewPictureView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
````

整合xml文件预览

````
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".TestActivity">


    <View
        android:background="@color/black"
        android:id="@+id/view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>


    <com.batchat.preview.PreviewPictureView
        android:id="@+id/mPreviewPictureView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>
````

初始化控件

````
    // 封装好的图片预览控件
    private var mPreviewPictureView :PreviewPictureView<String> ? =null
    // 初始化预览控件
    mPreviewPictureView=findViewById(R.id.mPreviewPictureView)
````

设置好所需传值

````
    // 需要传递过来的数据
    private val data by lazy {
        intent?.getStringArrayListExtra("data")
    }

    // 需要传递过来的数据
    private val index by lazy {
        intent?.getIntExtra("index",0)
    }
````

设置好启动方法

````
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
````

在初始化控件以`后`加入以下代码

````
        // 开始预览
        mPreviewPictureView?.start(index?:0,data?: arrayListOf(),this)
````

重写`onBackPressed`方法

````
    // 这个是必要的，因为共享元素的返回动画
    override fun onBackPressed() {
        super.onBackPressed()
        if (Build.VERSION.SDK_INT >= 21) {
            finishAfterTransition()
        } else {
            finish()
        }
    }
````


并实现`AlphaCallback`接口用于透明度回调和预览关闭回调

````

    // 改变透明度的动画回调
    override fun onChangeAlphaCallback(alpha: Float) {
        findViewById<View>(R.id.view).alpha=alpha
    }

    // 触发关闭的回调
    override fun onChangeClose() {
        onBackPressed()
    }
````

## 混淆


````
# PreviewPictureView
-keep class com.batchat.preview.** { *; }

````
`注意：该组件使用了Glide图片加载，如果要使用混淆，请配置Glide混淆`


















