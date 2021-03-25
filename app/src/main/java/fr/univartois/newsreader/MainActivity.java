package fr.univartois.newsreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.util.Patterns;
import android.util.Xml;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton news_btn = findViewById(R.id.news_btn);
        news_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Loading news ...", Snackbar.LENGTH_LONG).show();
                Downloader downloader = new Downloader();
                downloader.start();
            }
        });

        ListView news_list = findViewById(R.id.news_list);
        news_list.setOnItemClickListener(((parent, view, position, id) -> {
            RssItem rssItem = (RssItem) parent.getItemAtPosition(position);
            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(rssItem.link));
            startActivity(browser);
        }));
    }

    class Downloader extends Thread{
        @Override
        public void run() {
            List<RssItem> news = new ArrayList<>();
            try {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String url = preferences.getString("url", "https://www.lemonde.fr/sciences/rss_full.xml");
                InputStream stream = new URL(url).openConnection().getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(stream, null);
                int eventType = parser.getEventType();
                boolean done = false;
                RssItem item = null;
                while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                    String name = null;
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if (name.equalsIgnoreCase("item")) {
                                item = new RssItem();
                            } else if (item != null) {
                                if (name.equalsIgnoreCase("link")) {
                                    item.link = parser.nextText();
                                } else if (name.equalsIgnoreCase("description")) {
                                    item.description = parser.nextText().trim();
                                } else if (name.equalsIgnoreCase("pubDate")) {
                                    item.pubDate = parser.nextText();
                                } else if (name.equalsIgnoreCase("title")) {
                                    item.title = parser.nextText().trim();
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            name = parser.getName();
                            if (name.equalsIgnoreCase("item") && item != null) {
                                news.add(item);
                            } else if (name.equalsIgnoreCase("channel")) {
                                done = true;
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }


            //Changer String par RssItem sur l'adaptateur
            ArrayAdapter<RssItem> arrayAdapter = new ArrayAdapter<RssItem>(
                    getApplicationContext(),
                    android.R.layout.simple_list_item_2,
                    android.R.id.text1,
                    news ){
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view =  super.getView(position, convertView, parent);

                    TextView text2 = view.findViewById(android.R.id.text2);
                    text2.setText(news.get(position).description);

                    return view;
                }
            };

            ListView news_list = findViewById(R.id.news_list);
            news_list.post(() -> news_list.setAdapter(arrayAdapter));
            super.run();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent aboutIntent = new Intent(this, SettingsActivity.class);
            startActivity(aboutIntent);
            return true;
        }
        if (id == R.id.action_about) {
            //lance l'activity AboutActivity
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}