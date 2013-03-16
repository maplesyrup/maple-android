package com.example.maple_android.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.example.maple_android.LoginActivity;
import com.example.maple_android.MainActivity;
import com.example.maple_android.R;
import com.jayway.android.robotium.solo.Solo;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private TextView result;
	private Solo solo;
	
	public MainActivityTest() {
		super(MainActivity.class);
	}
	
	@Override
	public void setUp() {
		solo = new Solo(getInstrumentation(), getActivity());
		try {
			super.setUp();
			MainActivity mainActivity = getActivity();
			result = (TextView) mainActivity.findViewById(R.id.greeting);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testGreeting() {
		assertEquals(2+2, 4);
		assertNotNull(result);
	}
	
	public void testLogout() {
		solo.sendKey(Solo.MENU);
		solo.clickOnText("Logout");
		solo.assertCurrentActivity("Correct activity did not appear", LoginActivity.class);
	}
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}
