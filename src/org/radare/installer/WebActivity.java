/*
radare2 installer for Android
(c) 2012 Pau Oliva Fora <pof[at]eslack[dot]org>
*/
package org.radare.installer;

import org.radare.installer.Utils;

import android.app.Activity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;

import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.content.DialogInterface;
import android.app.AlertDialog;
import android.widget.EditText;

import android.widget.Toast;
import java.io.File;

import com.stericson.RootTools.*;

public class WebActivity extends Activity {

	private Utils mUtils;

        WebView webview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mUtils = new Utils(getApplicationContext());

		File radarebin = new File("/data/data/org.radare.installer/radare2/bin/radare2");
		if (radarebin.exists()) {

			setContentView(R.layout.webactivity);

			RootTools.useRoot = false;

			if (RootTools.isProcessRunning("radare2")) {
				RootTools.killProcess("radare2");
			}

			Bundle b = getIntent().getExtras();
			String file_to_open = b.getString("filename", "default");

			CommandCapture command = new CommandCapture(0, "/data/data/org.radare.installer/radare2/bin/radare2 -c=h " + file_to_open + " &");
			try {
				RootTools.getShell(RootTools.useRoot).add(command).waitForFinish();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(1000);
			} catch (Exception e) {
                                e.printStackTrace();
                        }

			webview = (WebView) findViewById(R.id.webview);
			webview.setWebViewClient(new RadareWebViewClient());
			webview.getSettings().setJavaScriptEnabled(true);
			webview.loadUrl("http://localhost:9090");



		} else {
			mUtils.myToast("Please install radare2 first!", Toast.LENGTH_SHORT);
			finish();
		}
	}

	private class RadareWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			view.loadUrl(url);
			return true;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}




}
