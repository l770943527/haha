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

	private final String REFRESH_RELEASE_STR = "�ͷ�ˢ��";
	private final String REFRESH_PULL_STR = "����ˢ��";
	private final String REFRESH_STR = "����ˢ��";
	
	private final String LOAD_RELEASE_STR = "�ͷż���";
	private final String LOAD_PULL_STR = "��������";
	private final String LOAD_STR = "���ڼ���";
	private final String LOAD_NEW_DATA = "�Ѿ�Ϊ��������";
	
	private final String INFO = "������";
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
	
	private LayoutInflater mLayoutInflater;//���ּ�����
	
	private int startMoveY;//��ʼ������ʱ���Y����
	private int offestY;//������ʱ���ƫ����
	
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
	 * ��ʼ��
	 * @param context
	 */
	private void initViews(Context context) {
		setHeaderDividersEnabled(false);
		setFooterDividersEnabled(false);
		setCacheColorHint(Color.TRANSPARENT);
		//��ʼ��������
		progressDlg = new ProgressDialog(context);
		progressDlg.setMessage("���ݼ�����...");
		progressDlg.setCancelable(true);
		progressDlg.setIndeterminate(true);
		//��ȡ���ּ��ط���
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//��ʼ��headerView����
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
		
		//��ʼ��footerView����
		footerView = mLayoutInflater.inflate(R.layout.loadlistview_footer, this, false);
		footerRootView = (LinearLayout) footerView.findViewById(R.id.loadlistviewfooter_root);
		//��ȡ�ײ���״̬
		footerStateTextView = (TextView) footerView.findViewById(R.id.loadlistviewfooter_stateTextView);
		//��ȡ�ײ�����ʾ��ϢTextView����ֵ
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
		//������ת����
		upAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		upAnimation.setDuration(200);
		upAnimation.setInterpolator(new LinearInterpolator());
		upAnimation.setFillAfter(true);
		//����ѡװ����
		downAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		downAnimation.setDuration(200);
		downAnimation.setInterpolator(new LinearInterpolator());
		downAnimation.setFillAfter(true);
		
		setOnScrollListener(this);
	}

	//������ͼ�ĸ߶�
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
     * ��ȡ��ǰ��ʱ��
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
				//ͷ�������϶���ʱ��
				if(getFirstVisiblePosition() == 0) {//������ͼ�еĵ�һ�����б���ǵ�һ��
					int pointerCount = ev.getPointerCount();
					for (int p = 0; p < pointerCount; p++) {
						//��ʷ�ۻ��ĸ߶�
			            int historicalY = (int) ev.getY(p);
			            offestY = (int) (((historicalY - startMoveY))/2);//����ƫ����
			            if(offestY > 0) {//ƫ��������0
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
				//β�������϶���ʱ��
				else if(getLastVisiblePosition() >= getCount()-1 || getLastVisiblePosition()>=getCount() -2) {//������ͼ�����һ�����б�����һ��
					int pointerCount = ev.getPointerCount();
					for (int p = 0; p < pointerCount; p++) {
						//��ʷ�ۻ��ĸ߶�
			            int historicalY = (int) ev.getY(p);
			            offestY = (int) ((startMoveY - historicalY)/2);//����ƫ����
			            if(offestY > 0) {//ƫ��������0
			            	currentPullPosition = PULL_FOOTER;
			            	footerRootView.getLayoutParams().height = offestY;
			            	footerRootView.requestLayout();
			            	smoothScrollToPosition(getCount()-1);
			            	if(canLoad) {//������Լ��أ������������ݵ�
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
			            	} else {//���ܼ���
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
			if(currentPullPosition == PULL_HEADER) {//�����ͷ�����еļ���ˢ���ͷź�
				if(currentPullState == STATE_RELEASE) {//������ͷ�ˢ�£���ʼ����ˢ��
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
			} else if(currentPullPosition == PULL_FOOTER) {//�����β�����еļ���ˢ���ͷź�
				if(currentPullState == STATE_RELEASE) {//������ͷ�ˢ�£���ʼ����ˢ��
					progressDlg.show();//��ʾ���ؽ���
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
	 * ��ɺ���õĺ���
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
	 * �ṩˢ�µĽӿ�
	 * @author candy
	 */
	public interface OnRefreshListener {//ˢ�½ӿ�
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
