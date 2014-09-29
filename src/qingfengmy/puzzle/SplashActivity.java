package qingfengmy.puzzle;

import com.qingfengmy.childpuzzle.R;

import net.youmi.android.AdManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

public class SplashActivity extends BaseActivity {

	private TextView tv_splash_version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_splash_version.setText("版本：" + getAppVersion());
		new Thread() {
			public void run() {
				SystemClock.sleep(2000);
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				
				if(PrefHelp.getFirst(SplashActivity.this)){
					// 是第一次
					PrefHelp.setFirst(SplashActivity.this, false);
					PrefHelp.setStyle(SplashActivity.this, 0);
				}
			}
		}.start();
		// 初始化应用的发布 ID 和密钥，以及设置测试模式
        AdManager.getInstance(this).init("e21f1d0cfa416437", "15429faff7b01959", false);
	}

	/**
	 * @return 从清单文件中获取的版本号。
	 */
	private String getAppVersion() {
		PackageManager pm = getPackageManager();
		try {
			return pm.getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
}
