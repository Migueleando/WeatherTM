package com.mr.weathertm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.mr.weathertm.Data.CityEntry;
import com.mr.weathertm.Data.DataSource;

import org.geonames.GeoNamesException;
import org.geonames.ToponymSearchCriteria;
import org.geonames.WebService;

import java.io.IOException;

public class InfoActivity extends AppCompatActivity {
    private String city;
    private TextView cityNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        city = intent.getStringExtra("cityName");

        cityNameView = (TextView) findViewById(R.id.cityNameText);
        cityNameView.setText(city);
        insertNewCity(city);
        reciveData(city);
    }

    private void reciveData(String city) {

        boolean parsable = true;
        String cityData = null;

        try {
            cityData = new String(city);
        } catch (NumberFormatException ex) {
            parsable = false;
            Toast.makeText(this,
                    "Latitude does not contain a parsable double",
                    Toast.LENGTH_LONG).show();
        }
        parsable = false;
        if (parsable) {
            new GeoNamesTask(cityNameView).execute(cityData);
        }
    }

    private class GeoNamesTask extends AsyncTask<String, Void, String> {
        TextView tResult;

        public GeoNamesTask(TextView vResult) {
            tResult = vResult;
            tResult.setText("");
        }

        @Override
        protected String doInBackground(String... params) {
            return queryGeoNames_countryCode(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            tResult.setText(s);
        }

        private String queryGeoNames_countryCode(String city) {
            String queryResult = "";

            /*
            Do not use the 'demo' account for your app or your tests.
            It is only meant for the sample links on the documentation pages.
            Create your own account instead.
             */
            WebService.setUserName("demo");
            ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
            searchCriteria.setQ(city);

            try {
                queryResult = "name:" + WebService.search(searchCriteria);
            } catch (IOException e) {
                e.printStackTrace();
                queryResult = e.getMessage();
            } catch (GeoNamesException e) {
                e.printStackTrace();
                queryResult = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return queryResult;
        }
    }


    public void insertNewCity(String city) {
        CityEntry.setCityName(city);
        DataSource dataSource = new DataSource(this);
        dataSource.insertCity();
    }
}
