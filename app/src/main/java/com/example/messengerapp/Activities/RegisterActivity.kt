 package com.example.messengerapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.messengerapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


 class RegisterActivity : AppCompatActivity() {

     private lateinit var mAuth: FirebaseAuth
     private lateinit var refUsers: DatabaseReference
     private var firebaseUserID:String=""

     private lateinit var username_register: TextView
     private lateinit var email_register: TextView
     private lateinit var password_register: TextView
     private lateinit var register_btn: Button

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_register)

         register_btn=findViewById(R.id.register_btn)
         username_register=findViewById(R.id.username_register)
         email_register=findViewById(R.id.email_register)
         password_register=findViewById(R.id.password_register)

         val toolbar: Toolbar = findViewById(R.id.toolbar_register);
         setSupportActionBar(toolbar)
         supportActionBar!!.title = "Register"
         supportActionBar!!.setDisplayHomeAsUpEnabled(true)
         toolbar.setNavigationOnClickListener {
             val intent = Intent(this@RegisterActivity, WelcomeActivity::class.java)
             startActivity(intent)
             finish()
         }

         mAuth = FirebaseAuth.getInstance()
         register_btn.setOnClickListener {
             registerUser()
         }
     }

     private fun registerUser() {
         val username: String = username_register.text.toString()
         val email: String = email_register.text.toString()
         val password: String = password_register.text.toString()

         if (username == "" || email == "" || password == "") {
             Toast.makeText(this@RegisterActivity, "Enter All Details", Toast.LENGTH_LONG).show()
         } else {
             mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                     firebaseUserID=mAuth.currentUser!!.uid
                     refUsers=FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserID)
//                     var databaseReference = Firebase.database.getReference("Users")


                     val userHashMap=HashMap<String,Any>()
                     userHashMap["uid"]=firebaseUserID
                     userHashMap["username"]=username
                     userHashMap["profile"]="https://firebasestorage.googleapis.com/v0/b/messengerapp-bf516.appspot.com/o/profile.png?alt=media&token=fb6a34d9-13ea-4081-88ae-9b22bfc04ae5"
                     userHashMap["cover"]="https://firebasestorage.googleapis.com/v0/b/messengerapp-bf516.appspot.com/o/cover.jpg?alt=media&token=c653ce1b-3c28-4fa5-933d-dba9110a02a8"
                     userHashMap["status"]="offline"
                     userHashMap["search"]=username.toLowerCase()
                     userHashMap["facebook"]="https://m.facebook.com"
                     userHashMap["instagram"]="https://m.instagram.com"
                     userHashMap["website"]="https://www.google.com"

                     refUsers.updateChildren(userHashMap).addOnCompleteListener{ task->
                         if(task.isSuccessful){
                             val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                             startActivity(intent)
                             finish()
                         }

                     }
                 } else {
                     Toast.makeText(this@RegisterActivity, "Error Message:"+task.exception!!.message.toString(), Toast.LENGTH_LONG)
                         .show()

                 }
             }
         }
     }
 }