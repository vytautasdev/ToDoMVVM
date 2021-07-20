package com.vytautas.dev.mytodo.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vytautas.dev.mytodo.R
import com.vytautas.dev.mytodo.data.models.TodoData
import com.vytautas.dev.mytodo.data.viewmodel.TodoViewModel
import com.vytautas.dev.mytodo.databinding.FragmentUpdateBinding
import com.vytautas.dev.mytodo.fragments.SharedViewModel

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mTodoViewModel: TodoViewModel by viewModels()

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Data binding
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.args = args


        setHasOptionsMenu(true)

        binding.updateFragPrioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> deleteItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteItem() {
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setTitle("Delete?")
            setMessage("Are you sure you want to delete '${args.currentItem.title}'?")
            setPositiveButton("Yes") { _, _ ->
                mTodoViewModel.deleteItem(args.currentItem)
                Toast.makeText(
                    requireContext(),
                    "Successfully removed: ${args.currentItem.title}",
                    Toast.LENGTH_SHORT
                )
                    .show()
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
            setNegativeButton("No") { _, _ -> }
                .create()
                .show()
        }
    }

    private fun updateItem() {
        val title = binding.updateFragTitleEt.text.toString()
        val description = binding.updateFragDescEt.text.toString()
        val getPriority = binding.updateFragPrioritiesSpinner.selectedItem.toString()

        val validation = mSharedViewModel.verifyUserData(title, description)
        if (validation) {
            val updatedItem = TodoData(
                args.currentItem.id,
                title,
                mSharedViewModel.parsePriority(getPriority),
                description
            )

            mTodoViewModel.updateData(updatedItem)
            Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_SHORT).show()
            // Navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}