package io.github.xiong_it.floatballdemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by xiongxunxiang on 2017/2/14.
 */

public class BottomWindow extends FrameLayout {

    public BottomWindow(Context context) {
        this(context, null);
    }

    public BottomWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_window, this, true);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FloatBallManager.getInstance(v.getContext()).hideBottomWindow();
                return false;
            }
        });

        findViewById(R.id.github_io_blog).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser("https://xiong-it.github.io");
            }
        });

        findViewById(R.id.csdn_blog).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openBrowser("http://blog.csdn.net/xiong_it");
            }
        });
    }

    private void openBrowser(String website) {
        Uri uri = Uri.parse(website);
        Intent browser = new Intent(Intent.ACTION_VIEW, uri);
        browser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(browser);
    }

}
