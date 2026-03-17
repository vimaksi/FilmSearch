package com.example.filmsearch.ui.films

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmsearch.ui.poster.DetailsActivity
import com.example.filmsearch.databinding.ActivityMainBinding
import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.presentation.films.MoviesState
import com.example.filmsearch.presentation.films.MoviesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val viewModel:MoviesViewModel by viewModel()

    private val adapter = FilmsAdapter {
        if (clickDebounce()) {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("poster", it.image)
            startActivity(intent)
        }
    }

    private var textWatcher: TextWatcher? = null
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.films.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.films.adapter = adapter


        viewModel?.observeState()?.observe(this) {
            render(it)
        }

        viewModel?.observeShowToast()?.observe(this) {
            showToast(it)
        }
//        private val adapter = FilmsAdapter {
//            if (clickDebounce()) {
//                val intent = Intent(this, DetailsActivity::class.java)
//                intent.putExtra("poster", it.image)
//                intent.putExtra("id", it.id)
//                startActivity(intent)
//            }
//        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel?.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }
        }
        textWatcher?.let { binding.queryInput.addTextChangedListener(it) }

    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let {
            binding.queryInput.removeTextChangedListener(it)
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


    fun showLoading() {
        binding.apply {
            films.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    fun showError(errorMessage: String) {
        binding.apply {
            films.visibility = View.GONE
            placeholderMessage.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            placeholderMessage.text = errorMessage
        }
    }

    fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    fun showContent(movies: List<Film>) {
        binding.apply {
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.GONE
            adapter.films.clear()
            adapter.films.addAll(movies)
            adapter.notifyDataSetChanged()
            films.visibility = View.VISIBLE
        }
    }

    fun render(state: MoviesState) {
        when (state) {
            is MoviesState.Loading -> showLoading()
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Error -> showError(state.errorMessage)
            is MoviesState.Empty -> showEmpty(state.message)
        }
    }

    fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
