package id.co.qualitas.barcodepp.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.co.qualitas.barcodepp.R;
import id.co.qualitas.barcodepp.adapter.EmployeeStartBaseAdapter;
import id.co.qualitas.barcodepp.constants.Constants;
import id.co.qualitas.barcodepp.helper.Helper;
import id.co.qualitas.barcodepp.model.EmployeeResponse;
import id.co.qualitas.barcodepp.model.ListEmployee;
import id.co.qualitas.barcodepp.model.PWOEmployee;
import id.co.qualitas.barcodepp.model.PWOResponse;
import id.co.qualitas.barcodepp.model.WSMessageEmployee;
import id.co.qualitas.barcodepp.session.SessionManager;

public class HomeActivity extends BaseActivity {
    //implements IOnScannerEvent

    private String statusString = "";
    private boolean bContinuousMode = false;
    private int dataLength = 0;
    private TextView txtPWONumber, txtPWODesc;
    private TextView txtEmployeeID, txtScanPWONumber;
    private Spinner spinnerOperation;
    private Button btnStart;
    private int PARAM = 0;
    private ImageButton imgReset;
    private String pwoNumber, employeeId, resource = "";
    private boolean flagPwo, flagEmployee = false;
    private PWOResponse pwoResponse;
    private EmployeeResponse employeeResponse;
    private PWOEmployee pwoEmployee;
    private WSMessageEmployee wsMessageEmployee;
    private SessionManager session;
    private ActionBar ab;

    private ListView lv1;
    private EmployeeStartBaseAdapter adapter = null;
    private int FLAG_BTN_START = 0;
    private EditText edt;
    private RelativeLayout r1,r2;
    private int flag = 0;

    //TODO: test26jul2019

    private Context mContext;
    private SharedPreferences sp;

    //XuXin
    private int scanMode = 0;
    private EditText edtTxt;
    private final static String TAG = "[Z] HomeActivity - ";

    private void getPwoNo() {
        if (!Helper.getRequestUrl()) {
            PARAM = 0;
            new RequestUrl().execute();
            flag = 0;
        }
    }

    public void getEmployeeID() {
        if (!Helper.getRequestUrl()) {
            PARAM = 1;
            new RequestUrl().execute();
            flag = 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        ab = getActionBar();
        initialize();
//        activateBarcode(this);
    }

    private void initialize() {
        lv1 = rootView.findViewById(R.id.listView);
        session = new SessionManager(getApplicationContext());
        imgReset = findViewById(R.id.imgReset);
        txtPWONumber = findViewById(R.id.txtPWONumber);
        txtPWODesc = findViewById(R.id.txtPWODesc);
        txtEmployeeID = findViewById(R.id.txtEmployeeID);
        txtScanPWONumber = findViewById(R.id.txtScanPWONumber);
        btnStart = (Button) findViewById(R.id.btnStart);
        spinnerOperation = (Spinner) findViewById(R.id.spinnerOperation);
        spinnerOperation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    flagEmployee = false;
                    lv1.setVisibility(View.GONE);
                }else{
                    flagEmployee = true;
                    pwoEmployee.getPwoResponse().setOperationNumber(pwoResponse.getListOperation().get(position-1).getOperation());
                    pwoResponse.setConfNoSap(pwoResponse.getListOperation().get(position-1).getConfNo());
                    if(pwoResponse.getListOperation().get(position-1).getStatus().equals("Started")){
                        PWOEmployee pwoEmployee = new PWOEmployee();
                        pwoResponse.setListEmployee(null);
                        pwoEmployee.setPwoResponse(pwoResponse);
                        Intent intent = new Intent(getApplicationContext(), ConfirmationActivity.class);
                        Helper.setRequestUrl(true);
                        startActivity(intent);
                    }else if(pwoResponse.getListOperation().get(position-1).getStatus().equals("Partial Confirmed")){
                        setListView(pwoEmployee.getPwoResponse().getOperationNumber());
                    }else if(pwoResponse.getListOperation().get(position-1).getStatus().equals("Fully Confirmed")){
                        flagEmployee = false;
                        Toast.makeText(getApplicationContext(), "This Operation Already Fully Confirmed, please try another Operation", Toast.LENGTH_SHORT).show();
                    }else{
                        if(pwoResponse.getListOperation().get(position-1).getStatus().equals("Partial Started")) {
                            setListView(pwoEmployee.getPwoResponse().getOperationNumber());
                        }else{
                            lv1.setVisibility(View.GONE);
                        }

                    }

                    new SessionManager(getApplicationContext()).createPESession(Helper.objectToString(pwoEmployee));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setSpinnerData();
        r1 = (RelativeLayout) findViewById(R.id.r1);
        r2 = (RelativeLayout) findViewById(R.id.r2);
        // btnStart.setEnabled(true);
        // setButton(btnStart, "START", R.drawable.btn_green);
        btnStart.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (FLAG_BTN_START == 0) {
                    if (!Helper.getRequestUrl()) {
                        PARAM = 2;
                        new RequestUrl().execute();
                        progress.show();
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), ConfirmationActivity.class);
                    Helper.setRequestUrl(true);
                    startActivity(intent);
                }
            }
        });
        imgReset.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                resetBarcode();
            }
        });

        r2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 1;
                showDialogEmployeeID();
            }
        });

        r1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 2;
                showDialogPwoNumber();
            }
        });
    }

    public void setListView(String operation){
        List<ListEmployee> dataEmployee = new ArrayList<>();
        for(int i=0;i<pwoResponse.getListEmployee().size();i++){
            if(pwoResponse.getListEmployee().get(i).getOpNo().equals(operation)){
                dataEmployee.add(pwoResponse.getListEmployee().get(i));
            }
        }
        lv1 = rootView.findViewById(R.id.listView);
        adapter = new EmployeeStartBaseAdapter(this, R.layout.employee_row_view);
        adapter.addAllItem(dataEmployee);
        lv1.setAdapter(adapter);
        lv1.setVisibility(View.VISIBLE);
    }

    public void setSpinnerData() {
        ArrayList<String> spinnerArray = new ArrayList<>(20);
        if(flagPwo == true) {
            spinnerArray.add(0, "Choose Operation Number");
            for(int i=0;i<pwoResponse.getListOperation().size();i++){
                spinnerArray.add(i+1, pwoResponse.getListOperation().get(i).getOperation() + " - " + pwoResponse.getListOperation().get(i).getStatus());
            }
            spinnerOperation.setEnabled(true);
        }else{
            spinnerArray.add(0, "please scan PWO Number first");
            spinnerOperation.setEnabled(false);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOperation.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                HomeActivity.this);
        alertDialog.setTitle("Close Application");
        alertDialog.setMessage("Are you sure want to close the application?");
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        deleteAppData();
                    }
                });
        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
        alertDialog.show();
    }

    public void resetBarcode() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are sure want to clear your data?");
        builder1.setCancelable(true);

        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clearData();
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

    private void deleteAppData() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }

    public void clearData() {
        txtScanPWONumber.setVisibility(View.VISIBLE);
        txtPWONumber.setVisibility(View.GONE);
        txtPWODesc.setVisibility(View.GONE);
        lv1.setVisibility(View.GONE);
        session.clearPE();
        txtEmployeeID.setText("Please scan Employee ID to Continue");
        Helper.setItemParam(Constants.EMPLOYEE, employeeResponse);
        Helper.removeItemParam(Constants.PWO_EMPLOYEE);
        setButton(btnStart, "START", R.drawable.btn_grey);
        btnStart.setEnabled(false);
        pwoResponse = null;
        flagPwo = false;
        flagEmployee = false;
        setSpinnerData();
    }

    public void pwoSetting() {
//        // TODO Auto-generated method stub
        Boolean flag = true;
        for(int i=0;i<pwoResponse.getListOperation().size();i++){
            if(pwoResponse.getListOperation().get(i).getStatus().equals("Fully Confirmed")){
                flag = false;
            }else{
                flag = true;
            }
        }
        if(flag){
            txtEmployeeID.setText("Please scan Employee ID to Continue");
            flagEmployee = false;
            pwoResponse = pwoEmployee.getPwoResponse();
            txtScanPWONumber.setVisibility(View.GONE);
            txtPWONumber.setVisibility(View.VISIBLE);
            txtPWODesc.setVisibility(View.VISIBLE);
            txtPWONumber.setText(pwoResponse.getPwoNo());
            txtPWODesc.setText(pwoResponse.getProductId().concat(" - ").concat(pwoResponse.getProductName()));
            Helper.setItemParam(Constants.PWO, pwoResponse);
            Helper.setItemParam(Constants.PROD_TYPE, pwoResponse.getProdType());
            setSpinnerData();
        }else{
            Toast.makeText(
                    getApplicationContext(),
                    "This PWO Number already fully confirmed",
                    Toast.LENGTH_LONG).show();
        }

//        pwoResponse = pwoEmployee.getPwoResponse();
//        if (pwoResponse.getProdType().equals("FG")) {
//            if (pwoResponse.getStep() == 1 && pwoResponse.getListEmployee().size() == 0) {
//                PWOEmployee pwoEmployee = new PWOEmployee();
//                pwoEmployee.setPwoResponse(pwoResponse);
//                new SessionManager(getApplicationContext()).createPESession(Helper.objectToString(pwoEmployee));
//                Intent intent = new Intent(getApplicationContext(), ConfirmationActivity.class);
//                Helper.setRequestUrl(true);
//                startActivity(intent);
//            } else if (pwoResponse.getStep() == 1 && pwoResponse.getListEmployee().size() != 0) {
//                lv1.setVisibility(View.VISIBLE);
//                txtScanPWONumber.setVisibility(View.GONE);
//                txtPWONumber.setVisibility(View.VISIBLE);
//                txtPWODesc.setVisibility(View.VISIBLE);
//                txtResource.setVisibility(View.VISIBLE);
//                txtPWONumber.setText(pwoResponse.getPwoNo());
//                txtPWODesc.setText(pwoResponse.getProductId().concat(" - ").concat(pwoResponse.getProductName()));
//                txtResource.setText(pwoResponse.getOperationNumber());
//                if (employeeResponse != null) {
//                    pwoResponse.setIdEmployee(employeeResponse.getEmployeeId());
//                }
//                Helper.setItemParam(Constants.PWO, pwoResponse);
//                Helper.setItemParam(Constants.PROD_TYPE, pwoResponse.getProdType());
//                lv1 = rootView.findViewById(R.id.listView);
//                adapter = new EmployeeStartBaseAdapter(this, R.layout.employee_row_view);
//                adapter.addAllItem(pwoResponse.getListEmployee());
//                lv1.setAdapter(adapter);
//            } else if (pwoResponse.getStep() == 3) {
//                clearData();
//                Toast.makeText(
//                        getApplicationContext(),
//                        "PWO Number with this operation number already fully confirm",
//                        Toast.LENGTH_LONG).show();
//            } else {
//                txtScanPWONumber.setVisibility(View.GONE);
//                txtPWONumber.setVisibility(View.VISIBLE);
//                txtPWODesc.setVisibility(View.VISIBLE);
//                txtPWONumber.setText(pwoResponse.getPwoNo());
//                txtPWODesc.setText(pwoResponse.getProductId().concat(" - ")
//                        .concat(pwoResponse.getProductName()));
//                if (pwoEmployee.getEmployeeResponse() != null) {
//                    txtEmployeeID.setText(pwoEmployee.getEmployeeResponse().getEmployeeId().substring(4)
//                            .concat(" - ").concat(pwoEmployee.getEmployeeResponse().getEmployeeName()));
//                    flagEmployee = true;
//                }
//                // txtPWODesc.setText(pwoResponse.getProductName());
//                Helper.setItemParam(Constants.PWO, pwoResponse);
//            }
//        } else {
//            if (pwoResponse.getStep() == 1) {
//                PWOEmployee pwoEmployee = new PWOEmployee();
//                pwoEmployee.setPwoResponse(pwoResponse);
//                new SessionManager(getApplicationContext())
//                        .createPESession(Helper.objectToString(pwoEmployee));
////                releaseEMDK();
//                Intent intent = new Intent(getApplicationContext(),
//                        ConfirmationActivity.class);
//                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
//                // | Intent.FLAG_ACTIVITY_NEW_TASK);
//                Helper.setRequestUrl(true);
//                startActivity(intent);
//            } else if (pwoResponse.getStep() == 2) {
//                lv1.setVisibility(View.VISIBLE);
//                txtScanPWONumber.setVisibility(View.GONE);
//                txtPWONumber.setVisibility(View.VISIBLE);
//                txtPWODesc.setVisibility(View.VISIBLE);
//                txtPWONumber.setText(pwoResponse.getPwoNo());
//                txtPWODesc.setText(pwoResponse.getProductId().concat(" - ")
//                        .concat(pwoResponse.getProductName()));
//                // txtPWODesc.setText(pwoResponse.getProductName());
//                Helper.setItemParam(Constants.PWO, pwoResponse);
//                Helper.setItemParam(Constants.PROD_TYPE,
//                        pwoResponse.getProdType());
//                lv1 = (ListView) rootView.findViewById(R.id.listView);
//                adapter = new EmployeeStartBaseAdapter(this,
//                        R.layout.employee_row_view);
//                adapter.addAllItem(pwoResponse.getListEmployee());
//                lv1.setAdapter(adapter);
//            } else if (pwoResponse.getStep() == 3) {
//                clearData();
//                Toast.makeText(
//                        getApplicationContext(),
//                        "PWO Number Already Confirm, please try another PWO Number",
//                        Toast.LENGTH_LONG).show();
//            } else {
//                txtScanPWONumber.setVisibility(View.GONE);
//                txtPWONumber.setVisibility(View.VISIBLE);
//                txtPWODesc.setVisibility(View.VISIBLE);
//                txtPWONumber.setText(pwoResponse.getPwoNo());
//                txtPWODesc.setText(pwoResponse.getProductId().concat(" - ")
//                        .concat(pwoResponse.getProductName()));
//                if (pwoEmployee.getEmployeeResponse() != null) {
//                    txtEmployeeID.setText(pwoEmployee.getEmployeeResponse().getEmployeeId().substring(4)
//                            .concat(" - ").concat(pwoEmployee.getEmployeeResponse().getEmployeeName()));
//                    flagEmployee = true;
//                }
//                // txtPWODesc.setText(pwoResponse.getProductName());
//                Helper.setItemParam(Constants.PWO, pwoResponse);
//            }
//        }
//
//        if (flagEmployee == true) {
//            if (pwoResponse.getStep() != 3) {
//                PWOEmployee pwoEmployee = new PWOEmployee();
//                pwoEmployee.setEmployeeResponse(employeeResponse);
//                pwoEmployee.setPwoResponse(pwoResponse);
//                new SessionManager(getApplicationContext())
//                        .createPESession(Helper.objectToString(pwoEmployee));
//                if (pwoResponse.getProdType().equals("FG")) {
//                    if (pwoResponse.getStep() == 2) {
//                        FLAG_BTN_START = 1;
//                        setButton(btnStart, "NEXT", R.drawable.btn_green);
//                    } else {
//                        FLAG_BTN_START = 0;
//                        setButton(btnStart, "START", R.drawable.btn_green);
//                    }
//                } else {
//                    setButton(btnStart, "START", R.drawable.btn_green);
//                }
//                btnStart.setEnabled(true);
//            }
//        }

    }

    public void employeeSetting() {
        // TODO Auto-generated method stub
        employeeResponse = pwoEmployee.getEmployeeResponse();
        txtEmployeeID.setText(employeeResponse.getEmployeeId().substring(4)
                    .concat(" - ").concat(employeeResponse.getEmployeeName()));
        if (pwoResponse.getProdType().equals("FG")) {
            if (pwoResponse.getListOperation().get(spinnerOperation.getSelectedItemPosition()-1).getStatus().equals("Partial Started")) {
                FLAG_BTN_START = 1;
                setButton(btnStart, "NEXT", R.drawable.btn_green);
            } else {
                FLAG_BTN_START = 0;
                setButton(btnStart, "START", R.drawable.btn_green);
            }
        } else {
            setButton(btnStart, "START", R.drawable.btn_green);
        }
        btnStart.setEnabled(true);
//        if (pwoEmployee.getPwoResponse() != null) {
//            pwoResponse = pwoEmployee.getPwoResponse();
//            txtScanPWONumber.setVisibility(View.GONE);
//            txtPWONumber.setVisibility(View.VISIBLE);
//            txtPWODesc.setVisibility(View.VISIBLE);
//            txtPWONumber.setText(pwoEmployee.getPwoResponse().getPwoNo());
//            txtPWODesc.setText(pwoEmployee.getPwoResponse().getProductId().concat(" - ")
//                    .concat(pwoEmployee.getPwoResponse().getProductName()));
//            flagPwo = true;
//        }
//        if (pwoResponse != null) {
//            if (pwoResponse.getStep() == 1
//                    && pwoResponse.getListEmployee().size() != 0) {
//                txtEmployeeID.setText(employeeResponse.getEmployeeId()
//                        .substring(4).concat(" - ")
//                        .concat(employeeResponse.getEmployeeName()));
//                if (employeeResponse != null) {
//                    pwoResponse.setIdEmployee(employeeResponse.getEmployeeId());
//                }
//                Helper.setItemParam(Constants.EMPLOYEE, employeeResponse);
//                PWOEmployee pwoEmployee = new PWOEmployee();
//                pwoEmployee.setPwoResponse(pwoResponse);
//                new SessionManager(getApplicationContext())
//                        .createPESession(Helper.objectToString(pwoEmployee));
//                if (pwoResponse.getProdType().equals("FG")) {
//                    setButton(btnStart, "NEXT", R.drawable.btn_green);
//                    FLAG_BTN_START = 1;
//                } else {
//                    setButton(btnStart, "START", R.drawable.btn_green);
//                    FLAG_BTN_START = 0;
//                }
//                btnStart.setEnabled(true);
//
//            } else {
//                FLAG_BTN_START = 0;
//                txtEmployeeID.setText(employeeResponse.getEmployeeId()
//                        .substring(4).concat(" - ")
//                        .concat(employeeResponse.getEmployeeName()));
//                Helper.setItemParam(Constants.EMPLOYEE, employeeResponse);
//                if (flagPwo == true) {
//                    PWOEmployee pwoEmployee = new PWOEmployee();
//                    pwoEmployee.setEmployeeResponse(employeeResponse);
//                    pwoEmployee.setPwoResponse(pwoResponse);
//                    new SessionManager(getApplicationContext())
//                            .createPESession(Helper.objectToString(pwoEmployee));
//                    setButton(btnStart, "START", R.drawable.btn_green);
//                    btnStart.setEnabled(true);
//                }
//            }
//        } else {
//            FLAG_BTN_START = 1;
//            txtEmployeeID.setText(employeeResponse.getEmployeeId().substring(4)
//                    .concat(" - ").concat(employeeResponse.getEmployeeName()));
//            Helper.setItemParam(Constants.EMPLOYEE, employeeResponse);
//            if (flagPwo == true) {
//                PWOEmployee pwoEmployee = new PWOEmployee();
//                pwoEmployee.setEmployeeResponse(employeeResponse);
//                pwoEmployee.setPwoResponse(pwoResponse);
//                new SessionManager(getApplicationContext())
//                        .createPESession(Helper.objectToString(pwoEmployee));
//                setButton(btnStart, "START", R.drawable.btn_green);
//                btnStart.setEnabled(true);
//            }
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ab.setDisplayHomeAsUpEnabled(false);
                btnStart.setEnabled(false);
                setButton(btnStart, "START", R.drawable.btn_grey);
                imgReset.setVisibility(View.VISIBLE);
        }
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    HomeActivity.this);
            alertDialog.setTitle("Change Server");
            final EditText input = new EditText(HomeActivity.this);
            input.setText(Helper.getItemParam(Constants.URL).toString());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            input.setLayoutParams(lp);
            alertDialog.setView(input);
            alertDialog.setPositiveButton("Save",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new SessionManager(getApplicationContext())
                                    .createUrlSession(input.getText()
                                            .toString());
                            Helper.setItemParam(Constants.URL, input.getText()
                                    .toString());
                            Toast.makeText(getApplicationContext(),
                                    "Server Successfully Changed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog.show();
            return true;
        }
        if (id == R.id.scan_barcode) {
            Helper.setItemParam(Constants.SCAN, "1");
            Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }//1.142

    private class RequestUrl extends AsyncTask<Void, Void, PWOResponse> {
        @Override
        protected PWOResponse doInBackground(Void... voids) {
            try {
                String url;
                if (PARAM == 0) {
                    // getlist claim
                    url = Helper.getItemParam(Constants.URL).toString()
                            .concat("getPwo?pwoNo=").concat(pwoNumber);
                    return (PWOResponse) Helper.getWebservice(url, PWOResponse.class);
                } else if (PARAM == 1) {
                    url = Helper.getItemParam(Constants.URL).toString()
                            .concat("getEmployee?employeeId=")
                            .concat(employeeId);
                    wsMessageEmployee = (WSMessageEmployee) Helper.getWebservice(url, WSMessageEmployee.class);
                    return null;
                } else {
                    url = Helper.getItemParam(Constants.URL).toString()
                            .concat("start");
                    // PWOResponse savePWOResponseTemp = new PWOResponse();
                    // pwoResponse.setPwoNo(pwoResponse.getPwoNo())

                    pwoResponse.setIdEmployee(employeeResponse.getEmployeeId());
                    if (pwoResponse.getProdType().equals("SFG")) {
                        pwoResponse.setFinishDate(null);
                        pwoResponse.setFinishTime(null);
                    }
                    return (PWOResponse) Helper.postWebserviceWithBody(url, PWOResponse.class, pwoResponse);
                }
            } catch (Exception ex) {
                connection = true;
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            progress.show();
            super.onPreExecute();
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onPostExecute(PWOResponse savePWOResponse) {
            if (PARAM == 0) {
                if (savePWOResponse != null) {
                    if (savePWOResponse.getMessageId() == 0) {
//                        Gson gson = new GsonBuilder().create();
                        pwoResponse = savePWOResponse;
//                        if (pwoResponse.getStep() != 3) {
//                            flagPwo = true;
//                        }
                        flagPwo = true;
                        if (Helper.getItemParam(Constants.PWO_EMPLOYEE) != null) {
                            pwoEmployee = (PWOEmployee) Helper.getItemParam(Constants.PWO_EMPLOYEE);
                            pwoEmployee.setPwoResponse(savePWOResponse);
                            Helper.setItemParam(Constants.PWO_EMPLOYEE, pwoEmployee);
                        } else {
                            pwoEmployee = new PWOEmployee();
                            pwoEmployee.setPwoResponse(savePWOResponse);
                            Helper.setItemParam(Constants.PWO_EMPLOYEE, pwoEmployee);
                        }
                        pwoSetting();
                    } else {
                        clearData();
                        Toast.makeText(getApplicationContext(),
                                savePWOResponse.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            Constants.INTERNET_CONNECTION, Toast.LENGTH_LONG)
                            .show();
                }
            } else if (PARAM == 1) {
                if (wsMessageEmployee != null) {
                    if (wsMessageEmployee.getMessageId() == 0) {
                        flagEmployee = true;
                        employeeResponse = wsMessageEmployee
                                .getResultEmployee();
                        if (Helper.getItemParam(Constants.PWO_EMPLOYEE) != null) {
                            pwoEmployee = (PWOEmployee) Helper.getItemParam(Constants.PWO_EMPLOYEE);
                            pwoEmployee.setEmployeeResponse(employeeResponse);
                            Helper.setItemParam(Constants.PWO_EMPLOYEE, pwoEmployee);
                        } else {
                            pwoEmployee = new PWOEmployee();
                            pwoEmployee.setEmployeeResponse(employeeResponse);
                            Helper.setItemParam(Constants.PWO_EMPLOYEE, pwoEmployee);
                        }
                        employeeSetting();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                wsMessageEmployee.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            Constants.INTERNET_CONNECTION, Toast.LENGTH_LONG)
                            .show();
                }
            } else {
                if (savePWOResponse != null) {
                    if (savePWOResponse.getMessageId() == 0) {
                        new SessionManager(getApplicationContext())
                                .createDataSession(Helper
                                        .objectToString(savePWOResponse));
                        setButton(btnStart, "STARTED", R.drawable.btn_grey);
                        btnStart.setEnabled(false);
                        spinnerOperation.setEnabled(false);
                        // Intent intent = new Intent(getApplicationContext(),
                        // ConfirmationActivity.class);
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

            }
            if (HomeActivity.this.isDestroyed()) { // or call isFinishing() if min sdk version < 17
                return;
            }
            progress.dismiss();
        }
    }

    public void showDialogPwoNumber() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_scan, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        edtTxt = dialogView.findViewById(R.id.edtTxt);
        final ImageButton imgBtnClose = dialogView.findViewById(R.id.imgClose);
        final ImageButton imgBtnReset = dialogView.findViewById(R.id.imgReset);
        Button btnNext = dialogView.findViewById(R.id.btnNext);
        edtTxt.setHint("Please input PWO Number to Continue");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtTxt.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Input  PWO Number first", Toast.LENGTH_LONG).show();
                } else {
                    b.dismiss();
                    pwoNumber = edtTxt.getText().toString().trim();
                    getPwoNo();
                }
            }

        });
        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                flag = 0;
            }
        });
        imgBtnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edtTxt.setText("");
            }
        });

        b.show();
    }

    public void showDialogResource() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_scan, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        edtTxt = dialogView.findViewById(R.id.edtTxt);
        final ImageButton imgBtnClose = dialogView.findViewById(R.id.imgClose);
        final ImageButton imgBtnReset = dialogView.findViewById(R.id.imgReset);
        Button btnNext = dialogView.findViewById(R.id.btnNext);
        edtTxt.setHint("Please input or scan Resource to Continue");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (edtTxt.getText().toString().trim().equals("")) {
//                    Toast.makeText(getApplicationContext(), "Please Input  Resource first", Toast.LENGTH_LONG).show();
//                } else {
                    b.dismiss();
                    resource = edtTxt.getText().toString().trim();
                    getPwoNo();
//                }
            }

        });
        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                flag = 0;
            }
        });
        imgBtnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edtTxt.setText("");
            }
        });

        b.show();
    }

    public void showDialogEmployeeID() {
        if(flagEmployee == true) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_scan, null);
            dialogBuilder.setView(dialogView);
            final AlertDialog b = dialogBuilder.create();
            b.setCanceledOnTouchOutside(false);
            edtTxt = dialogView.findViewById(R.id.edtTxt);
            final ImageButton imgBtnClose = dialogView.findViewById(R.id.imgClose);
            final ImageButton imgBtnReset = dialogView.findViewById(R.id.imgReset);
            Button btnNext = dialogView.findViewById(R.id.btnNext);
            edtTxt.setHint("Please input Employee ID to Continue");

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edtTxt.getText().toString().trim().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please input Employee ID first", Toast.LENGTH_LONG).show();
                    } else {
                        b.dismiss();
                        employeeId = edtTxt.getText().toString().trim();
                        getEmployeeID();
                    }
                }

            });
            imgBtnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    b.dismiss();
                    flag = 0;
                }
            });
            imgBtnReset.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    edtTxt.setText("");
                }
            });

            b.show();
        }else{
            Toast.makeText(getApplicationContext(),"Please choose operation number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        BarcodeScanner.getInstance(this);
//        BarcodeScanner.registerUIobject(this);
//        Helper.setRequestUrl(false);
        initUI();
        if (Helper.getItemParam(Constants.RESULT_BARCODE) != null) {
            result = Helper.getItemParam(Constants.RESULT_BARCODE).toString();
            Helper.removeItemParam(Constants.RESULT_BARCODE);
            setDataScan(result);
        } else {
            txtEmployeeID.setText("Please scan Employee ID to Continue");
            txtPWODesc.setVisibility(View.GONE);
            txtPWONumber.setVisibility(View.GONE);
            txtScanPWONumber.setVisibility(View.VISIBLE);
            lv1.setVisibility(View.GONE);
            setButton(btnStart, "START", R.drawable.btn_grey);
            flagPwo = false;
            flagEmployee = false;
            pwoResponse = null;
            Helper.setRequestUrl(false);
        }
    }

    private void setDataScan(String scanData) {
        if (scanData != null && !scanData.equals("")) {
            if (!scanData.equals(Constants.BARCODE_ERROR)) {
                if (flag == 0){
                    Toast.makeText(getApplicationContext(), "please select PWO Number or Employee ID first", Toast.LENGTH_SHORT).show();
                }else if (flag == 1) {
                    employeeId = scanData;
                    edtTxt.setText(employeeId);
                } else{
                    pwoNumber = scanData;
                    edtTxt.setText(pwoNumber);
//                    getPwoNo();
                }
            } else {
                Toast.makeText(getApplicationContext(),Constants.BARCODE_ERROR,Toast.LENGTH_SHORT).show();
            }
        } else {
            clearData();
        }
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
                    Toast.makeText(HomeActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
//                    enableSaveFile = true;
                    break;
                case 2:
                    Toast.makeText(HomeActivity.this, R.string.save_fail, Toast.LENGTH_SHORT).show();
//                    enableSaveFile = true;
                    break;
                default:
                    break;
            }
        }
    };

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
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d(TAG, "onKeyDown keyCode = " + keyCode + " repeatCount = " + event.getRepeatCount());
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BUTTON_A:
//                if(isOnResume){
//                    // Synchronous the state of scan key and the mContinousScanButton when CONTINUOUS_SHOOT_MODE
//                    if (scanMode == com.zltd.decoder.Constants.CONTINUOUS_SHOOT_MODE && event.getRepeatCount() == 0) {
//                        if(mContinousScanButton.isChecked()){
//                            scanCase = STOPCONTINUESHOOT;
//                            mContinousScanButton.setChecked(false);
//                        }else{
//                            if(scanCase != STARTCONTINUESHOOT){
//                                ScanTotalNum = 0;
//                            }
//                            scanCase = STARTCONTINUESHOOT;
//                            mContinousScanButton.setChecked(true);
//                        }
//                        mContinousScanButton.setEnabled(false);
//                    }else if(scanMode == com.zltd.decoder.Constants.SINGLE_SHOOT_MODE){
//                        mSingleScanButton.setEnabled(false);
//                    }
//                    mDecoderMgr.dispatchScanKeyEvent(event);
//                }
//                return true;
//            default:
//                return super.onKeyDown(keyCode, event);
//        }
//    }

}
