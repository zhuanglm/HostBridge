package com.epocal.host4.dmsettingscreen;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.epocal.common_ui.BaseActivity;
import com.epocal.host4.R;

public class DMSettingActivity extends BaseActivity implements DMSettingScreenContract.View  {

    DMSettingScreenContract.Presenter mPresenter;

    SwitchCompat switcherPresent;
    EditText editTextAddress;
    EditText editTextPort;
    Button buttonSave;
    TextView invalidPort;

    public DMSettingActivity()
    {
        mPresenter = new DMSettingPresenter(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmsetting);
        showHomeBack();
        changeTitle(getString(R.string.data_management));

        switcherPresent  = findViewById(R.id.dmsetting_present_switch);
        editTextAddress = findViewById(R.id.dmsetting_address);
        editTextPort = findViewById(R.id.dmsetting_port);
        buttonSave = findViewById(R.id.dmsetting_save);
        invalidPort = findViewById(R.id.dmsetting_port_invalid);

        invalidPort.setVisibility(View.GONE);
        buttonSave.setEnabled(false);

        mPresenter.load();
        switcherPresent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    editTextAddress.setEnabled(true);
                    editTextPort.setEnabled(true);
                    if (!mPresenter.validateUI())
                    {
                        invalidPort.setVisibility(View.VISIBLE);
                        buttonSave.setEnabled(false);
                    }
                    else {
                        buttonSave.setEnabled(true);
                    }
                }
                else
                {
                    editTextAddress.setEnabled(false);
                    editTextPort.setEnabled(false);
                    invalidPort.setVisibility(View.GONE);
                    buttonSave.setEnabled(true);
                }
            }
        });

        editTextAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mPresenter.validateUI())
                {
                    invalidPort.setVisibility(View.VISIBLE);
                    buttonSave.setEnabled(false);
                }
                else
                {
                    invalidPort.setVisibility(View.GONE);
                    buttonSave.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        editTextPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mPresenter.validateUI())
                {
                    invalidPort.setVisibility(View.VISIBLE);
                    buttonSave.setEnabled(false);
                }
                else
                {
                    invalidPort.setVisibility(View.GONE);
                    buttonSave.setEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        buttonSave.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(mPresenter != null)
                {
                    mPresenter.save();
                }
                buttonSave.setEnabled(false);
            }
        });

        if(switcherPresent.isChecked())
        {
            editTextAddress.setEnabled(true);
            editTextPort.setEnabled(true);
        }
        else
        {
            editTextAddress.setEnabled(false);
            editTextPort.setEnabled(false);
        }
    }

    @Override
    public String getAddress() {
        return editTextAddress.getText().toString();
    }

    @Override
    public String getPort() {
        return editTextPort.getText().toString();
    }

    @Override
    public boolean isPresent() {
        return switcherPresent.isChecked();
    }

    @Override
    public void setAddress(String address) {
        editTextAddress.setText(address);
    }

    @Override
    public void setPort(String port) {
        editTextPort.setText(port);
    }

    @Override
    public void setPresent(boolean present) {
        switcherPresent.setChecked(present);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unload();
    }
}
