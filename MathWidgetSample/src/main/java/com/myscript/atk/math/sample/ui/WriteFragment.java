package com.myscript.atk.math.sample.ui;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myscript.atk.math.sample.R;
import com.myscript.atk.math.sample.util.MyCertificate;
import com.myscript.atk.math.sample.widget.MyHorizontalScrollView;
import com.myscript.atk.math.sample.widget.MyScrollView;
import com.myscript.atk.math.widget.MathWidget;
import com.myscript.atk.math.widget.MathWidgetApi;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WriteFragment extends Fragment {

    @Bind(R.id.mathWidget)
    MathWidget mathWidget;
    @Bind(R.id.scroll)
    MyScrollView scrollView;
    @Bind(R.id.hScroll)
    MyHorizontalScrollView hScrollView;
    @Bind(R.id.result_view)
    TextView result_view;
    @Bind(R.id.drawOption)
    ImageView drawOption;
    @Bind(R.id.options)
    RelativeLayout options;
    @Bind(R.id.mathWidget_contain)
    RelativeLayout mathWidget_contain;

    boolean isDrawing = true;
    int height;
    int width;
    Configuration configuration;
    long time = 0;
    float lastX = 0;
    float lastY = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_write, container, false);
        ButterKnife.bind(this, view);
        if (!mathWidget.registerCertificate(MyCertificate.getBytes())) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
            dlgAlert.setMessage("Please use a valid certificate.");
            dlgAlert.setTitle("Invalid certificate");
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //dismiss the dialog
                }
            });
            dlgAlert.create().show();
            return view;
        }
        configuration = new Configuration();
//        configuration.orientation = Configuration.ORIENTATION_PORTRAIT;
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
//        mathWidget.requestFocus();
        mathWidget.setLayoutParams(layoutParams);
        mathWidget.clearSearchPath();
        mathWidget.addSearchDir("zip://" + getActivity().getPackageCodePath() + "!/assets/conf/");
        mathWidget.configure("math", "standard");
        mathWidget.setOnWritingListener(new MathWidgetApi.OnWritingListener() {
            @Override
            public void onWritingBegin(MathWidgetApi mathWidgetApi) {
                time = System.currentTimeMillis();
            }

            @Override
            public void onWritingEnd(MathWidgetApi mathWidgetApi) {
                time = System.currentTimeMillis();
            }
        });
        mathWidget.setOnRecognitionListener(new MathWidgetApi.OnRecognitionListener() {
            @Override
            public void onRecognitionBegin(MathWidgetApi mathWidgetApi) {

            }

            @Override
            public void onRecognitionEnd(final MathWidgetApi mathWidgetApi) {
                new Handler().postDelayed(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    public void run() {
                        if (time != 0 && System.currentTimeMillis() - time > 1000 && MainActivity.type == 0) {
                            width = scrollView.getWidth();
                            height = scrollView.getHeight();
                            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
                            mathWidget.setLayoutParams(layoutParams);
                            if ("".equals(mathWidget.getResultAsText())) {
                                result_view.setText("算式记录");
                            } else {
                                result_view.setText("计算结果 " + mathWidget.getResultAsText());
                            }
                        }
                    }
                }, 1000);
            }
        });
//        mathWidget_contain.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        lastX = motionEvent.getX();
//                        lastY = motionEvent.getY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        float x = lastX - motionEvent.getX();
//                        float y = lastY - motionEvent.getY();
//                        mathWidget_contain.scrollBy((int) x, (int) y);
//                        lastX = motionEvent.getX();
//                        lastY = motionEvent.getY();
//                        break;
//                }
//                return false;
//            }
//        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewTreeObserver vto = scrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                height = scrollView.getHeight();
                width = scrollView.getWidth();
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
                mathWidget.setLayoutParams(layoutParams);
                FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(width, height);
                mathWidget_contain.setLayoutParams(layoutParams1);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (configuration.orientation != newConfig.orientation) {
            height = scrollView.getHeight();
            width = scrollView.getWidth();
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
            mathWidget.setLayoutParams(layoutParams);
            configuration.orientation = newConfig.orientation;
        }
    }

    @OnClick(R.id.action_clear)
    public void clear() {
        mathWidget.clear(true);
    }

    @OnClick(R.id.action_redo)
    public void redo() {
        mathWidget.redo();
//        mathWidget.scrollBy(-5, -5);
    }

    @OnClick(R.id.action_undo)
    public void undo() {
        mathWidget.undo();
//        mathWidget.scrollBy(5, 5);
    }

    @OnClick(R.id.drawOption)
    public void drawOption() {
        time = 0;
        isDrawing = !isDrawing;
        scrollView.setDrawing(isDrawing);
        hScrollView.setDrawing(isDrawing);
        if (!(isDrawing || mathWidget.isEmpty())) {
            drawOption.setImageResource(R.drawable.drag);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (width * 1.5), (int) (height * 1.5));
            mathWidget.setLayoutParams(layoutParams);
        } else if (isDrawing) {
            drawOption.setImageResource(R.drawable.write);
        }
    }

    @Override
    public void onDestroy() {
        if (mathWidget != null) {
            mathWidget.release();
            mathWidget = null;
        }

        super.onDestroy();
    }
}
