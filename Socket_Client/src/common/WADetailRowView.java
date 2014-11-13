package common;

import com.socket.client.R;

import junit.framework.Assert;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Group中的每一�?
 * 
 * @author Omi
 * 
 */
public class WADetailRowView extends LinearLayout {

	/**
	 * C means colon
	 * 
	 * @author Omi
	 * 
	 */
	public static enum RowType {
		CUSTOMIZE, 
		TEXT, 
		NAME_C_VALUE, 
		NAME_C_VALUE_ICON,
		
		/**
		 * 图标描述
		 */
		NAME_C_VALUE_ICON_DES_CLICKABLE,
		
		/**
		 * 移动电话
		 */
		NAME_C_VALUE_ICON_MOBILE,
		
		/**
		 * 图定电话
		 */
		NAME_C_VALUE_ICON_TEL,
		
		NAME_C_VALUE_ICON_MAIL, 
		NAME_C_ICON, INDEX_NAME_CLICKABLE,
	}

	private RowType type;
	private View customView = null;
	private View iconView = null;

	private String index = null;
	private String name = null;
	private String value = null;
	private int iconResID = -1;
	private String iconDes = null;

	private Context context;

	private static final int TEXT_SIZE_NAME_480 = 20;//
	private static final int TEXT_SIZE_VALUE_480 = 24;//
	private static final int TEXT_SIZE_COLON_480 = 20;//
	private static final int TEXT_SIZE_TEXT_480 = 20;

	private static final int TEXT_SIZE_NAME_720 = 30;//
	private static final int TEXT_SIZE_VALUE_720 = 36;//
	private static final int TEXT_SIZE_COLON_720 = 30;//
	private static final int TEXT_SIZE_TEXT_720 = 30;

	private static int TEXT_SIZE_NAME = TEXT_SIZE_NAME_480;
	private static int TEXT_SIZE_VALUE = TEXT_SIZE_VALUE_480;
	private static int TEXT_SIZE_COLON = TEXT_SIZE_COLON_480;
	private static int TEXT_SIZE_TEXT = TEXT_SIZE_TEXT_480;
	
	private static int CONTENT_TEXT_SIZE_480 = 22;
	private static int CONTENT_TEXT_SIZE_720 = 33;
	private static int CONTENT_TEXT_SIZE = CONTENT_TEXT_SIZE_480;

	// /
	private static final int ROW_WIDTH_480 = 442;
	private static final int ROW_HEIGHT_480 = 60;//

	private static final int ROW_WIDTH_720 = 642;
	private static final int ROW_HEIGHT_720 = 88;//

	private static int ROW_HEIGHT = ROW_HEIGHT_480;

	// /
	private static final int ROW_PADDING_LEFT_480 = 12;//
	private static final int ROW_PADDING_TOP_480 = 12;
	private static final int ROW_PADDING_RIGHT_480 = 12;
	private static final int ROW_PADDING_BOTTOM_480 = 12;

	private static final int ROW_PADDING_LEFT_720 = 18;//
	private static final int ROW_PADDING_TOP_720 = 18;
	private static final int ROW_PADDING_RIGHT_720 = 18;
	private static final int ROW_PADDING_BOTTOM_720 = 18;

	private static final int ROW_TEXT_PADDING_LEFT_480 = 20;
	private static final int ROW_TEXT_PADDING_RIGHT_480 = 20;
	private static final int ROW_TEXT_PADDING_TOP_480 = 22;
	private static final int ROW_TEXT_PADDING_BOTTOM_480 = 22;
	
	private static final int ROW_TEXT_PADDING_LEFT_720 = 30;
	private static final int ROW_TEXT_PADDING_RIGHT_720 = 30;
	private static final int ROW_TEXT_PADDING_TOP_720 = 33;
	private static final int ROW_TEXT_PADDING_BOTTOM_720 = 33;
	
	
	private static int ROW_PADDING_LEFT = ROW_PADDING_LEFT_480;
	private static int ROW_PADDING_TOP = ROW_PADDING_TOP_480;
	private static int ROW_PADDING_RIGHT = ROW_PADDING_RIGHT_480;
	private static int ROW_PADDING_BOTTOM = ROW_PADDING_BOTTOM_480;
	
	private static int ROW_TEXT_PADDING_LEFT = ROW_TEXT_PADDING_LEFT_480;
	private static int ROW_TEXT_PADDING_RIGHT = ROW_TEXT_PADDING_RIGHT_480;
	private static int ROW_TEXT_PADDING_TOP = ROW_TEXT_PADDING_TOP_480;
	private static int ROW_TEXT_PADDING_BOTTOM = ROW_TEXT_PADDING_BOTTOM_480;
	// for INDEX_NAME_CLICKABLE
	private static final int inc_INDEX_WIDTH_480 = 34;
	private static final int inc_NAME_WIDTH_480 = 320;
	private static final int inc_INDEX_MARGIN_LEFT_480 = 2;
	private static final int inc_NAME_MARGIN_LEFT_480 = 12;
	private static final int inc_ARRAY_MARGIN_LEFT_480 = 8;

	private static final int inc_INDEX_WIDTH_720 = 48;
	private static final int inc_NAME_WIDTH_720 = 520;
	private static final int inc_INDEX_MARGIN_LEFT_720 = 3;
	private static final int inc_NAME_MARGIN_LEFT_720 = 18;
	private static final int inc_ARRAY_MARGIN_LEFT_720 = 14;

	private static int inc_INDEX_WIDTH = inc_INDEX_WIDTH_480;
	private static int inc_NAME_WIDTH = inc_NAME_WIDTH_480;
	private static int inc_INDEX_MARGIN_LEFT = inc_INDEX_MARGIN_LEFT_480;
	private static int inc_NAME_MARGIN_LEFT = inc_NAME_MARGIN_LEFT_480;
	private static int inc_ARRAY_MARGIN_LEFT = inc_ARRAY_MARGIN_LEFT_480;

	// for NAME_C_VALUE
	private static final int ncv_NAME_WIDTH_480 = 210;
	private static final int ncv_NAME_PADDING_LEFT_480 = 19;
	private static final int ncv_C_WIDTH_480 = 20;

	private static final int ncv_NAME_WIDTH_720 = 310;
	private static final int ncv_NAME_PADDING_LEFT_720 = 29;
	private static final int ncv_C_WIDTH_720 = 30;

	private static int ncv_NAME_WIDTH = ncv_NAME_WIDTH_480;
	private static int ncv_NAME_PADDING_LEFT = ncv_NAME_PADDING_LEFT_480;
	private static int ncv_C_WIDTH = ncv_C_WIDTH_480;
	
	
	// for NAME_C_VALUE_ICON
	private static final int ncvi_NAME_WIDTH_480 = 120;
	private static final int ncvi_VALUE_WIDTH_480 = 220;

	private static final int ncvi_NAME_WIDTH_720 = 180;
	private static final int ncvi_VALUE_WIDTH_720 = 360;

	private static int ncvi_NAME_WIDTH = ncvi_NAME_WIDTH_480;
	private static int ncvi_VALUE_WIDTH = ncvi_VALUE_WIDTH_480;
	
	public WADetailRowView(Context context, View customView) {
		super(context);
		this.customView = customView;
		this.type = RowType.CUSTOMIZE;
		this.context = context;
		initial();
	}

	public WADetailRowView(Context context, RowType type) {
		super(context);
		this.type = type;
		this.context = context;
		initial();
	}

	private void initial() {
		int width = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		if (width >= 640) {
			ROW_HEIGHT = ROW_HEIGHT_720;

			TEXT_SIZE_NAME = TEXT_SIZE_NAME_720;
			TEXT_SIZE_VALUE = TEXT_SIZE_VALUE_720;
			TEXT_SIZE_COLON = TEXT_SIZE_COLON_720;
			TEXT_SIZE_TEXT = TEXT_SIZE_TEXT_720;

			ROW_PADDING_LEFT = ROW_PADDING_LEFT_720;
			ROW_PADDING_TOP = ROW_PADDING_TOP_720;
			ROW_PADDING_RIGHT = ROW_PADDING_RIGHT_720;
			ROW_PADDING_BOTTOM = ROW_PADDING_BOTTOM_720;

			inc_INDEX_WIDTH = inc_INDEX_WIDTH_720;
			inc_NAME_WIDTH = inc_NAME_WIDTH_720;
			inc_INDEX_MARGIN_LEFT = inc_INDEX_MARGIN_LEFT_720;
			inc_NAME_MARGIN_LEFT = inc_NAME_MARGIN_LEFT_720;
			inc_ARRAY_MARGIN_LEFT = inc_ARRAY_MARGIN_LEFT_720;

			ncv_NAME_WIDTH = ncv_NAME_WIDTH_720;
			ncv_NAME_PADDING_LEFT = ncv_NAME_PADDING_LEFT_720;
			ncv_C_WIDTH = ncv_C_WIDTH_720;

			ncvi_NAME_WIDTH = ncvi_NAME_WIDTH_720;
			ncvi_VALUE_WIDTH = ncvi_VALUE_WIDTH_720;
			
			ROW_TEXT_PADDING_LEFT = ROW_TEXT_PADDING_LEFT_720;
			ROW_TEXT_PADDING_RIGHT = ROW_TEXT_PADDING_RIGHT_720;
			ROW_TEXT_PADDING_TOP = ROW_TEXT_PADDING_TOP_720;
			ROW_TEXT_PADDING_BOTTOM = ROW_TEXT_PADDING_BOTTOM_720;
			CONTENT_TEXT_SIZE = CONTENT_TEXT_SIZE_720;
		}
		setOrientation(HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		if (type == RowType.TEXT || type == RowType.CUSTOMIZE) {
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			setLayoutParams(params);
			setPadding(ROW_PADDING_LEFT, ROW_PADDING_TOP, ROW_PADDING_RIGHT, ROW_PADDING_BOTTOM);
		} else {
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, ROW_HEIGHT);
			setLayoutParams(params);
			setPadding(ROW_PADDING_LEFT, 0, ROW_PADDING_RIGHT, 0);
		}
	}

	public RowType getRowType() {
		return this.type;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		confirm();
	}

	/**
	 * �?��值设置完毕后�?��通过此方法做确认
	 * 
	 * @param TEXT
	 *            use the 'value' string field
	 */
	private void confirm() {
		int colorGray = Color.rgb(100, 100, 100);
		switch (type) {
		case TEXT: {
			TextView textTextView = new TextView(context);
			textTextView.setGravity(Gravity.CENTER_VERTICAL);
			textTextView.setTextColor(colorGray);
			textTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, CONTENT_TEXT_SIZE);
			textTextView.setPadding(ROW_TEXT_PADDING_LEFT, ROW_TEXT_PADDING_TOP, ROW_TEXT_PADDING_RIGHT, ROW_TEXT_PADDING_BOTTOM);
			textTextView.setText(value);
			
			addView(textTextView);
		}
			break;
		case NAME_C_VALUE: {
			TextView nameTextView = new TextView(context);
			TextView valueTextView = new TextView(context);
			TextView cTextView = new TextView(context);
			cTextView.setTextSize(TEXT_SIZE_COLON);
			if (null == name || "".equalsIgnoreCase(name)) {
				cTextView.setText("   ");
			} else {
				cTextView.setText("");
			}
			cTextView.setWidth(ncv_C_WIDTH);
			cTextView.setTextColor(colorGray);

			nameTextView.setSingleLine();
			valueTextView.setSingleLine();
			nameTextView.setEllipsize(TruncateAt.END);
			valueTextView.setEllipsize(TruncateAt.END);

			nameTextView.setWidth(ncv_NAME_WIDTH);
			nameTextView.setPadding(ncv_NAME_PADDING_LEFT, 0, 0, 0);
			nameTextView.setGravity(Gravity.RIGHT);
			nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, TEXT_SIZE_NAME);
			nameTextView.setText(name);
			nameTextView.setSingleLine();
			nameTextView.setTextColor(colorGray);

			valueTextView.setGravity(Gravity.LEFT);
			valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, TEXT_SIZE_VALUE);
			valueTextView.setText(value);
			addView(nameTextView);
			addView(cTextView);
			addView(valueTextView);
		}
			break;
		case NAME_C_VALUE_ICON_DES_CLICKABLE:
			break;
		case NAME_C_VALUE_ICON:
		case NAME_C_VALUE_ICON_MOBILE:
		case NAME_C_VALUE_ICON_TEL:
		case NAME_C_VALUE_ICON_MAIL: 
			{
				TextView nameTextView = new TextView(context);
				TextView valueTextView = new TextView(context);
				TextView cTextView = new TextView(context);
				iconView = new ImageView(context);
				cTextView.setTextColor(colorGray);
				cTextView.setText(" : ");
	
				nameTextView.setSingleLine();
				valueTextView.setSingleLine();
				nameTextView.setEllipsize(TruncateAt.END);
				valueTextView.setEllipsize(TruncateAt.END);
	
				nameTextView.setWidth(ncvi_NAME_WIDTH);
				nameTextView.setGravity(Gravity.RIGHT);
				nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, TEXT_SIZE_NAME);
				nameTextView.setText(name);
				nameTextView.setSingleLine();
				nameTextView.setTextColor(colorGray);
	
				valueTextView.setWidth(ncvi_VALUE_WIDTH);
				valueTextView.setGravity(Gravity.LEFT);
				valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, TEXT_SIZE_VALUE);
				valueTextView.setText(value);
	
				{
					LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.setMargins(inc_ARRAY_MARGIN_LEFT, 0, 0, 0);
					iconView.setLayoutParams(params);
					
					switch(type){
					case NAME_C_VALUE_ICON_MOBILE:
						((ImageView) iconView).setImageResource(R.drawable.wadetail_row_mobile);
						if(!"".equals(value)) 
							setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									System.out.println(" mobile icon");
									OnIconClickedActions.onMobileIconClicked(context, value);
								}
							});
						break;
					case NAME_C_VALUE_ICON_TEL:
						((ImageView) iconView).setImageResource(R.drawable.wadetail_row_tel);
						if(!"".equals(value)) 
							setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									System.out.println(" tel icon");
									OnIconClickedActions.onTelIconClicked(context, value);
								}
							});
						break;
					case NAME_C_VALUE_ICON_MAIL:
						((ImageView) iconView).setImageResource(R.drawable.wadetail_row_email);
						if(!"".equals(value)) 
							setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									System.out.println(" mail icon");
									OnIconClickedActions.onMailIconClicked(context, value);
								}
							});
						break;
					default:
						break;
					}
				}
	
				addView(nameTextView);
				addView(cTextView);
				addView(valueTextView);
				addView(iconView);
			}
			break;
		case INDEX_NAME_CLICKABLE: {
			TextView indexTextView = new TextView(context);
			TextView nameTextView = new TextView(context);
			iconView = new ImageView(context);
			{
				LayoutParams params = new LayoutParams(inc_INDEX_WIDTH, LayoutParams.WRAP_CONTENT);
				params.setMargins(inc_INDEX_MARGIN_LEFT, 0, 0, 0);
				indexTextView.setLayoutParams(params);
				indexTextView.setGravity(Gravity.RIGHT | Gravity.CENTER_HORIZONTAL);
				indexTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, TEXT_SIZE_NAME);
				indexTextView.setText(index);
			}
			{
				LayoutParams params = new LayoutParams(inc_NAME_WIDTH, LayoutParams.WRAP_CONTENT);
				params.setMargins(inc_NAME_MARGIN_LEFT, 0, 0, 0);
				nameTextView.setSingleLine();
				nameTextView.setEllipsize(TruncateAt.END);
				nameTextView.setLayoutParams(params);
				nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, TEXT_SIZE_NAME);
				nameTextView.setText(name);
			}

			{
				LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(inc_ARRAY_MARGIN_LEFT, 0, 0, 0);
				iconView.setLayoutParams(params);
				((ImageView) iconView).setImageResource(R.drawable.wadetail_row_arrow);
			}

			addView(indexTextView);
			addView(nameTextView);
			addView(iconView);
		}
			break;
		case NAME_C_ICON:
			break;
		case CUSTOMIZE:
			addView(customView);
			break;
		default:
			break;
		}
	}

	/**
	 * 设置图标点击事件 Should be set -after- setIconResID();
	 */
	public void setOnIconClickListener(View.OnClickListener onIconClickListener) {
		Assert.assertTrue(iconResID != -1);
	}

	public void setOnRowClickListener(View.OnClickListener onRowClickListener) {
		setOnClickListener(onRowClickListener);
	}

	public View getCustomView() {
		return customView;
	}

	public void setCustomView(View customView) {
		this.customView = customView;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getIconResID() {
		return iconResID;
	}

	public void setIconResID(int iconResID) {
		this.iconResID = iconResID;
		if (iconResID > 0) {
			iconView = new View(context);
			iconView.setBackgroundResource(iconResID);
		}
	}

	public String getIconDes() {
		return iconDes;
	}

	public void setIconDes(String iconDes) {
		this.iconDes = iconDes;
	}
}
