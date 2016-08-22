package com.myscript.atk.math.sample.ui;

import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.myscript.atk.math.sample.R;
import com.myscript.atk.math.sample.widget.ImageCycleView;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.ad_area)
    RelativeLayout adArea;
    @Bind(R.id.rg)
    RadioGroup radioGroup;
    @Bind(R.id.ads)
    ImageCycleView ads;

    boolean isFinish = false;
    WriteFragment writeFragment;
    CalculateFragment calculateFragment;
    public static int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initAds();
        type = 0;
        writeFragment = new WriteFragment();
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
                    ft.replace(R.id.main_content,new WriteFragment()).commit();
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
                    ft.replace(R.id.main_content,new CalculateFragment()).commit();
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
    }

    @OnClick(R.id.ad_clear)
    public void adClear() {
        adArea.setVisibility(View.GONE);
    }

    public void initAds() {
        List imageUrls = new ArrayList();
        imageUrls.add(new ImageCycleView.ImageInfo(R.drawable.ad, "ad1", ""));
        imageUrls.add(new ImageCycleView.ImageInfo(R.drawable.ad, "ad2", ""));
        imageUrls.add(new ImageCycleView.ImageInfo(R.drawable.ad, "ad3", ""));
        ads.setIndicationAvailable(false);
        ads.setCycleDelayed(8000);
        ads.loadData(imageUrls, new ImageCycleView.LoadImageCallBack() {
            @Override
            public ImageView loadAndDisplay(ImageCycleView.ImageInfo imageInfo) {
//                使用SmartImageView，既可以使用网络图片也可以使用本地资源
                SmartImageView smartImageView = new SmartImageView(MainActivity.this);
                smartImageView.setImageResource(Integer.parseInt(imageInfo.image.toString()));
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
}
