package com.project.gamersgeek.data.local

import com.project.gamersgeek.BuildConfig
import com.project.gamersgeek.utils.search.GameResultWrapper
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

    override fun saveSearchResult(suggestionHistory: GameResultWrapper) {
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

    private fun insertData(suggestionHistory: GameResultWrapper): Flow<Long> = flow {
        val value: Long = iSuggestionsDao.insert(suggestionHistory)
        emit(value)
    }
    override fun getSearchHistory(): List<GameResultWrapper>? {
        var historyList: List<GameResultWrapper>?= null
        runBlocking {
            val job: Deferred<List<GameResultWrapper>> = async { allSearchHistory()!! }
            historyList = job.await()
        }
        return historyList
    }
    private suspend fun allSearchHistory(): List<GameResultWrapper>? {
        return this.iSuggestionsDao.getSuggestions()
    }

    override fun deleteSearchHistory() {
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

    override fun deleteOldHistory() {
        launch {
            val delete: Flow<Int>? = removeOldHistory()
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
    private suspend fun removeOldHistory(): Flow<Int> = flow {
        val expireTime: Long = TimeUnit.DAYS.toDays(1)
        val searchHistory: List<GameResultWrapper>? = getSearchHistory()
        searchHistory?.let { history ->
            if (history.size > 5) {
                val oldHistory: List<GameResultWrapper>? = history.filter {old ->
                    old.date.toLong() < System.currentTimeMillis() - expireTime
                }
                val value: Int = iSuggestionsDao.deleteOldHistory(oldHistory)
                emit(value)
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = this.job + Dispatchers.IO
}