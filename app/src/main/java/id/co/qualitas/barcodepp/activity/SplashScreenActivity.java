package id.co.qualitas.barcodepp.activity;

import java.util.Map;

import id.co.qualitas.barcodepp.R;
import id.co.qualitas.barcodepp.constants.Constants;
import id.co.qualitas.barcodepp.helper.Helper;
import id.co.qualitas.barcodepp.model.PWOEmployee;
import id.co.qualitas.barcodepp.model.PWOResponse;
import id.co.qualitas.barcodepp.session.SessionManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

import com.zltd.sdk.scanner.core.BuildConfig;

public class SplashScreenActivity extends Activity {
	private SessionManager session;
	private ProgressDialog progress;
	private ActionBar ab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		getActionBar().hide();
		session = new SessionManager(getApplicationContext());
		TextView txtVersion = findViewById(R.id.txtVersion);
		String versionName = BuildConfig.VERSION_NAME;
		txtVersion.setText("NPS PP Version " + versionName);
		
		if (session.isUrlEmpty()) {
			Map<String, String> urlSession = session.getUrl();
			Helper.setItemParam(Constants.URL,
					urlSession.get(Constants.KEY_URL));
		} else {
			Helper.setItemParam(Constants.URL, Constants.URL);
		}
		new CountDownTimer(Constants.LONG_1000, Constants.LONG_100) {

			public void onTick(long millisUntilFinished) {

			}

			public void onFinish() {
//				if (session.isDataIn()) {
//					Map<String, String> dataSession = session.getDataDetails();
//					String mData = dataSession.get(Constants.KEY_DATA);
//					PWOResponse savePWOResponse = (PWOResponse) Helper
//							.stringToObject(mData);
//					if (savePWOResponse.getFinishTime() == null) {
//						Intent intent = new Intent(getApplicationContext(),
//								ConfirmationActivity.class);
//						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
//								| Intent.FLAG_ACTIVITY_NEW_TASK);
//						startActivity(intent);
//					}else{
//						Intent intent = new Intent(getApplicationContext(),
//								ConfirmationOkActivity.class);
//						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
//								| Intent.FLAG_ACTIVITY_NEW_TASK);
//						startActivity(intent);
//					}
//
//				}else{
					Intent intent = new Intent(getApplicationContext(),
							HomeActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
//				}
			}
		}.start();
	}
	
}
