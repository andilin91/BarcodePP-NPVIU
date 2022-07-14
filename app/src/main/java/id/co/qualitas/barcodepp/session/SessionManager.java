package id.co.qualitas.barcodepp.session;


import id.co.qualitas.barcodepp.constants.Constants;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;
	SharedPreferences prefURL;
	SharedPreferences prefPE;
	// Editor for Shared preferences
	Editor editor;
	Editor editorURL;
	Editor editorPE;
	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(Constants.PREF_NAME, PRIVATE_MODE);
		prefURL = _context.getSharedPreferences(Constants.PREF_NAME_URL, PRIVATE_MODE);
		prefPE = _context.getSharedPreferences(Constants.PREF_PE, PRIVATE_MODE);
		editor = pref.edit();
		editorURL = prefURL.edit();
		editorPE = prefPE.edit();
	}

	public void createDataSession(String ex) {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		// Storing email in pref
		editor.putBoolean(Constants.IS_DATA, true);
		editor.putString(Constants.KEY_DATA, ex);
		// commit changes
		editor.commit();
	}
	public void createPESession(String ex) {
		// Clearing all data from Shared Preferences
		editorPE.clear();
		editorPE.commit();
		// Storing email in pref
		editorPE.putBoolean(Constants.IS_PE, true);
		editorPE.putString(Constants.KEY_PE, ex);
		// commit changes
		editorPE.commit();
	}

	public void createUrlSession(String url) {
		// Clearing all data from Shared Preferences
		editorURL.clear();
		editorURL.commit();
		// Storing email in pref
		editorURL.putBoolean(Constants.IS_URL, true);
		editorURL.putString(Constants.KEY_URL, url);
		// commit changes
		editorURL.commit();
	}
	
	/**
	 * Get stored session data
	 * */
	public Map<String, String> getDataDetails() {
		HashMap<String, String> user = new HashMap<String, String>();

		// user email id
		user.put(Constants.KEY_DATA,
				pref.getString(Constants.KEY_DATA, null));
		// return user
		return user;
	}
	
	/**
	 * Get stored session data
	 * */
	public Map<String, String> getPEDetails() {
		HashMap<String, String> user = new HashMap<String, String>();

		// user email id
		user.put(Constants.KEY_PE,
				prefPE.getString(Constants.KEY_PE, null));
		// return user
		return user;
	}

	public Map<String, String> getUrl() {
		HashMap<String, String> url = new HashMap<String, String>();

		// user email id
		url.put(Constants.KEY_URL,
				prefURL.getString(Constants.KEY_URL, null));
		// return user
		return url;
	}
	
	/**
	 * Clear session details
	 * */
	public void clearData() {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

	}
	
	public void clearURL() {
		// Clearing all data from Shared Preferences
		editorURL.clear();
		editorURL.commit();

	}
	public void clearPE() {
		// Clearing all data from Shared Preferences
		editorPE.clear();
		editorPE.commit();

	}

	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isDataIn() {
		return pref.getBoolean(Constants.IS_DATA, false);
	}

	public boolean isUrlEmpty() {
		return prefURL.getBoolean(Constants.IS_URL, false);
	}
	public boolean isPEEmpty() {
		return prefPE.getBoolean(Constants.IS_PE, false);
	}
}

