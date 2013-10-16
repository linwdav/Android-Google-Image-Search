package org.davidlin.googleimagesearch;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult {

	private String title;
	private String fullUrl;
	private String thumbUrl;
	
	public ImageResult(JSONObject json) {
		try {
			this.title = json.getString("titleNoFormatting");
			this.fullUrl = json.getString("url");
			this.thumbUrl = json.getString("tbUrl");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getUrl() {
		return fullUrl;
	}
	
	public String getTbUrl() {
		return thumbUrl;
	}
	
	public String toString() {
		return this.thumbUrl;
	}

	public static List<ImageResult> fromJSONArray(JSONArray imageJsonResults) {
		List<ImageResult> imageArray = new ArrayList<ImageResult>();
		for (int i = 0; i < imageJsonResults.length(); i++) {
			try {
				imageArray.add(new ImageResult(imageJsonResults.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return imageArray;
	}
	
}
