package com.crp.wikisearcher

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.crp.wikisearcher.databinding.ActivityMainBinding
import com.crp.wikisearcher.utils.Helper
import com.crp.wikisearcher.view.State
import com.crp.wikisearcher.view.WikiAdapter
import com.crp.wikisearcher.viewmodel.WikiViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {


    private val viewModel: WikiViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.searchNow.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { callSearchApi(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


        viewModel.postsLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> {
                    loadingState(true)
                }
                is State.Success -> {
                    loadingState(false)
                    binding.wikiRv.adapter =
                        state.data.query?.pages?.let { WikiAdapter(it) { it1 -> openBrowser(it1 as String) } }
                }
                is State.Error -> {
                    loadingState(false)
                    binding.noDataState.visibility = VISIBLE
                }
            }
        })
    }

    private fun openBrowser(string: String) = startActivity(
        Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/$string"))
    )


    private fun loadingState(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingState.visibility = VISIBLE
            binding.defaultState.visibility = GONE
            binding.noDataState.visibility = GONE
        } else {
            Helper.hideKeyboard(this)
            binding.loadingState.visibility = GONE
        }
    }

    fun callSearchApi(searchString: String) {
        if (Helper.isNetworkAvailable(this))
            viewModel.getWikiData(searchString)
        else
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
    }

}
