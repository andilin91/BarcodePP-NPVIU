package id.co.qualitas.barcodepp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zltd.decoder.DecoderManager;

import id.co.qualitas.barcodepp.R;
import id.co.qualitas.barcodepp.constants.Constants;

public class BaseActivity extends Activity implements DecoderManager.IDecoderStatusListener{
    private String statusString = "";
    private boolean bContinuousMode = false;
    private int dataLength = 0;

    public String resultBarcode;
    public EditText edt;

    protected Context context;
    protected View rootView;
    protected ProgressDialog progress;
    public String result = "";
    protected boolean connection = false;
    public String pwoNumber, employeeId = "";

    //Xuxin
    protected Utils mUtils;
    DecoderManager mDecoderMgr = null;
    protected boolean isOnResume = false;
    private static final String TAG = "BaseActivity";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    protected int scanCase = 0;

    public void init() {
        context = this.getApplicationContext();
        rootView = findViewById(android.R.id.content);
        progress = new ProgressDialog(this);
        progress.setMessage(Constants.STR_WAIT);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
    }


    public ProgressDialog getProgress() {
        return progress;
    }

    public void setProgress(ProgressDialog progress) {
        this.progress = progress;
    }

    public void setButton(Button btn, String text, int id) {
        btn.setBackgroundDrawable(getResources().getDrawable(id));
//    	btn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),id));
        btn.setText(text);

    }
    public void setImage(ImageView img, int id) {
        img.setImageDrawable(getResources().getDrawable(id));
//    	img.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), id));
    }

    //XuXin
    @Override
    public void onDecoderStatusChanage(int status) {

    }

    @Override
    public void onDecoderResultChanage(String result, String time) {
        onResult(result);
    }

    @Override
    public void onDecoderResultChanage(String result, Bundle paramBundle) {
        onResult(result);
    }

    private void onResult(String result) {
        // Decode is interruptted or timeout ...
        if (result == null || result.startsWith("Decode is")) {
            // no result or time out
            onScannerError(result);
        } else {
            // result is the barcode scanned
            onScannerSuccess(result);
        }
    }

    protected void onScannerSuccess(final String code) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(BaseActivity.this, code, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onScannerError(String msg) {

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mUtils.release();
        Log.d(TAG, "onPause this=" + this);
        isOnResume = false;
        mDecoderMgr.removeDecoderStatusListener(this);
        mDecoderMgr.stopDecode();
        //lightControlHandler.sendEmptyMessageDelayed(CLOSELIGHT, 1);
        mDecoderMgr.disconnectDecoderSRV();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Log.d(TAG, "onCreate ..");
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        preferences = getSharedPreferences("settings", MODE_PRIVATE);
        mDecoderMgr = DecoderManager.getInstance();
        mDecoderMgr.setDataTransferType(com.zltd.decoder.Constants.TRANSFER_BY_API);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mUtils = Utils.getInstance();
        mUtils.init(this);
        Log.d(TAG, "onResume this=" + this);
        isOnResume = true;
        scanCase = 0;
        int res = mDecoderMgr.connectDecoderSRV();
        if(res == com.zltd.decoder.Constants.RETURN_CAMERA_CONN_ERR){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setMessage(R.string.scan_message)
                    .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            closeSelf();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
        mDecoderMgr.addDecoderStatusListener(this);
        //lightControlHandler.removeMessages(CLOSELIGHT);
        //lightControlHandler.sendEmptyMessage(OPENLIGHT);
    }

    protected void closeSelf() {
        this.finish();
    }

    protected void setScanRingEnable(boolean enable){
        editor = preferences.edit();
        editor.putBoolean("scan_ring", enable);
        editor.commit();
    }

    protected boolean getScanRingEnable(){
        return preferences.getBoolean("scan_ring", true);
    }

}
