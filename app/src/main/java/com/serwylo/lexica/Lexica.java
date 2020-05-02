/*
 *  Copyright (C) 2008-2009 Rev. Johnny Healey <rev.null@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.serwylo.lexica;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

public class Lexica extends Activity {

	@SuppressWarnings("unused")
	protected static final String TAG = "Lexica";

	private static final int DIALOG_NO_SAVED = 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		try {
       		super.onCreate(savedInstanceState);
			splashScreen();
		} catch (Exception e) {
			// Log.e(TAG,"top level",e);
		}
    }

	private void splashScreen() {
		setContentView(R.layout.splash);

		FancyButton newGame = findViewById(R.id.new_game);
		// Log.d(TAG,"b="+b);
		newGame.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent("com.serwylo.lexica.action.NEW_GAME"));
			}
		});

		if(savedGame()) {
			FancyButton restoreGame = findViewById(R.id.restore_game);
			restoreGame.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if(savedGame()) {
						// Log.d(TAG,"restoring game");
						startActivity(new
							Intent("com.serwylo.lexica.action.RESTORE_GAME"));
					} else {
						// Log.d(TAG,"no saved game :(");
						showDialog(DIALOG_NO_SAVED);
					}
				}
			});
			restoreGame.setEnabled(true);
		}

		FancyButton about = findViewById(R.id.about);
		about.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				Uri u = Uri.parse("https://github.com/lexica/lexica");
				i.setData(u);
				startActivity(i);
			}
		});

		FancyButton preferences = findViewById(R.id.preferences);
		preferences.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new
					Intent("com.serwylo.lexica.action.CONFIGURE"));
			}
		});

		int highScoreValue = ScoreActivity.getHighScore(this);

		TextView highScoreLabel = findViewById(R.id.high_score_label);
		highScoreLabel.setText(getResources().getString(R.string.high_score, 0));

		TextView highScore = findViewById(R.id.high_score);
		highScore.setText(String.format(Locale.getDefault(), "%d", highScoreValue));
	}

	public void onPause() {
		super.onPause();
		// Log.d(TAG,"Pausing");
	}

	public void onResume() {
		super.onResume();
		splashScreen();
	}

	public boolean savedGame() {
		return new GameSaverPersistent(this).hasSavedGame();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
			case DIALOG_NO_SAVED:
				return new AlertDialog.Builder(this)
					.setTitle(getResources().
						getString(R.string.dialog_no_saved))
					.setPositiveButton(R.string.dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
								int whichButton) {
									// do nothing.
								}
						})
					.create();
		}
		return null;
	}

}
