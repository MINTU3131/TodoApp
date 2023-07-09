package com.mintusharma.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.mintusharma.todoapp.databinding.ActivityMainBinding
import com.mintusharma.todoapp.databinding.ActivityUpdateRecordBinding

class UpdateRecord : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateRecordBinding
    private var taskId: String? = null
    private var title: String? = null
    private var desc: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setValuesToViews()

        binding.submit.setOnClickListener {
            updateData(taskId.toString(),binding.edittittle.text.toString(), binding.editdesc.text.toString())
            Toast.makeText(applicationContext, "Todo Data Updated", Toast.LENGTH_LONG).show()
            val intent = Intent(this@UpdateRecord, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setValuesToViews() {
         taskId = intent.getStringExtra("taskId")
         title = intent.getStringExtra("tittle")
         desc = intent.getStringExtra("desc")

        if (title != null) {
            binding.edittittle.text = Editable.Factory.getInstance().newEditable(title)
        }
        if (desc != null) {
            binding.editdesc.text = Editable.Factory.getInstance().newEditable(desc)
        }

    }

    private fun updateData(
        id: String,
        tittle: String,
        desc: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("TodoList").child(id)
        val empInfo = TodoItem(id, tittle, desc)
        dbRef.setValue(empInfo)

    }
}