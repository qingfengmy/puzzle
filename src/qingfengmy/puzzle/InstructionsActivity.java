package qingfengmy.puzzle;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

import com.qingfengmy.childpuzzle.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InstructionsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_instructions);

		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// 获取要嵌入广告条的布局
		LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
		// 将广告条加入到布局中
		adLayout.addView(adView);

		TextView github = (TextView) findViewById(R.id.github);
		String str = "https://github.com/qingfengmy";
		// 生成SpannableString对象（Spannable的意思是可扩展的）
		SpannableString spannableString = new SpannableString(str);
		// SpannableString可设置多种状态样式，这里是点击事件
		spannableString.setSpan(new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				Intent intent = new Intent();
				intent.setData(Uri.parse("https://github.com/qingfengmy"));
				intent.setAction(Intent.ACTION_VIEW);
				startActivity(intent);
			}
		}, 0, str.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		github.setText(spannableString);
		// 这个必须有，否则点击无效
		github.setMovementMethod(LinkMovementMethod.getInstance());

	}

}
