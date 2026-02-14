package com.example.filmsearch.ui.films

import android.content.Context
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
import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.presentation.films.FilmsView
import com.example.filmsearch.util.Creator

class MainActivity : AppCompatActivity(), FilmsView {
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
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

    override fun showPlaceholderMessage(isVisible: Boolean) {
        placeholder.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun showMoviesList(isVisible: Boolean) {
        filmsList.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun showProgressBar(isVisible: Boolean) {
        progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun changePlaceholderText(newPlaceholderText: String) {
        placeholder.text = newPlaceholderText
    }

    override fun updateFilmsList(newFilmsList: List<Film>) {
        adapter.films.clear()
        adapter.films.addAll(newFilmsList)
        adapter.notifyDataSetChanged()
    }

    override fun showToast(additionalMessage: String) {
        Toast.makeText(this, additionalMessage, Toast.LENGTH_LONG)
            .show()
    }
}