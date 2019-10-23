package com.dms.almacensoft.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.constraint.Group;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dms.almacensoft.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link CustomDialog} premite llamar un cuadro de diálogo completamente configurable de acuerdo
 * a la necesidad. Tiene dos tipos:
 * NORMAL -- Cuadro de diálogo con mensaje, icono y hasta dos botones cuyos métodos son configurables
 * LIST -- Similar al NORMAL pero con la adición de un RecyclerView para muestreo de una lista.
 */

public class CustomDialog extends Dialog {

    @BindView(R.id.title_text_view)
    TextView titleTextView;
    @BindView(R.id.message_text_view)
    TextView messageTextView;
    @BindView(R.id.negative_button)
    Button negativeButton;
    @BindView(R.id.positive_button)
    Button positiveButton;
    @BindView(R.id.icon_image_view)
    AppCompatImageView iconImageView;
    @BindView(R.id.data_recycler_view)
    RecyclerView dataRecyclerView;
    @BindView(R.id.icon_group)
    Group iconGroup;

    private String mTitle;
    private String mMessage;
    private boolean mCancelable;
    private boolean mPositiveButtonFlag;
    private boolean mNegativeButtonFlag;
    private String mPositiveButtonLabel;
    private String mNegativeButtonLabel;
    private IButton mPositiveButtonlistener;
    private IButton mNegativeButtonlistener;
    private RecyclerView.Adapter mAdapter;
    private int mIcon;
    private DIALOG_TYPE mType;

    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_base);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ButterKnife.bind(this);

        setCancelable(mCancelable);
        if (!TextUtils.isEmpty(mTitle)) {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(mTitle);
        }

        if (!TextUtils.isEmpty(mMessage)) {
            messageTextView.setText(mMessage);
        }

        switch (mType) {
            case NORMAL:
                messageTextView.setVisibility(View.VISIBLE);
                break;
            case LIST:
                dataRecyclerView.setVisibility(View.VISIBLE);
                setListAdapter(mAdapter);
                break;
        }

        if (mIcon == -1) {
            iconGroup.setVisibility(View.GONE);
        } else {
            iconGroup.setVisibility(View.VISIBLE);
            iconImageView.setImageResource(mIcon);
        }

        setButton(positiveButton, mPositiveButtonFlag, mPositiveButtonLabel, mPositiveButtonlistener);
        setButton(negativeButton, mNegativeButtonFlag, mNegativeButtonLabel, mNegativeButtonlistener);
    }


    private void setListAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null) return;
        dataRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//        dataRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext(), R.drawable.line_divider_black));
        dataRecyclerView.setLayoutManager(layoutManager);
        dataRecyclerView.setAdapter(adapter);
    }

    private void setButton(TextView button, boolean flag, String label, final IButton iButton) {
        if (!flag) {
            button.setVisibility(View.GONE);
            return;
        }

        button.setVisibility(View.VISIBLE);
        button.setText(label);
        button.setOnClickListener(view -> {
            CustomDialog.this.dismiss();
            CustomDialog.this.cancel();
            if (iButton != null) {
                iButton.onButtonClick();
            }
        });
    }

    public enum DIALOG_TYPE {
        NORMAL, LIST
    }

    public interface IButton {
        void onButtonClick();
    }

    public static class Builder {
        private Context context;
        private String title = "";
        private RecyclerView.Adapter adapter;
        private String message = "";
        private boolean cancelable = true;
        private DIALOG_TYPE type = DIALOG_TYPE.NORMAL;
        private boolean positiveButtonFlag = false;
        private boolean negativeButtonFlag = false;
        private String positiveButtonLabel = "Ok";
        private String negativeButtonLabel = "Cancel";
        private IButton positiveButtonlistener;
        private IButton negativeButtonlistener;
        private int theme = R.style.AppTheme_Dialog;
        private int icon = -1;

        public Builder(Context context) {
            this.context = context;
        }

        public CustomDialog build() {
            CustomDialog dialog = new CustomDialog(context, theme);
            dialog.mTitle = title;
            dialog.mMessage = message;
            dialog.mType = type;
            dialog.mCancelable = cancelable;
            dialog.mPositiveButtonFlag = positiveButtonFlag;
            dialog.mNegativeButtonFlag = negativeButtonFlag;
            dialog.mPositiveButtonLabel = positiveButtonLabel;
            dialog.mNegativeButtonLabel = negativeButtonLabel;
            dialog.mPositiveButtonlistener = positiveButtonlistener;
            dialog.mNegativeButtonlistener = negativeButtonlistener;
            dialog.mAdapter = adapter;
            dialog.mIcon = icon;

            return dialog;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPositiveButtonLabel(String label) {
            this.positiveButtonLabel = label;
            positiveButtonFlag = true;
            return this;
        }

        public Builder setPositiveButtonlistener(IButton iButton) {
            this.positiveButtonlistener = iButton;
            positiveButtonFlag = true;
            return this;
        }

        public Builder setNegativeButtonLabel(String label) {
            this.negativeButtonLabel = label;
            negativeButtonFlag = true;
            return this;
        }

        public Builder setNegativeButtonlistener(IButton iButton) {
            this.negativeButtonlistener = iButton;
            negativeButtonFlag = true;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setTheme(@StyleRes int theme) {
            this.theme = theme;
            return this;
        }

        public Builder setIcon(@DrawableRes int icon) {
            this.icon = icon;
            return this;
        }

        public Builder setType(DIALOG_TYPE type) {
            this.type = type;
            return this;
        }

        public Builder setAdapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
            return this;
        }
    }
}
