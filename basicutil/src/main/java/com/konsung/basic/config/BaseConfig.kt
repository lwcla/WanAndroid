package com.konsung.basic.config

class BaseConfig {

    companion object {

        val TAG: String = BaseConfig::class.java.simpleName

        const val DATABASE_NAME = "claDemo.db"

        const val USER_NAME = "userName"

        const val BASE_URL = "https://www.wanandroid.com/"


        const val WEB_URL = "webViewUrl"
        const val WEB_TITLE = "webViewTitle"
        const val WEB_DATA_POSITION = "webViewDataPosition"
        const val WEB_ARTICLE_ID = "webViewArticleId"
        /**
         * 是否已经收藏
         */
        const val IS_COLLECT = "isCollect"
        /**
         * 是否需要收藏按钮
         */
        const val NEED_COLLECT = "needCollect"

        const val COLLECT_RESULT_ACTION = "collect_result_action"

        /**
         * 收藏结果
         */
        const val COLLECT_RESULT = "collectResult"
        /**
         * 收藏的数据所在的位置
         */
        const val COLLECT_DATA_POSITION = "collectDataPosition"
        /**
         * 是去收藏还是取消收藏
         */
        const val TO_COLLECT = "toCollect"
        /**
         * 收藏文章的id
         */
        const val COLLECT_ID = "collectId"
    }

}