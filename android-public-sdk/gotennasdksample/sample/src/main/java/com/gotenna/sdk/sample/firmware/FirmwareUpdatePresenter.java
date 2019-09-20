package com.gotenna.sdk.sample.firmware;

import android.support.annotation.NonNull;
import android.util.Log;

import com.gotenna.sdk.exceptions.GTFirmwareFileDataException;
import com.gotenna.sdk.exceptions.GTFirmwareNrfException;
import com.gotenna.sdk.firmware.GTFirmwareDownloadHelper;
import com.gotenna.sdk.firmware.GTFirmwareFileDownloadInfo;
import com.gotenna.sdk.firmware.GTFirmwareUpdater;
import com.gotenna.sdk.firmware.GTFirmwareVersion;

/**
 * Created on 9/20/19.
 *
 * @author Pedro Torres
 */
public class FirmwareUpdatePresenter implements FirmwareUpdateContract.FirmwareUpdatePresenter, GTFirmwareUpdater.GTFirmwareUpdaterListener
{

    private static final String TAG = "FirmwareUpdatePresenter";

    private FirmwareUpdateContract.FirmwareUpdateView updateView;
    private GTFirmwareUpdater firmwareUpdater;

    public FirmwareUpdatePresenter(FirmwareUpdateContract.FirmwareUpdateView view)
    {
        this.updateView = view;
    }

    //==============================================================================================
    // FirmwareUpdateContract.FirmwareUpdatePresenter implementation
    //==============================================================================================

    @Override
    public void onDestroy()
    {
        updateView = null;
        if (firmwareUpdater != null) firmwareUpdater.stopFirmwareUpdate();
        Log.d(TAG, "onDestroy: Update stopped");
    }

    @Override
    public void downloadFromUrl(String url)
    {
        updateView.showInitialUpdateUi();

        GTFirmwareDownloadHelper.downloadFirmwareFromUrl(url, new GTFirmwareDownloadHelper.GTFirmwareDownloadHelperListener()
        {
            @Override
            public void onDownloadedFirmware(GTFirmwareVersion firmwareVersion, byte[] firmwareData)
            {
                //startUpdate(firmwareVersion, firmwareData);
                Log.d(TAG, "onDownloadedFirmware: " + firmwareVersion.toString() + " is " + firmwareData.length + " bytes");
                updateView.showFinalUpdateUi();
            }

            @Override
            public void onFailedToDownloadFirmware()
            {
                updateView.showFailedToDownloadDialog();
            }
        });
    }

    @Override
    public void startUpdate(GTFirmwareVersion firmwareVersion, byte[] firmwareData)
    {
        try
        {
            GTFirmwareFileDownloadInfo fileDownloadInfo = new GTFirmwareFileDownloadInfo(firmwareVersion, firmwareData);
            firmwareUpdater = new GTFirmwareUpdater(firmwareVersion, fileDownloadInfo, this);
            firmwareUpdater.startFirmwareUpdate();
        }
        catch (GTFirmwareFileDataException | GTFirmwareNrfException e)
        {

            updateView.showUpdateAbortedDialog(e.getMessage());
        }

    }

    //==============================================================================================
    // GTFirmwareUpdater.GTFirmwareUpdaterListener implementation
    //==============================================================================================

    @Override
    public void onFirmwareWritingStateUpdated(@NonNull GTFirmwareUpdater.FirmwareUpdateState firmwareUpdateState)
    {
        Log.d(TAG, "onFirmwareWritingStateUpdated: " + firmwareUpdateState.toString());

        switch (firmwareUpdateState)
        {
            case ABORT:

                break;
            case WRITING:

                break;
            case UPDATE_FAILED:

                break;
            case FINALIZING:

                break;
            case UPDATE_SUCCEEDED:
                updateView.showFinalUpdateUi();
                break;
        }

        updateView.updateProgressMessage(firmwareUpdateState.toString());
    }

    @Override
    public void onFirmwareWriteProgressUpdated(float progressValue)
    {
        updateView.updateProgress(progressValue);
    }

}
