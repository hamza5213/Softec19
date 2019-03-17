package softec19.com.softec19.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import softec19.com.softec19.Adapters.ViewPagerAdapter;
import softec19.com.softec19.R;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class AdminActivity extends AppCompatActivity {

    Twitter mtwitter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        SmartTabLayout smartTabLayout=findViewById(R.id.owner_screen_tabLayout);
        ViewPager viewPager=findViewById(R.id.viewpager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        smartTabLayout.setViewPager(viewPager);
        smartTabLayout.setDistributeEvenly(true);

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("81hnFl5b4VM3hUqaApWlZt9eU")
                .setOAuthConsumerSecret("MqWwpdEbryt3gyf4AbQGVlGJ4U8SGNXJJ5X7nYYBTaCNRbPQnB")
                .setOAuthAccessToken("988011971223216128-RlnnJWjgqVzYXo0NA9T2kCR29AEo7oQ")
                .setOAuthAccessTokenSecret("Ob28nHSnq3lA2zSD7tIWmrmiGAGSAEzHMK3VBVh6GhuQ8");
        TwitterFactory tf = new TwitterFactory(cb.build());
        mtwitter = tf.getInstance();

        PublishTweet publishTweet = new PublishTweet(AdminActivity.this);
        publishTweet.execute("IWe are a big news sharing platform");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload_icon:


                startActivity(new Intent(this,UploadVideo.class));
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.action_bar_spinner, menu);
            return true;
    }

    public class PublishTweet extends AsyncTask<String, Boolean, String> {
        Context c;

        public PublishTweet(Context c) {
            this.c = c;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                mtwitter.updateStatus(strings[0]);
                publishProgress(true);
            } catch (TwitterException e) {
                e.printStackTrace();
                publishProgress(false);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            super.onProgressUpdate(values);
            if (values[0])
                Toast.makeText(c, "Tweet Published!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(c, "Unable to tweet!", Toast.LENGTH_LONG).show();
        }
    }
}
