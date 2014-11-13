package common;

import com.socket.client.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class WADetailView extends LinearLayout {

	private Context context;

	public static final int GROUP_MARGIN_LEFT = 15;
	public static final int GROUP_MARGIN_TOP = 4;
	public static final int GROUP_MARGIN_RIGHT = 15;
	public static final int GROUP_MARGIN_BOTTOM = 4;

	public static final int GROUP_PADDING_TOP = 5;
	public static final int GROUP_PADDING_BOTTOM = GROUP_PADDING_TOP;

	public static final int GROUP_TITLE_MARGIN_LEFT = GROUP_MARGIN_LEFT + 8;

	private int titleIndex = 0;

	public WADetailView(Context context) {
		super(context);
		this.context = context;
		initial();
	}

	public WADetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initial();
	}

	private void initial() {
		setOrientation(VERTICAL);
	}

	/**
	 * 由上到下，顺序添加组
	 */
	public void addGroup(WADetailGroupView group) {
		addView(group.getTitleView());
		addView(group);
	}

	/**
	 * 由上到下，顺序添加详情抬�?
	 * 
	 * @param titleView
	 */
	public void addTitle(View titleView) {
		addView(titleView, titleIndex++);
		addTitleSeparator();
	}

	/**
	 * 增加台头之间的分割线
	 */
	private void addTitleSeparator() {
		View separator = new View(context);
		separator.setBackgroundResource(R.drawable.wadetail_title_separator);
		addView(separator, titleIndex++);
	}
}
