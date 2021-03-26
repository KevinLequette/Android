package fr.univartois.newsreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.webkit.URLUtil;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment(findViewById(R.id.isValid)))
                    .commit();

        }
        // back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }





    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        private TextView isValid;

        public SettingsFragment(TextView isValid) {
            this.isValid = isValid;
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);


        }
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
            String url = sharedPreferences.getString(key, "");
           // Url valide ou non
            if(URLUtil.isValidUrl(url)){
               isValid.setText("URL Valide");
               isValid.setTextColor(Color.parseColor("#6B8E23"));
            }
            else {
                isValid.setText("URL Invalide");
                isValid.setTextColor(Color.parseColor("#FF0000"));
            }
        }
    }
}