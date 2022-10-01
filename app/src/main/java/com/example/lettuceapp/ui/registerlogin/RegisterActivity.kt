package com.example.lettuceapp.ui.registerlogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.lettuceapp.R
import com.example.lettuceapp.databinding.ActivityRegisterBinding
import com.example.lettuceapp.model.QuizResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception


class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding

    private val TAG = "RegisterActivity"
    private lateinit var userProfile: HashMap<String, String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val loginText: TextView = findViewById(R.id.textViewRegisterLogin)
        loginText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        userProfile = HashMap()

        bindAdapterSpinner()

        binding.spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val year = binding.spinnerYear.getItemAtPosition(position).toString()
                val day = resources.getStringArray(R.array.dob_day)
                binding.spinnerDay.adapter = object : ArrayAdapter<String?>(
                    applicationContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    resources.getStringArray(R.array.dob_day)
                ) {
                    override fun getCount(): Int {
                        try {
                            val month = binding.spinnerMonth.selectedItem
                            if (month == "February") {
                                return if (year.toInt() % 4 == 0) {
                                    30
                                } else {
                                    29
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, e.stackTraceToString())
                        }
                        return day.size
                    }
                }
            }
        }

        binding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val month = binding.spinnerMonth.getItemAtPosition(position).toString()
                val large = resources.getStringArray(R.array.large_month)
                val day = resources.getStringArray(R.array.dob_day)
                binding.spinnerDay.adapter = object : ArrayAdapter<String?>(
                    applicationContext,
                    android.R.layout.simple_spinner_dropdown_item,
                    resources.getStringArray(R.array.dob_day)
                ) {
                    override fun getCount(): Int {
                        if ("February" == month) {
                            try {
                                val year = binding.spinnerYear.selectedItem.toString()
                                return if (year.toInt() % 4 == 0) {
                                    30
                                } else {
                                    29
                                }
                            } catch (e: Exception) {
                            }
                        } else if (!large.contains(month)) {
                            return day.size - 1
                        }
                        return day.size
                    }
                }
            }

        }

        val loginBtn: Button = findViewById(R.id.buttonSubmitLogin)
        loginBtn.setOnClickListener {
            if (!validateInput()) {
                return@setOnClickListener
            }
            performRegister()
            createUserProfile()
        }
    }

    private fun performRegister() {

        val email = binding.editTextRegisterEmail
        val password = binding.editTextPassword


        val inputEmail = email.text.toString()
        val inputPassword = password.text.toString()

        auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(this, "Profile had been created.", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error: " + it.message, Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Binding to disable first option, default option from selection
     */
    private fun bindAdapterSpinner() {
        binding.spinnerDay.adapter = spinnerAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.dob_day)
        )
        binding.spinnerMonth.adapter = spinnerAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.dob_month)
        )
        binding.spinnerYear.adapter = spinnerAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.dob_year)
        )

        binding.spinnerStatus.adapter = spinnerAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.register_status)
        )
    }

    private fun spinnerAdapter(
        context: Context,
        layoutResources: Int,
        list: Array<String?>
    ): ArrayAdapter<String?> {
        return object :
            ArrayAdapter<String?>(context, layoutResources, list) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }
        }
    }

    private fun validateInput(): Boolean {
        val password = binding.editTextPassword.text.toString()
        val confirmPassword = binding.editTextConfirmPassword.text.toString()
        val day = binding.spinnerDay.selectedItem.toString()
        val month = binding.spinnerMonth.selectedItem.toString()
        val year = binding.spinnerYear.selectedItem.toString()
        val status = binding.spinnerStatus.selectedItem.toString()
        val username = binding.editTextUsername.text.toString()
        val email = binding.editTextRegisterEmail.text.toString()

        if (email.isNullOrEmpty()) {
            binding.editTextRegisterEmail.error = "Email shall not leave blank"
            return false
        }
        if (username.isNullOrEmpty()) {
            binding.editTextUsername.error = "Username shall not leave blank"
            return false
        }
        userProfile["username"] = username
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextRegisterEmail.error = "Email presented as invalid format"
            return false
        }
        userProfile["email"] = email

        if (password.length < 8) {
            binding.editTextPassword.error = "Password length should greater than 6"
            return false
        }

        if (password != confirmPassword) {
            binding.editTextConfirmPassword.error = "Confirm password doesn't match password"
            return false
        }

        userProfile["date_of_birth"] = String.format("%s %s %s", day, month, year)
        userProfile["status"] = status

        return true
    }

    private fun createUserProfile(): Boolean {
        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        GlobalScope.launch {
            val database = FirebaseDatabase.getInstance()
            val databaseReference = database.getReference("users/$userId")

            try {
                databaseReference.setValue(userProfile)
            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

}

