package com.app.newsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.newsapp.adapter.BookmarkAdapter
import com.app.newsapp.model.Article
import com.app.newsapp.utils.BookmarkManager

class BookmarkedArticlesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookmarkAdapter
    private lateinit var bookmarkManager: BookmarkManager
    private var bookmarkedArticles = mutableListOf<Article>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookmarked_articles, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        bookmarkManager = BookmarkManager(requireContext())
        loadBookmarkedArticles()

        return view
    }

    private fun loadBookmarkedArticles() {
        bookmarkedArticles = bookmarkManager.getBookmarks().toMutableList()
        adapter = BookmarkAdapter(requireContext(), bookmarkedArticles) { article ->
            bookmarkManager.removeBookmark(article)
            bookmarkedArticles.remove(article)
            adapter.notifyDataSetChanged()
        }
        recyclerView.adapter = adapter
    }
}
