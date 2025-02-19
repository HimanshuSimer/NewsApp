package com.app.newsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.newsapp.adapter.NewsAdapter
import com.app.newsapp.model.Article
import com.app.newsapp.model.NewsResponse
import com.app.newsapp.retrofitapi.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private val newsList = mutableListOf<Article>()
    private lateinit var adapter: NewsAdapter
    private lateinit var categorySpinner: AutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        categorySpinner = view.findViewById(R.id.categorySpinner)
        adapter = NewsAdapter(requireContext(), newsList)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        fetchNewsByCategory("General")
        setUpCategoryDropdown()

        return view
    }

    private fun fetchNewsByCategory(category: String) {
        RetrofitClient.instance.searchNews(category).enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    newsList.clear()
                    newsList.addAll(response.body()!!.articles)
                    adapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed to fetch news", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setUpCategoryDropdown() {
        val categories = listOf("General", "Business", "Technology", "Entertainment", "Health", "Science")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categories
        )

        categorySpinner.setAdapter(adapter)

        categorySpinner.setOnItemClickListener { parent, _, position, _ ->
            val selectedCategory = parent.getItemAtPosition(position).toString()
            fetchNewsByCategory(selectedCategory)
            Toast.makeText(requireContext(), "Selected: $selectedCategory", Toast.LENGTH_SHORT).show()
        }


        categorySpinner.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                categorySpinner.showDropDown()
            }
        }

        categorySpinner.setOnClickListener {
            categorySpinner.showDropDown()
        }
    }
}
