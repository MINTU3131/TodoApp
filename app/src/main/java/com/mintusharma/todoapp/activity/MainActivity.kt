package com.mintusharma.todoapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.mintusharma.todoapp.models.TodoItem
import com.mintusharma.todoapp.adapter.TodoAdapter
import com.mintusharma.todoapp.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var empList: ArrayList<TodoItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        empList = ArrayList<TodoItem>()
        dbRef = FirebaseDatabase.getInstance().getReference("TodoList")

        binding.submit.setOnClickListener {
            insertData()
        }

        getEmployeesData()

    }

    private fun getEmployeesData() {

        binding.progressCircular.visibility = View.VISIBLE

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if (snapshot.exists()){
                    for (todoSnap in snapshot.children){
                        val empData = todoSnap.getValue(object : GenericTypeIndicator<TodoItem>() {})
                        empList.add(empData!!)
                    }
                    val mAdapter = TodoAdapter(empList)
                    val layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,false)
                    binding.todolist.layoutManager = layoutManager
                    binding.todolist.adapter = mAdapter

                    binding.progressCircular.visibility = View.GONE

                    mAdapter.setOnItemClickListener(object : TodoAdapter.onItemClickListener{
                        override fun onUpdateItemClick(position: Int) {
                            val intent = Intent(this@MainActivity, UpdateRecord::class.java)
                            intent.putExtra("taskId", empList[position].taskId)
                            intent.putExtra("tittle", empList[position].tittle)
                            intent.putExtra("desc", empList[position].description)
                            startActivity(intent)
                        }

                        override fun onDeleteItemClick(position: Int) {
                            val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                            alertDialogBuilder.setTitle("Delete Data")
                            alertDialogBuilder.setMessage("Are you sure you want to delete this data?")
                            alertDialogBuilder.setPositiveButton("Delete") { dialog, which ->
                                deleteRecord(empList[position].taskId.toString())
                            }
                            alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                                dialog.dismiss()
                            }

                            val alertDialog = alertDialogBuilder.create()
                            alertDialog.show()
                        }

                    })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error ${error.message}", Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("TodoList").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Todo data deleted", Toast.LENGTH_LONG).show()
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun insertData(){

        val tittle = binding.edittittle.text.toString()
        val description = binding.editdesc.text.toString()

        if (TextUtils.isEmpty(tittle)) {
            binding.edittittle.error = "Please enter tittle"
        }else if (TextUtils.isEmpty(description)) {
            binding.editdesc.error = "Please enter description"
        }else{
            val empId = dbRef.push().key!!
            val employee = TodoItem(empId, tittle, description)
            dbRef.child(empId).setValue(employee)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                    binding.edittittle.text.clear()
                    binding.editdesc.text.clear()


                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

        }

    }
}