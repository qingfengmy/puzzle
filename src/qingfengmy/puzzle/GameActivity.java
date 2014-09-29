package qingfengmy.puzzle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.spot.SpotManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qingfengmy.childpuzzle.R;

import qingfengmy.puzzle.CustomDialog.DialogCallBack;
import qingfengmy.puzzle.GameView.stepInter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends BaseActivity {

	private long seconds;
	private long temp;
	private TextView time;
	private TextView steps;
	private int stepNum;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
			String date = sdf.format(new Date(seconds + addSeconds));
			time.setText("时间：" + date);

		};
	};

	private int imgId;
	private GameView gameView;
	private long addSeconds;
	private int addSteps;
	private int[] mArrays;
	private ImageView imgsrc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		SpotManager.getInstance(this).loadSpotAds();

		gameView = (GameView) findViewById(R.id.img);
		time = (TextView) findViewById(R.id.times);
		steps = (TextView) findViewById(R.id.steps);
		imgsrc = (ImageView) findViewById(R.id.imgsrc);

		temp = System.currentTimeMillis();

		String from = getIntent().getStringExtra("from");
		if (from != null && from.equals("main")) {
			String arrays = PrefHelp.getNumArrays(this);
			addSeconds = PrefHelp.getSeconds(this);
			addSteps = PrefHelp.getSteps(this);
			stepNum = addSteps;
			steps.setText("步数： " + stepNum);

			JSONArray arr;
			try {
				arr = new JSONArray(arrays);
				mArrays = new int[arr.length()];
				for (int i = 0; i < arr.length(); i++) {
					mArrays[i] = (Integer) arr.get(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			imgId = PrefHelp.getImageId(this);
			if (imgId == -1) {
				// 上一次是保存的图片
				gameView.initBitmap();
				Bitmap map = ImageTools.getPhotoFromSDCard(Constants.path,
						Constants.img_name);
				imgsrc.setImageBitmap(map);
			} else {
				// 上一次是美女图片
				gameView.initArray(imgId, mArrays);
				imgsrc.setImageResource(imgId);
			}

		} else if (from != null && from.equals("crop")) {
			imgId = PrefHelp.getImageId(this);
			addSteps = 0;
			addSeconds = 0;

			Bitmap map = ImageTools.getPhotoFromSDCard(Constants.path,
					Constants.img_name);
			imgsrc.setImageBitmap(map);
			gameView.initBitmap();
		} else {
			addSteps = 0;
			addSeconds = 0;
			imgId = getIntent().getIntExtra("img", 1);
			gameView.init(imgId);

			imgsrc.setImageResource(imgId);
		}
		gameView.setStepInter(new stepInter() {

			@Override
			public void addStep() {
				stepNum++;
				steps.setText("步数： " + stepNum);
			}

			@Override
			public void gameWin() {
				final CustomDialog dialog = new CustomDialog(GameActivity.this);
				dialog.setCallBack(new DialogCallBack() {

					@Override
					public void yesCallBack() {
						gameView.init(imgId);
						gameView.invalidate();
						temp = System.currentTimeMillis();
						stepNum = 0;
						steps.setText("步数： " + stepNum);
						dialog.dismiss();
						PrefHelp.setContinue(GameActivity.this, false);
					}

					@Override
					public void noCallBack() {
						PrefHelp.setContinue(GameActivity.this, false);
						finish();
					}
				});

				dialog.show();

				SpotManager.getInstance(GameActivity.this).showSpotAds(
						GameActivity.this);
			}
		});
		Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(
				new Runnable() {

					@Override
					public void run() {
						seconds = System.currentTimeMillis() - temp;
						handler.sendEmptyMessage(0);
					}
				}, 1, 1, TimeUnit.SECONDS);

		SpotManager.getInstance(GameActivity.this).showSpotAds(
				GameActivity.this);
		
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// 获取要嵌入广告条的布局
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		// 将广告条加入到布局中
		adLayout.addView(adView);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			PrefHelp.setContinue(this, true);
			PrefHelp.setImageId(this, imgId);
			PrefHelp.setNumArrays(this, gameView.getArrays());
			PrefHelp.setSeconds(this, seconds + addSeconds);
			PrefHelp.setSteps(this, stepNum);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		SpotManager.getInstance(this).unregisterSceenReceiver();
		super.onDestroy();
	}
}
