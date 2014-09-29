package qingfengmy.puzzle;

import com.qingfengmy.childpuzzle.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PrefHelp {

	public static void setFirst(Context context, boolean isContinue) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putBoolean("first", isContinue);
		editor.commit();
	}
	
	public static boolean getFirst(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getBoolean("first", true);
	}
	public static void setContinue(Context context, boolean isContinue) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putBoolean("isContinue", isContinue);
		editor.commit();
	}

	public static boolean getContinue(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getBoolean("isContinue", false);
	}

	public static void setStyle(Context context, int style) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString("style", style+"");
		editor.commit();
		
		setContinue(context, false);
	}

	public static int getStyle(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		String style = sp.getString("style", "0");
		return Integer.parseInt(style) + 3;
	}

	public static void setImageId(Context context, int imgId) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putInt("imageId", imgId);
		editor.commit();
	}

	public static int getImageId(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getInt("imageId", -1);
	}

	public static void setImg(Context context, int img) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putInt("img", img);
		editor.commit();
	}
	
	public static int getImg(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getInt("img", R.drawable.bg0);
	}
	public static void setSteps(Context context, int steps) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putInt("steps", steps);
		editor.commit();
	}

	public static int getSteps(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getInt("steps", -1);
	}

	public static void setSeconds(Context context, long seconds) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putFloat("seconds", seconds);
		editor.commit();
	}

	public static long getSeconds(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return (long) sp.getFloat("seconds", -1);
	}

	public static void setNumArrays(Context context, String numArrays) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString("numArrays", numArrays);
		editor.commit();
	}

	public static String getNumArrays(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString("numArrays", "");
	}
}
