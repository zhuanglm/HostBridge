package com.epocal.host4.homescreen;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ListView;
import android.widget.TextView;

import com.epocal.common.epocobjects.QATestBuffer;
import com.epocal.common.types.TestType;
import com.epocal.common.types.am.TestMode;
import com.epocal.epoctest.TestManager;
import com.epocal.host4.BaseActivity;
import com.epocal.host4.R;

import java.util.ArrayList;

public class QATestMenuActivity extends BaseActivity {
    TextView performQATest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qatest_menu);
        changeTitle(getString(R.string.qa_testmenu_testtitle));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, com.epocal.epoctestprocedure.R.color.primaryBlueNew)));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        QAMenuAdapter adapter = new QAMenuAdapter(this, generateData(this));
        ListView listView = findViewById(R.id.listview_qatestmenu);
        if (listView != null) {
            listView.setAdapter(adapter);
        }
    }

    private ArrayList<QAMenuModel> generateData(final Context context){
        ArrayList<QAMenuModel> models = new ArrayList<>();
        models.add(new QAMenuModel(getString(R.string.qa_testmenu_subtitle_testnow)));
        models.add(new QAMenuModel(R.mipmap.ic_new, getString(R.string.perform_qa_test), new QAMenuModel.MenuListener() {
            @Override
            public void onMenuClicked() {
                QATestBuffer.getInstance().setTestType(TestType.Unknown);
                TestManager.getInstance().navigateToReaderDiscovery(context, TestMode.QA, TestType.Unknown, true);
            }
        }));
        models.add(new QAMenuModel(getString(R.string.qa_testmenu_subtitle_testhistory)));
        models.add(new QAMenuModel(R.mipmap.ic_qc_history, getString(R.string.qa_test_history), new QAMenuModel.MenuListener() {
            @Override
            public void onMenuClicked() {
                QATestBuffer.getInstance().setTestType(TestType.Unknown);
                TestManager.getInstance().navigateToQATestHistory(context, TestMode.QA, true);
            }
        }));
        models.add(new QAMenuModel(R.mipmap.ic_electronic_history, getString(R.string.ec_test_history), null));
        models.add(new QAMenuModel(R.mipmap.ic_thermal_history, getString(R.string.thermal_test_history), null));

        return models;
    }
}
