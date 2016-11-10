package com.lwp.library;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


/**
 * 拍照或选择相册选项弹窗dialog
 */
public class SelectPicOrCameraDialog extends Dialog implements View.OnClickListener {

    private OnSelectedListener listener;

    public interface OnSelectedListener {
        void OnSelected(View v);
    }

    public void setOnSelectedListener(OnSelectedListener l) {
        this.listener = l;
    }

    public SelectPicOrCameraDialog(Context context) {
        this(context, R.style.select_pic_dialog);
    }

    public SelectPicOrCameraDialog(Context context, int themeResId) {
        super(context, themeResId);
        View dialog_view = LayoutInflater.from(context).inflate(R.layout.dialog_select_pic, null);

        dialog_view.findViewById(R.id.tv_photograph).setOnClickListener(this);
        dialog_view.findViewById(R.id.tv_select_photo).setOnClickListener(this);
        dialog_view.findViewById(R.id.tv_cancel).setOnClickListener(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return true;
            }
        });

        super.setContentView(dialog_view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager wm = getWindow().getWindowManager();
        Display display = wm.getDefaultDisplay();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
//        attributes.width = display.getWidth();
        Point point = new Point();
        display.getSize(point);
        attributes.width = point.x;
        attributes.alpha = 0.95f;
        getWindow().setAttributes(attributes);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.OnSelected(v);
        }
        dismiss();
    }


}
