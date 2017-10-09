package dct.com.everyfoody.base.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Iterator;
import java.util.Map;

public class SharedPreferencesService {
    // 공유명칭
    private static final String SHARED_PREFS_CONFIGURATION = "DreamComeTruck_EveryFoody";

    private volatile static SharedPreferencesService sharedPreferencesManager;
    private SharedPreferences pref;

    public static SharedPreferencesService getInstance() {
        if (sharedPreferencesManager == null) {
            synchronized (SharedPreferencesService.class) {
                if (sharedPreferencesManager == null)
                    sharedPreferencesManager = new SharedPreferencesService();
            }
        }
        return sharedPreferencesManager;
    }

    /**
     * 공유설정 정보를 취득한다.
     */
    private void getPref(Context cont) {
        if (pref == null) {
            pref = cont.getSharedPreferences(SHARED_PREFS_CONFIGURATION, Context.MODE_PRIVATE);
        }
    }

    public void load(Context context) {
        getPref(context);
    }


    public long getPrefLongData(String key) {
        return getPrefLongData(key, 0);
    }

    public long getPrefLongData(String key, long defValue) {
        return pref.getLong(key, defValue);
    }

    public int getPrefIntegerData(String key) {
        return getPrefIntegerData(key, 0);
    }

    public int getPrefIntegerData(String key, int defValue) {
        return pref.getInt(key, defValue);
    }

    public float getPrefFloatData(String key) {
        return getPrefFloatData(key, 0.f);
    }

    public float getPrefFloatData(String key, float defValue) {
        return pref.getFloat(key, defValue);
    }


    public String getPrefStringData(String key, String defValue) {
        return pref.getString(key, defValue);
    }

    public String getPrefStringData(String key) {
        return getPrefStringData(key, "");
    }

    public boolean getPrefBooleanData(String key, boolean defValue) {
        return pref.getBoolean(key, defValue);
    }


    public boolean getPrefBooleanData(String key) {
        return getPrefBooleanData(key, false);
    }

    public void setPrefData(String key, boolean value) {
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }

    public void setPrefData(String key, int value) {
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);
        editor.commit();
    }

    public void setPrefData(String key, float value) {
        SharedPreferences.Editor editor = pref.edit();

        editor.putFloat(key, value);
        editor.commit();
    }

    public void setPrefData(String key, long value) {
        SharedPreferences.Editor editor = pref.edit();

        editor.putLong(key, value);
        editor.commit();
    }

    public void setPrefData(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }



    public void setPrefDatas(Map<String, Object> values) {
        SharedPreferences.Editor editor = pref.edit();

        Iterator<String> keyLists = values.keySet().iterator();

        while (keyLists.hasNext()) {
            String key = keyLists.next();
            Object value = values.get(key);

            if (value instanceof String) {
                editor.putString(key, (String) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
            }
        }

        editor.commit();
    }

    public void removeData(String... keys) {
        SharedPreferences.Editor editor = pref.edit();

        for (String key : keys) {
            editor.remove(key);
        }

        editor.commit();

    }
}
