package com.crp.wikisearcher.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crp.wikisearcher.datamodel.WikiResponse
import com.crp.wikisearcher.network.WikiAPI
import com.crp.wikisearcher.view.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject


class WikiViewModel : ViewModel(), KoinComponent {
    private val wikiAPI: WikiAPI by inject()
    private val _postsLiveData = MutableLiveData<State<WikiResponse>>()
    val postsLiveData: LiveData<State<WikiResponse>>
        get() = _postsLiveData

    fun getWikiData(searchStr: String) {
        _postsLiveData.value = State.loading<WikiResponse>()
        viewModelScope.launch(Dispatchers.IO) {
            val wikiResponse =
                wikiAPI.getSearchRespone(
                    searchStr,
                    "query",
                    "2",
                    "extracts|pageimages|pageterms",
                    "2",
                    "true",
                    "thumbnail",
                    "json",
                    "prefixsearch",
                    "300",
                    "true"
                )

            withContext(Main) {
                wikiResponse.query?.let {
                    it.pages?.let { it1 ->
                        if (it1.isNotEmpty()) {
                            _postsLiveData.value = State.success(wikiResponse)
                        } else {
                            _postsLiveData.value = State.error<WikiResponse>("No Data Found")
                        }
                    } ?: run {
                        _postsLiveData.value = State.error<WikiResponse>("No Data Found")
                    }
                } ?: run {
                    _postsLiveData.value = State.error<WikiResponse>("No Data Found")
                }

            }
        }
    }

}