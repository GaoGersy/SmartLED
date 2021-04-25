package com.gersy.smartled.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.piesat.outsideinvestigate.R;


public class CustomDialog {

    public static final int BUTTON_POSITIVE = -1;
    public static final int BUTTON_NEGATIVE = -2;

    private final Dialog dialog;
    private CustomController customController;
    private boolean autoDismiss;

    private CustomDialog(Builder builder) {
        dialog = createDialog(builder);
    }

    private Dialog createDialog(Builder builder) {
        autoDismiss = builder.autoDismiss;
        customController = builder.customController;
        View view = customController.view;
        Dialog dialog = new Dialog(builder.context, R.style.TANCStyle);
        dialog.setCanceledOnTouchOutside(builder.canceledOnTouchOutside);
        dialog.setCancelable(builder.cancelable);// 不可以用“返回键”取消
        dialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局

        initListener();
        return dialog;
    }

    private void initListener() {
        if (customController.negative != null) {
            customController.negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (customController.negativeListener != null) {
                        customController.negativeListener.onClick(BUTTON_NEGATIVE);
                    }
                    dismiss();
                }
            });
        }
        if (customController.positive != null) {
            customController.positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (autoDismiss) {
                        dismiss();
                    }
                    if (customController.positiveListener != null) {
                        customController.positiveListener.onClick(BUTTON_POSITIVE);
                    }

                }
            });
        }
    }

    public CustomDialog show() {
        dialog.show();
        return this;
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public static class Builder {
        private Context context;
        private int iconResId;
        private Drawable icon;

        private boolean cancelable = true;
        private boolean canceledOnTouchOutside = true;
        private boolean autoDismiss = true;
        private CustomController customController;

        public Builder(@NonNull Context context) {
            this.context = context;
            customController = new CustomController(context, R.layout.dialog_custom);
        }

        public Builder(Context context, int layoutId) {
            this.context = context;
            customController = new CustomController(context, layoutId);
        }

        public Builder(Context context, CustomDialogView customDialogView) {
            this.context = context;
            customController = new CustomController(context, customDialogView);
        }

        public Builder(Context context, CustomLayout customLayout) {
            this.context = context;
            customController = new CustomController(context, customLayout);
        }

        public Builder(Context context, CustomContentView customContentView) {
            this.context = context;
            customController = new CustomController(context, customContentView);
        }

        public Builder autoDismiss(boolean autoDismiss) {
            this.autoDismiss = autoDismiss;
            return this;
        }

        public Builder setTitle(@StringRes int titleId) {
            return setTitle(context.getText(titleId));
        }

        public Builder setTitle(@Nullable CharSequence title) {
            customController.setTitle(title);
            return this;
        }

        public Builder setMessage(@StringRes int messageId) {
            return setMessage(context.getText(messageId));
        }

        public Builder setMessage(@Nullable CharSequence message) {
            customController.setMessage(message);
            return this;
        }

        public Builder setIcon(@DrawableRes int iconId) {
            this.iconResId = iconId;
            return this;
        }

        public Builder setIcon(@Nullable Drawable icon) {
            this.icon = icon;
            return this;
        }

        public Builder setPositiveButton(@StringRes int textId, final OnClickListener listener) {
            return setPositiveButton(context.getText(textId), listener);
        }

        public Builder setPositiveButton(CharSequence text, final OnClickListener listener) {
            this.customController.setPositive(text);
            this.customController.positiveListener = listener;
            return this;
        }

        public Builder setNegativeButton(@StringRes int textId, final OnClickListener listener) {
            return setNegativeButton(context.getText(textId), listener);
        }

        public Builder setNegativeButton(CharSequence text, final OnClickListener listener) {
            this.customController.setNegative(text);
            this.customController.negativeListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public CustomDialog create() {
            return new CustomDialog(this);
        }
    }

    private static class CustomController {
        private TextView title;
        private TextView message;
        private TextView negative;
        private TextView positive;
        private final View view;
        private OnClickListener positiveListener;
        private OnClickListener negativeListener;

        public CustomController(Context context, CustomLayout customLayout) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(customLayout.layoutId, null);
            title = view.findViewById(customLayout.titleId);
            message = view.findViewById(customLayout.messageId);
            positive = view.findViewById(customLayout.positiveButtonId);
            negative = view.findViewById(customLayout.negativeButtonId);
        }

        public CustomController(Context context, CustomDialogView customDialogView) {
            view = (View) customDialogView;
            title = customDialogView.getTitleView();
            message = customDialogView.getMessageView();
            positive = customDialogView.getPositiveButton();
            negative = customDialogView.getNegativeButtonButton();
        }

        public CustomController(Context context, CustomContentView customContentView) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.dialog_custom_content, null);
            title = view.findViewById(R.id.title);
            positive = view.findViewById(R.id.positive);
            negative = view.findViewById(R.id.negative);
            FrameLayout container = view.findViewById(R.id.container);
            View contentView = customContentView.getContentView();
            ViewGroup parent = (ViewGroup) contentView.getParent();
            if (parent != null) {
                parent.removeView(contentView);
            }
            container.addView(contentView);
        }

        public CustomController(Context context, int layoutId) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(layoutId, null);
            title = view.findViewById(R.id.title);
            message = view.findViewById(R.id.message);
            positive = view.findViewById(R.id.positive);
            negative = view.findViewById(R.id.negative);
        }

        public void setTitle(CharSequence title) {
            this.title.setText(title);
        }

        public void setMessage(CharSequence message) {
            this.message.setText(message);
        }

        public void setNegative(CharSequence negative) {
            this.negative.setText(negative);
        }

        public void setPositive(CharSequence positive) {
            this.positive.setText(positive);
        }

    }

    public static interface OnClickListener {
        void onClick(int buttonType);
    }

    public static class CustomLayout {
        private int layoutId;
        private int titleId;
        private int messageId;
        private int negativeButtonId;
        private int positiveButtonId;

        public CustomLayout(int layoutId, int titleId, int messageId, int negativeButtonId, int positiveButtonId) {
            this.layoutId = layoutId;
            this.titleId = titleId;
            this.messageId = messageId;
            this.negativeButtonId = negativeButtonId;
            this.positiveButtonId = positiveButtonId;
        }

        public int getLayoutId() {
            return layoutId;
        }

        public void setLayoutId(int layoutId) {
            this.layoutId = layoutId;
        }

        public int getTitleId() {
            return titleId;
        }

        public void setTitleId(int titleId) {
            this.titleId = titleId;
        }

        public int getMessageId() {
            return messageId;
        }

        public void setMessageId(int messageId) {
            this.messageId = messageId;
        }

        public int getNegativeButtonId() {
            return negativeButtonId;
        }

        public void setNegativeButtonId(int negativeButtonId) {
            this.negativeButtonId = negativeButtonId;
        }

        public int getPositiveButtonId() {
            return positiveButtonId;
        }

        public void setPositiveButtonId(int positiveButtonId) {
            this.positiveButtonId = positiveButtonId;
        }
    }
}
