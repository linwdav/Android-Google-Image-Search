package org.davidlin.googleimagesearch;

import java.util.ArrayList;
import java.util.List;

import com.loopj.android.image.SmartImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

	List<ImageResult> imageArray = new ArrayList<ImageResult>();
	
	public ImageResultArrayAdapter(Context context, List<ImageResult> imageArray) {
		super(context, R.layout.item_image, imageArray);
		this.imageArray = imageArray;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageResult imageInfo = this.getItem(position);
		SmartImageView ivImage;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			ivImage = (SmartImageView) inflater.inflate(R.layout.item_image, parent, false);
		} else {
			ivImage = (SmartImageView) convertView;
			ivImage.setImageResource(android.R.color.transparent);
		}
		ivImage.setImageUrl(imageInfo.getTbUrl());
		return ivImage;
	}

}
