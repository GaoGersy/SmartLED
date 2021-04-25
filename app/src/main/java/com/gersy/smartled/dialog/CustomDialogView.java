package com.gersy.smartled.dialog;

import android.widget.TextView;

public interface CustomDialogView {
    TextView getPositiveButton();

    TextView getNegativeButtonButton();

    TextView getTitleView();

    TextView getMessageView();

//    void setNegativeClickListener(CustomDialog.OnClickListener listener);
//
//    void setPositiveClickListener(CustomDialog.OnClickListener listener);
}
