package com.project.gamersgeek.data.local

import android.content.Context
import javax.inject.Inject

class GamerGeeksLocalDbService @Inject constructor(context: Context) {
    private val localDb = GamerGeeksLocalDb.invoke(context)
}