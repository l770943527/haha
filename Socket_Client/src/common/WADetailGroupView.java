package common;

import com.socket.client.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * WADetailView中的�?
 * 
 * @author Omi
 * 
 */
public class WADetailGroupView extends LinearLayout {

	private Context context;
	private LinearLayout titleViewll;
	private TextView titleView;
	private TextView secondtitleView;
	private boolean isFirstRow = true;

	private static final int TEXT_SIZE_TITLE_W480 = 14;
	private static final int TEXT_SIZE_TITLE_W720 = 18;

	private static int TEXT_SIZE_TITLE = TEXT_SIZE_TITLE_W480;
	
	
	public WADetailGroupView(Context context) {
		super(context);
		this.context = context;
		initial();
	}

	public WADetailGroupView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initial();
	}

	private void initial() {
		int width = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		if(width >= 640){
			TEXT_SIZE_TITLE = TEXT_SIZE_TITLE_W720;
		}
		setBackgroundResource(R.drawable.wadetail_group_background);
		setOrientation(VERTICAL);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(WADetailView.GROUP_MARGIN_LEFT, WADetailView.GROUP_MARGIN_TOP, WADetailView.GROUP_MARGIN_RIGHT, WADetailView.GROUP_MARGIN_BOTTOM);
		setLayoutParams(params);
		setPadding(0, WADetailView.GROUP_PADDING_TOP, 0, WADetailView.GROUP_PADDING_BOTTOM);
		
		titleViewll = new LinearLayout(context);
		titleViewll.setVisibility(View.GONE);
		titleViewll.setOrientation(LinearLayout.HORIZONTAL);
		titleViewll.setLayoutParams(params);
		
		LayoutParams titleParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		titleParams.weight = 1.0f;
		titleView = new TextView(context);
		titleView.setVisibility(VISIBLE);
		titleView.setTextSize(TEXT_SIZE_TITLE);
		titleView.setLayoutParams(titleParams);
		titleViewll.addView(titleView);
		
		secondtitleView = new TextView(context);
		secondtitleView.setTextSize(TEXT_SIZE_TITLE);
		secondtitleView.setLayoutParams(titleParams);
		
		secondtitleView.setVisibility(View.GONE);
		titleViewll.addView(secondtitleView);
//		titleView.setLayoutParams(params);
	}

	public void setTitle(String title) {
		titleView.setText(title);
		if (title != null && !"".equalsIgnoreCase(title)) {
			titleViewll.setVisibility(VISIBLE);
		} else {
			titleViewll.setVisibility(GONE);
		}
	}
	
	public void setSecondTile(String title)
	{
		secondtitleView.setText(title);
		if(title != null && !"".equalsIgnoreCase(title))
		{
			secondtitleView.setVisibility(VISIBLE);
		} else {
			secondtitleView.setVisibility(GONE);
		}
	}
	
	public void setTitleColor(int color) {
		titleView.setTextColor(color);
		secondtitleView.setTextColor(color);
	}
	
	public LinearLayout getTitleView() {
		return titleViewll;
	}
/*	public String getTitle() {
		if (titleView != null) {
			return titleView.getText().toString();
		} else {
			return null;
		}
	}
	
	public void setTitle(String title) {
		titleView.setText(title);
		if (title != null && !"".equalsIgnoreCase(title)) {
			titleView.setVisibility(VISIBLE);
		} else {
			titleView.setVisibility(GONE);
		}
	}

	public TextView getTitleView() {
		return titleView;
	}*/

	/**
	 * 由上到下，顺序添加行
	 * 
	 * @param row
	 */
	public void addRow(WADetailRowView row) {
		if (isFirstRow) {
			isFirstRow = false;
			addView(row);
		} else {
			addRowSeparator();
			addView(row);
		}
	}

	/**
	 * 增加列之间的分割�?
	 */
	private void addRowSeparator() {
		View child = new View(context);
		LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
		params.leftMargin = 2;
		params.rightMargin = 2;
		child.setLayoutParams(params);
		child.setBackgroundResource(R.drawable.wadetail_row_separator);
		addView(child);
	}
}
