package com.video.reward.rewardedvideo;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener{
    private Button mWatch;
    private TextView mText;
    private RewardedVideoAd mAd;
    int coin=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWatch=(Button)findViewById(R.id.watch);
        mText=(TextView) findViewById(R.id.text);

        mWatch.setVisibility(View.INVISIBLE);

        mAd= MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (!mAd.isLoaded()) {
                           loadAd();
                        } else {
                        }
                    }
                });
            }
        }, 3, 5, TimeUnit.SECONDS);


        mWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAd.isLoaded()){
                    mAd.show();
                }
            }
        });
    }

    public void loadAd(){
        if(!mAd.isLoaded()){
            mAd.loadAd("ca-app-pub-3940256099942544/5224354917",new AdRequest.Builder().build());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        mWatch.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Rewarded Video Ad has loaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "Rewarded Video Ad has opened", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(this, "Rewarded Video Ad has started", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(this, "Rewarded Video Ad has closed", Toast.LENGTH_SHORT).show();
        loadAd();
        mWatch.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        //Toast.makeText(this, "You have rewarded "+rewardItem.getAmount()+" "+rewardItem.getType().toString(), Toast.LENGTH_SHORT).show();
        //mText.setText(String.format(Locale.getDefault(), "You got %d %s!", rewardItem.getAmount(), rewardItem.getType()));
        coin=coin+rewardItem.getAmount();
        mText.setText("Your coin : "+coin);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "Rewarded Video Ad has closed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {
        //loadAd();

    }

    @Override
    public void onPause() {
        super.onPause();
        mAd.pause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAd.resume(this);
        loadAd();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAd.destroy(this);
    }

    @Override
    protected void onStart() {
        loadAd();
        super.onStart();
    }
}
