package qingfengmy.puzzle;

import com.qingfengmy.childpuzzle.R;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GalleryActivity extends BaseActivity {
	private static int TOTAL_COUNT = 12;

	private RelativeLayout viewPagerContainer;
	private ViewPager viewPager;
	private TextView indexText;
	private Button ok;
	private int position;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);

		RelativeLayout layout = (RelativeLayout) findViewById(R.id.container);
		layout.setBackgroundResource(PrefHelp.getImg(this));

		viewPager = (ViewPager) findViewById(R.id.view_pager);
		indexText = (TextView) findViewById(R.id.view_pager_index);
		viewPagerContainer = (RelativeLayout) findViewById(R.id.pager_layout);
		viewPager.setAdapter(new MyPagerAdapter());
		// to cache all page, or we will see the right item delayed
		viewPager.setOffscreenPageLimit(TOTAL_COUNT);
		viewPager.setPageMargin(getResources().getDimensionPixelSize(
				R.dimen.page_margin));
		MyOnPageChangeListener myOnPageChangeListener = new MyOnPageChangeListener();
		viewPager.setOnPageChangeListener(myOnPageChangeListener);

		viewPagerContainer.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return viewPager.dispatchTouchEvent(event);
			}
		});
		indexText.setText(new StringBuilder().append("1/").append(TOTAL_COUNT));

		ok = (Button) findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GalleryActivity.this,
						GameActivity.class);
				intent.putExtra("img", R.drawable.image0 + position);
				Log.i("zt", "position=" + position);
				startActivity(intent);
				finish();
			}
		});

		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// 获取要嵌入广告条的布局
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		// 将广告条加入到布局中
		adLayout.addView(adView);
	}

	/**
	 * this is a example fragment, just a imageview, u can replace it with your
	 * needs
	 * 
	 * @author Trinea 2013-04-03
	 */
	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return TOTAL_COUNT;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(GalleryActivity.this);
			imageView.setImageResource(R.drawable.image0 + position);
			((ViewPager) container).addView(imageView, position);
			return imageView;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
			GalleryActivity.this.position = position;
			indexText.setText(new StringBuilder().append(position + 1)
					.append("/").append(TOTAL_COUNT));
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// to refresh frameLayout
			if (viewPagerContainer != null) {
				viewPagerContainer.invalidate();
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
}
