package com.gotenna.sdk.sample.firmware;

import com.gotenna.sdk.firmware.GTFirmwareVersion;

/**
 * Created on 9/20/19.
 *
 * @author Pedro Torres
 */
public class FirmwareUpdateContract
{
    public interface FirmwareUpdateView
    {
        void showInitialUpdateUi();
        void showFinalUpdateUi();
        void updateProgress(float progressValue);
        void updateProgressMessage(String message);
        void showUpdateAbortedDialog(String reason);
        void showFailedToDownloadDialog();
    }

    public interface FirmwareUpdatePresenter
    {
        void onDestroy();
        void downloadFromUrl(String url);
        void startUpdate(GTFirmwareVersion firmwareVersion, byte[] firmwareData);
    }
}
