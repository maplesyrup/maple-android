package com.example.maple_android;

/**
 * Each logo has three sizes: full, medium, thumb	
 * This class stores the urls for the three sizes and allows 
 * access to each
 * 
 * @author Eli
 *
 */
public class CompanyLogo {
	private String mFull;
	private String mMedium;
	private String mThumb;
	
	/** Create a new LogoURL object by specifying the urls
	 * for the full, medium, and thumb sizes
	 * @param full
	 * @param medium
	 * @param thumb
	 */
	public CompanyLogo(String Full, String Medium, String Thumb) {
		mFull = Full;
		mMedium = Medium;
		mThumb = Thumb;
	}

	public String getFull() {
		return mFull;
	}

	public String getMedium() {
		return mMedium;
	}

	public String getThumb() {
		return mThumb;
	}

}