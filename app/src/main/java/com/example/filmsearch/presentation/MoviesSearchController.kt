package com.example.filmsearch.presentation

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsearch.R
import com.example.filmsearch.domain.Creator
import com.example.filmsearch.domain.api.FilmsInteractor
import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.ui.films.FilmsAdapter

class MoviesSearchController(
    private val adapter: FilmsAdapter, private val activity: Activity
) {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searchRequest() }
    private val provideMoviesInteractor = Creator.provideMoviesInteractor(activity)
    private val films = ArrayList<Film>()
    private lateinit var queryInput: EditText
    private lateinit var placeholder: TextView
    private lateinit var filmsList: RecyclerView
    private lateinit var progressBar: ProgressBar

    fun onCreate() {
        placeholder = activity.findViewById(R.id.placeholderMessage)
        queryInput = activity.findViewById(R.id.queryInput)
        filmsList = activity.findViewById(R.id.films)
        progressBar = activity.findViewById(R.id.progressBar)

        adapter.films = films

        filmsList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        filmsList.adapter = adapter

        queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchDebounce()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            placeholder.visibility = View.VISIBLE
            films.clear()
            adapter.notifyDataSetChanged()
            placeholder.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(activity.applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            } else {
                placeholder.visibility = View.GONE
            }
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest() {
        if (queryInput.text.isNotEmpty()) {
            placeholder.visibility = View.GONE
            filmsList.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            provideMoviesInteractor.searchMovies(
                queryInput.text.toString(),
                object : FilmsInteractor.MoviesConsumer {
                    override fun consume(result: Result<List<Film>>) {
                        activity.runOnUiThread {
                            result.onSuccess { movies ->
                                if (movies.isNotEmpty()) {
                                    placeholder.visibility = View.VISIBLE
                                    filmsList.visibility = View.VISIBLE
                                    progressBar.visibility =
                                        View.GONE // Прячем ProgressBar после успешного выполнения запроса
                                    films.clear()
                                    films.addAll(movies)
                                    adapter.notifyDataSetChanged()
                                } else {
                                    showMessage(R.string.something_went_wrong.toString(), "")
                                }
                            }
                                .onFailure { t ->
                                    progressBar.visibility =
                                        View.GONE // Прячем ProgressBar после выполнения запроса с ошибкой
                                    showMessage("", t.message.toString())
                                }
                        }
                    }
                })
        }
    }
}