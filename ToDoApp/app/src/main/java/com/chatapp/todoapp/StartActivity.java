package com.chatapp.todoapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {
    Button button;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private String[] strings = new String[]{"Want to make progress in your daily tasks?","An effective to-do list can make a huge difference","So, Go ahead and take your first step towards success"}; // text to go with the pictures
    TextView tv_info;

    private int current_position;
    private Timer timer;
    SharedPreference sharedPreference = new SharedPreference(this);



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorWhite));




        viewPager = findViewById(R.id.viewPager);
        tv_info = findViewById(R.id.tv_pic_info);
        tv_info.setVisibility(View.VISIBLE);
       // tv_info.setText("ahsdjahd");
        button = findViewById(R.id.btn_start);
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(this);

        viewPager.setAdapter(viewPageAdapter);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);


        dotscount = viewPageAdapter.getCount();
        dots = new ImageView[dotscount];



        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        tv_info.setText(strings[0]);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                tv_info.setText(strings[position]);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        run(); // for automation



    }


    public void start(View view) {
        /*final Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim);
        MyBounceInterpolator myBounceInterpolator = new MyBounceInterpolator(0.2,20);
        animation.setInterpolator(myBounceInterpolator);
        button.setAnimation(animation);*/

        sharedPreference.save_flag(1);
        startActivity(new Intent(this, DisplayTasksActivity.class));
        finish();


    }

    private void run(){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(current_position == 3){
                    current_position = 0;

                }
                viewPager.setCurrentItem(current_position++,true);

            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);

            }
        },250,5000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sharedPreference.get_flag() != 0){
            startActivity(new Intent(this, DisplayTasksActivity.class));
            finish();
        }
    }
}
