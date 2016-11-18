package com.haddondroyd.spinglas; /**
 * Copyright (C) 2012 Wglxy.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * This is the Help activity for the application.
 * It displays some basic help information. Clicking on the icons takes the user to a detailed description.
 *
 */

public class AboutActivity extends Activity
{

static public final String ARG_TEXT_ID = "text_id";

/**
 * onCreate - called when the activity is first created.
 * Called when the activity is first created. 
 * This is where you should do all of your normal static set up: create views, bind data to lists, etc. 
 * This method also provides you with a Bundle containing the activity's previously frozen state, if there was one.
 * 
 * Always followed by onStart().
 *
 */

protected void onCreate(Bundle savedInstanceState) 
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.about);

    // Set up so that formatted text can be in the help_page_intro text and so that html links are handled.
    TextView textView = (TextView) findViewById (R.id.about_page_intro);
    if (textView != null) {
       textView.setMovementMethod (LinkMovementMethod.getInstance());
       textView.setText (Html.fromHtml (getString (R.string.about_page_intro_html)));
    }
    textView = (TextView) findViewById (R.id.about_text_section2);
    textView.setText(Html.fromHtml(getString(R.string.about_text_section2_html)));
}
    

/**
 */

} // end class
