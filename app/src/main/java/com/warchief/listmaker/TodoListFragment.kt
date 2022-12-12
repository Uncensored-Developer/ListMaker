package com.warchief.listmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.warchief.listmaker.databinding.ActivityMainBinding
import com.warchief.listmaker.databinding.FragmentTodoListBinding

class TodoListFragment : Fragment(), TodoListAdapter.TodoListClickListener {

    private lateinit var todoListRecyclerView: RecyclerView
    private lateinit var  listDataManager: ListDataManager
    private lateinit var binding: FragmentTodoListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTodoListBinding.bind(view)
        activity?.let {
            listDataManager = ViewModelProviders.of(this)[ListDataManager::class.java]
        }
        val lists = listDataManager.readLists()

        todoListRecyclerView = view.findViewById(R.id.listsRecyclerView)
        todoListRecyclerView.layoutManager = LinearLayoutManager(activity)
        todoListRecyclerView.adapter = TodoListAdapter(lists, this)

        binding.fab.setOnClickListener {
            showCreateTodoListDialog()
        }

    }

    interface OnFragmentInteractionListener {
        fun onTodoListClicked(list: TaskList)
    }

    companion object {

        @JvmStatic
        fun newInstance(): TodoListFragment {
            return TodoListFragment()
        }
    }

    override fun listItemClicked(list: TaskList) {
        showTaskListItems(list)
    }

    fun addList(list: TaskList) {
        listDataManager.saveList(list)
        val todoAdapter = todoListRecyclerView.adapter as TodoListAdapter
        todoAdapter.addList(list)
    }

    fun saveList(list: TaskList) {
        listDataManager.saveList(list)
        updateLists()
    }

    private fun updateLists() {
        val lists = listDataManager.readLists()
        todoListRecyclerView.adapter = TodoListAdapter(lists, this)
    }

    private fun showCreateTodoListDialog() {
        activity?.let {
            val dialogTitle = getString(R.string.name_of_list)
            val positiveButtonTitle = getString(R.string.create_list)
            val myDialog = AlertDialog.Builder(it)
            val todoTitleEditText = EditText(it)

            todoTitleEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            myDialog.setTitle(dialogTitle)
            myDialog.setView(todoTitleEditText)

            myDialog.setPositiveButton(positiveButtonTitle) {
                    dialog, _ ->
                val list = TaskList(todoTitleEditText.text.toString())
                addList(list)
                dialog.dismiss()

                showTaskListItems(list)
            }
            myDialog.create().show()
        }
    }

    private fun showTaskListItems(list: TaskList) {
        val action = TodoListFragmentDirections.actionTodoListFragmentToTaskDetailFragment(list.name)
        view?.findNavController()?.navigate(action)
    }
}