package qingfengmy.puzzle;

import com.qingfengmy.childpuzzle.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog extends AlertDialog {

	private TextView agin;
	private TextView back;
	private Context context;
	private DialogCallBack callBack;

	public void setCallBack(DialogCallBack callBack) {
		this.callBack = callBack;
	}

	public CustomDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_custom);
		initView();
		setPosition();
	}

	private void initView() {
		agin = (TextView) findViewById(R.id.agin);
		back = (TextView) findViewById(R.id.back);
		agin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				callBack.yesCallBack();
			}
			
		});
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callBack.noCallBack();
			}
		});
	}

	private void setPosition() {
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();

		float density = getDensity(context);
		params.width = (int) (250 * density);
		params.height = (int) (150 * density);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
	}

	private float getDensity(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return dm.density;
	}

	public interface DialogCallBack {
		public void yesCallBack();
		public void noCallBack();
	}
}
