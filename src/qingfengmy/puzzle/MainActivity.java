package qingfengmy.puzzle;

import java.io.File;

import com.qingfengmy.childpuzzle.R;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.diy.DiyManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnClickListener {

	private static final int TAKE_PICTURE = 0;
	private static final int CHOOSE_PICTURE = 1;
	private static final int CROP = 2;
	private static final int CROP_PICTURE = 3;

	private static final int SCALE = 5;// 照片缩小比例

	private Button newGame;
	private Button continueGame;
	private Button choose;
	private Dialog dialog;
	private RelativeLayout layout;
	private long exitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		layout = (RelativeLayout) findViewById(R.id.container);
		layout.setBackgroundResource(PrefHelp.getImg(this));

		newGame = (Button) findViewById(R.id.game);
		newGame.setOnClickListener(this);

		continueGame = (Button) findViewById(R.id.continue_button);
		continueGame.setOnClickListener(this);

		choose = (Button) findViewById(R.id.choose);
		choose.setOnClickListener(this);

		findViewById(R.id.instructions).setOnClickListener(this);

		findViewById(R.id.settngs).setOnClickListener(this);

		findViewById(R.id.exit_button).setOnClickListener(this);
		findViewById(R.id.more).setOnClickListener(this);

		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// 获取要嵌入广告条的布局
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		// 将广告条加入到布局中
		adLayout.addView(adView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.game:
			intent.setClass(this, GalleryActivity.class);
			startActivityForResult(intent, 123);
			break;
		case R.id.continue_button:
			if (PrefHelp.getContinue(this)) {
				intent.setClass(this, GameActivity.class);
				intent.putExtra("from", "main");
				startActivityForResult(intent, 123);
			} else {
				Toast.makeText(this, "您还没有玩过我们的游戏，请点击开始游戏按钮", 0).show();
			}
			break;
		case R.id.choose:
			showPicturePicker(this, true);
			break;
		case R.id.instructions:
			intent.setClass(this, InstructionsActivity.class);
			startActivityForResult(intent, 123);
			break;
		case R.id.settngs:
			// showSignleSelectDialog();
			intent.setClass(this, SettingActivity.class);
			startActivityForResult(intent, 123);
			break;
		case R.id.exit_button:
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			break;
		case R.id.more:
			DiyManager.showRecommendWall(this);
			break;
		default:
			break;
		}
	}

	private void showSignleSelectDialog() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("选择游戏类别");
		final ChoiceOnClickListener choiceListener = new ChoiceOnClickListener();
		int style = PrefHelp.getStyle(this);
		builder.setSingleChoiceItems(R.array.leibie, style - 3, choiceListener);
		dialog = builder.create();
		dialog.show();
	}

	private class ChoiceOnClickListener implements
			DialogInterface.OnClickListener {

		private int which = 0;

		@Override
		public void onClick(DialogInterface dialogInterface, int which) {
			this.which = which;
			dialog.dismiss();
			PrefHelp.setStyle(MainActivity.this, which + 3);
			Constants.lines = which + 3;
			PrefHelp.setContinue(MainActivity.this, false);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		layout.setBackgroundResource(PrefHelp.getImg(this));

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CROP:
				Uri uri = null;
				if (data != null) {
					uri = data.getData();
					System.out.println("Data");
				} else {
					System.out.println("File");
					String fileName = getSharedPreferences("temp",
							Context.MODE_WORLD_WRITEABLE).getString("tempName",
							"");
					uri = Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), fileName));
				}
				cropImage(uri, 500, 500, CROP_PICTURE);
				break;

			case CROP_PICTURE:
				Bitmap photo = null;
				Uri photoUri = data.getData();
				if (photoUri != null) {
					photo = BitmapFactory.decodeFile(photoUri.getPath());
				}
				if (photo == null) {
					Bundle extra = data.getExtras();
					if (extra != null) {
						photo = (Bitmap) extra.get("data");

						ImageTools.savePhotoToSDCard(photo, Constants.path,
								Constants.img_name);
						Intent intent = new Intent(this, GameActivity.class);
						intent.putExtra("from", "crop");
						PrefHelp.setImageId(this, -1);
						startActivity(intent);
					}
				}
				break;
			default:
				break;
			}
		}
	}

	public void showPicturePicker(Context context, boolean isCrop) {
		final boolean crop = isCrop;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("图片来源");
		builder.setNegativeButton("取消", null);
		builder.setItems(new String[] { "拍照", "相册" },
				new DialogInterface.OnClickListener() {
					// 类型码
					int REQUEST_CODE;

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case TAKE_PICTURE:
							Uri imageUri = null;
							String fileName = null;
							Intent openCameraIntent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							if (crop) {
								REQUEST_CODE = CROP;
								// 删除上一次截图的临时文件
								SharedPreferences sharedPreferences = getSharedPreferences(
										"temp", Context.MODE_WORLD_WRITEABLE);
								ImageTools.deletePhotoAtPathAndName(Environment
										.getExternalStorageDirectory()
										.getAbsolutePath(), sharedPreferences
										.getString("tempName", ""));

								// 保存本次截图临时文件名字
								fileName = String.valueOf(System
										.currentTimeMillis()) + ".jpg";
								Editor editor = sharedPreferences.edit();
								editor.putString("tempName", fileName);
								editor.commit();
							} else {
								REQUEST_CODE = TAKE_PICTURE;
								fileName = "image.jpg";
							}
							imageUri = Uri.fromFile(new File(Environment
									.getExternalStorageDirectory(), fileName));
							// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
							openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
									imageUri);
							startActivityForResult(openCameraIntent,
									REQUEST_CODE);
							break;

						case CHOOSE_PICTURE:
							Intent openAlbumIntent = new Intent(
									Intent.ACTION_GET_CONTENT);
							if (crop) {
								REQUEST_CODE = CROP;
							} else {
								REQUEST_CODE = CHOOSE_PICTURE;
							}
							openAlbumIntent
									.setDataAndType(
											MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
											"image/*");
							startActivityForResult(openAlbumIntent,
									REQUEST_CODE);
							break;

						default:
							break;
						}
					}
				});
		builder.create().show();
	}

	// 截取图片
	public void cropImage(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, requestCode);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
