package cn.lemonit.lemix.core.operate.ui;

import android.graphics.Color;
import android.view.View;

import cn.lemonit.lemix.base.BaseActivity;

/**
 * 原生和JS的最终执行类（原生可直接调用）
 */
public class UiStyle {

    /**
     * 是否隐藏标题栏
     * （此方法是的调用源头是JS调用的，webview中的机制决定是走的子线程，所以想刷新UI要在主线程）
     * @param isHidden
     */
    public void setNavigationBarHidden(final BaseActivity activity, final boolean isHidden){

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.getNavigationBar().setVisibility(isHidden ? View.GONE : View.VISIBLE);
            }
        });
    }

    /**
     * 设置主题颜色
     * @param activity
     * @param color
     */
    public void setNavigationBackgroundColor(final BaseActivity activity, final String color) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.getNavigationBar().setBackgroundColor(Color.parseColor(color));
            }
        });
    }

    /**
     * 设置标题文字
     * @param activity
     * @param title
     */
    public void setNavigationTitle(final BaseActivity activity, final String title) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.setNavigationBarTitle(title);
            }
        });
    }

    /**
     * 设置状态栏是dark还是light
     * @param activity
     * @param style
     */
    public void setStatusBarStyle(final BaseActivity activity, final String style) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(activity, MainActivity.class);
//                intent.putExtra("style", style);
//                activity.startActivity(intent);
            }
        });
    }

    /**
     * 设置状态栏是否隐藏
     * @param activity
     * @param isHidden
     */
    public void setStatusBarHidden(final BaseActivity activity, final boolean isHidden) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(activity, MainActivity.class);
//                intent.putExtra("isHidden", isHidden);
//                activity.startActivity(intent);
            }
        });
    }

}
