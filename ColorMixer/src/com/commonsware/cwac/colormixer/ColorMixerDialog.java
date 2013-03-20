/***
  Copyright (c) 2008-2010 CommonsWare, LLC
  
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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import com.commonsware.cwac.parcel.ParcelHelper;


public class ColorMixerDialog extends AlertDialog
  implements DialogInterface.OnClickListener {
  static private final String COLOR="c";
  private ColorMixer mixer=null;
  private int initialColor;
  private ColorMixer.OnColorChangedListener onSet=null;
  
  public ColorMixerDialog(Context ctxt,
                          int initialColor,
                          ColorMixer.OnColorChangedListener onSet) {
    super(ctxt);
    
    this.initialColor=initialColor;
    this.onSet=onSet;
    
    ParcelHelper parcel=new ParcelHelper("cwac-colormixer", ctxt);
    
    mixer=new ColorMixer(ctxt);
    mixer.setColor(initialColor);
    
    setView(mixer);
    setButton(ctxt.getText(parcel.getIdentifier("set", "string")),
              this);
    setButton2(ctxt.getText(parcel.getIdentifier("cancel", "string")),
               (DialogInterface.OnClickListener)null);
  }
  
  @Override
  public void onClick(DialogInterface dialog, int which) {
    if (initialColor!=mixer.getColor()) {
      onSet.onColorChange(mixer.getColor());
    }
  }
  
  @Override
  public Bundle onSaveInstanceState() {
    Bundle state=super.onSaveInstanceState();
    
    state.putInt(COLOR, mixer.getColor());
    
    return(state);
  }

  @Override
  public void onRestoreInstanceState(Bundle state) {
    super.onRestoreInstanceState(state);
      
    mixer.setColor(state.getInt(COLOR));
  }
}