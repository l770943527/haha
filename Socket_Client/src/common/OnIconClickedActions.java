package common;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class OnIconClickedActions {
	public static void onMobileIconClicked(final Context context, final String value) {
		new AlertDialog.Builder(context)  
		.setTitle(value)  
		.setItems(new String[] {"拨打手机","发送短信"}, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:" + value));
					context.startActivity(intent);
				} else if (which == 1) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_SENDTO);
					intent.setData(Uri.parse("smsto:" + value));
					context.startActivity(intent);
				}
			}
		})
		.show();
	}
	
	public static void onTelIconClicked(final Context context, final String value) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + value));
		context.startActivity(intent);
	}
	
	public static void onMailIconClicked(final Context context, final String value) {
		Uri uri = Uri.parse("mailto:" + value);   
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);  
		context.startActivity(Intent.createChooser(intent, "请选择一个邮件应用"));  
	}
}
