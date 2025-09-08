package com.example.project_06_09

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ActivityForListTask : AppCompatActivity() {
    private lateinit var btnBack : android.widget.Button;
    private lateinit var taskconteiner : LinearLayout;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_for_list_task)
        taskconteiner = findViewById(R.id.tasksContainer2)
        btnBack = findViewById(R.id.buttonback)
        btnBack.setOnClickListener { finish() }
        val db = FastDB(this);
        val tasks = db.getAllTasks();
        tasks.forEach{task ->
            // Создаем новый элемент задачи
            val taskView =
                LayoutInflater.from(this).inflate(R.layout.task_item, null)
            val taskTextView = taskView.findViewById<TextView>(R.id.taskText)
            val taskdescView = taskView.findViewById<TextView>(R.id.taskTextdescription)
            val taskCheckbox =
                taskView.findViewById<CheckBox>(R.id.taskCheckbox)
            val deleteButton =
                taskView.findViewById<ImageButton>(R.id.deleteButton)
// Устанавливаем текст задачи
            taskTextView.text = task["title"]
            taskdescView.text = task["descip"]
// Обработчик для чекбокса (отметка выполнения)
            taskCheckbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked && task["check"] == "1") {
                    taskTextView.paintFlags =
                        android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                    taskTextView.setTextColor(resources.getColor(android.R.color.darker_gray))
                    taskdescView.visibility = View.GONE

                } else {
                    taskTextView.paintFlags = 0
                    taskTextView.setTextColor(resources.getColor(android.R.color.black))
                    taskdescView.visibility = View.VISIBLE
                }
            }
// Обработчик для кнопки удаления
            deleteButton.setOnClickListener{
                taskconteiner.removeView(taskView)
                Toast.makeText(this, "Задача удалена",
                    Toast.LENGTH_SHORT).show()
            }
// Обработчик клика по всей задаче (отмечаем/снимаем отметку)
            taskView.setOnClickListener {
                taskCheckbox.isChecked = !taskCheckbox.isChecked
            }
// Добавляем задачу в контейнер
            taskconteiner.addView(taskView)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}