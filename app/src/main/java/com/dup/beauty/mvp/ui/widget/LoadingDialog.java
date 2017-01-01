package com.dup.beauty.mvp.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.dup.beauty.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 等待层dialog
 * Created by DP on 2016/10/11.
 */
public class LoadingDialog extends Dialog {
    @BindView(R.id.dialog_iv_big)
    public ImageView ivBig;
    @BindView(R.id.dialog_iv_small)
    public ImageView ivSmall;

    private final Animation animBig;
    private final Animation animSmall;

    public LoadingDialog(Context context) {
        super(context, R.style.base_dialog);
        setContentView(R.layout.dialog_loading);
        ButterKnife.bind(this, this);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    closeDialog();
                }
                return false;
            }
        });

        animBig = AnimationUtils.loadAnimation(context, R.anim.loading_big);
        animSmall = AnimationUtils.loadAnimation(context, R.anim.loading_small);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ivBig.startAnimation(animBig);
        ivSmall.startAnimation(animSmall);
    }

    public void closeDialog() {
        ivBig.clearAnimation();
        ivSmall.clearAnimation();
        dismiss();
    }

}
