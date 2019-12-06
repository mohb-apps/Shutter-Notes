/*
 *  Copyright (c) 2019 mohb apps - All Rights Reserved
 *
 *  Project       : ShutterNotes
 *  Developer     : Haraldo Albergaria Filho, a.k.a. mohb apps
 *
 *  File          : DeleteAllAlertFragment.java
 *  Last modified : 8/17/19 12:08 PM
 *
 *  -----------------------------------------------------------
 */

package com.apps.mohb.shutternotes.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.apps.mohb.shutternotes.R;


public class DeleteAllAlertFragment extends DialogFragment {

	public interface DeleteAllAlertDialogListener {
		void onDeleteAllDialogPositiveClick(DialogFragment dialog);

		void onDeleteAllDialogNegativeClick(DialogFragment dialog);
	}

	private DeleteAllAlertDialogListener mListener;


	@Override
	public AlertDialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.alert_title_delete_all_items).setMessage(R.string.alert_message_no_undone)
				.setPositiveButton(R.string.alert_button_yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mListener.onDeleteAllDialogPositiveClick(DeleteAllAlertFragment.this);
					}
				})
				.setNegativeButton(R.string.alert_button_no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mListener.onDeleteAllDialogNegativeClick(DeleteAllAlertFragment.this);
					}
				});

		return builder.create();

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NotesListClearDialogListener so we can send events to the host
			mListener = (DeleteAllAlertDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement DeleteAllDialogListener");
		}
	}

}