CWAC ColorMixer: Appealing To Your Users' Sense of Fashion
==========================================================
Sometimes, you want your users to pick a color. A simple
approach is to give the user a fixed roster of a handful
of colors -- easy, but limited. A fancy approach is to use
some form of color wheel, but these can be difficult to use
on a touchscreen and perhaps impossible without a touchscreen.

`ColorMixer` is a widget that provides a simple set of `SeekBars`
to let the user mix red, green, and blue to pick an arbitrary color.
It is not very big, so it is easy to fit on a form, and it is still
fairly finger-friendly.

It is also packaged as a dialog (`ColorMixerDialog`), a dialog-themed
activity (`ColorMixerActivity`), and a preference (`ColorPreference`).

This is distributed as an Android library project, following
the conventions of [the Android Parcel Project](http://andparcel.com).
You can download a ZIP file containing just the library project
(sans sample code) from the Downloads section of this GitHub
repository.

Usage
-----

### ColorMixer

`ColorMixer` is a simple widget. Given that you have the parcel
installed in your project, or have manually merged the source
and resources into your project, you can add the widget to a
layout like any other:

	<com.commonsware.cwac.colormixer.ColorMixer
		android:id="@+id/mixer"
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"
	/>

You can call `getColor()` and `setColor()` to manipulate the
color at runtime. You can also call `setOnColorChangedListener()`
to register a `ColorMixer.OnColorChangedListener` object, which
will be called with `onColorChanged()` when the color is altered
by the user.

### ColorMixerDialog

`ColorMixerDialog` is an `AlertDialog` subclass. Hence, to create
and show the dialog, all you need to do is create an instance
and `show()` it:

	new ColorMixerDialog(this, someColor, onDialogSet).show();

In the above code snippet, `this` is a `Context` (e.g., an `Activity`),
`someColor` is the color you want to start with, and `onDialogSet`
is a `ColorMixer.OnColorChangedListener` that will be notified
*if* the user clicks the "Set" button on the dialog *and* has
changed the color from the initial value.

### ColorMixerActivity

`ColorMixerActivity` is a dialog-themed `Activity`. This is
useful for situations where you want a dialog but do not want
to deal with a dialog.

To use it, add it as an activity to your project. You will
need to use the full package in your `<activity>` element,
marking it as using `Theme.Dialog`.
Here is one implementation, from the `demo/` project:

	<activity android:name="com.commonsware.cwac.colormixer.ColorMixerActivity"
					android:label="@string/app_name"
					android:theme="@android:style/Theme.Dialog">
	</activity>

In the `Intent` you use to start the activity, you can supply
the starting color via a `ColorMixerActivity.COLOR` integer
extra, and the dialog title via a `ColorMixerActivity.TITLE`
string extra. For example:

	Intent i=new Intent(this, ColorMixerActivity.class);
	
	i.putExtra(ColorMixerActivity.TITLE, "Pick a Color");
	i.putExtra(ColorMixerActivity.COLOR, mixer.getColor());
	
	startActivityForResult(i, COLOR_REQUEST);

### ColorPreference

`ColorPreference` is a `Preference` class, to be referenced
in preference XML and loaded into a `PreferenceActivity`. It
has no attributes beyond the standard ones.

	<PreferenceScreen
		xmlns:android="http://schemas.android.com/apk/res/android">
		<com.commonsware.cwac.colormixer.ColorPreference
			android:key="favoriteColor"
			android:defaultValue="0xFFA4C639"
			android:title="Your Favorite Color"
			android:summary="Blue.  No yel--  Auuuuuuuugh!" />
	</PreferenceScreen>

The preference is stored as an integer under the key you
specify in the XML.

Dependencies
------------
This depends upon the `cwac-parcel` library for accessing
project-level resources.

Version
-------
This is version v0.4.1 of this module, meaning it is creeping
towards respectability.

Demo
----
There is a `demo/` directory containing a demo project. It uses
the library project itself to access the source code and
resources of the `ColorMixer` library.

License
-------
The code in this project is licensed under the Apache
Software License 2.0, per the terms of the included LICENSE
file.

Questions
---------
If you have questions regarding the use of this code, please post a question
on [StackOverflow](http://stackoverflow.com/questions/ask) tagged with `commonsware` and `android`. Be sure to indicate
what CWAC module you are having issues with, and be sure to include source code 
and stack traces if you are encountering crashes.

Release Notes
-------------
v0.4.1: fixed `ColorPreference` to work better on Honeycomb
v0.4.0: converted to Android library project, added ColorMixerActivity

Who Made This?
--------------
<a href="http://commonsware.com">![CommonsWare](http://commonsware.com/images/logo.png)</a>

[gg]: http://groups.google.com/group/cw-android
