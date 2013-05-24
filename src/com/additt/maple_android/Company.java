package com.additt.maple_android;

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
	private ArrayList<CompanyLogo> mLogos;
	private boolean mEditable;
	private ArrayList<Campaign> mCampaigns;
	
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
	 * @param campaigns 
	 */
	public Company(int id, String name, String splashImage, String blurbTitle, String blurbBody, String moreInfoTitle, 
			String moreInfoBody, String companyUrl, ArrayList<CompanyLogo> logos, boolean editable, ArrayList<Campaign> campaigns){
		
		mId = id;
		mName = name;
		mSplashImage = splashImage;
		mBlurbTitle = blurbTitle;
		mBlurbBody = blurbBody;
		mMoreInfoTitle = moreInfoTitle;
		mMoreInfoBody = moreInfoBody;
		mCompanyUrl = companyUrl;
		mLogos = logos;
		mEditable = editable;		
		setCampaigns(campaigns);
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

	public ArrayList<CompanyLogo> getLogos() {
		return mLogos;
	}

	public boolean isEditable() {
		return mEditable;
	}
	
		
	public String toString() {
		return "Company [mId=" + mId + ", mName=" + mName + ", mSplashImage="
				+ mSplashImage + ", mBlurbTitle=" + mBlurbTitle
				+ ", mBlurbBody=" + mBlurbBody + ", mMoreInfoTitle="
				+ mMoreInfoTitle + ", mMoreInfoBody=" + mMoreInfoBody
				+ ", mCompanyUrl=" + mCompanyUrl + ", mLogoUrls=" + mLogos
				+ ", mEditable=" + mEditable + "]";
	}

	public ArrayList<Campaign> getCampaigns() {
		return mCampaigns;
	}

	public void setCampaigns(ArrayList<Campaign> mCampaigns) {
		this.mCampaigns = mCampaigns;
	}


}
