package com.project.gamersgeek.data.local

import com.project.gamersgeek.BuildConfig
import com.project.gamersgeek.utils.search.SearchResultWrapper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SuggestionsRepo @Inject constructor(): ISuggestionRepo, CoroutineScope {
    private val job = Job()
    @Inject
    lateinit var iSuggestionsDao: ISuggestionsDao

    override fun saveSearchResult(suggestionHistory: SearchResultWrapper) {
        launch {
            val insert: Flow<Long>? = insertData(suggestionHistory)
            insert?.collect {value ->
                val id: Long? = value
                if (id != null) {
                    if (BuildConfig.DEBUG) {
                        Timber.d("Save success with id $id")
                    }
                }
            }
        }
    }

    private fun insertData(suggestionHistory: SearchResultWrapper): Flow<Long> = flow {
        val value: Long = iSuggestionsDao.insert(suggestionHistory)
        emit(value)
    }
    override fun getSearchHistory(searchFor: String): List<SearchResultWrapper>? {
        var historyList: List<SearchResultWrapper>?= null
        runBlocking {
            val job: Deferred<List<SearchResultWrapper>> = async { allSearchHistory(searchFor)!! }
            historyList = job.await()
        }
        return historyList
    }
    private suspend fun allSearchHistory(searchFor: String): List<SearchResultWrapper>? {
        return this.iSuggestionsDao.getSuggestions(searchFor)
    }

    override fun deleteSearchHistory(searchFor: String) {
        launch {
            val delete: Flow<Int>?= deleteAll()
            delete?.collect {value ->
                val id: Int? = value
                if (id != null) {
                    if (BuildConfig.DEBUG) {
                        Timber.d("Deleted search history successfully with ID $id")
                    }
                }
            }
        }
    }

    override fun deleteByName(search: String) {
        launch {
            val deleteByName: Flow<Int>? = performDeleteByName(search)
            deleteByName?.collect {value ->
                val id: Int? = value
                if (id != null) {
                    if (BuildConfig.DEBUG) {
                        Timber.d("Deleted history $search with id $id")
                    }
                }
            }
        }
    }
    private suspend fun performDeleteByName(search: String): Flow<Int> = flow {
        val value: Int = iSuggestionsDao.deleteHistoryByName(search)
        emit(value)
    }
    private suspend fun deleteAll(): Flow<Int> = flow {
        val value: Int = iSuggestionsDao.deleteSuggestions()
        emit(value)
    }

    override fun deleteOldHistory(searchFor: String) {
        launch {
            val delete: Flow<Int>? = removeOldHistory(searchFor)
            delete?.collect {value ->
                val id: Int? = value
                if (id != null) {
                    if (BuildConfig.DEBUG) {
                        Timber.d("Delete old history $id")
                    }
                }
            }
        }
    }
    private suspend fun removeOldHistory(searchFor: String): Flow<Int> = flow {
        val expireTime: Long = TimeUnit.DAYS.toDays(1)
        val searchHistory: List<SearchResultWrapper>? = getSearchHistory(searchFor)
        searchHistory?.let { history ->
            if (history.size > 5) {
                val oldHistory: List<SearchResultWrapper>? = history.filter { h ->
                    var time: Long = 0
                    h.date?.let {
                        time = it.toEpochSecond()
                    }
                    time < System.currentTimeMillis() - expireTime
                }
                val value: Int = iSuggestionsDao.deleteOldHistory(oldHistory)
                emit(value)
            }
        }
    }

    override fun deleteOldRecords(expiredTime: Long) {
        launch {
            val delete: Flow<Int>? = deleteOldRec(expiredTime)
            delete?.collect { value ->
                val id: Int?= value
                if (id != null) {
                    if (BuildConfig.DEBUG) {
                        Timber.d("Delete old history $id")
                    }
                }
            }
        }
    }

    private suspend fun deleteOldRec(expiredTime: Long): Flow<Int> = flow {
        val value: Int = iSuggestionsDao.deleteOldRecords(expiredTime)
        emit(value)
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}