/*
 *  Copyright (c) 2020 mohb apps - All Rights Reserved
 *
 *  Project       : ShutterNotes
 *  Developer     : Haraldo Albergaria Filho, a.k.a. mohb apps
 *
 *  File          : FlickrNotesListActivity.java
 *  Last modified : 10/8/20 6:00 PM
 *
 *  -----------------------------------------------------------
 */

package com.apps.mohb.shutternotes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.apps.mohb.shutternotes.adapters.FlickrNotesListAdapter;
import com.apps.mohb.shutternotes.fragments.dialogs.ArchiveSelectedNotesAlertFragment;
import com.apps.mohb.shutternotes.fragments.dialogs.NoteDeleteAlertFragment;
import com.apps.mohb.shutternotes.lists.Archive;
import com.apps.mohb.shutternotes.lists.Notebook;
import com.apps.mohb.shutternotes.notes.FlickrNote;
import com.apps.mohb.shutternotes.views.GridViewWithHeaderAndFooter;

import java.io.IOException;
import java.util.ArrayList;


public class FlickrNotesListActivity extends AppCompatActivity implements
        NoteDeleteAlertFragment.NoteDeleteDialogListener,
        ArchiveSelectedNotesAlertFragment.ArchiveSelectedNotesAlertDialogListener {

    private Notebook notebook;
    private Archive archive;
    private GridViewWithHeaderAndFooter notesListGridView;

    private AdapterView.AdapterContextMenuInfo menuInfo;
    private MenuItem menuItemUploadToFlickr;
    private MenuItem menuItemArchiveAll;

    private int listItemHeight;

    private Toast mustSelect;
    private Toast allNotesArchived;


    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flickr_notes_list);

        // Create list header and footer, that will insert spaces on top and bottom of the
        // list to make material design effect elevation and shadow
        View listHeader = getLayoutInflater().inflate(R.layout.list_header, notesListGridView);
        View listFooter = getLayoutInflater().inflate(R.layout.list_footer, notesListGridView);

        notesListGridView = findViewById(R.id.flickrNotesList);
        registerForContextMenu(notesListGridView);

        notesListGridView.addHeaderView(listHeader);
        notesListGridView.addFooterView(listFooter);
        listHeader.setClickable(false);
        listFooter.setClickable(false);

        notebook = Notebook.getInstance(getApplicationContext());
        archive = Archive.getInstance(getApplicationContext());

        notesListGridView.setOnItemClickListener((adapterView, view, i, l) -> {
            if (notebook.getFlickrNotes().size() > 0) { // Fix java.lang.IndexOutOfBoundsException: Invalid index 0, size is 0
                if (!notebook.getFlickrNotes().get(i).isSelected()) {
                    notebook.getFlickrNotes().get(i).setSelected(true);
                } else {
                    notebook.getFlickrNotes().get(i).setSelected(false);
                }
                notesListGridView.invalidateViews();
                try {
                    notebook.saveState();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mustSelect = Toast.makeText((this), R.string.toast_must_select, Toast.LENGTH_SHORT);

        // Define form factor of notes items accorging to screen height in pixels
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        listItemHeight = (int) (metrics.heightPixels / Constants.LIST_ITEM_HEIGHT_FACTOR);

    }

    @Override
    protected void onResume() {
        super.onResume();
        unselectAllNotes();
        ArrayList<FlickrNote> flickrNotesList = notebook.getFlickrNotes();
        notesListGridView.invalidateViews();
        FlickrNotesListAdapter notesAdapter = null;
        notesAdapter = new FlickrNotesListAdapter(this, flickrNotesList, listItemHeight);
        notesListGridView.setAdapter(notesAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            archive.saveState();
            notebook.saveState();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // CONTEXT MENU

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_notes, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            // Archive
            case R.id.archive:
                archive.addNote(notebook.getFlickrNotes().get(getCorrectPosition(menuInfo.position)));
                notebook.removeFlickrNote(getCorrectPosition(menuInfo.position));
                notesListGridView.invalidateViews();
                return true;

            // Delete
            case R.id.delete:
                NoteDeleteAlertFragment dialogDelete = new NoteDeleteAlertFragment();
                dialogDelete.show(getSupportFragmentManager(), "NoteDeleteDialogFragment");
                return true;

            default:
                return super.onContextItemSelected(item);

        }
    }

    // OPTIONS MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_flickr_notes, menu);
        menuItemUploadToFlickr = menu.findItem(R.id.action_upload_to_flickr);
        menuItemArchiveAll = menu.findItem(R.id.action_archive_all);
        menuItemUploadToFlickr.setEnabled(true);
        menuItemArchiveAll.setEnabled(true);
        if (notebook.getFlickrNotes().isEmpty()) {
            menuItemUploadToFlickr.setEnabled(false);
            menuItemArchiveAll.setEnabled(false);
        }
        MenuItem menuHelp = menu.findItem(R.id.action_help);
        menuHelp.setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        boolean noNoteSelected = true;
        for (int i = notebook.getFlickrNotes().size() - 1; i >= 0; i--) {
            FlickrNote note = notebook.getFlickrNotes().get(i);
            if (note.isSelected()) {
                noNoteSelected = false;
                break;
            }
        }

        switch (id) {

            // Upload to Flickr
            case R.id.action_upload_to_flickr: {
                if (noNoteSelected) {
                    mustSelect.show();
                } else {
                    Intent intent = new Intent(this, FlickrPhotosetsListActivity.class);
                    startActivity(intent);
                }
                break;
            }

            // Archive all notes
            case R.id.action_archive_all: {
                if (noNoteSelected) {
                    mustSelect.show();
                } else {
                    ArchiveSelectedNotesAlertFragment dialogArchive = new ArchiveSelectedNotesAlertFragment();
                    dialogArchive.show(getSupportFragmentManager(), "ArchiveSelectedNotesAlertFragment");
                }
                break;
            }

            // Select all notes
            case R.id.action_select_all: {
                selectAllNotes();
                break;
            }

            // Unselect all notes
            case R.id.action_unselect_all: {
                unselectAllNotes();
                break;
            }

            // Help
            case R.id.action_help: {
                Intent intent = new Intent(this, HelpActivity.class);
                intent.putExtra(Constants.KEY_URL, getString(R.string.url_help_flickr_notes));
                startActivity(intent);
                break;
            }

        }

        notesListGridView.invalidateViews();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            mustSelect.cancel();
            allNotesArchived.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNoteDeleteDialogPositiveClick(DialogFragment dialog) {
        notebook.removeFlickrNote(getCorrectPosition(menuInfo.position));
        notesListGridView.invalidateViews();
    }

    @Override
    public void onNoteDeleteDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void onArchiveSelectedNotesDialogPositiveClick(DialogFragment dialog) {
        for (int i = notebook.getFlickrNotes().size() - 1; i >= 0; i--) {
            FlickrNote note = notebook.getFlickrNotes().get(i);
            if (note.isSelected()) {
                archive.addNote(note);
                notebook.getFlickrNotes().remove(i);
            }
        }
        notesListGridView.invalidateViews();
        allNotesArchived = Toast.makeText((this), R.string.toast_all_notes_archived, Toast.LENGTH_SHORT);
        allNotesArchived.show();
        if (notebook.getFlickrNotes().isEmpty()) {
            menuItemUploadToFlickr.setEnabled(false);
            menuItemArchiveAll.setEnabled(false);
        }
    }

    @Override
    public void onArchiveSelectedNotesDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }


    // CLASS METHODS

    private int getCorrectPosition(int position) {
        return position - Constants.GRID_HEADER_POSITION;
    }

    private void selectAllNotes() {
        for (int i = 0; i < notebook.getFlickrNotes().size(); i++) {
            notebook.getFlickrNotes().get(i).setSelected(true);
        }
        try {
            notebook.saveState();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void unselectAllNotes() {
        for (int i = 0; i < notebook.getFlickrNotes().size(); i++) {
            notebook.getFlickrNotes().get(i).setSelected(false);
        }
        try {
            notebook.saveState();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}