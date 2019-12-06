/*
 *  Copyright (c) 2019 mohb apps - All Rights Reserved
 *
 *  Project       : ShutterNotes
 *  Developer     : Haraldo Albergaria Filho, a.k.a. mohb apps
 *
 *  File          : NoteDeleteAlertFragment.java
 *  Last modified : 7/21/19 11:52 PM
 *
 *  -----------------------------------------------------------
 */

package com.apps.mohb.shutternotes.fragments.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.apps.mohb.shutternotes.R;


public class NoteDeleteAlertFragment extends DialogFragment {

	public interface NoteDeleteDialogListener {
		void onNoteDeleteDialogPositiveClick(DialogFragment dialog);

		void onNoteDeleteDialogNegativeClick(DialogFragment dialog);
	}

	private NoteDeleteDialogListener mListener;

	@Override
	public AlertDialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.alert_title_delete_note).setMessage(R.string.alert_message_no_undone)
				.setPositiveButton(R.string.alert_button_yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mListener.onNoteDeleteDialogPositiveClick(NoteDeleteAlertFragment.this);
					}
				})
				.setNegativeButton(R.string.alert_button_no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mListener.onNoteDeleteDialogNegativeClick(NoteDeleteAlertFragment.this);
					}
				});

		return builder.create();

	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NetworkDeleteDialogListener so we can send events to the host
			mListener = (NoteDeleteDialogListener) context;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(context.toString()
					+ " must implement NoteDeleteDialogListener");
		}
	}

}