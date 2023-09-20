package com.example.foodies

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

/** dialog which shows up when a user wants to change their avatar */
class AvatarSelectionDialogFragment : DialogFragment() {

    interface OnAvatarSelectedListener {
        fun onAvatarSelected(avatarResId: Int)
    }

    private var avatarSelectedListener: OnAvatarSelectedListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        builder.setTitle("Select Your Avatar")

        val avatarOptions = arrayOf(
            R.drawable.penguin,
            R.drawable.rabbit,
            R.drawable.sloth,
            R.drawable.camel,
            R.drawable.secret
        )
        val avatarOptionNames = arrayOf(
            "penguin",
            "rabbit",
            "sloth",
            "camel",
            "secret"
        )

        /** notify when one is selected */
        builder.setItems(avatarOptionNames) { dialog: DialogInterface, which: Int ->
            avatarSelectedListener?.onAvatarSelected(avatarOptions[which])
            dialog.dismiss()
        }

        return builder.create()
    }
    fun setOnAvatarSelectedListener(listener: OnAvatarSelectedListener) {
        this.avatarSelectedListener = listener
    }
}
