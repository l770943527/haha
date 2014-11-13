package common;

import java.util.Date;

import com.socket.client.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class WALoadListView extends ListView implements OnScrollListener {

	private final String REFRESH_RELEASE_STR = "释放刷新";
	private final String REFRESH_PULL_STR = "下拉刷新";
	private final String REFRESH_STR = "正在刷新";
	
	private final String LOAD_RELEASE_STR = "释放加载";
	private final String LOAD_PULL_STR = "上拉加载";
	private final String LOAD_STR = "正在加载";
	private final String LOAD_NEW_DATA = "已经为最新数据";
	
	private final String INFO = "最后更新";
	private boolean canLoad = true;
	
	private final int PULL_HEADER = 0x01;
	private final int PULL_FOOTER = 0x02;
	private final int PULL_BODY = 0x03;
	
	private final int STATE_START = 0x00;
	private final int STATE_PULL = 0x01;
	private final int STATE_RELEASE = 0x02;
	private final int STATE_REFRESH = 0x03;
	
	private int currentPullPosition;
	private int currentPullState;
	
	private ProgressDialog progressDlg;
	private View headerView;
	private View footerView;
	private LinearLayout headerRootView;
	private LinearLayout footerRootView;
	private TextView headerStateTextView;
	private TextView headerInfoTextView;
	
	private TextView footerStateTextView;
	private TextView footerInfoTextView;
	
	private ImageView headerArrowImageView;
	private ImageView footerArrowImageView;
	
//	private ProgressBar headerProgressBar;
//	private ProgressBar footerProgressBar;
	
	private LayoutInflater mLayoutInflater;//布局加载器
	
	private int startMoveY;//开始滑动的时候的Y坐标
	private int offestY;//滑动的时候的偏移量
	
	private int headerViewHeight;
	private int footerViewHeight;
	
	private Animation upAnimation;
	private Animation downAnimation;
	
	private OnRefreshListener onRefreshListener;
	
	public WALoadListView(Context context) {
		super(context);
		initViews(context);
	}

	public WALoadListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	public WALoadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}
	
	/**
	 * 初始化
	 * @param context
	 */
	private void initViews(Context context) {
		setHeaderDividersEnabled(false);
		setFooterDividersEnabled(false);
		setCacheColorHint(Color.TRANSPARENT);
		//初始化进度条
		progressDlg = new ProgressDialog(context);
		progressDlg.setMessage("数据加载中...");
		progressDlg.setCancelable(true);
		progressDlg.setIndeterminate(true);
		//获取布局加载服务
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//初始化headerView布局
		headerView = mLayoutInflater.inflate(R.layout.loadlistview_header, this, false);
		
		headerRootView = (LinearLayout) headerView.findViewById(R.id.loadlistviewheader_root);
		headerStateTextView = (TextView) headerView.findViewById(R.id.loadlistviewheader_stateTextView);
		headerInfoTextView = (TextView) headerView.findViewById(R.id.loadlistviewheader_infoTextView);
		headerInfoTextView.setText(INFO);
		headerArrowImageView = (ImageView) headerView.findViewById(R.id.loadlistviewheader_arrow);
		//headerProgressBar = (ProgressBar) headerView.findViewById(R.id.loadlistviewheader_progress);
		addHeaderView(headerView);
		measureView(headerView);
		headerViewHeight = headerView.getMeasuredHeight()+5;
		//headerProgressBar.setVisibility(View.GONE);
		headerRootView.getLayoutParams().height = 0;
		headerArrowImageView.getLayoutParams().height = BitmapFactory.decodeResource(getResources(), R.drawable.waexloadlistview_down_arrow).getHeight();
		
		//初始化footerView布局
		footerView = mLayoutInflater.inflate(R.layout.loadlistview_footer, this, false);
		footerRootView = (LinearLayout) footerView.findViewById(R.id.loadlistviewfooter_root);
		//获取底部的状态
		footerStateTextView = (TextView) footerView.findViewById(R.id.loadlistviewfooter_stateTextView);
		//获取底部的提示信息TextView并赋值
		footerInfoTextView = (TextView) footerView.findViewById(R.id.loadlistviewfooter_infoTextView);
		footerInfoTextView.setText(INFO);
		footerArrowImageView = (ImageView) footerView.findViewById(R.id.loadlistviewfooter_arrow);
		//footerProgressBar = (ProgressBar) footerView.findViewById(R.id.loadlistviewfooter_progress);
		addFooterView(footerView);
		measureView(footerView);
		footerViewHeight = footerView.getMeasuredHeight()+5;
		//footerProgressBar.setVisibility(View.GONE);
		footerRootView.getLayoutParams().height = 0;
		footerArrowImageView.getLayoutParams().height = BitmapFactory.decodeResource(getResources(), R.drawable.waexloadlistview_up_arrow).getHeight();
		//向上旋转动画
		upAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(200);
		upAnimation.setInterpolator(new LinearInterpolator());
		upAnimation.setFillAfter(true);
		//向下选装动画
		downAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		downAnimation.setDuration(200);
		downAnimation.setInterpolator(new LinearInterpolator());
		downAnimation.setFillAfter(true);
		
		setOnScrollListener(this);
	}

	//测量视图的高度
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0,0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {  
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);   
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);     
    }
    
    /**
     * 获取当前的时间
     * @return
     */
    @SuppressWarnings("static-access")
	private String getCurrentDate() {
    	DateFormat df = new DateFormat();
    	return df.format("yyyy-MM-dd kk:mm:ss", new Date()).toString();
    }
	@SuppressLint({ "NewApi", "NewApi" })
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch(ev.getAction()) {
		case MotionEvent.ACTION_DOWN :
			if(currentPullState != STATE_REFRESH) {
				startMoveY = (int) ev.getY();
				currentPullPosition = PULL_BODY;
			}
			break;
		case MotionEvent.ACTION_MOVE :
			//System.out.println(getCount()+"----"+getLastVisiblePosition()+"----"+getAdapter().getCount());
			if(currentPullState != STATE_REFRESH) {
				//头部进行拖动的时候
				if(getFirstVisiblePosition() == 0) {//可视视图中的第一条是列表的是第一条
					int pointerCount = ev.getPointerCount();
					for (int p = 0; p < pointerCount; p++) {
						//历史累积的高度
			            int historicalY = (int) ev.getY(p);
			            offestY = (int) (((historicalY - startMoveY))/2);//计算偏移量
			            if(offestY > 0) {//偏移量大于0
			            	currentPullPosition = PULL_HEADER;
			            	headerRootView.getLayoutParams().height = offestY;
			            	headerRootView.requestLayout();
			            	smoothScrollToPosition(0);
			            	if(headerRootView.getHeight() < headerViewHeight) {
			            		if(currentPullState != STATE_PULL) {
			            			headerStateTextView.setText(REFRESH_PULL_STR);
									currentPullState = STATE_PULL;
									headerArrowImageView.clearAnimation();
				            		headerArrowImageView.startAnimation(downAnimation);
			            		}
			            	} else if(headerRootView.getHeight() >= headerViewHeight){
			            		if(currentPullState != STATE_RELEASE) {
			            			headerArrowImageView.clearAnimation();
				            		headerArrowImageView.startAnimation(upAnimation);
				            		currentPullState = STATE_RELEASE;
				            		headerStateTextView.setText(REFRESH_RELEASE_STR);
			            		}
			            	}
			            }
			        }
				}
				//尾部进行拖动的时候
				else if(getLastVisiblePosition() >= getCount()-1 || getLastVisiblePosition()>=getCount() -2) {//可视视图的最后一条是列表的最后一条
					int pointerCount = ev.getPointerCount();
					for (int p = 0; p < pointerCount; p++) {
						//历史累积的高度
			            int historicalY = (int) ev.getY(p);
			            offestY = (int) ((startMoveY - historicalY)/2);//计算偏移量
			            if(offestY > 0) {//偏移量大于0
			            	currentPullPosition = PULL_FOOTER;
			            	footerRootView.getLayoutParams().height = offestY;
			            	footerRootView.requestLayout();
			            	smoothScrollToPosition(getCount()-1);
			            	if(canLoad) {//如果可以加载，不是最新数据的
				            	if(footerRootView.getHeight() < footerViewHeight) {
				            		if(currentPullState != STATE_PULL) {
				            			footerStateTextView.setText(LOAD_PULL_STR);
				            			currentPullState = STATE_PULL;
				            			footerArrowImageView.clearAnimation();
				            			footerArrowImageView.startAnimation(downAnimation);
				            		}
				            	} else if(footerRootView.getHeight() >= footerViewHeight) {
				            		if(currentPullState != STATE_RELEASE) {
				            			footerArrowImageView.clearAnimation();
				            			footerArrowImageView.startAnimation(upAnimation);
				            			currentPullState = STATE_RELEASE;
				            			footerStateTextView.setText(LOAD_RELEASE_STR);
				            		}
				            	} 
			            	} else {//不能加载
			            		currentPullState = STATE_PULL;
			            		footerStateTextView.setText(LOAD_NEW_DATA);
			            		footerArrowImageView.clearAnimation();
		            			footerArrowImageView.startAnimation(upAnimation);
		            			footerArrowImageView.clearAnimation();
			            		footerArrowImageView.setVisibility(View.GONE);
			            	}
			            }
			        }
				} 
			}
			break;
		case MotionEvent.ACTION_UP :
			if(currentPullPosition == PULL_HEADER) {//如果是头部进行的加载刷新释放后
				if(currentPullState == STATE_RELEASE) {//如果是释放刷新，开始进行刷新
					progressDlg.show();
					headerRootView.getLayoutParams().height = headerViewHeight;
					headerArrowImageView.clearAnimation();
					headerArrowImageView.startAnimation(downAnimation);
					headerArrowImageView.clearAnimation();
					headerArrowImageView.setVisibility(View.GONE);
					//headerProgressBar.setVisibility(View.VISIBLE);
					currentPullState = STATE_REFRESH;
					headerStateTextView.setText(REFRESH_STR);
					if(onRefreshListener != null)
						onRefreshListener.onUpRefresh();
				} else if(currentPullState == STATE_PULL) {
					headerRootView.getLayoutParams().height = 0;
					headerRootView.requestLayout();
				}
			} else if(currentPullPosition == PULL_FOOTER) {//如果是尾部进行的加载刷新释放后
				if(currentPullState == STATE_RELEASE) {//如果是释放刷新，开始进行刷新
					progressDlg.show();//显示加载进度
					footerRootView.getLayoutParams().height = footerViewHeight;
					footerArrowImageView.clearAnimation();
					footerArrowImageView.startAnimation(downAnimation);
					footerArrowImageView.clearAnimation();
					footerArrowImageView.setVisibility(View.GONE);
					//footerProgressBar.setVisibility(View.VISIBLE);
					currentPullState = STATE_REFRESH;
					footerStateTextView.setText(LOAD_STR);
					if(onRefreshListener != null)
						onRefreshListener.onDownRefresh();
				} else if(currentPullState == STATE_PULL) {
					footerRootView.getLayoutParams().height = 0;
					footerRootView.requestLayout();
				}
			}
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	/**
	 * 完成后调用的函数
	 */
	public void onRefreshComplete() { 
		if(currentPullState == STATE_REFRESH){
			//setEnabled(true);
			progressDlg.dismiss();
		}
		if(currentPullPosition == PULL_HEADER) {
			currentPullState = STATE_START;
	        //headerProgressBar.setVisibility(View.GONE);
	        headerArrowImageView.setVisibility(View.VISIBLE);
	        headerInfoTextView.setText(INFO + ":" + getCurrentDate());
	        headerRootView.getLayoutParams().height = 0;
		} else if(currentPullPosition == PULL_FOOTER) {
			currentPullState = STATE_START;
			//footerProgressBar.setVisibility(View.GONE);
	        footerArrowImageView.setVisibility(View.VISIBLE);
	        footerInfoTextView.setText(INFO + ":" + getCurrentDate());
	        footerRootView.getLayoutParams().height = 0;
		}
    }
	public void setOnRefreshListener(OnRefreshListener listener) {
		this.onRefreshListener = listener;
	}
	/**
	 * 提供刷新的接口
	 * @author candy
	 */
	public interface OnRefreshListener {//刷新接口
		public void onUpRefresh();
		
		public void onDownRefresh();
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	public boolean isCanLoad() {
		return canLoad;
	}

	public void setCanLoad(boolean canLoad) {
		this.canLoad = canLoad;
	}
}
