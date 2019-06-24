package com.screening.rotatedialog.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.screening.rotatedialog.base.BaseDefaultContentDialog;


/**
 * Created by shangzheng on 2017/9/27
 * ☃☃☃ 09:26.
 * <p>
 * 只用于显示一句Message的Dialog,且只有[确定]按钮,而且不关心按钮点击后的返回值
 */

public class OkDialog extends BaseDefaultContentDialog {

    private static final String MESSAGE = "message";

    public static OkDialog newInstance(String message) {

        Bundle arguments = new Bundle();
        arguments.putString(MESSAGE, message);

        OkDialog okDialog = new OkDialog();
        okDialog.setArguments(arguments);
        okDialog.setCancelable(false);
        return okDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        String message = arguments.getString(MESSAGE);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setCancelable(false);

        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }

        return builder.setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
    }


}
