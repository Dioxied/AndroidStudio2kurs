package com.example.project_06_09

import android.content.Intent
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

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FastDB(context: Context) : SQLiteOpenHelper(context, "fast.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT, email TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    // Быстрое добавление
    fun addUser(name: String, email: String): Long {
        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
        }
        return writableDatabase.insert("users", null, values)
    }

    // Быстрое получение всех
    fun getAllUsers(): ArrayList<HashMap<String, String>> {
        val users = ArrayList<HashMap<String, String>>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM users", null)
        
        while (cursor.moveToNext()) {
            val user = HashMap<String, String>()
            user["id"] = cursor.getString(0)
            user["name"] = cursor.getString(1)
            user["email"] = cursor.getString(2)
            users.add(user)
        }
        cursor.close()
        return users
    }

    // Быстрое удаление
    fun deleteUser(id: String): Int {
        return writableDatabase.delete("users", "id=?", arrayOf(id))
    }
}


class MainActivity : AppCompatActivity() {
    private lateinit var taskInput: EditText
    private lateinit var addButton: android.widget.Button
    private lateinit var tasksContainer: LinearLayout
    private lateinit var taskdescriptionInput: EditText
    private lateinit var  btnNextAtc: android.widget.Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
// Инициализация элементов
        taskInput = findViewById(R.id.taskInput)
        taskdescriptionInput = findViewById(R.id.editTextText)
        addButton = findViewById(R.id.addButton)
        tasksContainer = findViewById(R.id.tasksContainer)
        btnNextAtc = findViewById(R.id.button123)

// Обработчик нажатия кнопки "Добавить"
        addButton.setOnClickListener {
            addTask()
        }
        btnNextAtc.setOnClickListener {
            nextact()
        }
    }
    private fun nextact(){
        val intent = Intent(this, ActivityForListTask::class.java);
        intent.putExtra("key", "value");
        startActivity(intent)
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
