package com.project.gamersgeek.models.localobj

data class NavigationHeaderItems(override val profileImage: String, var backgroundImage: String, var userName: String):
    BaseNavigationDrawer(profileImage, backgroundImage, userName, null,
    "", null, "")