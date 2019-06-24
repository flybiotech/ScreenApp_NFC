package com.screening.rotatedialog.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.activity.R;
import com.screening.activity.FTPSettingActivity;
import com.screening.activity.WifiSettingActivity;
import com.screening.rotatedialog.util.DipPixelUtil;
import com.screening.rotatedialog.util.WindowUtil;
import com.util.Constss;

public class MyDialog extends DialogFragment {
    private int mBeginDialogWidth;
    private int mBeginDialogHeight;
    private OnDialogButtonClickListener buttonClickListner;
    private View v;
    TextView tv_message, tv_title;
    protected int mRotation;
    String msgmsg = "提示";
    protected boolean isFirstCreateDialog = true; // 表示第一次初始化本DialogFragment
    private Context mContext;

    public MyDialog() {

    }

    public MyDialog(Context context) {
        this.mContext = context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Window window = getDialog().getWindow();
                if (window != null) {

                    mBeginDialogWidth = v.getWidth();
                    mBeginDialogHeight = v.getHeight() + dp2px(24);

                    /*
                     * 由于showListener的调用时间比onResume还晚,所以需要在显示的时候,手动调用一次旋转.
                     */
                    setRotation(mRotation);
                    setCancelable(false);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.mydialog_layout, container, false);
        Button button = (Button) v.findViewById(R.id.btn_ok);
        tv_message = v.findViewById(R.id.tv_message);
        tv_message.setText(msgmsg);
        tv_title = v.findViewById(R.id.tv_title);

        TextPaint textPaint = tv_title.getPaint();
        textPaint.setFakeBoldText(true);
        button.setOnClickListener(new View.OnClickListener() {
            Intent intent = null;

            public void onClick(View v) {
                switch (Constss.temp) {
                    case -1://不执行操作
                        break;
                    case 0://返回到FTP设置界面
                        intent = new Intent(mContext, FTPSettingActivity.class);
                        break;
                    case 1://返回到wifi设置界面
                        intent = new Intent(mContext, WifiSettingActivity.class);
                        break;

                    default:
                        break;
                }
                if (intent != null) {
                    mContext.startActivity(intent);
                    getActivity().finish();
                }
                getDialog().dismiss();
            }
        });

        return v;
    }


    public void setMessage(String message) {
        this.msgmsg = message;
    }

    public void setRotation(int rotation) {
//        tv_message.setText(msgmsg);
        Size windowSize = WindowUtil.getWindowSize();
        if (getDialog() == null) {
            return;
        }
        Window window = getDialog().getWindow();
        if (window == null) {
            Log.e("TAG", "setRotation: window = null");
            return;
        }

        if (v == null) {
            return;
        }

        int w, h;
        int tranX, tranY;
        if (rotation == 1 || rotation == 3) {//横屏
            w = (int) (windowSize.getHeight() * 0.70 + 0.5f);
            h = mBeginDialogHeight - 20;
            tranX = (h - w) / 2;
            tranY = (w - h) / 2;
            window.setLayout(h + 80, w + 100);
        } else {
            w = mBeginDialogWidth;
            h = mBeginDialogHeight - dp2px(24);
            tranX = 0;
            tranY = 0;
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        v.getLayoutParams().width = w;
        v.getLayoutParams().height = h;
        v.setLayoutParams(v.getLayoutParams());

        int duration = isFirstCreateDialog ? 0 : 200;

        v.animate()
                .rotation(90 * (rotation))
                .translationX(tranX)
                .translationY(tranY)
                .setDuration(duration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isFirstCreateDialog = false;
                    }
                });

    }

    ;

    private int dp2px(int dp) {
        return DipPixelUtil.dip2px(getActivity(), dp);
    }

    /**
     * 显示Dialog
     *
     * @param rotation 当前希望显示的方向
     */
    public void show(FragmentManager manager, int rotation) {
        mRotation = rotation;
        if (manager == null) {
            return;
        }
        super.show(manager, getClass().getSimpleName());
    }

    //实现回调功能
    public interface OnDialogButtonClickListener {
        public void okButtonClick();
//        public void cancelButtonClick();

    }

    public void setOnButtonClickListener(OnDialogButtonClickListener listener) {
        this.buttonClickListner = listener;
    }

    public boolean isShowing(MyDialog myDialog) {
        if (myDialog != null && myDialog.getDialog() != null && myDialog.getDialog().isShowing()) {
            return true;
        }
        return false;
    }
}
