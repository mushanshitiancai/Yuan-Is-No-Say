package com.yuan.yuanisnosay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class WelcomeActivity extends Activity{
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        // 取消标题  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        // 取消状态栏  
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        setContentView(R.layout.welcome);  
        ImageView logoImage = (ImageView) this.findViewById(R.id.welcome);  
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);  
        alphaAnimation.setDuration(3000);  
        logoImage.startAnimation(alphaAnimation);  
        alphaAnimation.setAnimationListener(new AnimationListener() {  
  
            @Override  
            public void onAnimationStart(Animation animation) {  
  
            }  
  
            @Override  
            public void onAnimationRepeat(Animation animation) {  
  
            }  
  
            @Override  
            public void onAnimationEnd(Animation animation) {  
                Intent intent = new Intent();  
                intent.setClass(WelcomeActivity.this, MainActivity.class);  
                startActivity(intent);  
                finish();  
            }  
        });  
    }  
  
    @Override  
    public boolean onCreateOptionsMenu(Menu menu) {  
        
        return true;  
    }  
  
}
