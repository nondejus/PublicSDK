package com.gotenna.sdk.sample.firmware;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gotenna.sdk.sample.R;

/**
 * A screen that allows users to update their devices by providing a url.
 *
 * Created on 9/20/19.
 *
 * @author Pedro Torres
 */
public class FirmwareUpdateActivity extends AppCompatActivity
        implements View.OnClickListener, FirmwareUpdateContract.FirmwareUpdateView
{

    private FirmwareUpdateContract.FirmwareUpdatePresenter firmwarePresenter;

    private EditText firmwareUrlEditText;
    private Button updateFirmwareButton;
    private TextView updateMessageTextView;
    private ProgressBar updateProgressBar;

    //==============================================================================================
    // Class static methods
    //==============================================================================================

    public static void start(Context context)
    {
        Intent intent = new Intent(context, FirmwareUpdateActivity.class);
        context.startActivity(intent);
    }

    //==============================================================================================
    // Class instance methods
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firmware_update);

        firmwarePresenter = new FirmwareUpdatePresenter(this);

        initViews();
    }

    private void initViews()
    {
        firmwareUrlEditText = findViewById(R.id.firmwareUrlEditText);

        updateFirmwareButton = findViewById(R.id.updateFirmwareButton);
        updateFirmwareButton.setOnClickListener(this);

        updateMessageTextView = findViewById(R.id.updateMessageTextView);
        updateProgressBar = findViewById(R.id.updateProgressBar);

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (firmwarePresenter != null) firmwarePresenter.onDestroy();
    }

    //==============================================================================================
    // View.OnClickListener Implementation
    //==============================================================================================

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.updateFirmwareButton:
                firmwarePresenter.downloadFromUrl(firmwareUrlEditText.getText().toString());
                break;
        }
    }
    //==============================================================================================
    // FirmwareUpdateContract.FirmwareUpdateView
    //==============================================================================================


    @Override
    public void showInitialUpdateUi()
    {
        updateMessageTextView.setText("Downloading firmware");
        updateProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFinalUpdateUi()
    {
        updateMessageTextView.setText("Firmware update completed");
        updateProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void updateProgress(float progressValue)
    {

    }

    @Override
    public void updateProgressMessage(String message)
    {

    }

    @Override
    public void showUpdateAbortedDialog(String reason)
    {

    }

    @Override
    public void showFailedToDownloadDialog()
    {
        Toast.makeText(this, "Failed to download firmware", Toast.LENGTH_LONG).show();
        updateMessageTextView.setText("Download failed");
    }
}
