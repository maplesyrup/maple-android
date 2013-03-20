/***
  Copyright (c) 2008-2010 CommonsWare, LLC
  Portions Copyright (c) 2007 The Android Open Source Project
  
  Licensed under the Apache License, Version 2.0 (the "License"); you may
  not use this file except in compliance with the License. You may obtain
  a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package com.commonsware.cwac.colormixer;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.commonsware.cwac.colormixer.ColorMixer;
import com.commonsware.cwac.parcel.ParcelHelper;


public class ColorPreference extends DialogPreference {
  private int lastColor=0;
  private ColorMixer mixer=null;

  public ColorPreference(Context ctxt, AttributeSet attrs) {
    super(ctxt, attrs);
    
    ParcelHelper parcel=new ParcelHelper("cwac-colormixer", ctxt);
    
    setPositiveButtonText(ctxt.getText(parcel.getIdentifier("set", "string")));
    setNegativeButtonText(ctxt.getText(parcel.getIdentifier("cancel", "string")));
  }

  @Override
  protected View onCreateDialogView() {
    mixer=new ColorMixer(getContext());
    
    return(mixer);
  }
  
  @Override
  protected void onBindDialogView(View v) {
    super.onBindDialogView(v);
    
    mixer.setColor(lastColor);
  }
  
  @Override
  protected void onDialogClosed(boolean positiveResult) {
    super.onDialogClosed(positiveResult);

    if (positiveResult) {
      if (callChangeListener(mixer.getColor())) {
        lastColor=mixer.getColor();
        persistInt(lastColor);
      }
    }
  }

  @Override
  protected Object onGetDefaultValue(TypedArray a, int index) {
    return(a.getInt(index, 0xFFA4C639));
  }

  @Override
  protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
    lastColor=(restoreValue ? getPersistedInt(lastColor) : (Integer)defaultValue);
  }
}