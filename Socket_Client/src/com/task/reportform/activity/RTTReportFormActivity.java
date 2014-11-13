package com.task.reportform.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.GraphicalView;
import org.achartengine.chart.TimeChart;
import org.achartengine.tools.PanListener;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.chart.DemoChart;
import com.json.util.EncapSendInforUtil;
import com.json.util.ParseJSONStringUtil;
import com.socket.client.R;
import com.socket.util.InternetUtil;
import com.task.activity.data.DAInforItem;
import com.task.activity.data.TimeValue;

import android.R.integer;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RTTReportFormActivity extends Activity {

	private TextView title;
	private TextView infor;
	private Handler handler;
	private Button backBtn;
	private Button Btn1, Btn2, Btn3, Btn4, Btn5;
	private List<Date> timeArr = new ArrayList<Date>();
	private List<integer> valueArr = new ArrayList<integer>();
	private Map<String, Integer> map = new LinkedHashMap<String, Integer>();
	private DemoChart demo;
	private Random r;
	private GraphicalView chartView;
	private List<TimeValue> guideList = new ArrayList<TimeValue>();
	private ArrayList<String> records = new ArrayList<String>();
	private ArrayList<String> headers = new ArrayList<String>();
	private double zoomRate;
	private double xMin,xMax,xMinAll=0,xMaxAll=0;
	private double yMin,yMax;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置横屏
		setContentView(R.layout.result_infor_activity);

		Bundle bundle = this.getIntent().getExtras();
		headers = bundle.getStringArrayList("headers");
		records = bundle.getStringArrayList("records");

		initialTitles();
		initialTab();
		Log.v("QLQ", " " + "****************************before initialChart");
		initialChart();
		Log.v("QLQ", " " + "****************************after initialChart");
		Log.v("QLQ",
				" "
						+ "****************************before initialNetworkInternetUtil");
		initialNetWork();
		Log.v("QLQ",
				" "
						+ "****************************after initialNetworkInternetUtil.sendMessage");
	}

	private void initialChart() {
		// TODO Auto-generated method stub
		LinearLayout linearView = (LinearLayout) findViewById(R.id.chart_show);
		demo = new DemoChart();
		r = new Random();
		chartView = demo.getDemoChartGraphicalView(this);
		chartView.addZoomListener(new ZoomListener() {
			public void zoomApplied(ZoomEvent e) {


//				zoomRate = e.getZoomRate();
//				
//				if (e.getZoomRate() > 2) {
//					Log.v("QLQ", "need more data");
//					
//				} else {
//					Log.v("QLQ", "all use compressed num " + " rate "
//							+ e.getZoomRate());
//					System.out.println(e.toString());
//				}

			}

			public void zoomReset() {
				Log.v("Zoom", "Reset");
			}
		}, true, true);
		// 设置拖动图表时后台打印出图表坐标的最大最小值.
		chartView.addPanListener(new PanListener() {
			public void panApplied() {
				xMin = demo.getRenderer().getXAxisMin();
				xMax = demo.getRenderer().getXAxisMax();
//				yMin = demo.getRenderer().getYAxisMin();
//				yMax = demo.getRenderer().getYAxisMax();
				if(xMax-xMin < xMinAll-xMaxAll/2){
					InternetUtil.sendMessage(EncapSendInforUtil.encapRTTResultImforReq(
							Integer.parseInt(records.get(2)), (long)xMin, (long)xMax, 1));// taskid,startpos,endpos,type
					Log.v("QLQ", "New X range=[" + demo.getRenderer().getXAxisMin()
							+ ", " + demo.getRenderer().getXAxisMax()
							+ "], Y range=[" + demo.getRenderer().getYAxisMax()
							+ ", " + demo.getRenderer().getYAxisMax() + "]");
				}
			}
		});

		linearView.addView(chartView, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

	}

	private void initialTitles() {
		title = (TextView) findViewById(R.id.tasktitlepanel_titleTextView);
		infor = (TextView) findViewById(R.id.infor);
		infor.setVisibility(8);
		backBtn = (Button) findViewById(R.id.tasktitlepanel_leftBtn);
		// String temp
		// ="{\"data\":{\"Result\":[{\"2014-02-27 21:00:06\":210,\"2014-02-27 20:59:36\":198,\"2014-02-27 21:01:01\":218,\"2014-02-27 20:59:31\":1470,\"2014-02-27 21:02:56\":236,\"2014-02-27 21:01:56\":222,\"2014-02-27 21:00:56\":211,\"2014-02-27 21:00:11\":242,\"2014-02-27 21:01:51\":221,\"2014-02-27 21:02:16\":252,\"2014-02-27 21:02:51\":219,\"2014-02-27 21:00:51\":238,\"2014-02-27 21:02:11\":212,\"2014-02-27 20:59:46\":243,\"2014-02-27 20:59:41\":236,\"2014-02-27 21:00:41\":237,\"2014-02-27 20:58:56\":234,\"2014-02-27 20:59:01\":274,\"2014-02-27 21:00:46\":203,\"2014-02-27 21:02:46\":228,\"2014-02-27 20:58:51\":233,\"2014-02-27 20:59:06\":215,\"2014-02-27 21:02:06\":230,\"2014-02-27 21:02:41\":249,\"2014-02-27 21:00:01\":225,\"2014-02-27 21:02:01\":237,\"2014-02-27 20:59:16\":255,\"2014-02-27 20:59:51\":213";
		// infor.setText(temp);
		title.setText("结果信息");
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RTTReportFormActivity.this.finish();
			}
		});
	}

	private void initialNetWork() {
		Log.v("QLQ", " "
				+ "****************************InternetUtil.sendMessage");
		InternetUtil.sendMessage(EncapSendInforUtil.encapRTTResultImforReq(
				Integer.parseInt(records.get(2)), 0, 15, 1));// taskid,startpos,endpos

		// handler类接收数据
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					// 动态更新UI界面
					String temp1 = "11";
					// t.setText("11");
					String str = msg.getData().getString("title");
					infor.setText("let's have a test3");
					guideList.clear();
					JSONTokener jsonParser = new JSONTokener(str);
					try {
						JSONObject inforOj = (JSONObject) jsonParser
								.nextValue();
						JSONObject headOj = inforOj.getJSONObject("head");
						String messageType = headOj.getString("message_type");
						if (messageType.equals("13001")) {
							timeArr.clear();
							valueArr.clear();
							map.clear();
							JSONObject dataOj = inforOj.getJSONObject("data");
							JSONArray resultArr = dataOj.getJSONArray("Result");
							// Log.v("QLQ", " " + resultArr.toString());
							// Log.v("QLQ", " " + "let's have a test4.2");
							infor.setText(resultArr.toString());
							// map =
							// ParseJSONStringUtil.parseMapResultData(resultOj.toString());
							// for(int j=0;j<map.size();j++){
							// timeArr.add(java.sql.Date.valueOf());
							// }

							for (int i = 0; i < resultArr.length(); i++) {
								JSONObject temp = (JSONObject) resultArr.opt(i);
								TimeValue item = new TimeValue(
										temp.getString("time"),
										temp.getInt("value"));
								guideList.add(item);
							}

							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							demo.clearData();
							// for(int i=0;i<500;i++){
							// demo.updateData(new Date(new
							// Date().getTime()+i*TimeChart.DAY/86400),
							// r.nextDouble()*5);
							// chartView.postInvalidate();
							// }
							try {
								for (int i = 0; i < guideList.size(); i++) {

									Date date = sdf.parse(guideList.get(i)
											.getTime());

									Log.v("QLQ", date.toString());
									demo.updateData(date, guideList.get(i)
											.getValue());
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
							chartView.postInvalidate();
							xMinAll = demo.getRenderer().getXAxisMin();
							xMaxAll = demo.getRenderer().getXAxisMax();
							// progressDlg.dismiss();
							// Log.v("QLQ", "after*****************2**");
							// adapter.notifyDataSetChanged();
							// //发送消息通知ListView更新
							// listView.setAdapter(adapter); //
							// 重新设置ListView的数据适配器
							// listView.onRefreshComplete();

						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			};
		};

		Thread t = new Thread(new InternetUtil(handler));
		t.start();

	}

	private void initialTab() {
		Btn1 = (Button) findViewById(R.id.btn1);
		Btn2 = (Button) findViewById(R.id.btn2);
		Btn3 = (Button) findViewById(R.id.btn3);
		Btn4 = (Button) findViewById(R.id.btn4);
		Btn5 = (Button) findViewById(R.id.btn5);

		Button.OnClickListener onClickListener = new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Btn1.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.common_threegroupbtn_left_unpress));
				Btn2.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.common_threegroupbtn_mid_unpress));
				Btn3.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.common_threegroupbtn_mid_unpress));
				Btn4.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.common_threegroupbtn_mid_unpress));
				Btn5.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.common_threegroupbtn_right_unpress));
				switch (v.getId()) {
				case R.id.btn1:
					Btn1.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.common_threegroupbtn_left_pressed));
					InternetUtil
							.sendMessage(EncapSendInforUtil
									.encapRTTResultImforReq(
											Integer.parseInt(records.get(2)),
											0, 15, 1));// taskid,startpos,endpos
					break;

				case R.id.btn2:
					Btn2.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.common_threegroupbtn_mid_pressed));
					InternetUtil
							.sendMessage(EncapSendInforUtil
									.encapRTTResultImforReq(
											Integer.parseInt(records.get(2)),
											0, 15, 2));// taskid,startpos,endpos
					break;

				case R.id.btn3:
					Btn3.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.common_threegroupbtn_mid_pressed));
					InternetUtil
							.sendMessage(EncapSendInforUtil
									.encapRTTResultImforReq(
											Integer.parseInt(records.get(2)),
											0, 15, 3));// taskid,startpos,endpos
					break;

				case R.id.btn4:
					Btn4.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.common_threegroupbtn_mid_pressed));
					InternetUtil
							.sendMessage(EncapSendInforUtil
									.encapRTTResultImforReq(
											Integer.parseInt(records.get(2)),
											0, 15, 4));// taskid,startpos,endpos
					break;

				case R.id.btn5:
					Btn5.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.common_threegroupbtn_right_pressed));
					// graphType = 1;
					// type = "FCURPRICE";
					// new MThread().start();
					InternetUtil
							.sendMessage(EncapSendInforUtil
									.encapRTTResultImforReq(
											Integer.parseInt(records.get(2)),
											0, 15, 5));// taskid,startpos,endpos
					break;

				}
			}
		};
		Btn1.setOnClickListener(onClickListener);
		Btn2.setOnClickListener(onClickListener);
		Btn3.setOnClickListener(onClickListener);
		Btn4.setOnClickListener(onClickListener);
		Btn5.setOnClickListener(onClickListener);
	}
	// 动态刷新图形
	// class ChartTask extends TimerTask{
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// demo.updateData(new Date(), r.nextDouble()*5);
	// //这里是动态修改图表的关键，因为TimerTask是在子线程里面的，所以当自己要修改控件
	// //时，我们需要向主线程发送一个消息，当然如果是在主线程里面修改view的话我们需要调用
	// //chartview.invalidate();
	// chartView.postInvalidate();
	// }
	// }

}
