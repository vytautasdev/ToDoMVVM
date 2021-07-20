package com.vytautas.dev.mytodo.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vytautas.dev.mytodo.R
import com.vytautas.dev.mytodo.data.models.TodoData
import com.vytautas.dev.mytodo.data.viewmodel.TodoViewModel
import com.vytautas.dev.mytodo.databinding.FragmentAddBinding


class AddFragment : Fragment() {

    private val mTodoViewModel: TodoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        binding.updateFragPrioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val mTitle = binding.updateFragTitleEt.text.toString()
        val mPriority = binding.updateFragPrioritiesSpinner.selectedItem.toString()
        val mDescription = binding.updateFragDescEt.text.toString()

        val validation = mSharedViewModel.verifyUserData(mTitle, mDescription)

        if (validation) {
            // Insert Data to Database
            val newData =
                TodoData(0, mTitle, mSharedViewModel.parsePriority(mPriority), mDescription)
            mTodoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "New data successfully added.", Toast.LENGTH_SHORT)
                .show()
            // Navigate Back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(
                requireContext(),
                "Please fill out all fields.",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}