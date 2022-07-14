package id.co.qualitas.barcodepp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import id.co.qualitas.barcodepp.R;
import id.co.qualitas.barcodepp.helper.BroadcastConfig;
import id.co.qualitas.barcodepp.helper.ListnerBiz;

public class XuxinActivity extends Activity {

    private Context mContext;
    private SharedPreferences sp;
    private EditText etScan;
    private Switch stSound;
    private Switch stVibrate;
    private Switch stContinue;
    private Button btScan;
    private EditText etScanTime;
    private Button btScanTime;
    private EditText etChixu;
    private Button btChixu;
    private TextView tvExposure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuxin);
        mContext = this;

        sp = getSharedPreferences("config", Context.MODE_PRIVATE);

        initBroadcastReciever();
        setListner();

		// 开启程序后打开扫描灯（因为发送关闭的广播此处不发送打开的广播，扫描灯是不会亮的）
//		Intent  intent = new Intent(BroadcastConfig.SCAN_LIGHT);
//		intent.putExtra("enabled", true);
//		sendBroadcast(intent);

		// will open the continuous scan

//        Intent intent = new Intent("com.android.scanner.service_settings");
//
//        intent.putExtra("scan_continue", true);
//
//        sendBroadcast(intent);

//        // Set the bar code parameters to continue to light out
//
//        Intent intentParam = new Intent("com.android.scanner.PARAM_SETTINGS");
//
//        intent.putExtra("number", 0x0c);
//
//        intent.putExtra("value", 1);
//
//        sendBroadcast(intentParam);
    }

    private Switch st4;
    @Override
    protected void onStart() {
        super.onStart();
        // 特殊需求，演示而已
        ListnerBiz.setChixutime(getApplicationContext(), st4);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
        // 声音控制
        ListnerBiz.setSound(getApplicationContext(),sp, stSound);

        // 震动控制
        ListnerBiz.setVibrate(getApplicationContext(),sp, stVibrate);

        // 扫描工具内应用设置下，循环扫描控制
        ListnerBiz.setContinue(getApplicationContext(), sp,stContinue);

        // 调用模拟按键实现扫描
        ListnerBiz.setScanClick(getApplicationContext(), btScan);

        // 长按扫描框清空内容
        ListnerBiz.setetScan(getApplicationContext(), etScan);

        // 扫描工具下条码设置内，基本设置内单次出光时间的控制（一般是不需要配置，因为有的机器是不提供该修改项的）
        ListnerBiz.setChixu(getApplicationContext(), sp,btChixu, etChixu);

        // 扫描工具下应用设置内，间隔时间的修改(和循环扫描配合使用)
        ListnerBiz.setContinueTime(getApplicationContext(), sp,etScanTime, btScanTime);

        // 二维扫描头修改，一维不必修改
        ListnerBiz.setExposure(mContext, tvExposure);
    }

    /**
     * 初始化广播接收器，AUTOID系列安卓产品上的系统软件扫描工具相对应
     */
    private void initBroadcastReciever() {
        // 发送广播到扫描工具内的应用设置项
        Intent intent = new Intent(BroadcastConfig.BROADCAST_SETTING);
        // 修改扫描工具内应用设置中的开发者项下的广播名称
        intent.putExtra(BroadcastConfig.BROADCAST_KEY, BroadcastConfig.CUSTOM_NAME);
        // 修改扫描工具内应用设置下的条码发送方式为 "广播"
        intent.putExtra(BroadcastConfig.SEND_KEY, "BROADCAST");
        // 修改扫描工具内应用设置下的结束符为 "NONE"
        intent.putExtra(BroadcastConfig.END_KEY, "NONE");

        sendBroadcast(intent);
    }

    private void setListner() {
        etScan = (EditText) findViewById(R.id.etScan);
        etScanTime = (EditText) findViewById(R.id.et3); // 扫描工具下应用设置内，间隔时间输入单位ms
        btScanTime = (Button) findViewById(R.id.bt3); // 扫描工具下应用设置内，间隔时间的修改
        stSound = (Switch) findViewById(R.id.switch1);
        stVibrate = (Switch) findViewById(R.id.switch2);
        stContinue = (Switch) findViewById(R.id.switch3);
        st4 = (Switch) findViewById(R.id.switch4);
        btScan = (Button) findViewById(R.id.btScan);
        etChixu = (EditText) findViewById(R.id.et4);
        btChixu = (Button) findViewById(R.id.bt4);
        tvExposure = (TextView) findViewById(R.id.tvExposure);
    }

    /**
     * 注册自定义的广播
     */
    private void registerReceiver(){
        IntentFilter filter = new IntentFilter(BroadcastConfig.CUSTOM_NAME);
        registerReceiver(receiver, filter);
    }

    /**
     * 需要自定义的广播接收器
     */
    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String barcode = intent.getStringExtra("scannerdata");
            etScan.setText(barcode);
            Log.i("tag", "barcode:"+barcode);
        }
    };

    /**
     * 需要取消注册广播
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        // 退出发送关闭扫描灯的广播，需要配合开启广播一起使用
//		Intent intent = new Intent(BroadcastConfig.SCAN_LIGHT);
//		intent.putExtra("enabled", false);
//		sendBroadcast(intent);
    };
}
