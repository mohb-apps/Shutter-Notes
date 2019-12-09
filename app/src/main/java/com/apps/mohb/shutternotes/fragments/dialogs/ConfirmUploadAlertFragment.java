/*
 *  Copyright (c) 2019 mohb apps - All Rights Reserved
 *
 *  Project       : ShutterNotes
 *  Developer     : Haraldo Albergaria Filho, a.k.a. mohb apps
 *
 *  File          : ConfirmUploadAlertFragment.java
 *  Last modified : 8/11/19 10:39 PM
 *
 *  -----------------------------------------------------------
 */

package com.apps.mohb.shutternotes.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.apps.mohb.shutternotes.R;


public class ConfirmUploadAlertFragment extends DialogFragment {

	public interface ConfirmUploadAlertDialogListener {
		void onConfirmUploadDialogPositiveClick(DialogFragment dialog);

		void onConfirmUploadDialogNegativeClick(DialogFragment dialog);
	}

	private ConfirmUploadAlertDialogListener mListener;


	@NonNull
	@Override
	public AlertDialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.alert_title_confirm_upload).setMessage(R.string.alert_message_confirm_upload)
				.setPositiveButton(R.string.alert_button_yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mListener.onConfirmUploadDialogPositiveClick(ConfirmUploadAlertFragment.this);
					}
				})
				.setNegativeButton(R.string.alert_button_no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mListener.onConfirmUploadDialogNegativeClick(ConfirmUploadAlertFragment.this);
					}
				});

		return builder.create();

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the ConfirmUploadDialogListener so we can send events to the host
			mListener = (ConfirmUploadAlertDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement ConfirmUploadDialogListener");
		}
	}

}
