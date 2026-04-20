package com.example.filmsearch.ui.name

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsearch.databinding.FragmentNamesBinding
import com.example.filmsearch.domain.models.Name
import com.example.filmsearch.presentation.name.NameState
import com.example.filmsearch.presentation.name.NameViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NamesFragment : Fragment() {
    private val viewModel by viewModel<NameViewModel>()
    private val adapter = NameAdapter()
    private lateinit var binding: FragmentNamesBinding
    private lateinit var textWatcher: TextWatcher

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNamesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.name.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.name.adapter = adapter
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
        textWatcher?.let { binding.queryInput.addTextChangedListener(it) }

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
        textWatcher?.let { binding.queryInput.removeTextChangedListener(it) }
    }

    private fun showToast(additionalMessage: String?) {
        // Здесь пришлось поправить использование Context
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_LONG).show()
    }

    private fun render(state: NameState) {
        when (state) {
            is NameState.Content -> showContent(state.names)
            is NameState.Empty -> showEmpty(state.message)
            is NameState.Error -> showError(state.errorMessage)
            is NameState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.apply {
            name.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showError(errorMessage: String) {
        binding.apply {
            name.visibility = View.GONE
            placeholderMessage.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

            placeholderMessage.text = errorMessage
        }
    }

    private fun showEmpty(emptyMessage: String) {
        showError(emptyMessage)
    }

    private fun showContent(names: List<Name>) {
        binding.apply {
        name.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE
            }
        adapter.names.clear()
        adapter.names.addAll(names)
        adapter.notifyDataSetChanged()
    }
}