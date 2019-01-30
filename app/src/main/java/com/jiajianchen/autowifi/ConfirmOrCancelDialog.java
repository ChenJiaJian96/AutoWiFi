package com.jiajianchen.autowifi;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by jiajianchen
 * on 2018/11/7
 */

public class ConfirmOrCancelDialog extends Dialog {

    private static final String TAG = "ConfirmOrCancelDialog";

    ConfirmOrCancelDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;

        private DialogInterface.OnClickListener mClickListener;
        private String mTitle;

        public Builder(Context context, String mTitle) {
            this.context = context;
            this.mTitle = mTitle;
        }

        public void setClickListener(OnClickListener clickListener) {
            mClickListener = clickListener;
        }

        public ConfirmOrCancelDialog create() {

            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ConfirmOrCancelDialog dialog = new ConfirmOrCancelDialog(context, R.style.BaseDialog);
            View layout = inflater.inflate(R.layout.dialog_confirm_or_cancel, null);

            TextView mMsgText = layout.findViewById(R.id.dialog_msg);
            RelativeLayout mConfirmLayout = layout.findViewById(R.id.tv_confirm);
            RelativeLayout mCancelLayout = layout.findViewById(R.id.tv_cancel);

            mMsgText.setText(mTitle);

            if (mClickListener != null) {
                mConfirmLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mClickListener.onClick(dialog, BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });
                mCancelLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mClickListener.onClick(dialog, BUTTON_NEGATIVE);
                        dialog.dismiss();
                    }
                });
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
