package com.shail.google.cast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.cast.samples.democastplayer.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class CastModeSelectActivity extends ActionBarActivity {
    private static final int REQUEST_GMS_ERROR = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mode_select_activity);

        Button button = (Button) findViewById(R.id.sdk_mode_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SDKCastActivity.class);
            }
        });

        button = (Button) findViewById(R.id.mrp_mode_button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MediaRouterCastActivity.class);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.mode_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, CastSettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startActivity(Class<?> classType) {
        startActivity(new Intent(CastModeSelectActivity.this, classType));
    }

    @Override
    public void onResume() {
        super.onResume();

        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (errorCode != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, REQUEST_GMS_ERROR).show();
        }
    }

}
