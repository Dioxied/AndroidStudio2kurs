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
        db.execSQL("CREATE TABLE tasks (id INTEGER PRIMARY KEY, title TEXT, descip TEXT, checker INTEGER DEFAULT 1)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tasks")
        onCreate(db)
    }

    // Быстрое добавление
    fun addTask(title: String, descip: String): Long {
        val values = ContentValues().apply {
            put("title", title)
            put("descip", descip)
        }
        return writableDatabase.insert("tasks", null, values)
    }

    // Быстрое получение всех
    fun getAllTasks(): ArrayList<HashMap<String, String>> {
        val tasks = ArrayList<HashMap<String, String>>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM tasks", null)
        
        while (cursor.moveToNext()) {
            val task = HashMap<String, String>()
            task["id"] = cursor.getString(0)
            task["title"] = cursor.getString(1)
            task["descip"] = cursor.getString(2)
            task["checker"] = cursor.getString(3)
            tasks.add(task)
        }
        cursor.close()
        return tasks
    }


    // Быстрое удаление
    fun deleteTask(title: String): Int {
        return writableDatabase.delete("tasks", "title=?", arrayOf(title))
    }
}


class MainActivity : AppCompatActivity() {
    private lateinit var taskInput: EditText
    private lateinit var addButton: android.widget.Button
    private lateinit var tasksContainer: LinearLayout
    private lateinit var taskdescriptionInput: EditText
    private lateinit var  btnNextAtc: android.widget.Button
    val db = FastDB(this)
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
        val textDescip = taskdescriptionInput.text.toString().trim()
        db.addTask(taskText, textDescip)
        taskdescriptionInput.text.clear()
        taskInput.text.clear()
}
}
