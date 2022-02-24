package com.example.androidapp.ui_controllers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidapp.databinding.BooksItemCardBinding
import com.example.androidapp.models.Book

class BooksListAdapter(
    var booksList: MutableList<Book>,
    var listEmpty: () -> Unit,
    var deleteCallback: (Book) -> Unit,
    var requestPermission: (Int) -> Unit
) : RecyclerView.Adapter<BooksListAdapter.BooksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = BooksItemCardBinding.inflate(inflater)

        return BooksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return booksList.size
    }

    public fun updateBooksList(updatedBooksList: MutableList<Book>) {
        booksList.clear()
        booksList = updatedBooksList
        notifyDataSetChanged()
    }


    inner class BooksViewHolder(private val binding: BooksItemCardBinding) :
        RecyclerView.ViewHolder(binding.root)/*, View.OnClickListener*/ {

        fun bind() {
            binding.item = booksList[layoutPosition]
            binding.delete.setOnClickListener {
                if (booksList.size > 0) {
                    deleteCallback.invoke(booksList[layoutPosition])
                    booksList.removeAt(layoutPosition)
                    if (booksList.size == 0)
                        listEmpty.invoke()
                    else
                        notifyItemRangeRemoved(layoutPosition, booksList.size)
                }

            }

            binding.savePdf.setOnClickListener {
                requestPermission.invoke(layoutPosition)
            }
        }
    }

}