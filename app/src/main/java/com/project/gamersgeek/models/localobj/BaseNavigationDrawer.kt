package com.project.gamersgeek.models.localobj

abstract class BaseNavigationDrawer(open val profileImage: String?) {
    private var backgroundImage: String?= null
    private var userName: String?= null
    private var itemIcon: Int?= null
    private var itemTitle: String?= null
    private var footerIcon: Int?= null
    private var footerTitle: String?= null

    constructor(profileImage: String, backgroundImage: String, userName: String): this(profileImage) {
        this.backgroundImage = backgroundImage
        this.userName = userName
    }
    constructor(profileImage: String?, backgroundImage: String?, userName: String?, icon: Int?, items: String?): this(profileImage!!, backgroundImage!!, userName!!) {
        this.itemIcon = icon
        this.itemTitle = items
    }
    constructor(profileImage: String?, backgroundImage: String?, userName: String?, icon: Int?, items: String?, footerIcon: Int?, footerTitle: String?):
            this(profileImage, backgroundImage, userName, icon, items) {
        this.footerIcon = footerIcon
        this.footerTitle = footerTitle
    }

    fun backgroundImage(): String {
        return this.backgroundImage!!
    }
    fun itemIcon(): Int {
        return this.itemIcon!!
    }
    fun itemTitle(): String {
        return this.itemTitle!!
    }
    fun footerIcon(): Int {
        return this.footerIcon!!
    }
    fun footerTitle(): String {
        return this.footerTitle!!
    }
}