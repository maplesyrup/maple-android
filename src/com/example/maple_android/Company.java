package com.example.maple_android;

import java.util.ArrayList;

/**
 * This class encapsulates all of the JSON data that 
 * specifies a company profile
 * 
 * @author Eli
 *
 */
public class Company {
	
	private int mId;
	private String mName;
	private String mSplashImage;
	private String mBlurbTitle;
	private String mBlurbBody;
	private String mMoreInfoTitle;
	private String mMoreInfoBody;
	private String mCompanyUrl;
	private ArrayList<LogoURL> mLogoUrls;
	private boolean mEditable;
	
	/** Take all the fields of the JSON string and create a
	 * Company Object with it
	 * 
	 * @param id
	 * @param name
	 * @param splashImage
	 * @param blurbTitle
	 * @param blurbBody
	 * @param moreInfoTitle
	 * @param moreInfoBody
	 * @param companyUrl
	 * @param logoUrls
	 * @param editable
	 */
	public Company(int id, String name, String splashImage, String blurbTitle, String blurbBody, String moreInfoTitle, 
			String moreInfoBody, String companyUrl, ArrayList<LogoURL> logoUrls, boolean editable){
		
		mId = id;
		mName = name;
		mSplashImage = splashImage;
		mBlurbTitle = blurbTitle;
		mBlurbBody = blurbBody;
		mMoreInfoTitle = moreInfoTitle;
		mMoreInfoBody = moreInfoBody;
		mCompanyUrl = companyUrl;
		mLogoUrls = logoUrls;
		mEditable = editable;		
	}
	
	public int getId(){
		return mId;
	}
	
	public String getName() {
		return mName;
	}

	public String getSplashImage() {
		return mSplashImage;
	}

	public String getBlurbTitle() {
		return mBlurbTitle;
	}

	public String getBlurbBody() {
		return mBlurbBody;
	}

	public String getMoreInfoTitle() {
		return mMoreInfoTitle;
	}

	public String getMoreInfoBody() {
		return mMoreInfoBody;
	}

	public String getCompanyUrl() {
		return mCompanyUrl;
	}

	public ArrayList<LogoURL> getLogoUrls() {
		return mLogoUrls;
	}

	public boolean isEditable() {
		return mEditable;
	}
	
		
	public String toString() {
		return "Company [mId=" + mId + ", mName=" + mName + ", mSplashImage="
				+ mSplashImage + ", mBlurbTitle=" + mBlurbTitle
				+ ", mBlurbBody=" + mBlurbBody + ", mMoreInfoTitle="
				+ mMoreInfoTitle + ", mMoreInfoBody=" + mMoreInfoBody
				+ ", mCompanyUrl=" + mCompanyUrl + ", mLogoUrls=" + mLogoUrls
				+ ", mEditable=" + mEditable + "]";
	}


	/**
	 * Each logo has three sizes: full, medium, thumb	
	 * This class stores the urls for the three sizes and allows 
	 * access to each
	 * 
	 * @author Eli
	 *
	 */
	public class LogoURL {
		private String mFull;
		private String mMedium;
		private String mThumb;
		
		/** Create a new LogoURL object by specifying the urls
		 * for the full, medium, and thumb sizes
		 * @param full
		 * @param medium
		 * @param thumb
		 */
		public LogoURL(String full, String medium, String thumb){
			mFull = full;
			mMedium = medium;
			mThumb = thumb;
		}
		
		public String getFull(){
			return mFull;
		}
		
		public String getMedium(){
			return mMedium;
		}
		
		public String getThumb(){
			return mThumb;
		}		
	}

}
