package com.vytautas.dev.mytodo.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vytautas.dev.mytodo.R
import com.vytautas.dev.mytodo.adapters.ListAdapter
import com.vytautas.dev.mytodo.data.models.TodoData
import com.vytautas.dev.mytodo.data.viewmodel.TodoViewModel
import com.vytautas.dev.mytodo.databinding.FragmentListBinding
import com.vytautas.dev.mytodo.utils.hideKeyboard
import com.vytautas.dev.mytodo.utils.observeOnce


class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private val mTodoViewModel: TodoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mAdapter: ListAdapter by lazy { ListAdapter() }

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Data binding
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        setupRecyclerView()

        // Observe LiveData
        mTodoViewModel.getAllData.observe(viewLifecycleOwner, { data ->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            mAdapter.setData(data)
            binding.recyclerView.scheduleLayoutAnimation()
        })

        // Set Menu
        setHasOptionsMenu(true)

        // Hide soft keyboard
        hideKeyboard(requireActivity())

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }


        swipeToDelete(binding.recyclerView)
    }

    private fun swipeToDelete(mRecyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = mAdapter.dataList[viewHolder.adapterPosition]

                // Delete item
                mTodoViewModel.deleteItem(deletedItem)
                mAdapter.notifyItemChanged(viewHolder.adapterPosition)

                // Restore deleted item
                restoreDeletedData(viewHolder.itemView, deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(mRecyclerView)
    }

    private fun restoreDeletedData(mView: View, deletedItem: TodoData) {
        val snackBar =
            Snackbar.make(mView, "Deleted: '${deletedItem.title}'", Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo") {
            mTodoViewModel.insertData(deletedItem)
        }
        snackBar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> deleteAllData()
            R.id.menu_priority_high -> mTodoViewModel.sortByHighPriority.observe(
                viewLifecycleOwner,
                { mAdapter.setData(it) })
            R.id.menu_priority_low -> mTodoViewModel.sortByLowPriority.observe(
                viewLifecycleOwner,
                { mAdapter.setData(it) })

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }


    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchDatabase(newText)
        }
        return true
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"

        mTodoViewModel.searchDatabase(searchQuery).observeOnce(viewLifecycleOwner, { list ->
            list?.let {
                mAdapter.setData(it)
            }
        })
    }

    private fun deleteAllData() {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Delete?")
            setMessage("Are you sure you want to delete all data?")
            setPositiveButton("Yes") { _, _ ->
                mTodoViewModel.deleteAllData()
                Toast.makeText(
                    requireContext(),
                    "All data was successfully deleted.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            setNegativeButton("No") { _, _ -> }
                .create()
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}