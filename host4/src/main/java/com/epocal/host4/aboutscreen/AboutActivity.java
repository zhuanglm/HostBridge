package com.epocal.host4.aboutscreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.epocal.common.eventmessages.EpocNavigationObject;
import com.epocal.common.types.UIScreens;
import com.epocal.host4.R;

import com.epocal.host4.aboutscreen.di.AboutScreenModule;
import com.epocal.host4.aboutscreen.di.DaggerAboutScreenComponent;
import com.epocal.host4.init.App;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;


public class AboutActivity extends AppCompatActivity implements AboutScreenContract.View {

    // TODO: this is a temporary Activity to test DI

    AboutActivity thus;
    RecyclerView recyclerView;
    Button gotoMain;
    // di majic
    @Inject
    AboutPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_test);
        thus = this;
        // do the injection
        DaggerAboutScreenComponent.builder()
                .appComponent(((App) getApplicationContext()).getAppComponent())
                .aboutScreenModule(new AboutScreenModule(this))
                .build().inject(this);

        // setup recycler view showing all existing users
        recyclerView = (RecyclerView) findViewById(R.id.main_activity_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        AboutRecycleViewAdapter adapter = new AboutRecycleViewAdapter(this, mPresenter.loadUsers(mRealm));
//        recyclerView.setAdapter(adapter);

        gotoMain = (Button)findViewById(R.id.btnNavigateToMain);
        gotoMain.setOnClickListener(NavigateToLogin);
    }

    // create clicklistener delegate handler
    View.OnClickListener NavigateToLogin = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            EpocNavigationObject message = new EpocNavigationObject();
            message.setContext(thus);
            message.setTargetscreen(UIScreens.LoginScreen);
            EventBus.getDefault().post(message);
        }
    };

    @Override
    public void showUsers() {
    }


    @Override
    public void showError(String message) {
        //Show error message Toast
        Toast.makeText(getApplicationContext(), "Error" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showComplete() {
        //Show completed message Toast
        Toast.makeText(getApplicationContext(), "That's all I could find", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
