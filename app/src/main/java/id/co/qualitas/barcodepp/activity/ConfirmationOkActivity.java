package id.co.qualitas.barcodepp.activity;

import java.util.Map;

import id.co.qualitas.barcodepp.R;
import id.co.qualitas.barcodepp.constants.Constants;
import id.co.qualitas.barcodepp.helper.Helper;
import id.co.qualitas.barcodepp.model.EmployeeResponse;
import id.co.qualitas.barcodepp.model.PWOResponse;
import id.co.qualitas.barcodepp.model.PWOResponse;
import id.co.qualitas.barcodepp.model.WSMessageEmployee;
import id.co.qualitas.barcodepp.session.SessionManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmationOkActivity extends BaseActivity {

	private ImageView imgPartial, imgFull;
	private boolean flag = false;
	private EmployeeResponse employeeResponse;
	private PWOResponse pwoResponse;
	private SessionManager session;
	private TextView txtFullPartial, txtTime;
	private ImageView imgFullPartial;
	private int PARAM = 0;
	private PWOResponse savePWOResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmation_ok);
		init();
		initialize();

	}

	private class RequestUrl extends AsyncTask<Void, Void, PWOResponse> {
		@Override
		protected PWOResponse doInBackground(Void... voids) {
			try {
				String url;
				if (PARAM == 0) {
					url = Helper.getItemParam(Constants.URL).toString()
							.concat("save");
					return (PWOResponse) Helper.postWebserviceWithBody(url,
							PWOResponse.class, savePWOResponse);
				} else {
					return null;
				}
			} catch (Exception ex) {
				connection = true;
				return null;
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(PWOResponse savePWOResponse) {
			progress.dismiss();
			if (PARAM == 0) {
				if (savePWOResponse != null) {
					if (savePWOResponse.getMessageId() == 0) {
						new SessionManager(getApplicationContext())
								.createDataSession(Helper
										.objectToString(savePWOResponse));
						Intent intent = new Intent(getApplicationContext(),
								ConfirmationOkActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					} else {
						Toast.makeText(getApplicationContext(),
								savePWOResponse.getMessage(), Toast.LENGTH_LONG)
								.show();
					}
				} else {
					Toast.makeText(getApplicationContext(),
							Constants.INTERNET_CONNECTION, Toast.LENGTH_LONG)
							.show();
				}
			} else {

			}
		}
	}

	private void initialize() {
		// TODO Auto-generated method stub
		imgFullPartial = (ImageView) findViewById(R.id.imgFullPartial);
		txtFullPartial = (TextView) findViewById(R.id.txtFullPartial);
		txtTime = (TextView) findViewById(R.id.txtTIme);
		session = new SessionManager(this);
		Map<String, String> dataSession = session.getDataDetails();
		String mData = dataSession.get(Constants.KEY_DATA);
		savePWOResponse = (PWOResponse) Helper.stringToObject(mData);
		if (savePWOResponse.isPartial()) {
			imgFullPartial.setImageDrawable(ContextCompat.getDrawable(
					getApplicationContext(), R.drawable.icon_partial));
			txtFullPartial.setText("Partial");
		}else{
			imgFullPartial.setImageDrawable(ContextCompat.getDrawable(
					getApplicationContext(), R.drawable.icon_full));
			txtFullPartial.setText("Full");
		}
		txtTime.setText(savePWOResponse.getDuration());
	}
}