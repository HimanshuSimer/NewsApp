package com.app.newsapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.newsapp.R
import com.app.newsapp.model.Article
import com.bumptech.glide.Glide // Use Coil or Picasso if preferred

class BookmarkAdapter(
    private val context: Context,
    private val bookmarkedArticles: MutableList<Article>,
    private val onRemoveBookmark: (Article) -> Unit
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bookmarked_news, parent, false)
        return BookmarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val article = bookmarkedArticles[position]
        holder.bookmarkTitle.text = article.title
        holder.bookmarkDescription.text = article.description ?: "No description available"

        Glide.with(context)
            .load(article.urlToImage)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.bookmarkImage)

        holder.bookmarkRemoveButton.setOnClickListener {
            onRemoveBookmark(article)
            Toast.makeText(context, "Bookmark Removed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = bookmarkedArticles.size

    class BookmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookmarkImage: ImageView = itemView.findViewById(R.id.bookmarkImage)
        val bookmarkTitle: TextView = itemView.findViewById(R.id.bookmarkTitle)
        val bookmarkDescription: TextView = itemView.findViewById(R.id.bookmarkDescription)
        val bookmarkRemoveButton: ImageView = itemView.findViewById(R.id.bookmarkRemoveButton)
    }
}
