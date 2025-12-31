package id.co.qualitas.barcodepp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.Result;

import id.co.qualitas.barcodepp.R;
import id.co.qualitas.barcodepp.constants.Constants;
import id.co.qualitas.barcodepp.helper.Helper;
import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

import androidx.core.content.ContextCompat;

public class ScanActivity extends BaseActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private ImageView btnBack;
    private Button btnOk;
    private EditText doId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
//        scannerView = new ZXingScannerView(this);
//        setContentView(scannerView);
        init();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
//                Toast.makeText(ScanActivity.this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                requestPermissions();
            }
        }
        initialize();
    }

    private void initialize() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        doId = findViewById(R.id.customer_name);
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> {

            String doIdTemp = doId.getText().toString();
            if (doIdTemp.isEmpty()) {
                Toast.makeText(getApplicationContext(), getString(R.string.scan_barcode), Toast.LENGTH_SHORT).show();
                return;
            } else {
                Helper.setItemParam(Constants.RESULT_BARCODE, doIdTemp);
                onBackPressed();
            }
        });
        ViewGroup contentFrame = findViewById(R.id.content_frame);

        scannerView = new ZXingScannerView(this) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        contentFrame.addView(scannerView);
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(ScanActivity.this, CAMERA)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            super.requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
        }
    }

    public void onRequestPermissionsResults(int requestCode, String permission[], int grantResults[]) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
//                        Toast.makeText(ScanActivity.this, "Permission granted", Toast.LENGTH_LONG).show();
                    } else {
//                        Toast.makeText(ScanActivity.this, "Permission denied", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                displayAlertMessage("Please allow access for both permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                setCamera();
            } else {
                requestPermissions();
            }
        } else {
            setCamera();
        }
    }

    private void setCamera() {
        if (scannerView == null) {
            scannerView = new ZXingScannerView(this);
            setContentView(scannerView);
        }
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(ScanActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", listener)
                .setNegativeButton("Cancel", null)
                .create().show();
    }

    @Override
    public void handleResult(Result result) {
        final String scanResult = result.getText();
//        Toast.makeText(ScanActivity.this, scanResult, Toast.LENGTH_LONG).show();
        scannerView.resumeCameraPreview(ScanActivity.this);

        Helper.setItemParam(Constants.RESULT_BARCODE, scanResult);
//        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        onBackPressed();
    }

    private static class CustomViewFinderView extends ViewFinderView {
        public static final String TRADE_MARK_TEXT = "Scanning...";
        public static final int TRADE_MARK_TEXT_SIZE_SP = 20;
        public final Paint PAINT = new Paint();

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            PAINT.setColor(Color.WHITE);
            PAINT.setAntiAlias(true);
            float textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    TRADE_MARK_TEXT_SIZE_SP, getResources().getDisplayMetrics());
            PAINT.setTextSize(textPixelSize);
            setSquareViewFinder(true);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawTradeMark(canvas);
        }

        private void drawTradeMark(Canvas canvas) {
            Rect framingRect = getFramingRect();
            float tradeMarkTop;
            float tradeMarkLeft;
            if (framingRect != null) {
                tradeMarkTop = framingRect.bottom + PAINT.getTextSize() + 10;
                tradeMarkLeft = framingRect.left;
            } else {
                tradeMarkTop = 10;
                tradeMarkLeft = canvas.getHeight() - PAINT.getTextSize() - 10;
            }
            canvas.drawText(TRADE_MARK_TEXT, tradeMarkLeft, tradeMarkTop, PAINT);
        }

    }
}
