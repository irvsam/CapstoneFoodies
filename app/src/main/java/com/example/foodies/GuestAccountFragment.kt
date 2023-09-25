package com.example.foodies

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController

/** this is the account page for a guest user which prompts them to log in */
class GuestAccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_guest_account, container, false)

        //set the link logic to navigate back to the login page */
        val guestRegisterLink = view.findViewById<TextView>(R.id.guestRegisterLink)
        val spannableString = SpannableString(guestRegisterLink.text)
        spannableString.setSpan(object : ClickableSpan() {

            override fun onClick(widget: View) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        }, 27,37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        guestRegisterLink.text = spannableString
        guestRegisterLink.movementMethod = LinkMovementMethod.getInstance()
        return view
    }
}
