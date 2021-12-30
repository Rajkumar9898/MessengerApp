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

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    private lateinit var login_btn:Button
    private lateinit var email_login:TextView
    private lateinit var password_login:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_btn=findViewById(R.id.login_btn)
        email_login=findViewById(R.id.email_login)
        password_login=findViewById(R.id.password_login)

        val toolbar: Toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Login"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        mAuth = FirebaseAuth.getInstance()

        login_btn.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email: String = email_login.text.toString()
        val password: String = password_login.text.toString()

        if(email=="" || password==""){
         Toast.makeText(this@LoginActivity,"Enter detail",Toast.LENGTH_LONG).show()
        }
        else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
                if(task.isSuccessful){
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                else
                {
                    Toast.makeText(this@LoginActivity, "Error Message:"+task.exception!!.message.toString(), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

    }
}