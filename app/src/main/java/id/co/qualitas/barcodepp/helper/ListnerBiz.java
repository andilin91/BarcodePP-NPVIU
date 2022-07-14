package id.co.qualitas.barcodepp.helper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class ListnerBiz {

	/**
	 *扫描工具内应用设置下， 控制声音
	 * @param context
	 * @param stSound
	 */
	public static void setSound(final Context context, final SharedPreferences sp ,Switch stSound){
		
		stSound.setChecked(sp.getBoolean("sound", false));
		
		stSound.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				Intent intent = new Intent(BroadcastConfig.BROADCAST_SETTING);
				
				if (isChecked) {
					
					intent.putExtra(BroadcastConfig.SOUND_KEY, isChecked);
					context.sendBroadcast(intent);
					sp.edit().putBoolean("sound", true).commit();
					
				} else {
					
					intent.putExtra(BroadcastConfig.SOUND_KEY, isChecked);
					context.sendBroadcast(intent);
					sp.edit().putBoolean("sound", false).commit();
					
				}
			}
		});
	}
	
	/**
	 *扫描工具内应用设置下， 控制震动
	 * @param context
	 * @param stVibrate
	 */
	public static void setVibrate(final Context context, final SharedPreferences sp ,Switch stVibrate){
		stVibrate.setChecked(sp.getBoolean("viberate", false));
		stVibrate.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				Intent intent = new Intent(BroadcastConfig.BROADCAST_SETTING);
				
				if (isChecked) {
					
					intent.putExtra(BroadcastConfig.VIBERATE_KEY, isChecked);
					context.sendBroadcast(intent);
					sp.edit().putBoolean("viberate", true).commit();
					
				} else {
					
					intent.putExtra(BroadcastConfig.VIBERATE_KEY, isChecked);
					context.sendBroadcast(intent);
					sp.edit().putBoolean("viberate", false).commit();
					
				}
			}
		});
	}
	
	/**
	 * 扫描工具内应用设置下，循环扫描控制
	 * @param context
	 * @param stContinue
	 */
	public static void setContinue(final Context context, final SharedPreferences sp,Switch stContinue){
		stContinue.setChecked(sp.getBoolean("continiu", false));
		stContinue.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				Intent intent = new Intent(BroadcastConfig.BROADCAST_SETTING);
				
				if (isChecked) {
					
					intent.putExtra(BroadcastConfig.CONTINIU_KEY, isChecked);
					context.sendBroadcast(intent);
					sp.edit().putBoolean("continiu", true).commit();
					
				} else {
					
					intent.putExtra(BroadcastConfig.CONTINIU_KEY, isChecked);
					context.sendBroadcast(intent);
					sp.edit().putBoolean("continiu", false).commit();
					
				}
			}
		});
	}
	
	/**
	 * 扫描工具内条码设置下，基本设置内单次光持续时间的修改，配合
	 * 扫描工具内应用设置下，循环扫描一起使用可以达到扫描灯的自定义出光频率
	 * @param context
	 * @param stContinue
	 */
	public static void setChixu(final Context context, final SharedPreferences sp,Button bt4 ,final EditText et4){
		et4.setText(sp.getString("chixutime", ""));
		bt4.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String chixuTime = et4.getText().toString().trim();
				if (chixuTime.equals("")) {
					Toast.makeText(context, "请输入时间", Toast.LENGTH_LONG).show();
					return ;
				}
				Intent intent = new Intent(BroadcastConfig.ACTION_PARAM_SETTINGS);
				// 持续时间对应的key 和 value
				intent.putExtra("number", 0x01);
				// 持续时间对应的可配置时间的修改项的key 和 value
				intent.putExtra("value", Integer.parseInt(chixuTime));
				context.sendBroadcast(intent);
				sp.edit().putString("chixutime", chixuTime).commit();
				Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	/**
	 * 扫描工具内应用设置下，间隔时间的修改，这个需要配合循环扫描一起使用
	 * @param context
	 * @param stContinue
	 */
	public static void setContinueTime(final Context context, final SharedPreferences sp ,final EditText et3, final Button bt3){
		et3.setText(sp.getString("continiutime", ""));
		bt3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String etTime = et3.getText().toString().trim();
				if (etTime.equals("")) {
					Toast.makeText(context, "请输入时间", Toast.LENGTH_LONG).show();
					return;
				}
				Intent intent = new Intent(BroadcastConfig.BROADCAST_SETTING);
				intent.putExtra("interval", Integer.parseInt(etTime));
				context.sendBroadcast(intent);
				sp.edit().putString("continiutime", etTime).commit();
				Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	/**
	 * 调用模拟按键button来控制扫描光的熄灭
	 * @param context
	 * @param btScan
	 */
	public static void setScanClick(final Context context, Button btScan){
		
		btScan.setOnTouchListener(new OnTouchListener() {
			
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					
					Intent intent = new Intent(BroadcastConfig.SCAN_START);
					context.sendBroadcast(intent);
					
				}else if (event.getAction() == KeyEvent.ACTION_UP) {
					
					Intent intent = new Intent(BroadcastConfig.SCAN_STOP);
					context.sendBroadcast(intent);
					
				}
				
				return false;
			}
		});
	}
	
	// 特殊需求，如果switch设置打开，则demo开启后会自动连续出光，和上述的循环扫描不是一个概念
	static boolean flag ;
	public static void setChixutime(final Context context, Switch st4){
		st4.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView,  boolean isChecked) {
				flag = isChecked;
				new Thread(){
					public void run() {
						if (flag) {
							while (flag) {
								Intent intent = new Intent(BroadcastConfig.SCAN_START);
								context.sendBroadcast(intent);
								try {
									Thread.sleep(100);
									Log.e("tag", Thread.currentThread().getName());
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}else{
							Intent intent2 = new Intent(BroadcastConfig.SCAN_STOP);
							context.sendBroadcast(intent2);
						}
					}
				}.start();
				Log.e("tag", Thread.currentThread().getName());
			}
		});
	}
	
	/**
	 * 二维扫描头修改，一维不必修改
	 * @param context
	 * @param tvExposure
	 */
	public static void setExposure(final Context context,final TextView tvExposure){
		tvExposure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Builder builder = new Builder(context);
				final String[] items = {"auto","low","mid","high"};
				builder.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						tvExposure.setText(items[which]);
						Intent intent = new Intent(BroadcastConfig.ACTION_PARAM_SETTINGS);
						intent.putExtra("number", 0x0d);
						intent.putExtra("value", which);
						context.sendBroadcast(intent);
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
			}
		});
		
	}
	
	/**
	 * 长按扫描框清除内容
	 * @param context
	 * @param etScan
	 */
	public static void setetScan(final Context context, final EditText etScan){
		etScan.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				etScan.setText("");
				Toast.makeText(context, "清除成功", Toast.LENGTH_SHORT).show();
			 	return false;
			}
		});
	}
}
