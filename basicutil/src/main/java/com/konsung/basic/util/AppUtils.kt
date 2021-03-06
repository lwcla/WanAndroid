package com.konsung.basic.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import com.konsung.basic.bean.tree.SystemTreeTitle
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.config.BasicHelper


class AppUtils {

    companion object {
        val instance = AppUtils()

        private var basicConfig: BasicHelper? = null

        fun init(basicConfig: BasicHelper) {
            this.basicConfig = basicConfig
        }

        fun getBasicConfig() = basicConfig

        fun isDebug(): Boolean {
            return basicConfig?.debug() ?: true
        }

        fun getContext(): Context {
            return basicConfig?.getContext() ?: throw NullPointerException()
        }

        fun startLoginAty(context: Context?) {
            return basicConfig?.startLoginAty(context) ?: throw NullPointerException()
        }

        fun startScreenImageAty(activity: Activity?, url: String) {
            return basicConfig?.startScreenImageAty(activity, url) ?: throw NullPointerException()
        }

        fun startSystemTreeDetailAty(activity: Activity?, view: View, title: SystemTreeTitle) {
            return basicConfig?.startSystemTreeDetailAty(activity, view, title)
                    ?: throw NullPointerException()
        }

        fun startWebAty(context: Context?, title: String?, link: String?, artId: Int, collect: Boolean, dataPosition: Int = -1, needCollect: Boolean = true) {
            return basicConfig?.startWebAty(context, title, link, artId, collect, dataPosition, needCollect)
                    ?: throw NullPointerException()
        }

        fun startWebAty(activity: Activity?, context: Context?, view: View, title: String?, link: String?, artId: Int, collect: Boolean, dataPosition: Int = -1, needCollect: Boolean = true) {
            return basicConfig?.startWebAty(activity, context, view, title, link, artId, collect, dataPosition, needCollect)
                    ?: throw NullPointerException()
        }
    }

    /**
     * 复制到系统剪贴板
     */
    fun copyToClip(context: Context?, info: String?, toast: Boolean = true) {

        context?.let {

            if (info.isNullOrEmpty()) {
                if (toast) {
                    it.toast(StringUtils.TAG, R.string.data_error)
                }
                return
            }

            // 获取系统剪贴板
            val clipboard = it.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
            val clipData = ClipData.newPlainText(null, info)
            // 把数据集设置（复制）到剪贴板
            clipboard.primaryClip = clipData

            if (toast) {
                context.toast(StringUtils.TAG, R.string.link_copy_success)
            }
        }
    }

    /**
     * 是否已经登录
     *
     * @return true 已经登录  false 还没有登录
     */
    fun hasLogined(context: Context): Boolean {
        return !SpUtils.getString(context, BaseConfig.USER_NAME, "").isNullOrEmpty()
    }

    /**
     * 调用系统分享
     */
    fun shareToSystem(context: Context?, info: String?, toast: Boolean = true) {
        context?.let {

            if (info.isNullOrEmpty()) {
                if (toast) {
                    it.toast(StringUtils.TAG, R.string.data_error)
                }
                return
            }

            var shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, info)
            //切记需要使用Intent.createChooser，否则会出现别样的应用选择框，您可以试试
            shareIntent = Intent.createChooser(shareIntent, it.getString(R.string.share_to_other))
            it.startActivity(shareIntent)
        }
    }

    /**
     * 以浏览器打开
     */
    fun openByBrowser(context: Context?, link: String?, toast: Boolean = true) {

        context?.let {
            if (link.isNullOrEmpty()) {
                if (toast) {
                    it.toast(StringUtils.TAG, R.string.data_error)
                }
                return
            }

            val uri = Uri.parse(link)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            it.startActivity(intent)
        }
    }
}