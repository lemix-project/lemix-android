package cn.lemonit.lemix.base;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.lemonit.lemix.R;
import cn.lemonit.lemix.util.ScreenUtil;
import cn.lemonit.lemix.util.StatusBarUtils;

public class BaseActivity extends AppCompatActivity {

    private LinearLayout rootLayout;
    private RelativeLayout navigationBar;
    private TextView titleTextview;
    private ImageView backButton;

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);
        initView();
    }

    private void initView() {
        rootLayout = findViewById(R.id.rootLayout);
        navigationBar = findViewById(R.id.navigationBar);
        titleTextview = findViewById(R.id.titleTextview);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BaseActivity.this.finish();
//                backPage();
            }
        });

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) navigationBar.getLayoutParams();
            layoutParams.setMargins(0, ScreenUtil.dp2px(this, -10), 0, 0);
            navigationBar.setLayoutParams(layoutParams);
        }
        StatusBarUtils.with(this).init();
    }

    protected void addView(int layoutId) {
        View view = LayoutInflater.from(this).inflate(layoutId, null);
        rootLayout.addView(view);
    }

    public RelativeLayout getNavigationBar() {
        return navigationBar;
    }

    public void setNavigationBarTitle(String title) {
        titleTextview.setText(title);
    }

    /**
     * 设置标题栏
     * @param title
     */
    public void setTitle(String title) {
        titleTextview.setText(title);
    }

    /**
     * 设置标题栏左侧按钮是否显示
     */
    public void isHideLeftButton(boolean isHide) {
        if(isHide) {
            backButton.setVisibility(View.GONE);
        }
    }

    /**
     * 设置是否显示状态栏
     * @param isHidden
     */
    public void setStatusBarHidden(boolean isHidden) {
        if(isHidden) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // 显示状态栏
        }else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);  // 隐藏状态栏
        }
    }

    /**
     * 减小标题栏的高度
     */
    public void setNavigationBarHeight() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) navigationBar.getLayoutParams();
        layoutParams.setMargins(0, ScreenUtil.dp2px(this, -20), 0, 0);
        navigationBar.setLayoutParams(layoutParams);
    }

    /**
     * 当由JS跳转过来时，会把状态栏标题栏信息传递过来对应显示
     */
    private void setConfigStatus() {
        intent = getIntent();
        // 标题栏是否隐藏
        boolean navigationHidden = intent.getBooleanExtra("navigationHidden", false);
        // 标题栏背景颜色
        String navigationBackgroundColor = intent.getStringExtra("navigationBackgroundColor");
        // 标题栏字体颜色
        String navigationItemColor = intent.getStringExtra("navigationItemColor");
        // 标题栏标题
        String navigationTitle = intent.getStringExtra("navigationTitle");
        // 状态栏是否隐藏
        boolean statusBarHidden = intent.getBooleanExtra("statusBarHidden", false);
        // 状态栏字体颜色（6.0 系统）
        String statusBarStyle = intent.getStringExtra("statusBarStyle");

        // 具体设置
        navigationBar.setVisibility(navigationHidden ? View.GONE : View.VISIBLE);
        if(!TextUtils.isEmpty(navigationBackgroundColor)) {
            navigationBar.setBackgroundColor(Color.parseColor(navigationBackgroundColor));
        }
        if(!TextUtils.isEmpty(navigationItemColor)) {
            titleTextview.setTextColor(Color.parseColor(navigationItemColor));
        }
        if(!TextUtils.isEmpty(navigationTitle)) {
            titleTextview.setText(navigationTitle);
        }
        if(statusBarHidden) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);  // 隐藏状态栏
            setNavigationBarHeight();
        }else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // 显示状态栏
        }
        if (!TextUtils.isEmpty(statusBarStyle) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                // 设置状态栏字体黑色
                if (statusBarStyle.equals("dark")) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }
}
