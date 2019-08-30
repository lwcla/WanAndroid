package com.konsung.basic.bean

/**
 * admin : false
 * chapterTops : []
 * collectIds : []
 * email :
 * icon :
 * id : 29648
 * nickname : ks123456
 * password :
 * token :
 * type : 0
 * username : ks123456
 */
class UserDto {
    var isAdmin: Boolean = false
    var email: String? = null
    var icon: String? = null
    var id: Int = 0
    var nickname: String? = null
    var password: String? = null
    var token: String? = null
    var type: Int = 0
    var username: String? = null
    var chapterTops: List<Any>? = null
    var collectIds: List<Any>? = null
}
