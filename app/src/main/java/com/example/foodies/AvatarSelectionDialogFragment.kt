package com.example.foodies

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AvatarSelectionDialogFragment : DialogFragment() {

    // Interface to communicate the selected avatar back to the calling activity/fragment
    interface OnAvatarSelectedListener {
        fun onAvatarSelected(avatarResId: Int)
    }

    private var avatarSelectedListener: OnAvatarSelectedListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        // Set the dialog title
        builder.setTitle("Select Your Avatar")

        // Array of avatar resource IDs (you can replace these with your own avatar images)
        val avatarOptions = arrayOf(
            R.drawable.penguin,
            R.drawable.rabbit,
            R.drawable.sloth,
            R.drawable.camel,
            R.drawable.secret
        )

        // Array of avatar option names (optional)
        val avatarOptionNames = arrayOf(
            "penguin",
            "rabbit",
            "sloth",
            "camel",
            "secret"
        )

        // Set the list of avatar options
        builder.setItems(avatarOptionNames) { dialog: DialogInterface, which: Int ->
            // Notify the listener when an avatar is selected
            avatarSelectedListener?.onAvatarSelected(avatarOptions[which])
            dialog.dismiss()
        }

        return builder.create()
    }

    // Helper function to set the listener
    fun setOnAvatarSelectedListener(listener: OnAvatarSelectedListener) {
        this.avatarSelectedListener = listener
    }
}
