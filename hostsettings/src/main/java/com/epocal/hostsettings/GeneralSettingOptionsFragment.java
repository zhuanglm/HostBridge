package com.epocal.hostsettings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.epocal.common_ui.types.UIEditFieldType;
import com.epocal.common_ui.types.UIValueBucket;
import com.epocal.common_ui.types.UIValueBucketList;

import java.util.ArrayList;


@SuppressLint("ValidFragment")
public class GeneralSettingOptionsFragment extends Fragment {

    public interface IGeneralSettingOptionsCallback {
        void onUIInit(GeneralSettingOptionsFragment obj, LinearLayout mainLayout);

        void onPotiveAction(GeneralSettingOptionsFragment obj, int value);

        void onPotiveAction(GeneralSettingOptionsFragment obj, String value);

        void onNegativeAction(GeneralSettingOptionsFragment obj, int value);
    }

    public static final String bundle_Value_List = "Value_List";
    public static final String bundle_Value_Current = "Value_Current";

    private final Context mContext;
    private UIEditFieldType mUIEditFieldType;
    private LinearLayout mMainLayout;
    private GeneralSettingOptionsFragment instance;
    private IGeneralSettingOptionsCallback mIGeneralSettingOptionsCallback;

    public void setIGeneralSettingOptionsCallback(IGeneralSettingOptionsCallback iGeneralSettingOptionsCallback) {
        this.mIGeneralSettingOptionsCallback = iGeneralSettingOptionsCallback;
    }

    public GeneralSettingOptionsFragment(Context context, UIEditFieldType uiEditFieldType) {
        super();
        mContext = context;
        mUIEditFieldType = uiEditFieldType;
        instance = this;
    }

    public Context getmContext() {
        return mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceSate) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return inflater.inflate(R.layout.fragment_general_setting_options, group, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void createView() {
        if (this.getView() != null) {
            mMainLayout = this.getView().findViewById(R.id.mainLayout);
            ExpandableListView expandableListView = mMainLayout.findViewById(R.id.expListView);
            expandableListView.setVisibility(View.GONE);

            if (mUIEditFieldType.value == UIEditFieldType.RadioButton.value) {
                createUIRadioGroup(mMainLayout);
            } else if (mUIEditFieldType.value == UIEditFieldType.TextView.value) {
                createUITextViewList(mMainLayout);
            } else if (mUIEditFieldType.value == UIEditFieldType.EditTextBox.value) {
                createUIEditTextBox(mMainLayout);
            } else if (mUIEditFieldType.value == UIEditFieldType.CustomLayout.value) {
                createUICustom(mMainLayout);
            } else if (mUIEditFieldType.value == UIEditFieldType.CheckBox.value) {
                createUICheckboxList(mMainLayout);
            } else if (mUIEditFieldType.value == UIEditFieldType.Combination.value) {
                createUICombinationList(mMainLayout);
            }
        }
    }

    private void createUITextViewList(LinearLayout mainLayout) {
        Bundle bundle = getArguments();
        ArrayList<UIValueBucket> list = bundle.getParcelableArrayList(GeneralSettingOptionsFragment.bundle_Value_List);
        int currentindex = bundle.getInt(GeneralSettingOptionsFragment.bundle_Value_Current);

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                TextView xbox = new TextView(mContext);
                ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                xbox.setLayoutParams(params);
                xbox.setId(list.get(i).getID());
                xbox.setText(list.get(i).getTextString());
                xbox.setTextSize(17);
                xbox.setGravity(Gravity.CENTER_VERTICAL);
                xbox.setPadding(20, 12, 0, 12);
                xbox.setTag(R.layout.fragment_general_setting_options, list.get(i).getID());
                xbox.setClickable(true);
                xbox.setFocusable(true);
                mainLayout.addView(xbox);
                xbox.setOnClickListener(new TextView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selectedId = (int) v.getTag(R.layout.fragment_general_setting_options);
                        if (mIGeneralSettingOptionsCallback != null) {
                            mIGeneralSettingOptionsCallback.onPotiveAction(null, selectedId);
                        }
                    }
                });

                ViewGroup.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                params2.height = 1;
                View v = new View(mContext);
                v.setLayoutParams(params2);
                v.setBackgroundColor(Color.LTGRAY);
                mainLayout.addView(v);
            }
        }
    }

    private void createUIEditTextBox(LinearLayout mainLayout) {
        Bundle bundle = getArguments();
        String currentVal = bundle.getString(GeneralSettingOptionsFragment.bundle_Value_Current);

        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        final EditText editBox = new EditText(mContext);
        ViewGroup.LayoutParams paramsBox = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        editBox.setLayoutParams(params);
        editBox.setId(0);
        editBox.setText(currentVal);
        editBox.setTextSize(17);
        editBox.setGravity(Gravity.CENTER_VERTICAL);
        editBox.setPadding(20, 12, 0, 12);
        editBox.setTag(R.layout.fragment_general_setting_options, 0);
        editBox.setFocusable(true);
        if(this.getView() != null) {
            LinearLayout innerLayout2 = this.getView().findViewById(R.id.beforeExpListView);
            innerLayout2.addView(editBox);
        }
        editBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideAlert();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        AppCompatButton button = this.getView().findViewById(R.id.button_Positive);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIGeneralSettingOptionsCallback != null) {
                    mIGeneralSettingOptionsCallback.onPotiveAction(instance, editBox.getText().toString());
                }
            }
        });
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editBox, InputMethodManager.SHOW_FORCED);
        }
        editBox.requestFocus();
    }

    private void createUIRadioGroup(LinearLayout mainLayout) {
        Bundle bundle = getArguments();
        ArrayList<UIValueBucket> list = bundle.getParcelableArrayList(GeneralSettingOptionsFragment.bundle_Value_List);
        int currentindex = bundle.getInt(GeneralSettingOptionsFragment.bundle_Value_Current);

        RadioGroup radioGroup = new RadioGroup(mContext);
        mainLayout.addView(radioGroup);
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 20, 10, 5);

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                /*
                RadioButton tv = new RadioButton(mContext);
                tv.setText("Dynamic layouts ftw!");
                tv.setGravity(Gravity.LEFT);
                tv.setTextSize(17);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                tv.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

                group.addView(tv);
                ViewGroup.LayoutParams iv_params_b = tv.getLayoutParams();
                iv_params_b.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                iv_params_b.width = ViewGroup.LayoutParams.MATCH_PARENT;
                tv.setLayoutParams(iv_params_b);
                */
                params.setMargins(10, 5, 10, 5);
                boolean selected = i == currentindex;
                RadioButton radio = createRadioButton(i, list.get(i).getID(), list.get(i).getTextString(), selected, params);
                radioGroup.addView(radio);
            }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonID = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) group.getChildAt(radioButtonID);
                int selectedId = (int) rb.getTag(R.layout.fragment_general_setting_options);
                if (mIGeneralSettingOptionsCallback != null) {
                    mIGeneralSettingOptionsCallback.onPotiveAction(null, selectedId);
                }
            }
        });
    }

    private void createUICheckboxList(LinearLayout mainLayout) {
        Bundle bundle = getArguments();
        ArrayList<UIValueBucket> list = bundle.getParcelableArrayList(GeneralSettingOptionsFragment.bundle_Value_List);
        int currentindex = bundle.getInt(GeneralSettingOptionsFragment.bundle_Value_Current);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 10, 20, 10);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {

                boolean selected = list.get(i).getValueString().equals("TRUE");
                CheckBox tv = createCheckBox(i, list.get(i).getID(), list.get(i).getTextString(), selected, params);
                mainLayout.addView(tv);
            }
        }
    }

    private void createUICustom(LinearLayout mainLayout) {
        if (mIGeneralSettingOptionsCallback != null) {
            mIGeneralSettingOptionsCallback.onUIInit(this, mainLayout);
        }
    }

    private void createUICombinationList(LinearLayout mainLayout) {
        Bundle bundle = getArguments();
        ArrayList<UIValueBucketList> list = bundle.getParcelableArrayList(GeneralSettingOptionsFragment.bundle_Value_List);
        int currentindex = bundle.getInt(GeneralSettingOptionsFragment.bundle_Value_Current);
        LinearLayout linearLayoutListView = mainLayout.findViewById(R.id.beforeExpListView);

        boolean hasExpandable = false;
        ArrayList<UIValueBucketList> listExp = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).isExtendable()) {
                    for (int j = 0; j < list.get(i).getListItem().size(); j++) {
                        if (list.get(i).getListItem().get(j).getUIEditFieldType() == UIEditFieldType.EditTextBox) {
                            createtEditTextView(list.get(i).getListItem().get(j), linearLayoutListView);
                        }
                    }
                }
                if (list.get(i).isExtendable()) {
                    hasExpandable = true;
                    listExp.add(list.get(i));
                }
            }
        }

        if (hasExpandable) {
            ExpandableListView expandableListView = mainLayout.findViewById(R.id.expListView);
            GeneralSettingOptionsExpandableAdapter adapter = new GeneralSettingOptionsExpandableAdapter(mContext, listExp);
            expandableListView.setAdapter(adapter);
            expandableListView.setVisibility(View.VISIBLE);
        }
        createPositiveButton(mainLayout).setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = (int) v.getTag(R.layout.fragment_general_setting_options);
                if (mIGeneralSettingOptionsCallback != null) {
                    mIGeneralSettingOptionsCallback.onPotiveAction(null, 0);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mMainLayout != null)
            mMainLayout.removeAllViews();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        super.onDestroy();
    }

    public void customView(@LayoutRes int layoutRes, boolean wrapInScrollView) {
        LayoutInflater li = LayoutInflater.from(mContext);
        customView(li.inflate(layoutRes, null), wrapInScrollView);
    }

    public void customView(@NonNull View view, boolean wrapInScrollView) {
        if (wrapInScrollView) {
            ScrollView scrollView = new ScrollView(mContext);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params.setMarginStart(16);
            params.setMarginEnd(16);
            scrollView.setLayoutParams(params);
            mMainLayout.addView(scrollView);
            scrollView.addView(view);
        } else {
            mMainLayout.addView(view);
        }
    }

    public void triggerPositive() {
        if (mIGeneralSettingOptionsCallback != null) {
            mIGeneralSettingOptionsCallback.onPotiveAction(this, 0);
        }
    }

    public void triggerNegative() {
        if (mIGeneralSettingOptionsCallback != null) {
            mIGeneralSettingOptionsCallback.onNegativeAction(this, 0);
        }
    }

    private CheckBox createCheckBox(int index, int id, String title, boolean selected, LinearLayout.LayoutParams params) {
        CheckBox tv = new CheckBox(mContext);
        tv.setId(index);
        tv.setText(title);
        tv.setTextSize(17);
        tv.setGravity(Gravity.START);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        tv.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        tv.setTag(R.layout.fragment_general_setting_options, id);
        if (selected) {
            tv.setChecked(true);
        } else {
            tv.setChecked(false);
        }
        tv.setLayoutParams(params);

        tv.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = (int) v.getTag(R.layout.fragment_general_setting_options);
                if (mIGeneralSettingOptionsCallback != null) {
                    mIGeneralSettingOptionsCallback.onPotiveAction(null, selectedId);
                }
            }
        });
        return tv;
    }

    private RadioButton createRadioButton(int index, int id, String title, boolean selected, LinearLayout.LayoutParams params) {
        RadioButton radio = new RadioButton(mContext);
        radio.setId(index);
        radio.setText(title);
        radio.setTextSize(17);
        radio.setGravity(Gravity.START);
        radio.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        radio.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        params.setMargins(10, 5, 10, 5);
        radio.setLayoutParams(params);
        radio.setTag(R.layout.fragment_general_setting_options, id);
        if (selected) {
            radio.setChecked(true);
        }
        return radio;
    }

    private void createtEditTextView(UIValueBucket bucket, LinearLayout linearLayoutListView) {
        LinearLayout innner = new LinearLayout(mContext);

        innner.setOrientation(LinearLayout.HORIZONTAL);
        innner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        innner.setGravity(Gravity.CENTER);

        TextView xbox = new TextView(mContext);
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        xbox.setLayoutParams(params);
        xbox.setId(bucket.getID());
        xbox.setText(bucket.getTextString());
        xbox.setTextSize(17);
        xbox.setGravity(Gravity.CENTER_VERTICAL);
        xbox.setPadding(20, 12, 0, 12);
        innner.addView(xbox);

        EditText editBox = new EditText(mContext);
        ViewGroup.LayoutParams paramsBox = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        editBox.setLayoutParams(params);
        editBox.setBackground(null);
        editBox.setId(bucket.getID());
        editBox.setText(bucket.getValueString());
        editBox.setTextSize(17);
        editBox.setGravity(Gravity.CENTER_VERTICAL);
        editBox.setPadding(20, 12, 0, 12);
        editBox.setTag(R.layout.fragment_general_setting_options, bucket.getID());
        editBox.setFocusable(true);
        innner.addView(editBox);

        linearLayoutListView.addView(innner);

        editBox.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = (int) v.getTag(R.layout.fragment_general_setting_options);
                if (mIGeneralSettingOptionsCallback != null) {
                    mIGeneralSettingOptionsCallback.onPotiveAction(null, selectedId);
                }
            }
        });

        ViewGroup.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        params2.height = 1;
        View v = new View(mContext);
        v.setLayoutParams(params2);
        v.setBackgroundColor(Color.LTGRAY);
        linearLayoutListView.addView(v);
    }

    private AppCompatButton createPositiveButton(LinearLayout linearLayoutListView) {
        AppCompatButton button = new AppCompatButton(mContext);
        LinearLayout.LayoutParams size = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(size);
        button.setGravity(Gravity.CENTER);
        button.setText(getResources().getText(R.string.save));
        button.setBackgroundColor(getResources().getColor(R.color.primaryBlueNew));
        button.setTextColor(getResources().getColor(R.color.colorWhite));
        button.setHeight(24);
        linearLayoutListView.addView(button);
        return button;
    }

    public void showAlert(String alert) {
        if (this.getView() != null) {
            TextView errorView = this.getView().findViewById(R.id.general_setting_edit_error_message);
            if (errorView != null) {
                errorView.setText(alert);
                errorView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void hideAlert() {
        if (this.getView() != null) {
            TextView errorView = this.getView().findViewById(R.id.general_setting_edit_error_message);
            if (errorView != null)
                errorView.setVisibility(View.GONE);
        }
    }
}

