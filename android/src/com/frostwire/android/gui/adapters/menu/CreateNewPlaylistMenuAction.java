/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml)
 * Copyright (c) 2011-2014, FrostWire(R). All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.frostwire.android.gui.adapters.menu;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andrew.apollo.utils.MusicUtils;
import com.frostwire.android.R;
import com.frostwire.android.gui.views.MenuAction;

/**
 * Created by gubatron on 12/18/14.
 * Adapter in control of the List View shown when we're browsing the files of
 * one peer.
 *
 * @author gubatron
 * @author aldenml
 */
public class CreateNewPlaylistMenuAction extends MenuAction {
    private final long[] fileDescriptors;

    /* Dialog's default input text */
    EditText input;

    public CreateNewPlaylistMenuAction(Context context, long[] fileDescriptors) {
        super(context, getIconResourceId(context), R.string.new_empty_playlist);
        this.fileDescriptors = fileDescriptors;
    }

    @Override
    protected void onClick(Context context) {
        showCreateNewPlaylistDialog();
    }

    private void showCreateNewPlaylistDialog() {

        final Dialog newCreateNewPlaylistDialog = new Dialog(getContext(), R.style.DefaultDialogTheme);
        newCreateNewPlaylistDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        newCreateNewPlaylistDialog.setContentView(R.layout.dialog_default_input);

        TextView title = (TextView) newCreateNewPlaylistDialog.findViewById(R.id.dialog_default_input_title);
        title.setText(R.string.new_empty_playlist);

        input = (EditText) newCreateNewPlaylistDialog.findViewById(R.id.dialog_default_input_text);
        input.setText("");
        input.setHint(R.string.create_playlist_prompt);
        input.selectAll();

        Button noButton = (Button) newCreateNewPlaylistDialog.findViewById(R.id.dialog_default_input_button_no);
        noButton.setText(R.string.cancel);
        Button yesButton = (Button) newCreateNewPlaylistDialog.findViewById(R.id.dialog_default_input_button_yes);
        yesButton.setText(android.R.string.ok);

        noButton.setOnClickListener(new NegativeButtonOnClickListener(this, newCreateNewPlaylistDialog));
        yesButton.setOnClickListener(new PositiveButtonOnClickListener(this, newCreateNewPlaylistDialog));


//        buttonYes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String playlistName = input.getText().toString();
//                if (MusicUtils.getIdForPlaylist(getContext(), playlistName) != -1) {
//                    playlistName += "+";
//                    input.setText(playlistName);
//
//                } else {
//                    onClickCreatePlaylistButton(playlistName);
//                    newCreateNewPlaylistDialog.dismiss();
//                }
//            }
//        });

        newCreateNewPlaylistDialog.show();
    }

    private void onClickCreatePlaylistButton(CharSequence text) {
        long playlistId = MusicUtils.createPlaylist(getContext(), text.toString());
        MusicUtils.refresh();

        if (fileDescriptors != null) {
            MusicUtils.addToPlaylist(getContext(), fileDescriptors, playlistId);
        }
    }

    private static int getIconResourceId(Context context) {
        return R.drawable.contextmenu_icon_playlist_add_dark;
    }

    private class NegativeButtonOnClickListener implements View.OnClickListener {
        private final Dialog newCreateNewPlaylistDialog;
        private final CreateNewPlaylistMenuAction createNewPlaylistMenuAction;

        public NegativeButtonOnClickListener(CreateNewPlaylistMenuAction createNewPlaylistMenuAction, Dialog newCreateNewPlaylistDialog) {
            this.newCreateNewPlaylistDialog = newCreateNewPlaylistDialog;
            this.createNewPlaylistMenuAction = createNewPlaylistMenuAction;
        }

        @Override
        public void onClick(View view) {
            newCreateNewPlaylistDialog.dismiss();
        }
    }

    private class PositiveButtonOnClickListener implements View.OnClickListener {
        private final Dialog newCreateNewPlaylistDialog;
        private final CreateNewPlaylistMenuAction createNewPlaylistMenuAction;


        public PositiveButtonOnClickListener(CreateNewPlaylistMenuAction createNewPlaylistMenuAction, Dialog newCreateNewPlaylistDialog) {
            this.newCreateNewPlaylistDialog = newCreateNewPlaylistDialog;
            this.createNewPlaylistMenuAction = createNewPlaylistMenuAction;
        }

        @Override
        public void onClick(View view) {
            String playlistName = input.getText().toString();
            if (MusicUtils.getIdForPlaylist(getContext(), playlistName) != -1) {
                playlistName += "+";
                input.setText(playlistName);

            } else {
                onClickCreatePlaylistButton(playlistName);
                newCreateNewPlaylistDialog.dismiss();
            }
        }
    }
}