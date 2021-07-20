package com.vytautas.dev.mytodo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vytautas.dev.mytodo.data.models.TodoData
import com.vytautas.dev.mytodo.databinding.RowLayoutBinding
import com.vytautas.dev.mytodo.fragments.TodoDiffUtil

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList = emptyList<TodoData>()

    class MyViewHolder(private val binding: RowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(todoData: TodoData) {
            binding.todoData = todoData
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(todoData: List<TodoData>) {
        val todoDiffUtil = TodoDiffUtil(dataList, todoData)
        val todoDiffResult = DiffUtil.calculateDiff(todoDiffUtil)
        this.dataList = todoData
        todoDiffResult.dispatchUpdatesTo(this)

    }
}