package com.warchief.listmaker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.warchief.listmaker.databinding.ActivityDatailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var list: TaskList
    private lateinit var binding: ActivityDatailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDatailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        list = intent.getParcelableExtra<TaskList>(MainActivity.INTENT_LIST_KEY)!! as TaskList
        title = list.name

        binding.taskListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.taskListRecyclerView.adapter = TaskListAdapter(list)

        binding.addTaskButton.setOnClickListener {
            showCreateTaskDialog()
        }

    }

    private fun showCreateTaskDialog() {
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.task_to_add))
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task) {
                dialog, _ ->
                    val task = taskEditText.text.toString()
                    list.tasks.add(task)
                    dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onBackPressed() {
        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_LIST_KEY, list)

        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }
}