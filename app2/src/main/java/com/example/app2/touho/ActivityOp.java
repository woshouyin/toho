package com.example.app2.touho;

import com.example.app2.touho.utils.ReplayLogger;

public interface ActivityOp {

    void callOptionDialog();
    void callReplayDialog();
    void callSaveReplayDialog(ReplayLogger replayLogger);
    void callQuitDialog();
    void callReplayOverDialog();
    void callRestartDialog();
}
