package com.example.foodies

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodies.databaseManagement.ApplicationCore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
/** the first page to open, allows a user to login or continue as a guest or asks them to register */
class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditedText: EditText
    private lateinit var passwordEditedText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val registerLinkTextView = findViewById<TextView>(R.id.registerLink)

        //registration link */
        val spannableString = SpannableString(registerLinkTextView.text)
        spannableString.setSpan(object : ClickableSpan() {

            override fun onClick(widget: View) {

                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }, 23, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        registerLinkTextView.text = spannableString
        registerLinkTextView.movementMethod = LinkMovementMethod.getInstance()

        //login button logic */
        val loginButton = findViewById<Button>(R.id.login_btn)
        loginButton.setOnClickListener {

            emailEditedText = findViewById(R.id.email_input)
            passwordEditedText = findViewById(R.id.password_input)
            val email = emailEditedText.text.toString()
            val password = passwordEditedText.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                val user = ApplicationCore.database.accountDao().getUserByEmailAndPassword(email, password)
                withContext(Dispatchers.Main) {
                    if (user != null) {
                        showToast("Login Successful")

                        // send user data in the intent and open fragment holder activity*/
                        val intent = Intent(this@LoginActivity, FragmentHolderActivity::class.java)
                        intent.putExtra("user", user.id)
                        intent.putExtra("user_name", user.username)
                        intent.putExtra("user_email", user.email)

                        if(user.type=="Vendor"){
                            intent.putExtra("is_vendor",true)
                        }
                        startActivity(intent)

                    } else {
                        showToast("Invalid credentials. Please try again.")
                    }
                }
            }

        }
        //continue as guest */
        val guestButton = findViewById<Button>(R.id.guestCont_btn)
        guestButton.setOnClickListener {
            val guestIntent = Intent(this@LoginActivity, FragmentHolderActivity::class.java)
            guestIntent.putExtra("is_guest", true)
            startActivity(guestIntent)
        }
    }
        private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
