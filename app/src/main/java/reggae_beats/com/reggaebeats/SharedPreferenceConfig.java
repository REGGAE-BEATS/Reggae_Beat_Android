package reggae_beats.com.reggaebeats;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ron on 18/08/2017.
 */

public class SharedPreferenceConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferenceConfig(Context ctx) {
        this.context = ctx;
        this.sharedPreferences = getSharedpreferences();
    }

    public SharedPreferences getSharedpreferences() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(context.getResources().getString(R.string.SHARED_PREF_CONST), MODE_PRIVATE);
        return sharedPrefs;
    }

    public boolean checkSharedPreferences(String key) {
        if (sharedPreferences.getString(key, "") != null) {
            return true;
        } else {


        }

        return false;
    }

    public void addToSharedPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String findValue(String key) {
        String value = sharedPreferences.getString(key, "");
        return value;
    }


    public void deleteValue(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }
}

