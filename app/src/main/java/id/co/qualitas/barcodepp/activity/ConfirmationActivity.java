package id.co.qualitas.barcodepp.activity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import id.co.qualitas.barcodepp.R;
import id.co.qualitas.barcodepp.adapter.EmployeeConfirmationBaseAdapter;
import id.co.qualitas.barcodepp.adapter.EmployeeStartBaseAdapter;
import id.co.qualitas.barcodepp.constants.Constants;
import id.co.qualitas.barcodepp.helper.Helper;
import id.co.qualitas.barcodepp.model.EmployeeResponse;
import id.co.qualitas.barcodepp.model.ListEmployee;
import id.co.qualitas.barcodepp.model.PWOEmployee;
import id.co.qualitas.barcodepp.model.PWOResponse;
import id.co.qualitas.barcodepp.model.PWOResponse;
import id.co.qualitas.barcodepp.model.WSMessageEmployee;
import id.co.qualitas.barcodepp.model.WSMessagePWO;
import id.co.qualitas.barcodepp.service.TimerService;
import id.co.qualitas.barcodepp.session.SessionManager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class ConfirmationActivity extends BaseActivity {

    private ImageView imgPartial, imgFull;
    private ImageButton imgUndo;
    private boolean flag = true;
    private PWOResponse pwoResponse;
    private LinearLayout l1, l2, l3,l4;
    private Long timeElapsed, delayedTime = null;
    private final Handler timer = new Handler();
    private Button btnConfirm;
    private boolean flagQTY = false;
    private String duration;
    private TextView txtPWONumber, txtPWODesc, txtQty,txtResource;
    private SessionManager session;
    private int PARAM = 0;
    private EditText edtQTY,edtResource;
    private String mday,mmonth,myear = null;

    private ListView lv1;
    private EmployeeConfirmationBaseAdapter adapter = null;
    private ActionBar ab;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    //XuXin
    private int scanMode = 0;
    private final static String TAG = "[Z] ConfirmationActivity - ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        session = new SessionManager(getApplicationContext());
        init();
        initialize();
    }

    private void setDataScan(String scanData) {
        if (scanData != null && !scanData.equals("")) {
            if (!scanData.equals(Constants.BARCODE_ERROR)) {
                edtResource.setText(scanData);
            } else {
                Toast.makeText(getApplicationContext(),Constants.BARCODE_ERROR,Toast.LENGTH_SHORT).show();
            }
        } else {
            edtResource.setText("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUI();
        if (Helper.getItemParam(Constants.RESULT_BARCODE) != null) {
            result = Helper.getItemParam(Constants.RESULT_BARCODE).toString();
            Helper.removeItemParam(Constants.RESULT_BARCODE);
            setDataScan(result);
        }
    }

    private void initialize() {
        // TODO Auto-generated method stub
        ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        txtPWONumber = (TextView) findViewById(R.id.txtPWONumber);
        txtPWODesc = (TextView) findViewById(R.id.txtPWODesc);
        txtQty = (TextView) findViewById(R.id.txtQty);
        imgUndo = (ImageButton) findViewById(R.id.imgUndo);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        edtQTY = (EditText) findViewById(R.id.edtQty);
        txtResource = (TextView) findViewById(R.id.txtResource);
        edtResource = (EditText) findViewById(R.id.edtResource);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        session = new SessionManager(this);
        Map<String, String> dataSessionPE = session.getPEDetails();
        String mDataPE = dataSessionPE.get(Constants.KEY_PE);
        PWOEmployee pwoEmployee = (PWOEmployee) Helper.stringToObject(mDataPE);
        pwoResponse = pwoEmployee.getPwoResponse();
        pwoResponse.setPartial(true);
        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (LinearLayout) findViewById(R.id.l2);
        l4 = (LinearLayout) findViewById(R.id.l4);
        lv1 = (ListView) rootView.findViewById(R.id.listView);
        imgPartial = (ImageView) findViewById(R.id.imgPartial);
        Helper.setItemParam(Constants.PROD_TYPE,
                pwoResponse.getProdType());

        if (pwoResponse.getProdType().equals("FG")) {
            flagQTY = true;
            lv1.setVisibility(View.GONE);
//            setButton(btnConfirm, "CONFIRM", R.drawable.btn_blue);
            setButton(btnConfirm, "STOP", R.drawable.btn_pink);
            if (pwoResponse.getListEmployee() != null && pwoResponse.getListEmployee().size() != 0) {
                lv1.setVisibility(View.VISIBLE);
                adapter = new EmployeeConfirmationBaseAdapter(this,
                        R.layout.employee_row_view);
                adapter.addAllItem(pwoResponse.getListEmployee());
                lv1.setAdapter(adapter);
            }
        } else {
            flagQTY = false;
            lv1.setVisibility(View.GONE);
            edtQTY.setVisibility(View.GONE);
            txtQty.setVisibility(View.GONE);
//            txtResource.setVisibility(View.VISIBLE);
//            edtResource.setVisibility(View.VISIBLE);
//            l4.setVisibility(View.VISIBLE);
            setButton(btnConfirm, "STOP", R.drawable.btn_pink);
            if (pwoResponse.getListEmployee() != null && pwoResponse.getListEmployee().size() != 0) {
                lv1.setVisibility(View.VISIBLE);
                adapter = new EmployeeConfirmationBaseAdapter(this,
                        R.layout.employee_row_view);
                adapter.addAllItem(pwoResponse.getListEmployee());
                lv1.setAdapter(adapter);
            }

        }
//        if(pwoResponse.getOrderType().equals("ZHBS") || pwoResponse.getOrderType().equals("ZVBS")){
//            flagQTY = false;
//            edtQTY.setVisibility(View.GONE);
//            txtQty.setVisibility(View.GONE);
//        }
        txtPWONumber.setText(pwoResponse.getPwoNo());
        txtPWODesc.setText(pwoResponse.getProductId().concat(" - ")
                .concat(pwoResponse.getProductName()));
//		txtPWODesc.setText(pwoResponse.getProductName());
        imgFull = (ImageView) findViewById(R.id.imgFull);

        imgPartial.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
//                if (flag == false) {
                    flag = true;
                    if (pwoResponse.getProdType().equals("FG")) {
                        setButton(btnConfirm, "SAVE", R.drawable.btn_pink);
                    } else {
                        setButton(btnConfirm, "STOP", R.drawable.btn_pink);
                    }
                    setButton(btnConfirm, "STOP", R.drawable.btn_pink);
                    setImage(imgPartial, R.drawable.partial_enabled);
                    setImage(imgFull, R.drawable.full_disabled);
                    pwoResponse.setPartial(flag);
//                }
//				else {
//					flag = false;
//					if (pwoResponse.getProdType().equals("FG")) {
//						setButton(btnConfirm, "CONFIRM", R.drawable.btn_blue);
//					} else {
//						setButton(btnConfirm, "STOP", R.drawable.btn_pink);
//					}
//					setImage(imgPartial, R.drawable.partial_disabled);
//					setImage(imgFull, R.drawable.full_enabled);
//					pwoResponse.setPartial(flag);
//				}
            }
        });
        imgFull.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
//                if (flag == true) {
                    flag = false;
                    if (pwoResponse.getProdType().equals("FG")) {
//                        setButton(btnConfirm, "CONFIRM", R.drawable.btn_blue);
                        setButton(btnConfirm, "STOP", R.drawable.btn_pink);
                    } else {
                        setButton(btnConfirm, "STOP", R.drawable.btn_pink);
                    }
                    setImage(imgPartial, R.drawable.partial_disabled);
                    setImage(imgFull, R.drawable.full_enabled);
                    pwoResponse.setPartial(flag);
//                }
//				if (flag == false) {
//					flag = true;
//					if (pwoResponse.getProdType().equals("FG")) {
//						setButton(btnConfirm, "SAVE", R.drawable.btn_pink);
//					} else {
//						setButton(btnConfirm, "STOP", R.drawable.btn_pink);
//					}
//					setImage(imgPartial, R.drawable.partial_enabled);
//					setImage(imgFull, R.drawable.full_disabled);
//					pwoResponse.setPartial(flag);
//				} else {
//					flag = false;
//					if (pwoResponse.getProdType().equals("FG")) {
//						setButton(btnConfirm, "CONFIRM", R.drawable.btn_blue);
//					} else {
//						setButton(btnConfirm, "STOP", R.drawable.btn_pink);
//					}
//					setImage(imgPartial, R.drawable.partial_disabled);
//					setImage(imgFull, R.drawable.full_enabled);
//					pwoResponse.setPartial(flag);
//				}
            }
        });


        btnConfirm.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (flagQTY) {
                    if (edtQTY.getText().toString().trim().equals("")) {
                        Toast.makeText(getApplicationContext(),
                                "Quantity cannot empty", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        if (flag) {
                            dialogStop();
                        } else {
                            dialogOk();
                        }
                    }
                } else {
                    dialogStop();
                }
            }
        });
        imgUndo.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialogResetBarcode();
            }
        });
        edtQTY.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (!s.toString().trim().equals("")) {
                    pwoResponse.setYield(Double.parseDouble(s.toString()));
                }
            }
        });
        edtQTY.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboardFrom(getApplicationContext(), textView);
                    return true;
                }
                return false;
            }
        });
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void dialogResetBarcode() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Cancel this process will lose your all data, are you sure?");
        builder1.setCancelable(true);

        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(),
                                HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        session.clearPE();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void dialogStop() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure to stop this process?");
        builder1.setCancelable(true);

        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PARAM = 0;
                        new RequestUrl().execute();
                        progress.show();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void dialogOk() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure to finish this process?");
        builder1.setCancelable(true);

        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PARAM = 0;
                        new RequestUrl().execute();
                        progress.show();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private class RequestUrl extends AsyncTask<Void, Void, PWOResponse> {
        @SuppressLint("WrongThread")
        @Override
        protected PWOResponse doInBackground(Void... voids) {
            try {
                String url;
                if (PARAM == 0) {
                    pwoResponse.setResource(edtResource.getText().toString());
                    if (flag == false) {
                        pwoResponse.setPartial(flag);
                    }
                    url = Helper.getItemParam(Constants.URL).toString()
                            .concat("stop");
                    if(pwoResponse.getManuDate()==null){
                        pwoResponse.setManuDate(myear.concat("-").concat(mmonth).concat("-").concat(mday));
                    }
//					pwoResponse.setTimeStart(pwoResponse.getStartTime());
                    return (PWOResponse) Helper.postWebserviceWithBody(url,
                            PWOResponse.class, pwoResponse);
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
                        setButton(btnConfirm, "STOPPED", R.drawable.btn_grey);
                        edtQTY.setEnabled(false);
                        edtResource.setEnabled(false);
                        imgPartial.setEnabled(false);
                        setImage(imgPartial, R.drawable.partial_disabled);
                        imgFull.setEnabled(false);
                        setImage(imgFull, R.drawable.full_disabled);
                        btnConfirm.setEnabled(false);
                        // Intent intent = new Intent(getApplicationContext(),
                        // ConfirmationOkActivity.class);
                        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        // | Intent.FLAG_ACTIVITY_NEW_TASK);
                        // startActivity(intent);
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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Helper.removeItemParam(Constants.PWO_EMPLOYEE);
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return false;
    }


    //XuXin
    private void initUI() {
//        mContinousScanButton.setChecked(false);
//        mSingleScanButton.setEnabled(true);
        switch (mDecoderMgr.getScanMode()) {
            case com.zltd.decoder.Constants.SINGLE_SHOOT_MODE:
//                mSingleScanButton.setEnabled(true);
//                mContinousScanButton.setEnabled(false);
//                //autoTestButton.setEnabled(true);
//                mScanTotalTextView.setVisibility(View.GONE);
                scanMode = com.zltd.decoder.Constants.SINGLE_SHOOT_MODE;
                break;
            case com.zltd.decoder.Constants.CONTINUOUS_SHOOT_MODE:
//                mSingleScanButton.setEnabled(false);
//                mContinousScanButton.setEnabled(true);
//                //autoTestButton.setEnabled(false);
//                mScanTotalTextView.setVisibility(View.VISIBLE);
//                scanMode = com.zltd.decoder.Constants.CONTINUOUS_SHOOT_MODE;
                break;
            case com.zltd.decoder.Constants.HOLD_SHOOT_MODE:
//                mSingleScanButton.setEnabled(false);
//                mContinousScanButton.setEnabled(false);
//                //autoTestButton.setEnabled(false);
//                mScanTotalTextView.setVisibility(View.GONE);
//                scanMode = com.zltd.decoder.Constants.HOLD_SHOOT_MODE;
                break;

            default:
                break;
        }
    }

    @SuppressLint("LongLogTag")
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (mDecoderMgr != null) {
            mDecoderMgr.enableLight(com.zltd.decoder.Constants.FLASH_LIGHT, false);
            mDecoderMgr.enableLight(com.zltd.decoder.Constants.FLOOD_LIGHT, false);
            mDecoderMgr.enableLight(com.zltd.decoder.Constants.LOCATION_LIGHT, false);
        }
    }

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    String decodeResult;
                    String decodeTime;
                    HashMap<String, String> result = (HashMap<String, String>) msg.obj;
                    String[] strs = result.values().toArray(new String[0]);
                    setDataScan(strs[0]);
                    break;
                case 1:
                    Toast.makeText(ConfirmationActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
//                    enableSaveFile = true;
                    break;
                case 2:
                    Toast.makeText(ConfirmationActivity.this, R.string.save_fail, Toast.LENGTH_SHORT).show();
//                    enableSaveFile = true;
                    break;
                default:
                    break;
            }
        }
    };

    @SuppressLint("LongLogTag")
    @Override
    public void onDecoderResultChanage(String result, String time) {
        super.onDecoderResultChanage(result, time);
        if(isOnResume){
            Log.d(TAG, "onDecoderResultChanage decodeTime=" + time
                    + " decodeResult " + result);
            HashMap<String, String> hResult = new HashMap<String, String>();
            hResult.put("decodeTime", time);
            hResult.put("decodeResult", result);
            switch (scanMode) {
                case com.zltd.decoder.Constants.SINGLE_SHOOT_MODE:
                    mHandler.obtainMessage(0, hResult).sendToTarget();
                    break;
                case com.zltd.decoder.Constants.CONTINUOUS_SHOOT_MODE:
//                    if(scanCase == STARTCONTINUESHOOT){
//                        ScanTotalNum++;
//                        mHandler.obtainMessage(0, hResult).sendToTarget();
//                    }
                    break;
                case com.zltd.decoder.Constants.HOLD_SHOOT_MODE:
//                    if(!isScanTimeOut())
//                    {
//                        mHandler.obtainMessage(0, hResult).sendToTarget();
//                    }
                    break;
                default:
                    break;
            }
        }
    }


}