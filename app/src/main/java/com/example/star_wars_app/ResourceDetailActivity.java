package com.example.star_wars_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.star_wars_app.utils.SWAPIUtils;

import java.text.DateFormat;

public class ResourceDetailActivity extends AppCompatActivity {

    private TextView mTempDescriptionTV;

    private SWAPIUtils.PersonResource mForecastItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_item_detail);

        mTempDescriptionTV = findViewById(R.id.tv_temp_description);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(SWAPIUtils.EXTRA_RESOURCE)) {
            mForecastItem = (SWAPIUtils.PersonResource)intent.getSerializableExtra(
                    SWAPIUtils.EXTRA_RESOURCE
            );
            fillInLayout(mForecastItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.forecast_item_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //case R.id.action_share:
            //    shareForecast();
            //    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fillInLayout(SWAPIUtils.PersonResource forecastItem) {

        String detailString = getString(R.string.forecast_item_details, forecastItem.name);

        mTempDescriptionTV.setText(detailString);
    }
}
