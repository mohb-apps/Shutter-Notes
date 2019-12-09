/*
 *  Copyright (c) 2019 mohb apps - All Rights Reserved
 *
 *  Project       : ShutterNotes
 *  Developer     : Haraldo Albergaria Filho, a.k.a. mohb apps
 *
 *  File          : GearNotesListAdapter.java
 *  Last modified : 8/17/19 12:08 PM
 *
 *  -----------------------------------------------------------
 */

package com.apps.mohb.shutternotes.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.apps.mohb.shutternotes.Constants;
import com.apps.mohb.shutternotes.R;
import com.apps.mohb.shutternotes.notes.GearNote;

import java.util.ArrayList;
import java.util.Objects;


public class GearNotesListAdapter extends ArrayAdapter {

	private int itemHeight;

	public GearNotesListAdapter(@NonNull Context context, ArrayList<GearNote> notesList, int itemHeight) {
		super(context, Constants.LIST_ADAPTER_RESOURCE_ID, notesList);
		this.itemHeight = itemHeight;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

		GearNote note = (GearNote) getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
		}

		convertView.setMinimumHeight(itemHeight);

		TextView txtNote = convertView.findViewById(R.id.textView);

		txtNote.setText(note.getGearList());

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
		String prefKey = settings.getString(Constants.PREF_KEY_FONT_SIZE, Constants.PREF_FONT_SIZE_MEDIUM);

		switch (Objects.requireNonNull(prefKey)) {

			case Constants.PREF_FONT_SIZE_SMALL:
				txtNote.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.FONT_SIZE_SMALL_SMALL);
				break;

			case Constants.PREF_FONT_SIZE_MEDIUM:
				txtNote.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.FONT_SIZE_MEDIUM_SMALL);
				break;

			case Constants.PREF_FONT_SIZE_LARGE:
				txtNote.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.FONT_SIZE_LARGE_SMALL);
				break;

		}

		return convertView;

	}
}
