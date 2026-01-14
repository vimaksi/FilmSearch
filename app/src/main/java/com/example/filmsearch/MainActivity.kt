package com.example.filmsearch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val filmBaseUrl = " https://tv-api.com"

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
    private lateinit var searchButton: Button
    private lateinit var queryInput: EditText
    private lateinit var placeholder: TextView
    private lateinit var filmsList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        placeholder = findViewById(R.id.placeholderMessage)
        searchButton = findViewById(R.id.searchButton)
        queryInput = findViewById(R.id.queryInput)
        filmsList = findViewById(R.id.films)

        adapter.films = films

        filmsList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        filmsList.adapter = adapter

        searchButton.setOnClickListener {
            if (queryInput.text.isNotEmpty()) {
                filmService.getFilms(queryInput.text.toString())
                    .enqueue(object : retrofit2.Callback<FilmsResponse> {
                        override fun onResponse(
                            call: Call<FilmsResponse>,
                            response: Response<FilmsResponse>
                        ) {
                            when (response.code()) {
                                200 -> {
                                    if (response.body()?.results?.isNotEmpty() == true) {
                                        films.clear()
                                        films.addAll(response.body()?.results!!)
                                        adapter.notifyDataSetChanged()
                                    }
                                    if (films.isEmpty()) {
                                        showMessage(getString(R.string.nothing_found), "")
                                    } else {
                                        showMessage("", "")
                                    }
                                }

                                else -> {
                                    showMessage(
                                        getString(R.string.something_went_wrong),
                                        response.code().toString()
                                    )
                                }
                            }
                        }

                        override fun onFailure(call: Call<FilmsResponse>, t: Throwable) {
                            showMessage("", t.message.toString())
                        }
                    })
                }
            }
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
    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}