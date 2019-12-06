/*
 *  Copyright (c) 2019 mohb apps - All Rights Reserved
 *
 *  Project       : ShutterNotes
 *  Developer     : Haraldo Albergaria Filho, a.k.a. mohb apps
 *
 *  File          : AboutActivity.java
 *  Last modified : 8/17/19 12:08 PM
 *
 *  -----------------------------------------------------------
 */

package com.apps.mohb.shutternotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.apps.mohb.shutternotes.fragments.dialogs.MaterialIconsDialogFragment;
import com.apps.mohb.shutternotes.fragments.dialogs.PrivacyPolicyDialogFragment;
import com.apps.mohb.shutternotes.fragments.dialogs.TermsOfUseDialogFragment;


public class AboutActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		// displays app version number
		TextView version = (TextView) findViewById(R.id.textAppVersion);
		version.setText(getString(R.string.version_name) + " " + getString(R.string.version_number));
	}


	// OPTIONS MENU

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		DialogFragment dialog;
		Intent intent;
		Bundle bundle;

		switch (id) {

			// Feedback
			case R.id.action_feedback:
				intent = new Intent(this, FeedbackActivity.class);
				bundle = new Bundle();
				bundle.putString(Constants.KEY_URL, getString(R.string.url_contact));
				intent.putExtras(bundle);
				startActivity(intent);
				break;

			// Bug report
			case R.id.action_bug_report:
				intent = new Intent(this, FeedbackActivity.class);
				bundle = new Bundle();
				bundle.putString("url", getString(R.string.url_bug_report));
				intent.putExtras(bundle);
				startActivity(intent);
				break;

			// Terms of use
			case R.id.action_terms_of_use:
				dialog = new TermsOfUseDialogFragment();
				dialog.show(getSupportFragmentManager(), "TermsOfUseDialogFragment");
				break;

			// Privacy policy
			case R.id.action_privacy_policy:
				dialog = new PrivacyPolicyDialogFragment();
				dialog.show(getSupportFragmentManager(), "PrivacyPolicyDialogFragment");
				break;

			// Icons attribution
			case R.id.action_material_icons:
				dialog = new MaterialIconsDialogFragment();
				dialog.show(getSupportFragmentManager(), "MaterialIconsDialogFragment");
				break;

		}

		return super.onOptionsItemSelected(item);
	}

}