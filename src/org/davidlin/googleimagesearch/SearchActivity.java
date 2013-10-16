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
import android.app.Activity;
import android.content.Intent;
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
	
	private EditText etSearchBox;
	private GridView gvImages;
	private List<ImageResult> imageArray = new ArrayList<ImageResult>();
	private ImageResultArrayAdapter imageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void setupViews() {
		etSearchBox = (EditText) findViewById(R.id.etSearchBox);
		gvImages = (GridView) findViewById(R.id.gvImages);
	}
	
	public void onSearchButtonClicked(View v) {
		// Grab search term
		String searchTerm = etSearchBox.getText().toString();
		Toast.makeText(getApplicationContext(), "Searching for " + searchTerm, Toast.LENGTH_SHORT).show();
		
		imageArray.clear();
		for (int i = 0; i <= 8 ; i+=4) {
			String query = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&start=" + i + "&q=" + Uri.encode(searchTerm);
			getImages(query);
		}
	}
	
	public void getImages(String query) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(query, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject response) {
					JSONArray imageJsonResults;
					try {
						imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
						imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
						//Log.d(LOGTAG, imageArray.toString());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
		    }
		);
	}
	
	public void onSettingsButtonClicked(MenuItem mi) {
		
	}

}
