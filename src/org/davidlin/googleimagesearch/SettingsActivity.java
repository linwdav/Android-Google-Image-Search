package org.davidlin.googleimagesearch;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class SettingsActivity extends Activity {

	private Spinner spSize;
	private Spinner spColorFilter;
	private Spinner spType;
	private EditText etSiteFilter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		setupViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	public void setupViews() {
		spSize = (Spinner) findViewById(R.id.spSize);
		spColorFilter = (Spinner) findViewById(R.id.spColorFilter);
		spType = (Spinner) findViewById(R.id.spType);
		etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		String size = pref.getString("size", "none");
		String colorfilter = pref.getString("colorfilter", "none");
		String type = pref.getString("type", "none");
		String sitefilter = pref.getString("sitefilter", "");
		
		setSpinnerToValue(spSize, size);
		setSpinnerToValue(spColorFilter, colorfilter);
		setSpinnerToValue(spType, type);
		etSiteFilter.setText(sitefilter);
	}
	
	public void setSpinnerToValue(Spinner spinner, String value) {
		int index = 0;
		SpinnerAdapter adapter = spinner.getAdapter();
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).equals(value)) {
				index = i;
			}
		}
		spinner.setSelection(index);
	}
	
	public void onSaveButtonClicked(View v) {
		Intent i = new Intent(this.getApplicationContext(), SearchActivity.class);
		i.putExtra("size", spSize.getSelectedItem().toString());
		i.putExtra("colorfilter", spColorFilter.getSelectedItem().toString());
		i.putExtra("type", spType.getSelectedItem().toString());
		i.putExtra("sitefilter", etSiteFilter.getText().toString());
		setResult(RESULT_OK, i);
		finish();
	}

}
