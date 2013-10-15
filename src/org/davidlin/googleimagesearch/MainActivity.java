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
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String LOGTAG = "org.davidlin.debug";
	private EditText searchBox;
	private GridView imageGrid;
	private List<ImageResult> imageList = new ArrayList<ImageResult>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void setupViews() {
		searchBox = (EditText) findViewById(R.id.etSearchBox);
		imageGrid = (GridView) findViewById(R.id.gvImages);
	}
	
	public void imageSearch(View v) {
		// Grab search term
		String searchTerm = searchBox.getText().toString();
		Toast.makeText(getApplicationContext(), "Searching for " + searchTerm, Toast.LENGTH_SHORT).show();
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + Uri.encode(searchTerm), new JsonHttpResponseHandler() {
		        @Override
		        public void onSuccess(JSONObject response) {
		            //Log.d(LOGTAG, response);
		        	JSONArray imageJsonResults;
		        	try {
						imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
						imageList.clear();
						imageList.addAll(ImageResult.fromJSONArray(imageJsonResults));
					} catch (JSONException e) {
						e.printStackTrace();
					}
		        }
		    }
		);
	}

}
