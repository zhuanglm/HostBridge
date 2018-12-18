package com.epocal.epoctest.uidriver;

import android.content.Context;

import com.epocal.common.epocobjects.AnalyteOption;
import com.epocal.common.types.EpocTestPanelType;
import com.epocal.common.types.UIChangeRequestReason;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.epocexceptions.EpocTestDriverException;
import com.epocal.common.types.EpocTestFieldType;
import com.epocal.epoctest.uimessage.TestMessageContext;

import java.util.ArrayList;
import java.util.EnumSet;


/**
 * Created by bmate on 7/12/2017.
 */

public interface IUITestDriver {

    void show();
    void hide();
    void start();
    void restart();
    void fullReset() throws EpocTestDriverException;
    void resetUI(UIChangeRequestReason reason);
    void terminateWithOption(final Context context);
    void showCommunicationBoard();
    void loadInputScreen(int testVariableUIFieldType);
    void loadCustomVariableScreen(String variablename);
    void setCustomTestInclusions(EnumSet<AnalyteName> analyteTypes);
    void setTestInclusions(String panelName);
    void setTestInclusions(EpocTestPanelType testPanelType);
    ArrayList<AnalyteName> getRuntimeTestInclusions();
    ArrayList<AnalyteOption> getCustomSelectedTests ();
    //List<UIAnalyteGroupDisplay> GetResults();
    boolean showCustomTestSelectionOnly();
    void updateCommunicationBoard(TestMessageContext messageContext);

}
