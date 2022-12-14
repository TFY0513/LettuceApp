package com.example.lettuceapp.ui.registerlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.lettuceapp.MainActivity
//import com.example.heechintong.databinding.ActivityLoginBinding
import com.example.lettuceapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        //If current user still under authenticate, allows login synchronously
        if(auth.currentUser !== null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.textViewLoginRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSubmitLogin.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val email = binding.editTextLoginEmail.text.toString()
        val password = binding.editTextLoginUserPass.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Email and password cannot be blank", Toast.LENGTH_SHORT).show()
        }else{
            auth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error: " + it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

}