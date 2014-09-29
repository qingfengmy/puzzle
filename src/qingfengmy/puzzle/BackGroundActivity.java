package qingfengmy.puzzle;

import com.qingfengmy.childpuzzle.R;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

public class BackGroundActivity extends BaseActivity {

	private ListView listview;
	private int[] array = { R.drawable.bg0, R.drawable.bg1, R.drawable.bg2, R.drawable.bg3,
			R.drawable.bg4 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_background);

		listview = (ListView) findViewById(R.id.listview);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PrefHelp.setImg(BackGroundActivity.this, array[position]);
				Toast.makeText(BackGroundActivity.this, "设置成功", 0).show();
				finish();
			}
		});
		listview.setAdapter(new BackAdapter());

		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// 获取要嵌入广告条的布局
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		// 将广告条加入到布局中
		adLayout.addView(adView);
	}

	private class BackAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return array.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(BackGroundActivity.this,
					R.layout.item_background, null);
			ImageView img = (ImageView) view.findViewById(R.id.img);
			img.setImageResource(array[position]);
			return view;
		}

	}
}
