package com.example.briru.budgetapptest;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;

public class NavActivity extends AppCompatActivity {

    static ProgressBar spinner;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_progress:
                    spinner.setVisibility(View.VISIBLE);
                    getSupportActionBar().setTitle("Your Progress");
                    HomeFragment hf = new HomeFragment();
                    FragmentTransaction fragTrans1 = getSupportFragmentManager().beginTransaction();
                    fragTrans1.replace(R.id.frameLayout,hf,"ProgressFragment");
                    fragTrans1.commit();
                    return true;
                case R.id.nav_add:
                    spinner.setVisibility(View.GONE);
                    getSupportActionBar().setTitle("Add Cash/Items");
                    AddFragment af = new AddFragment();
                    FragmentTransaction fragTrans2 = getSupportFragmentManager().beginTransaction();
                    fragTrans2.replace(R.id.frameLayout,af,"AddFragment");
                    fragTrans2.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nav);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        spinner.getIndeterminateDrawable()
                .setColorFilter(ContextCompat.getColor(this, R.color.moneycolor), PorterDuff.Mode.SRC_IN );
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView navigation =  findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportActionBar().setTitle("Your progress");
        HomeFragment hf = new HomeFragment();
        FragmentTransaction fragTrans1 = getSupportFragmentManager().beginTransaction();
        fragTrans1.replace(R.id.frameLayout,hf,"ProgressFragment");
        fragTrans1.commit();

        Button signoutBtn = findViewById(R.id.signoutBtn);
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdentityManager.getDefaultIdentityManager().signOut();
                AuthenticatorActivity.signinUI = (SignInUI) AWSMobileClient.getInstance().getClient(NavActivity.this, SignInUI.class);
                AuthenticatorActivity.signinUI.login(NavActivity.this, AuthenticatorActivity.class).authUIConfiguration(AuthenticatorActivity.config).execute();
            }
        });
    }

}
