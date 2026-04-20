package com.example.filmsearch.ui.poster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.filmsearch.R
import com.example.filmsearch.databinding.FragmentAboutBinding
import com.example.filmsearch.domain.models.MovieDetails
import com.example.filmsearch.presentation.poster.AboutState
import com.example.filmsearch.presentation.poster.AboutViewModel
import com.example.filmsearch.ui.cast.MoviesCastFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AboutFragment : Fragment() {

    private val aboutViewModel: AboutViewModel by viewModel {
        parametersOf(requireArguments().getString(MOVIE_ID))//????
    }
    private lateinit var binding: FragmentAboutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        aboutViewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is AboutState.Content -> showDetails(it.movieDetails)
                is AboutState.Error -> showErrorMessage(it.errorMessage)
            }
        }

        binding.showCast.setOnClickListener {
            findNavController().navigate(
                R.id.moviesCastFragment,
                MoviesCastFragment.createArgs(requireArguments().getString(MOVIE_ID).orEmpty())
            )
        }
    }

    private fun showErrorMessage(message: String) {
        binding.apply {
            details.visibility = View.GONE
            errorMessage.visibility = View.VISIBLE
            errorMessage.text = message
        }
    }

    private fun showDetails(movieDetails: MovieDetails) {
        binding.apply {
            details.visibility = View.VISIBLE
            errorMessage.visibility = View.GONE
            title.text = movieDetails.title
            ratingValue.text = movieDetails.imDbRating
            yearValue.text = movieDetails.year
            countryValue.text = movieDetails.countries
            genreValue.text = movieDetails.genres
            directorValue.text = movieDetails.directors
            writerValue.text = movieDetails.writers
            castValue.text = movieDetails.stars
            plot.text = movieDetails.plot
        }
    }

    companion object {
        const val MOVIE_ID = "movie_id"

        fun newInstance(id: String) = AboutFragment().apply {
            arguments = Bundle().apply {
                putString(MOVIE_ID, id)
            }
        }
    }
}

