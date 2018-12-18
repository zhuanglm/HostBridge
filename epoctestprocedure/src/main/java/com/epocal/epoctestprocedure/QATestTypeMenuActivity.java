package com.epocal.epoctestprocedure;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.epocal.common.epocobjects.QATestBuffer;
import com.epocal.common.types.TestType;
import com.epocal.common.types.am.TestMode;
import com.epocal.epoctest.TestManager;
import com.epocal.hardware.EMDKScanner;

public class QATestTypeMenuActivity extends BaseActivity {
    EditText mLotNumberEditText;
    RadioGroup mTestTypeRadioGroup;
    ImageView mImageViewBarcode;
    EMDKScanner mEMDKScanner;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qatest_type_menu);
        changeTitle("Run QA Test");

        mLotNumberEditText = findViewById(R.id.editLotNumberText);
        mTestTypeRadioGroup = findViewById(R.id.testTypeGroup);
        mTestTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked)
                {
                    if(checkedId == R.id.radioButtonQC) {
                        runQATest(TestType.QualityControl);
                    } else if(checkedId == R.id.radioButtonEQC) {
                        runQATest(TestType.EQC);
                    } else if(checkedId == R.id.radioButtonProficiency) {
                        runQATest(TestType.Proficiency);
                    } else if(checkedId == R.id.radioButtonThermalCheck) {
                        runQATest(TestType.ThermalCheck);
                    } else if(checkedId == R.id.radioButtonOther) {
                        runQATest(TestType.Other);
                    } else if(checkedId == R.id.radioButtonCalVer) {
                        runQATest(TestType.CalVer);
                    }
                }
            }
        });
        mEMDKScanner = new EMDKScanner(this);
        mImageViewBarcode = findViewById(R.id.image_view_barcode);
        mImageViewBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do scanner stuff
            }
        });
    }

    private void runQATest(TestType testType) {
        QATestBuffer.getInstance().setTestType(testType);
        TestManager.getInstance().navigateToReaderDiscovery(this, TestMode.QA, testType, true);
    }
}
