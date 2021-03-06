/*
 * Copyright (C) 2016 Francisco José Montiel Navarro.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.konsung.basic.net.cookie

import android.content.Context
import com.konsung.basic.config.BaseConfig
import com.konsung.basic.net.cookie.cache.CookieCache
import com.konsung.basic.net.cookie.cache.SetCookieCache
import com.konsung.basic.net.cookie.persistence.CookiePersistor
import com.konsung.basic.net.cookie.persistence.SharedPrefsCookiePersistor
import com.konsung.basic.util.SpUtils
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.util.*

class PersistentCookieJar(private val context: Context) : ClearAbleCookieJar {

    companion object {
        val TAG: String = PersistentCookieJar::class.java.simpleName
    }

    private val cache: CookieCache
    private val persistor: CookiePersistor

    init {
        cache = SetCookieCache()
        persistor = SharedPrefsCookiePersistor(context)

        this.cache.addAll(persistor.loadAll())
    }

    @Synchronized
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
//        Debug.info(TAG, "PersistentCookieJar saveFromResponse url=$url")
        cache.addAll(cookies)
        persistor.saveAll(filterPersistentCookies(cookies))
    }

    private fun filterPersistentCookies(cookies: List<Cookie>): List<Cookie> {
        val persistentCookies = ArrayList<Cookie>()

        for (cookie in cookies) {
            if (cookie.persistent) {
//                Debug.info(TAG, "PersistentCookieJar filterPersistentCookies cookie=$cookie")
                persistentCookies.add(cookie)
            }
        }
        return persistentCookies
    }

    @Synchronized
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
//        Debug.info(TAG, "PersistentCookieJar loadForRequest url=$url")
        val cookiesToRemove = ArrayList<Cookie>()
        val validCookies = ArrayList<Cookie>()

        val it = cache.iterator()
        while (it.hasNext()) {
            val currentCookie = it.next()

            if (isCookieExpired(currentCookie)) {
                cookiesToRemove.add(currentCookie)
                it.remove()
                SpUtils.delete(context, BaseConfig.USER_NAME)
            } else if (currentCookie.matches(url)) {
//                Debug.info(TAG, "PersistentCookieJar loadForRequest cookid=$currentCookie")
                validCookies.add(currentCookie)
            }
        }

        persistor.removeAll(cookiesToRemove)

        return validCookies
    }

    /**
     * cookie是否过期
     */
    private fun isCookieExpired(cookie: Cookie): Boolean {
        return cookie.expiresAt < System.currentTimeMillis()
    }

    @Synchronized
    override fun clearSession() {
//        Debug.info(TAG, "PersistentCookieJar clearSession")
        cache.clear()
        cache.addAll(persistor.loadAll())
        SpUtils.delete(context, BaseConfig.USER_NAME)
    }

    @Synchronized
    override fun clear() {
//        Debug.info(TAG, "PersistentCookieJar clear")
        cache.clear()
        persistor.clear()
        SpUtils.delete(context, BaseConfig.USER_NAME)
    }
}
