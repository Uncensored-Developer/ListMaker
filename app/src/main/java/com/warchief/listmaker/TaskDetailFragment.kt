package com.warchief.listmaker

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.warchief.listmaker.databinding.ActivityMainBinding
import com.warchief.listmaker.databinding.FragmentTaskDetailBinding


class TaskDetailFragment : Fragment() {

    private lateinit var list: TaskList
    private lateinit var binding: FragmentTaskDetailBinding
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var listDataManager: ListDataManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaskDetailBinding.bind(view)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)

        listDataManager = ViewModelProviders.of(this)[ListDataManager::class.java]
        arguments?.let {
            val args = TaskDetailFragmentArgs.fromBundle(it)
            list = listDataManager.readLists().filter { list -> list.name == args.listString }[0]
        }

        activityMainBinding.toolbar.title = list.name

        activity?.let {
            binding.taskListRecyclerView.layoutManager = LinearLayoutManager(it)
            binding.taskListRecyclerView.adapter = TaskListAdapter(list)

            binding.addTaskButton.setOnClickListener {
                showCreateTaskDialog()
            }
        }
    }

    private fun showCreateTaskDialog() {
        activity?.let {
            val taskEditText = EditText(it)
            taskEditText.inputType = InputType.TYPE_CLASS_TEXT
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.task_to_add))
                .setView(taskEditText)
                .setPositiveButton(R.string.add_task) {
                        dialog, _ ->
                    val task = taskEditText.text.toString()
                    list.tasks.add(task)
                    listDataManager.saveList(list)
                    dialog.dismiss()
                }
                .create()
                .show()
        }

    }

    companion object {
        private val ARG_LIST = "list"
        fun newInstance(list: TaskList) : TaskDetailFragment {
            val bundle = Bundle()
            bundle.putParcelable(ARG_LIST, list)

            val fragment = TaskDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}