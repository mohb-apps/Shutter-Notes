/*
 *  Copyright (c) 2020 mohb apps - All Rights Reserved
 *
 *  Project       : ShutterNotes
 *  Developer     : Haraldo Albergaria Filho, a.k.a. mohb apps
 *
 *  File          : GearNoteActivity.java
 *  Last modified : 10/15/20 7:30 AM
 *
 *  -----------------------------------------------------------
 */

package com.apps.mohb.shutternotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.apps.mohb.shutternotes.adapters.GearNoteAdapter;
import com.apps.mohb.shutternotes.fragments.dialogs.DeleteAllAlertFragment;
import com.apps.mohb.shutternotes.fragments.dialogs.EditGearListDialogFragment;
import com.apps.mohb.shutternotes.fragments.dialogs.GearDeleteAlertFragment;
import com.apps.mohb.shutternotes.lists.GearList;

import java.io.IOException;
import java.util.Objects;


public class GearNoteActivity extends AppCompatActivity
        implements EditGearListDialogFragment.EditGearListDialogListener,
        GearDeleteAlertFragment.GearDeleteDialogListener,
        DeleteAllAlertFragment.DeleteAllAlertDialogListener {

    private GearList gearList;
    private ListView gearListView;
    private GearNoteAdapter gearNoteAdapter;

    private AdapterView.AdapterContextMenuInfo menuInfo;

    private int callerActivity;

    private Toast mustPickup;
    private Toast reorderedItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gear_note);

        View listHeader = getLayoutInflater().inflate(R.layout.list_header, gearListView);
        View listFooter = getLayoutInflater().inflate(R.layout.list_footer, gearListView);

        Button buttonCancel = findViewById(R.id.buttonGearNoteCancel);
        Button buttonReset = findViewById(R.id.buttonGearNoteReset);
        Button buttonOK = findViewById(R.id.buttonGearNoteOk);

        gearListView = findViewById(R.id.gearList);

        gearListView.addHeaderView(listHeader);
        gearListView.addFooterView(listFooter);
        listHeader.setClickable(false);
        listFooter.setClickable(false);

        gearList = GearList.getInstance();

        gearListView.setOnItemClickListener((adapterView, view, i, l) -> {

            boolean itemIsSelected;

            try {
                itemIsSelected = gearList.get(getCorrectPosition(i)).isSelected();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            if (!itemIsSelected) {
                gearList.get(getCorrectPosition(i)).setSelected(true);
                gearList.moveToBottomOfLastSelected(getCorrectPosition(i));
            } else {
                gearList.get(getCorrectPosition(i)).setSelected(false);
                gearList.moveToBottom(getCorrectPosition(i));
            }
            gearListView.invalidateViews();
        });

        // menu shown when a list item is long clicked
        registerForContextMenu(gearListView);

        callerActivity = Objects.requireNonNull(getIntent().getExtras()).getInt(Constants.KEY_CALLER_ACTIVITY);

        try {
            switch (callerActivity) {
                case Constants.ACTIVITY_GEAR_NOTE:
                    Objects.requireNonNull(gearList).loadState(getApplicationContext(), Constants.GEAR_LIST_SAVED_STATE);
                    break;
                case Constants.ACTIVITY_FLICKR_NOTE:
                    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.activity_title_add_tags);
                    buttonOK.setText(R.string.button_ok);
                    Objects.requireNonNull(gearList).loadState(getApplicationContext(), Constants.GEAR_LIST_SELECTED_STATE);
                    if (gearList.getList().isEmpty()) {
                        gearList.loadState(getApplicationContext(), Constants.GEAR_LIST_SAVED_STATE);
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        buttonCancel.setOnClickListener(view -> onBackPressed());

        buttonReset.setOnClickListener(view -> {
            try {
                gearList.loadState(getApplicationContext(), Constants.GEAR_LIST_SAVED_STATE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            gearNoteAdapter = null;
            gearNoteAdapter = new GearNoteAdapter(this, gearList.getList());
            gearListView.setAdapter(gearNoteAdapter);
        });

        buttonOK.setOnClickListener(view -> {

            switch (callerActivity) {
                case Constants.ACTIVITY_GEAR_NOTE:
                    String textString = gearList.getGearListText();
                    if (textString.equals(Constants.EMPTY)) {
                        mustPickup = Toast.makeText((this), R.string.toast_must_pickup, Toast.LENGTH_SHORT);
                        mustPickup.show();
                    } else {
                        Intent intent = new Intent(this, FullscreenNoteActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.KEY_FULL_SCREEN_TEXT, textString.trim());
                        bundle.putInt(Constants.KEY_CALLER_ACTIVITY, Constants.ACTIVITY_GEAR_NOTE);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    break;

                case Constants.ACTIVITY_FLICKR_NOTE:
                    try {
                        gearList.saveState(getApplicationContext(), Constants.GEAR_LIST_SELECTED_STATE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    onBackPressed();
                    break;

            }
        });

        gearNoteAdapter = new GearNoteAdapter(this, gearList.getList());

        // create notes list
        gearListView.setAdapter(gearNoteAdapter);


    }

    // CONTEXT MENU

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_gear, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            // Edit
            case R.id.edit:
                gearList.setEditedGearItemText(getApplicationContext(), gearList.getGearItem(getCorrectPosition(menuInfo.position)));
                gearList.setEditedGearItemPosition(getApplicationContext(), getCorrectPosition(menuInfo.position));
                DialogFragment addGearDialog = new EditGearListDialogFragment();
                addGearDialog.show(getSupportFragmentManager(), "AddGearDialogFragment");
                return true;

            // Delete
            case R.id.delete:
                GearDeleteAlertFragment dialogDelete = new GearDeleteAlertFragment();
                dialogDelete.show(getSupportFragmentManager(), "GearDeleteDialogFragment");
                return true;

            default:
                return super.onContextItemSelected(item);

        }
    }

    // OPTIONS MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_gear_note, menu);
        MenuItem menuAddGear = menu.findItem(R.id.action_add_gear);
        menuAddGear.setEnabled(true);
        MenuItem menuHelp = menu.findItem(R.id.action_help);
        menuHelp.setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            // Add gear
            case R.id.action_add_gear: {
                gearList.setEditedGearItemText(getApplicationContext(), Constants.EMPTY);
                gearList.setEditedGearItemPosition(getApplicationContext(), Constants.NULL_POSITION);
                DialogFragment addGearDialog = new EditGearListDialogFragment();
                addGearDialog.show(getSupportFragmentManager(), "AddGearDialogFragment");
                break;
            }

            // Select all
            case R.id.action_select_all: {
                for (int i = 0; i < gearList.size(); i++) {
                    gearList.get(i).setSelected(true);
                }
                gearListView.invalidateViews();
                break;
            }

            // Delete all
            case R.id.action_delete_all: {
                DialogFragment dialog = new DeleteAllAlertFragment();
                dialog.show(getSupportFragmentManager(), "DeleteAllAlertFragment");
                break;
            }

            // Reorder
            case R.id.action_reorder: {
                try {
                    gearList.saveState(getApplicationContext(), Constants.GEAR_LIST_SAVED_STATE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reorderedItems = Toast.makeText((this), R.string.toast_reordered, Toast.LENGTH_SHORT);
                reorderedItems.show();
                break;
            }

            // Help
            case R.id.action_help: {
                Intent intent = new Intent(this, HelpActivity.class);
                intent.putExtra(Constants.KEY_URL, getString(R.string.url_help_gear_note));
                startActivity(intent);
                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            mustPickup.cancel();
            reorderedItems.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEditGearListDialogPositiveClick(DialogFragment dialog) {
        SharedPreferences gearTextEdit = this.getSharedPreferences(Constants.EDIT_GEAR_TEXT, Constants.PRIVATE_MODE);
        int itemPosition = gearTextEdit.getInt(Constants.GEAR_ITEM_POSITION, Constants.NULL_POSITION);
        if (itemPosition < 0) {
            try {
                gearList.loadState(getApplicationContext(), Constants.GEAR_LIST_SAVED_STATE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String editedText = gearTextEdit.getString(Constants.GEAR_EDITED_TEXT, Constants.EMPTY);
            gearList.setGearItem(itemPosition, editedText);
        }
        gearNoteAdapter = null;
        gearNoteAdapter = new GearNoteAdapter(this, gearList.getList());
        gearListView.setAdapter(gearNoteAdapter);
        try {
            gearList.saveState(getApplicationContext(), Constants.GEAR_LIST_SAVED_STATE);
            if (callerActivity == Constants.ACTIVITY_FLICKR_NOTE) {
                gearList.saveState(getApplicationContext(), Constants.GEAR_LIST_SELECTED_STATE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onEditGearListDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void onGearDeleteDialogPositiveClick(DialogFragment dialog) {
        gearList.remove(getCorrectPosition(menuInfo.position));
        try {
            gearList.saveState(getApplicationContext(), Constants.GEAR_LIST_SAVED_STATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gearListView.invalidateViews();
    }

    @Override
    public void onGearDeleteDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void onDeleteAllDialogPositiveClick(DialogFragment dialog) {
        gearList.getList().clear();
        gearListView.setAdapter(gearNoteAdapter);
        try {
            gearList.saveState(getApplicationContext(), Constants.GEAR_LIST_SAVED_STATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeleteAllDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    // CLASS METHODS

    private int getCorrectPosition(int position) {
        return position - Constants.LIST_HEADER_POSITION;
    }

}
