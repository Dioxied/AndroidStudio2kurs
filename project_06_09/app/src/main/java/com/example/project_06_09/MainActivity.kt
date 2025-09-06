package com.example.project_06_09

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton

import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var taskInput: EditText
    private lateinit var addButton: android.widget.Button
    private lateinit var tasksContainer: LinearLayout
    private lateinit var taskdescriptionInput: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
// Инициализация элементов
        taskInput = findViewById(R.id.taskInput)
        taskdescriptionInput = findViewById(R.id.editTextText)
        addButton = findViewById(R.id.addButton)
        tasksContainer = findViewById(R.id.tasksContainer)
// Обработчик нажатия кнопки "Добавить"
        addButton.setOnClickListener {
            addTask()
        }
    }
    private fun addTask() {
        val taskText = taskInput.text.toString().trim()
        if (taskText.isEmpty()) {
            Toast.makeText(this, "Введите задачу",
                Toast.LENGTH_SHORT).show()
            return
        }
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
        taskTextView.text = taskText
        taskdescView.text = taskdescriptionInput.text.toString().trim()
// Обработчик для чекбокса (отметка выполнения)
        taskCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
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
        tasksContainer.removeView(taskView)
        Toast.makeText(this, "Задача удалена",
            Toast.LENGTH_SHORT).show()
    }
// Обработчик клика по всей задаче (отмечаем/снимаем отметку)
    taskView.setOnClickListener {
        taskCheckbox.isChecked = !taskCheckbox.isChecked
    }
// Добавляем задачу в контейнер
    tasksContainer.addView(taskView)
// Очищаем поле ввода
    taskdescriptionInput.text.clear()
    taskInput.text.clear()
}
}