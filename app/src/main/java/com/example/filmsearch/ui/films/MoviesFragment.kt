package com.example.filmsearch.ui.films

import android.graphics.Movie
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmsearch.databinding.FragmentMoviesBinding
import com.example.filmsearch.domain.models.Film
import com.example.filmsearch.presentation.films.MoviesState
import android.text.Editable
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsearch.R
import com.example.filmsearch.presentation.films.MoviesViewModel
import com.example.filmsearch.ui.details.DetailsFragment
import com.example.filmsearch.ui.root.RootActivity
import com.example.filmsearch.util.debounce
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import org.koin.androidx.viewmodel.ext.android.viewModel


class MoviesFragment : Fragment() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val viewModel by viewModel<MoviesViewModel>()
    private lateinit var onMovieClickDebounce: (Film) -> Unit
    private var adapter: FilmsAdapter? = null
    private var isClickAllowed = true
    private lateinit var binding: FragmentMoviesBinding
    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var textWatcher: TextWatcher

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onMovieClickDebounce = debounce<Film>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { movie ->
            findNavController().navigate(
                R.id.action_moviesFragment_to_detailsFragment,
                DetailsFragment.createArgs(movie.id, movie.image)
            )
        }
        adapter = FilmsAdapter { movie ->
            (activity as RootActivity).animateBottomNavigationView()
            onMovieClickDebounce(movie)
        }
        placeholderMessage = binding.placeholderMessage
        queryInput = binding.queryInput
        moviesList = binding.films
        progressBar = binding.progressBar

        // Здесь пришлось поправить использование Context
        moviesList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        moviesList.adapter = adapter

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let { queryInput.addTextChangedListener(it) }

        // Здесь пришлось заменить LifecycleOwner на ViewLifecycleOwner
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        // Здесь пришлось заменить LifecycleOwner на ViewLifecycleOwner
        viewModel.observeShowToast().observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        moviesList.adapter = null
        textWatcher?.let { queryInput.removeTextChangedListener(it) }
    }

    private fun showToast(additionalMessage: String?) {
        // Здесь пришлось поправить использование Context
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_LONG).show()
    }

    private fun render(state: MoviesState) {
        when (state) {
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Empty -> showEmpty(state.message)
            is MoviesState.Error -> showError(state.errorMessage)
            is MoviesState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showError(errorMessage: String) {
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        placeholderMessage.text = errorMessage
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    private fun showContent(movies: List<Film>) {
        moviesList.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE

        adapter?.films?.clear()
        adapter?.films?.addAll(movies)
        adapter?.notifyDataSetChanged()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
    //Activity в качестве объекта Context можно передавать ссылку на саму Activity, потому что класс Activity наследуется от Context не напрямую,
    //но через цепочку классов.
    //Fragment не наследуется от Context, поэтому для проброса нужного объекта мы пользуемся функцией requireContext().
}