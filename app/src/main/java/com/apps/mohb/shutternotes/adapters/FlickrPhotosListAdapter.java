/*
 *  Copyright (c) 2019 mohb apps - All Rights Reserved
 *
 *  Project       : ShutterNotes
 *  Developer     : Haraldo Albergaria Filho, a.k.a. mohb apps
 *
 *  File          : FlickrPhotosListAdapter.java
 *  Last modified : 8/17/19 12:08 PM
 *
 *  -----------------------------------------------------------
 */

package com.apps.mohb.shutternotes.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.mohb.shutternotes.Constants;
import com.apps.mohb.shutternotes.R;

import com.flickr4java.flickr.photos.Photo;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.List;


public class FlickrPhotosListAdapter extends ArrayAdapter {


	public FlickrPhotosListAdapter(@NonNull Context context, Collection<Photo> photosList) {
		super(context, Constants.LIST_ADAPTER_RESOURCE_ID, (List) photosList);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.photo_item, parent, false);
		}

		ImageView photoView = convertView.findViewById(R.id.imagePhoto);
		TextView titleView = convertView.findViewById(R.id.textPhotoTitle);
		TextView descriptionView = convertView.findViewById(R.id.textPhotoDescription);

		Photo photo = (Photo) getItem(position);
		String imgPhotoUrl = photo.getSquareLargeUrl();

		String txtTitle = photo.getTitle();
		String txtDescription = photo.getDescription();

		Picasso.get().load(imgPhotoUrl).into(photoView);

		titleView.setText(txtTitle);
		titleView.setTypeface(Typeface.defaultFromStyle(android.graphics.Typeface.BOLD));
		descriptionView.setText(txtDescription);

		return convertView;

	}
}