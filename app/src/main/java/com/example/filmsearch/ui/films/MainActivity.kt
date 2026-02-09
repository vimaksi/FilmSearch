package com.example.filmsearch.ui.films

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsearch.ui.poster.PosterActivity
import com.example.filmsearch.R
import com.example.filmsearch.data.dto.FilmsSearchResponse
import com.example.filmsearch.data.network.FilmApi
import com.example.filmsearch.domain.Creator
import com.example.filmsearch.domain.api.FilmsInteractor
import com.example.filmsearch.domain.models.Film
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val filmBaseUrl = " https://tv-api.com"
    private val searchRunnable = Runnable { searchRequest() }

    private val provideMoviesInteractor = Creator.provideMoviesInteractor()

    private val retrofit = Retrofit.Builder()
        .baseUrl(filmBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val filmService = retrofit.create(FilmApi::class.java)

    private val films = ArrayList<Film>()//+

    private val adapter = FilmsAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", it.image)
            startActivity(intent)
        }
    }
    private lateinit var queryInput: EditText
    private lateinit var placeholder: TextView
    private lateinit var filmsList: RecyclerView

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        placeholder = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.queryInput)
        filmsList = findViewById(R.id.films)
        progressBar = findViewById(R.id.progressBar)


        adapter.films = films

        filmsList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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

    //        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    private fun showMessage(text: String, additionalMessage: String) {
        if (text.isNotEmpty()) {
            placeholder.visibility = View.VISIBLE
            films.clear()
            adapter.notifyDataSetChanged()
            placeholder.text = text
            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            } else {
                placeholder.visibility = View.GONE
            }
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
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
            filmService.getFilms(queryInput.text.toString())
            provideMoviesInteractor.searchMovies(
                queryInput.text.toString(),
                object : FilmsInteractor.MoviesConsumer {
                    override fun consume(result: Result<List<Film>>) {
                        runOnUiThread {
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
                                    showMessage(getString(R.string.something_went_wrong), "")
                                }
                            }
                                .onFailure { t ->
                                    progressBar.visibility =
                                        View.GONE // Прячем ProgressBar после выполнения запроса с ошибкой
                                    showMessage("", t.message.toString())
                                }
                        }
                    }m
                })
        }
    }
}