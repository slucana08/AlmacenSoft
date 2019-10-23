package com.dms.almacensoft.features.sync;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dms.almacensoft.App;
import com.dms.almacensoft.R;
import com.dms.almacensoft.data.models.recepciondespacho.BodyDetalleDocumentoPendiente;
import com.dms.almacensoft.injection.ActivityComponent;
import com.dms.almacensoft.injection.DaggerActivityComponent;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link SyncDialog} es el cuadro de diálogo que se encarga de controlar la descarga de datos maestros
 * cuando se pasa al modo de trabajo en batch. También se encarga de procesar el envío de datos cuando
 * se vuelve a trabajar en modo en línea.
 */

public class SyncDialog extends Dialog implements SyncContract.View{

    @BindView(R.id.ok_button)
    Button okButton;
    @BindView(R.id.icon_image_view)
    AppCompatImageView iconImageView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.percentage_text_view)
    TextView percentageTextView;
    @BindView(R.id.quantity_text_view)
    TextView quantityTextView;
    @BindView(R.id.title_text_view)
    TextView titleTextView;
    @BindView(R.id.message_text_view)
    TextView messageTextView;

    @Inject
    SyncPresenter presenter;

    private IButton mButtonlistener;

    private List<BodyDetalleDocumentoPendiente> bodyDetalleList;

    public SyncDialog(@NonNull Context context) {
        super(context);
    }

    public SyncDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ButterKnife.bind(this);

        ActivityComponent activityComponent = DaggerActivityComponent.builder()
                .appComponent(App.get().getAppComponent())
                .build();
        activityComponent.inject(this);
        presenter.attachView(this);

        setCancelable(false);

        okButton.setVisibility(View.VISIBLE);
        okButton.setText(getContext().getString(R.string.label_ok));

        presenter.setupView();
    }

    @Override
    protected void onStop() {
        presenter.detachView();
        super.onStop();
    }

    @Override
    public void setupDialog(String title, String message, boolean download) {
        progressBar.setVisibility(View.GONE);
        percentageTextView.setVisibility(View.GONE);
        quantityTextView.setVisibility(View.GONE);
        iconImageView.setImageResource(download ? R.drawable.ic_cloud_download : R.drawable.ic_cloud_upload);
        titleTextView.setText(title);
        messageTextView.setText(message);
        okButton.setOnClickListener(view -> presenter.startSync(bodyDetalleList));
    }

    @Override
    public void startSync() {
        progressBar.setVisibility(View.VISIBLE);
        percentageTextView.setVisibility(View.VISIBLE);
        quantityTextView.setVisibility(View.VISIBLE);

        messageTextView.setText(getContext().getString(R.string.espere));
        okButton.setVisibility(View.GONE);
    }

    /**
     * Actualiza el progreso en progressBar de acuerdo al avance realizado
     * @param doneItems es la cantidad de items completados
     * @param totalItems es la cantidad total de items
     */
    @Override
    public void updateProgress(int doneItems, int totalItems) {
        if (doneItems == totalItems) {
            presenter.clearDisposable();
            presenter.syncSuccess();
        }

        quantityTextView.setText(String.format(Locale.US, "%d/%d", doneItems, totalItems));
        int percentage = (totalItems == 0) ? 100 : (doneItems * 100 / totalItems);
        percentageTextView.setText(String.format(Locale.US, "%d %%", percentage));
        progressBar.setProgress(percentage);

    }

    @Override
    public void syncSuccess() {
        okButton.setVisibility(View.VISIBLE);
        messageTextView.setText(getContext().getString(R.string.sincronizacion_exitosa));

        okButton.setOnClickListener(view -> {
            SyncDialog.this.dismiss();
            SyncDialog.this.cancel();
            mButtonlistener.onButtonClick(true);
        });
    }

    @Override
    public void syncError(String message) {
        okButton.setVisibility(View.VISIBLE);
        messageTextView.setText(getContext().getString(R.string.ocurrio_error));

        okButton.setOnClickListener(view -> {
            SyncDialog.this.dismiss();
            SyncDialog.this.cancel();
            mButtonlistener.onButtonClick(false);
        });
    }

    /**
     * Es la interfaz que permite establecer métodos a ejecutar de manera dinámica
     */
    public interface IButton {
        void onButtonClick(boolean success);
    }

    public static class Builder {
        private Context context;
        private IButton buttonlistener;
        private List<BodyDetalleDocumentoPendiente> bodyDetalleList;
        private int theme = R.style.AppTheme_Dialog;

        public Builder(Context context) {
            this.context = context;
        }

        public SyncDialog build() {
            SyncDialog dialog = new SyncDialog(context, theme);
            dialog.mButtonlistener = buttonlistener;
            dialog.bodyDetalleList = bodyDetalleList;
            return dialog;
        }

        public Builder setButtonlistener(IButton iButton) {
            this.buttonlistener = iButton;
            return this;
        }

        public Builder setTheme(@StyleRes int theme) {
            this.theme = theme;
            return this;
        }

        public Builder setBodyDetalleList(List<BodyDetalleDocumentoPendiente> bodyDetalleList){
            this.bodyDetalleList = bodyDetalleList;
            return this;
        }
    }

}
