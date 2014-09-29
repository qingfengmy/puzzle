package qingfengmy.puzzle;

import java.util.Random;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class GameView extends View {

	private Bitmap map;
	private Rect[] mapRect;
	private Rect[] dstRect;
	private int[] mArraw;
	private int line_width;
	private Context context;
	private int nullFlag;
	private stepInter sInter;
	private int block_sum;

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
	}

	public GameView(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		setMeasuredDimension(widthSize, widthSize);
	}

	public void initBitmap() {
		Constants.lines = PrefHelp.getStyle(context);
		block_sum = Constants.lines * Constants.lines;
		mapRect = new Rect[block_sum];
		dstRect = new Rect[block_sum];
		mArraw = new int[block_sum];

		map = ImageTools.getPhotoFromSDCard(Constants.path, Constants.img_name);
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		int width = dm.widthPixels;// 宽度
		line_width = width / Constants.lines;

		map = resizeImage(map, width, width);

		for (int i = 0, n = 0; i < Constants.lines; i++) {
			for (int j = 0; j < Constants.lines; j++) {
				mapRect[n] = new Rect(j * line_width, i * line_width,
						line_width * (j + 1), line_width * (i + 1));
				dstRect[n] = new Rect(j * line_width, i * line_width,
						line_width * (j + 1), line_width * (i + 1));
				n++;
			}
		}

		mArraw = getSequence(block_sum);
	}

	public void initArray(int imgId, int[] arrays) {
		Constants.lines = PrefHelp.getStyle(context);
		block_sum = Constants.lines * Constants.lines;
		mapRect = new Rect[block_sum];
		dstRect = new Rect[block_sum];
		mArraw = new int[block_sum];

		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		int width = dm.widthPixels;// 宽度
		line_width = width / Constants.lines;

		map = BitmapFactory.decodeResource(context.getResources(), imgId);

		map = resizeImage(map, width, width);

		for (int i = 0, n = 0; i < Constants.lines; i++) {
			for (int j = 0; j < Constants.lines; j++) {
				mapRect[n] = new Rect(j * line_width, i * line_width,
						line_width * (j + 1), line_width * (i + 1));
				dstRect[n] = new Rect(j * line_width, i * line_width,
						line_width * (j + 1), line_width * (i + 1));
				n++;
			}
		}

		mArraw = arrays;
	}

	public void init(int id) {
		Constants.lines = PrefHelp.getStyle(context);
		block_sum = Constants.lines * Constants.lines;
		mapRect = new Rect[block_sum];
		dstRect = new Rect[block_sum];
		mArraw = new int[block_sum];

		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		int width = dm.widthPixels;// 宽度
		line_width = width / Constants.lines;

		map = BitmapFactory.decodeResource(context.getResources(), id);

		map = resizeImage(map, width, width);

		for (int i = 0, n = 0; i < Constants.lines; i++) {
			for (int j = 0; j < Constants.lines; j++) {
				mapRect[n] = new Rect(j * line_width, i * line_width,
						line_width * (j + 1), line_width * (i + 1));
				dstRect[n] = new Rect(j * line_width, i * line_width,
						line_width * (j + 1), line_width * (i + 1));
				n++;
			}
		}

		mArraw = getSequence(block_sum);

	}

	public void setStepInter(stepInter sInter) {
		this.sInter = sInter;
	}

	public String getArrays() {
		JSONArray numbers = new JSONArray();
		for (int number : mArraw) {
			numbers.put(number);
		}

		return numbers.toString();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		Rect rect = null;

		paint.setColor(Color.GREEN);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(5);

		for (int i = 0; i < block_sum; i++) {
			if (mArraw[i] == block_sum - 1) {
				nullFlag = i;
			} else {
				canvas.drawBitmap(map, mapRect[mArraw[i]], dstRect[i], null);
			}
			rect = new Rect(i % Constants.lines * line_width, i
					/ Constants.lines * line_width, i % Constants.lines
					* line_width + line_width, i / Constants.lines * line_width
					+ line_width);
			canvas.drawRect(rect, paint);
		}

		if (checkWin()) {
			sInter.gameWin();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			float x2 = event.getX();
			float y2 = event.getY();
			if (onLeft(x2, y2)) {
				int temp = mArraw[nullFlag];
				mArraw[nullFlag] = mArraw[nullFlag - 1];
				mArraw[nullFlag - 1] = temp;
				nullFlag--;
				invalidate();
				sInter.addStep();
			} else if (onRight(x2, y2)) {
				int temp = mArraw[nullFlag];
				mArraw[nullFlag] = mArraw[nullFlag + 1];
				mArraw[nullFlag + 1] = temp;
				nullFlag++;
				invalidate();
				sInter.addStep();
			} else if (onTop(x2, y2)) {
				int temp = mArraw[nullFlag];
				mArraw[nullFlag] = mArraw[nullFlag - Constants.lines];
				mArraw[nullFlag - Constants.lines] = temp;
				nullFlag -= Constants.lines;
				invalidate();
				sInter.addStep();
			} else if (onBottom(x2, y2)) {
				int temp = mArraw[nullFlag];
				mArraw[nullFlag] = mArraw[nullFlag + Constants.lines];
				mArraw[nullFlag + Constants.lines] = temp;
				nullFlag += Constants.lines;
				invalidate();
				sInter.addStep();
			}

			break;

		default:
			break;
		}
		return true;
	}

	private boolean onBottom(float x2, float y2) {
		int row = nullFlag / Constants.lines;
		int col = nullFlag % Constants.lines;
		int xx1 = col * line_width;
		int xx2 = (col + 1) * line_width;
		int yy1 = (row + 1) * line_width;
		int yy2 = (row + 2) * line_width;
		if (x2 >= xx1 && x2 < xx2 && y2 > yy1 && y2 < yy2) {
			return true;
		}
		return false;
	}

	private boolean onTop(float x2, float y2) {
		int row = nullFlag / Constants.lines;
		int col = nullFlag % Constants.lines;
		int xx1 = col * line_width;
		int xx2 = (col + 1) * line_width;
		int yy1 = (row - 1) * line_width;
		int yy2 = row * line_width;
		if (x2 >= xx1 && x2 < xx2 && y2 > yy1 && y2 < yy2) {
			return true;
		}
		return false;
	}

	private boolean onRight(float x2, float y2) {
		int row = nullFlag / Constants.lines;
		int col = nullFlag % Constants.lines;
		int xx1 = (col + 1) * line_width;
		int xx2 = (col + 2) * line_width;
		int yy1 = row * line_width;
		int yy2 = (row + 1) * line_width;
		if (x2 >= xx1 && x2 < xx2 && y2 > yy1 && y2 < yy2) {
			return true;
		}
		return false;
	}

	private boolean onLeft(float x2, float y2) {
		int row = nullFlag / Constants.lines;
		int col = nullFlag % Constants.lines;
		int xx1 = (col - 1) * line_width;
		int xx2 = col * line_width;
		int yy1 = row * line_width;
		int yy2 = (row + 1) * line_width;
		if (x2 >= xx1 && x2 < xx2 && y2 > yy1 && y2 < yy2) {
			return true;
		}
		return false;
	}

	private boolean checkWin() {
		for (int i = 0; i < block_sum; i++) {
			if (mArraw[i] != i)
				return false;
		}
		return true;
	}

	/**
	 * 对给定数目的自0开始步长为1的数字序列进行乱序
	 * 
	 * @param no
	 *            给定数目
	 * @return 乱序后的数组
	 */
	public int[] getSequence(int no) {
		int[] sequence = new int[no];
		for (int i = 0; i < no; i++) {
			sequence[i] = i;
		}
		Random random = new Random();
		for (int i = 0; i < no; i++) {
			int p = random.nextInt(no);
			int tmp = sequence[i];
			sequence[i] = sequence[p];
			sequence[p] = tmp;
		}
		random = null;
		return sequence;
	}

	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int newWidth = w;
		int newHeight = h;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();

		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return resizedBitmap;
	}

	public interface stepInter {
		void addStep();

		void gameWin();
	}
}
