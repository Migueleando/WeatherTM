package com.mr.weathertm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    ListView listView;
    ProgressDialog mProgressDialog;
    List<ParseObject> ob;
    ArrayAdapter<String> adapter;
    private EditText cityEditText;
    private String searchCitySrt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializa Parse DataBase
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityEditText = (EditText) findViewById(R.id.searchEditText);
                searchCitySrt = cityEditText.getText().toString();
                cityEditText.setText("");
                adapter.notifyDataSetChanged();
                searchCity(view, searchCitySrt);
            }
        });
    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(MainActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Locate the class table named "Cities" in Parse.com
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "Cities");
            query.orderByDescending("_created_at");
            try {
                ob = query.find();
            } catch (com.parse.ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listView in listview_main.xml
            listView = (ListView) findViewById(R.id.citiesListView);
            // Pass the results into an ArrayAdapter
            adapter = new ArrayAdapter<>(MainActivity.this,
                    R.layout.listview_item);
            // Retrieve object "name" from Parse.com database
            for (ParseObject Cities : ob) {
                adapter.add((String) Cities.get("name"));
            }
            // Binds the Adapter to the ListView
            listView.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
            // Capture button clicks on ListView items
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // Send single item click data to InfoActivity Class
                    Intent i = new Intent(MainActivity.this,
                            InfoActivity.class);
                    // Pass data "name" followed by the position
                    i.putExtra("cityName", ob.get(position).getString("name"));
                    // Open InfoActivity.java Activity
                    startActivity(i);
                }
            });
        }
    }


    private void searchCity(View view, String searchCitySrt) {
        // Force user to fill up the form
        if (searchCitySrt.equals("")) {
            Snackbar.make(view, "Please, enter the city's name", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            Intent intent = new Intent(this, InfoActivity.class);
            intent.putExtra("cityName", searchCitySrt);
            startActivity(intent);
            finish();
        }
    }
}
