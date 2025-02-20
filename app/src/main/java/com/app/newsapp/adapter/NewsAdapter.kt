package com.app.newsapp.adapter

import android.content.Context
import com.app.newsapp.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.newsapp.model.Article
import com.app.newsapp.utils.BookmarkManager
import com.bumptech.glide.Glide

class NewsAdapter(private val context: Context, private var newsList: List<Article>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val bookmarkManager = BookmarkManager(context)

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val newsTitle: TextView = view.findViewById(R.id.newsTitle)
        val newsDescription: TextView = view.findViewById(R.id.newsDescription)
        val newsImage: ImageView = view.findViewById(R.id.newsImage)
        val bookmarkButton: ImageView = view.findViewById(R.id.bookmarkButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.newsTitle.text = news.title ?: "No Title"
        holder.newsDescription.text = news.description ?: "No Description"

        Glide.with(holder.itemView.context)
            .load(news.urlToImage)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.newsImage)

        holder.bookmarkButton.setOnClickListener {
            if (bookmarkManager.getBookmarks().contains(news)) {
                bookmarkManager.removeBookmark(news)
                Toast.makeText(context, "Bookmark Removed", Toast.LENGTH_SHORT).show()
            } else {
                bookmarkManager.saveBookmark(news)
                Toast.makeText(context, "Bookmarked", Toast.LENGTH_SHORT).show()
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = newsList.size

    fun updateList(newList: List<Article>) {
        newsList = newList
        notifyDataSetChanged()
    }
}
