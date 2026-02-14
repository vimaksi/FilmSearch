package com.example.filmsearch.ui.films

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsearch.ui.poster.PosterActivity
import com.example.filmsearch.R
import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.presentation.films.FilmsView
import com.example.filmsearch.ui.films.models.MoviesState
import com.example.filmsearch.util.Creator

class MainActivity : AppCompatActivity(), FilmsView {
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var queryInput: EditText
    private lateinit var placeholder: TextView
    private lateinit var filmsList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var textWatcher: TextWatcher? = null

    private var isClickAllowed = true

    private val adapter = FilmsAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, PosterActivity::class.java)
            intent.putExtra("poster", it.image)
            startActivity(intent)
        }
    }
    private val moviesSearchPresenter =
        Creator.provideMoviesSearchPresenter(this, context = this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //moviesSearchPresenter.onCreate()

        placeholder = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.queryInput)
        filmsList = findViewById(R.id.films)
        progressBar = findViewById(R.id.progressBar)

        filmsList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        filmsList.adapter = adapter

//        queryInput.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                moviesSearchPresenter.searchDebounce()
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//            }
//        })

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                moviesSearchPresenter.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let { queryInput.addTextChangedListener(it) }

        moviesSearchPresenter.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { queryInput.removeTextChangedListener(it) }
        moviesSearchPresenter.onDestroy()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }


    fun showLoading() {
        filmsList.visibility = View.GONE
        placeholder.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    fun showError(errorMessage: String) {
        filmsList.visibility = View.GONE
        placeholder.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        placeholder.text = errorMessage
    }

    fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    fun showContent(movies: List<Film>) {
        filmsList.visibility = View.VISIBLE
        placeholder.visibility = View.GONE
        progressBar.visibility = View.GONE

        adapter.films.clear()
        adapter.films.addAll(movies)
        adapter.notifyDataSetChanged()
    }
    override fun render(state: MoviesState) {
        when (state) {
            is MoviesState.Loading -> showLoading()
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Error -> showError(state.errorMessage)
            is MoviesState.Empty -> showEmpty(state.message)
        }

//        when {
//            state.isLoading -> showLoading()
//            state.errorMessage != null -> showError(state.errorMessage)
//            else -> showContent(state.movies)
//        }
        when (state) {
            is MoviesState.Loading -> showLoading()
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Error -> showError(state.errorMessage)
            is MoviesState.Empty -> showEmpty(state.message)
        }
    }

    override fun showToast(additionalMessage: String) {
        TODO("Not yet implemented")
    }
}