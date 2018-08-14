package com.example.mqtest.circle_progress;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ControllerView controllerView;

    private static final String TAG = "MainActivity";

    int mCurrentBitInt = 0;
    Bitmap[] bitmaps;
    Bitmap bitmap1;

    private Handler handlerChangePicture = new Handler();
    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            controllerView.bitmap = bitmaps[mCurrentBitInt];
            if (mCurrentBitInt < bitmaps.length - 1) {
                mCurrentBitInt++;
            } else {
                mCurrentBitInt = 0;
            }
            controllerView.invalidate();
            Log.d(TAG, "handleMessage: 刷新");
            handlerChangePicture.postDelayed(this, 500);
        }
    };

    //另一种定时器，方案一
    /*private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                controllerView.bitmap = bitmaps[mCurrentBitInt];
                if (mCurrentBitInt < bitmaps.length - 1) {
                    mCurrentBitInt++;
                } else {
                    mCurrentBitInt = 0;
                }
                controllerView.invalidate();
                Log.d(TAG, "handleMessage: 刷新");
            }
            super.handleMessage(msg);


        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controllerView = findViewById(R.id.circle_view);
        bitmap1 = BitmapFactory.decodeResource(this.getResources(), R.mipmap.one);
        Bitmap bitmap2 = BitmapFactory.decodeResource(this.getResources(), R.mipmap.two);
        Bitmap bitmap3 = BitmapFactory.decodeResource(this.getResources(), R.mipmap.three);
        bitmaps = new Bitmap[]{bitmap1, bitmap2, bitmap3};
        handlerChangePicture.postDelayed(runnable, 100);
        //handlerChangePicture.post(runnable);

        //另一种定时器，方案一
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();*/
        controllerView.setOnItemClickListener(new ControllerView.OnItemClickListener() {
            @Override
            public void onItemselect() {
                Toast.makeText(MainActivity.this, "以点击", Toast.LENGTH_SHORT).show();
                if (controllerView.mStep > controllerView.mCurrentStep) {
                    ++controllerView.mCurrentStep;
                    controllerView.invalidate();
                } else {
                    controllerView.mCurrentStep = 1;
                    controllerView.invalidate();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerChangePicture.removeCallbacks(runnable);
    }
}
