package com.myscript.atk.math.sample.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.image.SmartImageView;
import com.myscript.atk.math.sample.R;
import com.myscript.atk.math.sample.entity.Ad;
import com.myscript.atk.math.sample.network.JsonConnection;
import com.myscript.atk.math.sample.widget.ImageCycleView;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.ad_area)
    RelativeLayout adArea;
    @Bind(R.id.rg)
    RadioGroup radioGroup;
    @Bind(R.id.ad_clear)
    ImageView ad_clear;

    ImageCycleView ads;
    boolean isFinish = false;
    WriteFragment writeFragment;
    CalculateFragment calculateFragment;
    public static int type;
    List<Ad> adList;
    int screenHeight;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        writeFragment = new WriteFragment();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenHeight = wm.getDefaultDisplay().getHeight();
//        calculateFragment = new CalculateFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.add(R.id.main_content, calculateFragment);
//        ft.add(R.id.main_content, writeFragment);
//        ft.show(writeFragment).commit();
        ft.replace(R.id.main_content, writeFragment).commit();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb0) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    type = 0;
//                    if (calculateFragment != null) {
//                        ft.hide(calculateFragment);
//                    }
                    ft.replace(R.id.main_content, new WriteFragment()).commit();
//                    if (writeFragment == null) {
//                        writeFragment = new WriteFragment();
//                        ft.add(R.id.main_content, calculateFragment);
//                    }
//                    ft.show(writeFragment);
//                    ft.commit();
                } else if (i == R.id.rb1) {
                    type = 1;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.main_content, new CalculateFragment()).commit();
//                    if (writeFragment != null) {
//                        ft.hide(writeFragment);
//                    }
//                    if (calculateFragment == null) {
//                        calculateFragment = new CalculateFragment();
//                        ft.add(R.id.main_content, calculateFragment);
//                    }
//                    ft.show(calculateFragment);
//                    ft.commit();
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        new GetAdTask().execute();
    }

    @OnClick(R.id.ad_clear)
    public void adClear() {
        adArea.setVisibility(View.GONE);
    }

    public void initAds(List<Ad> adList) {
        ads = new ImageCycleView(MainActivity.this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight / 9);
        ads.setLayoutParams(layoutParams);
        adArea.addView(ads);
        List<ImageCycleView.ImageInfo> imageUrls = new ArrayList<>();
        for (Ad ad : adList) {
            imageUrls.add(new ImageCycleView.ImageInfo(ad.getImg(), "ad", ""));
        }
        ads.setIndicationAvailable(false);
        ads.setCycleDelayed(8000);
        ads.loadData(imageUrls, new ImageCycleView.LoadImageCallBack() {
            @Override
            public ImageView loadAndDisplay(ImageCycleView.ImageInfo imageInfo) {
                SmartImageView smartImageView = new SmartImageView(MainActivity.this);
                Picasso.with(MainActivity.this).load(imageInfo.image.toString()).into(smartImageView);
                return smartImageView;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isFinish) {
            finish();
        } else {
            isFinish = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            isFinish = false;
                        }
                    }, 2000
            );
        }
    }

    private class GetAdTask extends AsyncTask<Void, Void, Boolean> {

//        List<Ad> adList;

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                initAds(adList);
                ad_clear.bringToFront();
            } else {
                adArea.setVisibility(View.GONE);
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Calendar cal = Calendar.getInstance();
                String t1;
                String jsonarrys;
                int old = 0;
                do {
                    cal.add(Calendar.DATE, old);
                    t1 = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
                    jsonarrys = JsonConnection.getURLResponse(t1);
                    adList = new Gson().fromJson(jsonarrys, new TypeToken<List<Ad>>() {
                    }.getType());
                    old--;
                    if (old < -30) {
                        return false;
                    }
                } while (adList.isEmpty());
                for (int i = 0; i < adList.size(); i++) {
                    Log.d("Task", adList.get(i).toString());
                }
                Log.d("Task", jsonarrys);
                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
