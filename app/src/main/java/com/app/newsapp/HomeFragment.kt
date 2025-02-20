package com.app.newsapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
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
    private lateinit var searchView: SearchView
    private lateinit var categorySpinner: AutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        searchView = view.findViewById(R.id.searchView)
        searchView.queryHint = "Search News..."
        categorySpinner = view.findViewById(R.id.categorySpinner)
        adapter = NewsAdapter(requireContext(), newsList)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        searchView.isIconified = false
        searchView.clearFocus()

        fetchNewsByCategory("General")
        setUpSearchView()
        setUpCategoryDropdown()

        return view
    }

    private fun fetchNewsByCategory(category: String) {
        RetrofitClient.instance.searchNews(category).enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    newsList.clear()
                    newsList.addAll(response.body()!!.articles)
                    adapter.updateList(newsList)
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
            if (hasFocus && isAdded && userInteracted) {
                categorySpinner.post { categorySpinner.showDropDown() }
            }
        }

        categorySpinner.setOnClickListener {
            if (isAdded && userInteracted) {
                categorySpinner.post { categorySpinner.showDropDown() }
            }
        }
    }


    private var userInteracted = false

    override fun onResume() {
        super.onResume()
        userInteracted = true
    }



    private fun setUpSearchView() {
        val searchText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchText.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue_500))
        searchText.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.blue_500))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterNews(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterNews(newText)
                return true
            }
        })
    }

    private fun filterNews(query: String?) {
        val filteredList = if (!query.isNullOrEmpty()) {
            newsList.filter { it.title?.contains(query, ignoreCase = true) == true }
        } else {
            newsList
        }
        Log.d("SearchDebug", "Filtered List Size: ${filteredList.size}")
        adapter.updateList(filteredList)
    }
}
