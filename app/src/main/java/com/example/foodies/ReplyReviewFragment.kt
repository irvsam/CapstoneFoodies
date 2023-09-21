package com.example.foodies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import classes.Entities
import classes.ReviewViewModel
import com.example.foodies.databaseManagement.ApplicationCore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReplyReviewFragment(private var review:Entities.Review):DialogFragment() {

    private lateinit var reviewViewModel: ReviewViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reviewViewModel = ViewModelProvider(requireActivity())[ReviewViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reply_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val reply = view.findViewById<EditText>(R.id.replyEditText)
        val submitButton = view.findViewById<Button>(R.id.submitReplyButton)

        submitButton.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main){

                    val VendorsReply = reply.text.toString()
                    if(VendorsReply!="") {
                        ApplicationCore.database.reviewDao()
                            .updateReply(reply.text.toString(), review.id)
                        Toast.makeText(
                            requireContext(),
                            "Your reply has been submitted!",
                            Toast.LENGTH_SHORT
                        ).show()
                        dismiss()
                    }
                    else{Toast.makeText(requireContext(),"Reply cannot be empty",Toast.LENGTH_SHORT).show()}
                }
            }
        }

    }
}