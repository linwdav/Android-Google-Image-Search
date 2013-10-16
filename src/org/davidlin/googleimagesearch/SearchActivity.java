package org.davidlin.googleimagesearch;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class SearchActivity extends Activity {
	
	private static final String LOGTAG = "org.davidlin.debug";
	private static final int INITIAL_ENDING_IMAGE_INDEX = 12;
	private static final int IMAGE_COUNT_PER_PAGE = 4;
	private static final int MAX_IMAGE_INDEX = 64;
	private static int currentImageIndex = 0;
	
	private EditText etSearchBox;
	private GridView gvImages;
	private List<ImageResult> imageArray = new ArrayList<ImageResult>();
	private ImageResultArrayAdapter imageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void setupViews() {
		etSearchBox = (EditText) findViewById(R.id.etSearchBox);
		gvImages = (GridView) findViewById(R.id.gvImages);
		gvImages.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				Log.d(LOGTAG, "Page is " + page);
				Log.d(LOGTAG, "Total item count is " + totalItemsCount);
				performSearch();
			}
		});
		imageAdapter = new ImageResultArrayAdapter(this, imageArray);
		gvImages.setAdapter(imageAdapter);
		gvImages.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long arg3) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageArray.get(position);
				i.putExtra("url", imageResult.getUrl());
				startActivity(i);
			}
		});
	}
	
	public void performNewSearch(View v) {
		imageAdapter.clear();
		currentImageIndex = 0;
		performSearch();
	}
	
	public void performSearch() {
		// Grab search term
		String searchTerm = etSearchBox.getText().toString();
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		String size = pref.getString("size", "none");
		String colorfilter = pref.getString("colorfilter", "none");
		String type = pref.getString("type", "none");
		String sitefilter = pref.getString("sitefilter", "");
		
		int stopIndex;
		if (currentImageIndex == 0) {
			stopIndex = INITIAL_ENDING_IMAGE_INDEX;
			Toast.makeText(getApplicationContext(), "Searching for " + searchTerm, Toast.LENGTH_SHORT).show();
		}
		else {
			stopIndex = currentImageIndex + IMAGE_COUNT_PER_PAGE;
		}
		
		for (; currentImageIndex <= stopIndex && currentImageIndex < MAX_IMAGE_INDEX; currentImageIndex += IMAGE_COUNT_PER_PAGE) {
			String query = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&start=" + currentImageIndex + "&q=" + Uri.encode(searchTerm);
			if ("none".compareTo(size) != 0) {
				query += "&imgsz=" + Uri.encode(size);
			}
			if ("none".compareTo(colorfilter) != 0) {
				query += "&imgcolor=" + Uri.encode(colorfilter);
			}
			if ("none".compareTo(type) != 0) {
				query += "&imgtype=" + Uri.encode(type);
			}
			if ("".compareTo(sitefilter) != 0) {
				query += "&as_sitesearch=" + Uri.encode(sitefilter);
			}
			getImages(query);
		}
	}
	
	public void getImages(String query) {
		Log.d(LOGTAG, "Running the query: " + query);
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(query, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject response) {
					JSONArray imageJsonResults;
					try {
						imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
						imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		    }
		);
	}
	
	public void onSettingsButtonClicked(MenuItem mi) {
		Intent i = new Intent(this, SettingsActivity.class);
		startActivityForResult(i, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 0) {
			// Save to preferences file
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
			Editor edit = pref.edit();
			edit.putString("size", data.getStringExtra("size"));
			edit.putString("colorfilter", data.getStringExtra("colorfilter"));
			edit.putString("type", data.getStringExtra("type"));
			edit.putString("sitefilter", data.getStringExtra("sitefilter"));
			edit.commit();
			
			performNewSearch(null);
		}
	}

}
