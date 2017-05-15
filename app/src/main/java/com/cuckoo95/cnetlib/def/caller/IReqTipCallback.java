package com.cuckoo95.cnetlib.def.caller;

/**
 *
 * @author Cuckoo
 * @date 2016-09-05
 * @descripiton
 *      To manage the show or dismiss of loading dialog.
 */
public interface IReqTipCallback {
    /**
     * Show loading dialog.
     */
    void showLoading();

    /**
     * Dismiss loading dialog.
     */
    void dismissLoading();

    /**
     * Show tip dialog.
     * @param message
     *
     */
    void showTipDialog(String message);
}
