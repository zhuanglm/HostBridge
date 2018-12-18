package com.epocal.epoctestprocedure;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.epocal.common.epocobjects.QATestBuffer;
import com.epocal.common.types.am.TestMode;
import com.epocal.epoctest.TestManager;

public class MultiScreenTestActivity extends BaseActivity {
    ListView mListView;
    IMultiScreenTestPresenter mPresenter;

    @Override
    public void onResume() {
        if(mPresenter != null)
            mPresenter.loadTests();
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_screen_test);
        changeTitle("QA Tests");

        mPresenter = new MultiScreenTestPresenter(this);
        mListView = (ListView) findViewById(R.id.listView_tests);
        mListView.setAdapter(mPresenter.getListViewAdapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, "New Test");
        menu.add(Menu.NONE, 2, Menu.NONE, "Stop All Tests");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 1: // Restarting the bluetooth discovery
                try {
                    TestManager.getInstance().startTest(this, TestMode.QA, QATestBuffer.getInstance().getTestType(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case 2:
                terminateWithOption();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            terminateWithOption();
        }
        return false;
    }

    private void terminateWithOption() {
        new MaterialDialog.Builder(this)
                .content("Stop all tests?")
                .positiveText("yes")
                .positiveColor(getResources().getColor(R.color.primaryBlueNew))
                .negativeText("no")
                .negativeColor(getResources().getColor(R.color.primaryBlueNew))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {
                            mPresenter.stopAllTests();
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .show();
    }
}
