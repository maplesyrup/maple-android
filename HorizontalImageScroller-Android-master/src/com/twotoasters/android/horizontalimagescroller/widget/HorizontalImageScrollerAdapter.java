/*
Copyright 2012 Two Toasters, LLC

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.twotoasters.android.horizontalimagescroller.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.twotoasters.android.horizontalimagescroller.R;
import com.twotoasters.android.horizontalimagescroller.image.BitmapHelper;
import com.twotoasters.android.horizontalimagescroller.image.ImageToLoad;
import com.twotoasters.android.horizontalimagescroller.image.ImageToLoadDrawableResource;
import com.twotoasters.android.horizontalimagescroller.image.ImageToLoadUrl;
import com.twotoasters.android.horizontalimagescroller.io.ImageCacheManager;
import com.twotoasters.android.horizontalimagescroller.io.ImageUrlRequest;

public class HorizontalImageScrollerAdapter extends BaseAdapter {
	protected Context _context;
	protected int _imageSize;
	protected int _frameColor;
	protected int _frameOffColor;
	protected int _transparentColor;
	protected int _imageLayoutResourceId;
	protected int _loadingImageResourceId;
	protected LayoutInflater _inflater;
	protected int _currentImageIndex = -1;
	protected boolean _highlightActive = true;
	protected boolean _showImageFrame = true;
	protected ImageCacheManager _imageCacheManager;
	protected OnClickListener _imageOnClickListener;
	protected int _defaultImageFailedToLoadResourceId;
	protected List<ImageToLoad> _images;
	protected int _imageIdInLayout;
	protected int _textIdInLayout;
	protected int _innerWrapperIdInLayout;

	/*
	 * Text only mode was added to allow display of text styles. A list of
	 * paints is passed in, as well as a text string to display
	 */
	protected boolean _textOnlyMode = false;
	protected String _textToShow;
	protected ArrayList<TextStyle> _styles;
	protected float _textStyleSize;
	protected int _textHeight;
	protected int _textWidth;

	// Text shown beneath an image. Indices match up with those of _images
	protected ArrayList<String> _text;
	protected boolean _showText = false;

	public HorizontalImageScrollerAdapter(final Context context,
			final List<ImageToLoad> images, final int imageSize,
			final int frameColorResourceId, final int frameOffColorResourceId,
			final int transparentColorResourceId,
			final int imageLayoutResourceId, final int loadingImageResourceId) {
		_context = context;
		_inflater = LayoutInflater.from(context);
		_images = images;
		Resources res = context.getResources();
		_imageSize = imageSize;
		_frameColor = res.getColor(frameColorResourceId);
		_frameOffColor = res.getColor(frameOffColorResourceId);
		_transparentColor = res.getColor(transparentColorResourceId);
		_imageLayoutResourceId = imageLayoutResourceId;
		_loadingImageResourceId = loadingImageResourceId;
		_imageCacheManager = ImageCacheManager.getInstance(context);
	}

	public HorizontalImageScrollerAdapter(final Context context,
			final List<ImageToLoad> images) {
		_context = context;
		_inflater = LayoutInflater.from(context);
		_images = images;
		_setDefaultValues();
	}

	private void _setDefaultValues() {
		Resources res = _context.getResources();
		_imageSize = (int) res.getDimension(R.dimen.default_image_size);
		_frameColor = res.getColor(R.color.default_frame_color);
		_frameOffColor = res.getColor(R.color.default_frame_off_color);
		_transparentColor = res.getColor(R.color.default_transparent_color);
		_imageLayoutResourceId = R.layout.horizontal_image_scroller_item;
		_imageCacheManager = ImageCacheManager.getInstance(_context);
		_imageIdInLayout = R.id.image;
		_textIdInLayout = R.id.text;
		_innerWrapperIdInLayout = R.id.image_frame;
	}

	/**
	 * Enables text only mode. This is a completely different mode that doesn't
	 * show images, and is not related to setShowText. In this mode, a single
	 * string of text is repeated with different styles. This is meant to be
	 * used to display different paint styles. If this mode is enabled, any
	 * options for images or setShowText will be ignored.
	 * 
	 * @param bool
	 *            Whether or not this mode is enabled.
	 * @param text
	 *            String to display, or null if mode is false
	 * @param paints
	 *            Paints to use for each frame, or null if mode is false
	 */
	public void setTextOnlyMode(boolean bool, String text, float size,
			ArrayList<TextStyle> styles, int width, int height) {
		_textOnlyMode = bool;
		_textToShow = text;
		_textStyleSize = size;
		_styles = styles;	
		_textWidth = width;
		_textHeight= height;
	}

	/**
	 * Set whether or not text should be shown beneath the image in the
	 * scrollview. If this is true, a supporting image layout must have also
	 * been set, and text values must be supplied through setTextList
	 * 
	 * @param show
	 *            Whether or not to show text beneath image
	 */
	public void setShowText(boolean show) {
		_showText = show;
	}

	/**
	 * The list of strings to display underneath the images. The indices should
	 * match with the list of images
	 * 
	 * @param text
	 *            List of strings to show in the scroller
	 */
	public void setTextList(ArrayList<String> text) {
		_text = text;
	}

	public int getImageSize() {
		return _imageSize;
	}

	public void setImageSize(int imageSize) {
		_imageSize = imageSize;
		notifyDataSetChanged();
	}

	public int getFrameColor() {
		return _frameColor;
	}

	public void setFrameColor(int frameColor) {
		_frameColor = frameColor;
		notifyDataSetChanged();
	}

	public int getFrameOffColor() {
		return _frameOffColor;
	}

	public void setFrameOffColor(int frameOffColor) {
		_frameOffColor = frameOffColor;
		notifyDataSetChanged();
	}

	public int getTransparentColor() {
		return _transparentColor;
	}

	public void setTransparentColor(int transparentColor) {
		_transparentColor = transparentColor;
		notifyDataSetChanged();
	}

	public int getImageLayoutResourceId() {
		return _imageLayoutResourceId;
	}

	public void setImageLayoutResourceId(int imageLayoutResourceId) {
		_imageLayoutResourceId = imageLayoutResourceId;
		notifyDataSetChanged();
	}

	public int getLoadingImageResourceId() {
		return _loadingImageResourceId;
	}

	public void setLoadingImageResourceId(int loadingImageResourceId) {
		_loadingImageResourceId = loadingImageResourceId;
		notifyDataSetChanged();
	}

	public boolean isShowImageFrame() {
		return _showImageFrame;
	}

	public void setCurrentIndex(int index) {
		_currentImageIndex = index;
		notifyDataSetChanged();
	}

	public int getCurrentIndex() {
		return _currentImageIndex;
	}

	public boolean hasCurrentIndex() {
		return _currentImageIndex >= 0;
	}

	public void setHighlightActiveImage(boolean highlight) {
		_highlightActive = highlight;
	}

	public void setShowImageFrame(boolean b) {
		_showImageFrame = b;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (getCount() > 0) {
			if (view == null) {
				view = _inflater.inflate(_imageLayoutResourceId, null);
			}
			
			

			// if text only mode is enabled, set up text styles 
			if (_textOnlyMode) {
				ImageView imageView = (ImageView) view.findViewById(_imageIdInLayout);
				
				if (_imageOnClickListener != null)
					imageView.setOnClickListener(_imageOnClickListener);
				
				// adjust layout
				_setupImageViewLayout(view, null, position);
				_setupInnerWrapper(view, null, position);
				
				// create bitmap showing the text with given style
				Bitmap bm = myDrawText(_textToShow, _styles.get(position));
				imageView.setImageBitmap(bm);
			} 
			// otherwise use the scroller for images like normal
			else {
				ImageToLoad imageToLoad = getItem(position);
				ImageView imageView = (ImageView) view
						.findViewById(_imageIdInLayout);
				_imageCacheManager.unbindImage(imageView);
				imageToLoad.setImageView(imageView);

				_setupImageViewLayout(view, imageToLoad, position);
				_setupInnerWrapper(view, imageToLoad, position);
				if (imageToLoad instanceof ImageToLoadUrl) {
					ImageToLoadUrl imageToLoadUrl = (ImageToLoadUrl) imageToLoad;
					ImageUrlRequest imageUrlRequest = new ImageUrlRequest(
							imageToLoadUrl, _imageSize, _imageSize);
					if (imageToLoadUrl.getOnImageLoadFailureResourceId() == 0
							&& _defaultImageFailedToLoadResourceId != 0) {
						imageUrlRequest
								.setImageFailedToLoadResourceId(_defaultImageFailedToLoadResourceId);
					} else if (imageToLoadUrl.getOnImageLoadFailureResourceId() != 0) {
						imageUrlRequest
								.setImageFailedToLoadResourceId(imageToLoadUrl
										.getOnImageLoadFailureResourceId());
					}
					BitmapHelper.applySampledResourceToImageView(
							_context.getResources(), _loadingImageResourceId,
							_imageSize, _imageSize, imageView);
					_imageCacheManager.bindDrawable(imageUrlRequest);
				} else if (imageToLoad instanceof ImageToLoadDrawableResource) {
					ImageToLoadDrawableResource imageToLoadDrawableResource = (ImageToLoadDrawableResource) imageToLoad;
					BitmapHelper
							.applySampledResourceToImageView(_context
									.getResources(),
									imageToLoadDrawableResource
											.getDrawableResourceId(),
									_imageSize, _imageSize, imageView);
				}
				
				// add text to the view if applicable
				if (_showText) {
					_setupTextViewLayout(view, position);
				}
			}			
		}
		return view;
	}
	
	/** Create a bitmap showing a text drawn in a certain style
	 * 
	 */
	private Bitmap myDrawText(String text, TextStyle style) {		
		Bitmap bm = Bitmap.createBitmap(_textWidth, _textHeight, Bitmap.Config.ARGB_8888);; 
	    Canvas myCanvas = new Canvas(bm); 
	    // draw each paint in the style to get the intended effect
	    for(Paint p : style){
	    	p.setTextSize(_textStyleSize);
	    	myCanvas.drawText(text, (_textWidth / 2) - (style.getWidth(text) / 2), (_textHeight / 2) - (style.getHeight() / 2), p);
	    }
	    return bm;
	}

	protected void _setupTextViewLayout(View view, int position) {
		TextView textView = (TextView) view.findViewById(_textIdInLayout);
		textView.setText(_text.get(position));
	}

	protected void _setupImageViewLayout(View view, ImageToLoad imageToLoad,
			int position) {
		ImageView imageView = (ImageView) view.findViewById(_imageIdInLayout);
		LayoutParams params = imageView.getLayoutParams();
		params.width = LayoutParams.MATCH_PARENT;
		params.height = _imageSize;
		imageView.setLayoutParams(params);
	}

	protected void _setupInnerWrapper(View view, ImageToLoad imageToLoad,
			int position) {
		View frame = view.findViewById(_innerWrapperIdInLayout);
		LayoutParams frameParams = frame.getLayoutParams();
		frameParams.width = LayoutParams.WRAP_CONTENT;
		frameParams.height = LayoutParams.WRAP_CONTENT;
		frame.setLayoutParams(frameParams);
		if (_showImageFrame == false) {
			frame.setBackgroundColor(_transparentColor);
		} else if (_highlightActive && _currentImageIndex == position) {
			frame.setBackgroundColor(_frameColor);
		} else {
			frame.setBackgroundColor(_frameOffColor);
		}
	}

	@Override
	public ImageToLoad getItem(int position) {
		return _images.get(position);
	}

	@Override
	public int getCount() {
		return _images.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void unbindImageViews() {
		if (_images != null) {
			ImageCacheManager icm = ImageCacheManager.getInstance(_context);
			for (ImageToLoad image : _images) {
				if (image instanceof ImageToLoadUrl) {
					icm.unbindImage(((ImageToLoadUrl) image).getImageView());
				}
			}
		}
	}

	public int getDefaultImageFailedToLoadResourceId() {
		return _defaultImageFailedToLoadResourceId;
	}

	public void setDefaultImageFailedToLoadResourceId(
			int defaultImageFailedToLoadResourceId) {
		_defaultImageFailedToLoadResourceId = defaultImageFailedToLoadResourceId;
	}

	/**
	 * Returns the bitmap of the image at the given index of the scroller
	 * 
	 * @param index
	 *            The location in the scroller
	 * @return The image shown at this location, or null if it is not available
	 */
	public Bitmap getImageAtPos(int index) {
		// Image cache manager keeps track of the bitmaps.
		// It uses a ImageUrlRequest to create a key into the
		// cache
		ImageToLoadUrl imageToLoadUrl = (ImageToLoadUrl) _images.get(index);
		ImageUrlRequest imageUrlRequest = new ImageUrlRequest(imageToLoadUrl,
				_imageSize, _imageSize);
		return _imageCacheManager.getBitmap(imageUrlRequest);
	}

}