package com.chatapp.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {
    Button button;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private String[] strings = new String[]{"abc","bcd","def"};
    TextView tv_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);



        viewPager = findViewById(R.id.viewPager);
        tv_info = findViewById(R.id.tv_pic_info);
        tv_info.setVisibility(View.VISIBLE);
        tv_info.setText("ahsdjahd");
        button = findViewById(R.id.btn_start);
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(this);
        viewPager.setAdapter(viewPageAdapter);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);


        dotscount = viewPageAdapter.getCount();
        dots = new ImageView[dotscount];

        ObjectAnimator animY = ObjectAnimator.ofFloat(button, "translationY", -150f, 10f); //btn animation
        animY.setDuration(1000);//1sec
        animY.setInterpolator(new BounceInterpolator());
        animY.setRepeatMode(ValueAnimator.REVERSE);
        animY.setRepeatCount(2);
        animY.start();



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



    }


    public void start(View view) {
        /*final Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim);
        MyBounceInterpolator myBounceInterpolator = new MyBounceInterpolator(0.2,20);
        animation.setInterpolator(myBounceInterpolator);
        button.setAnimation(animation);*/


    }
}
