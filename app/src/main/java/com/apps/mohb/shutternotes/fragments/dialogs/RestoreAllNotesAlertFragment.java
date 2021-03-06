/*
 *  Copyright (c) 2020 mohb apps - All Rights Reserved
 *
 *  Project       : ShutterNotes
 *  Developer     : Haraldo Albergaria Filho, a.k.a. mohb apps
 *
 *  File          : RestoreAllNotesAlertFragment.java
 *  Last modified : 10/15/20 7:30 AM
 *
 *  -----------------------------------------------------------
 */

package com.apps.mohb.shutternotes.fragments.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.apps.mohb.shutternotes.R;


public class RestoreAllNotesAlertFragment extends DialogFragment {

    public interface RestoreAllNotesAlertDialogListener {
        void onRestoreAllNotesDialogPositiveClick(DialogFragment dialog);

        void onRestoreAllNotesDialogNegativeClick(DialogFragment dialog);
    }

    private RestoreAllNotesAlertDialogListener mListener;


    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.alert_title_restore_all_notes).setMessage(R.string.alert_message_restore_all_notes)
                .setPositiveButton(R.string.alert_button_yes, (dialog, id) -> mListener.onRestoreAllNotesDialogPositiveClick(RestoreAllNotesAlertFragment.this))
                .setNegativeButton(R.string.alert_button_no, (dialog, id) -> mListener.onRestoreAllNotesDialogNegativeClick(RestoreAllNotesAlertFragment.this));

        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the RestoreAllNotesDialogListener so we can send events to the host
            mListener = (RestoreAllNotesAlertDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement RestoreAllNotesDialogListener");
        }
    }

}
