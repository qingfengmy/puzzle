package qingfengmy.puzzle;

import com.qingfengmy.childpuzzle.R;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SettingActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(1);
		setRequestedOrientation(1);
		setTheme(R.style.perference_set_activity);
		super.onCreate(savedInstanceState);
		// 所的的值将会自动保存到 SharePreferences
		addPreferencesFromResource(R.xml.setting);

		findPreference("background").setOnPreferenceClickListener(
				new android.preference.Preference.OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						Intent intent = new Intent(SettingActivity.this,
								BackGroundActivity.class);
						startActivity(intent);
						return false;
					}
				});
	}

}
