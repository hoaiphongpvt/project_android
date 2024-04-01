package com.example.project_android.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.project_android.R
import com.example.project_android.viewmodel.LoginViewModel


class LoginActivity : AppCompatActivity() {

    private lateinit var btnLogin: Button
    private lateinit var btnSignup: Button
    private lateinit var apiKey: EditText
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.btnLogin)
        btnSignup = findViewById(R.id.btnSignup)
        apiKey = findViewById(R.id.api_key)

        loginViewModel = LoginViewModel(this)

        btnLogin.setOnClickListener {

            if (apiKey.text.isNotEmpty()) {
                loginViewModel.login(apiKey.text.toString())
            } else {
                Toast.makeText(this, "Please enter your API KEY.", Toast.LENGTH_LONG).show()
            }

        }

        btnSignup.setOnClickListener {
            Toast.makeText(this, "Going to the registration page...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}