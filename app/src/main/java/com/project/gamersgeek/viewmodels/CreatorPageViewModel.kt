package com.project.gamersgeek.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.gamersgeek.data.remote.IRawgGamerGeekApiHelper
import com.project.gamersgeek.models.base.BaseResModel
import com.project.gamersgeek.models.creators.CreatorResults
import com.project.gamersgeek.utils.ResultHandler
import com.project.gamersgeek.utils.gamersGeekLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class CreatorPageViewModel @Inject constructor(): ViewModel(), CoroutineScope {
    @Inject
    lateinit var repo: IRawgGamerGeekApiHelper
    var resultLiveData: LiveData<ResultHandler<BaseResModel<CreatorResults>>> = MutableLiveData()
    private val job = Job()

    fun fetchCreatorsList() {
        this.resultLiveData = gamersGeekLiveData {
            this.repo.fetchGameCreators()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}