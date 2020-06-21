package com.example.covid19app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView tvcases,tvRecovered,tvCritical,tvActive,tvTodayCases,tvTotalDeaths,tvTodayDeaths,tvAffectedContries;
    SimpleArcLoader simpleArcLoader;
    ScrollView scrollView;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvcases=findViewById(R.id.tvCases);
        tvRecovered=findViewById(R.id.Recover);

        tvCritical=findViewById(R.id.Critical);
        tvActive=findViewById(R.id.Active);
        tvTodayCases=findViewById(R.id.TodayCases);
        tvTodayDeaths=findViewById(R.id.TodayDeaths);
        tvTotalDeaths=findViewById(R.id.TodayDeaths);
        tvAffectedContries=findViewById(R.id.AffectedContries);
        simpleArcLoader=findViewById(R.id.loader);
        tvAffectedContries=findViewById(R.id.AffectedContries);

        scrollView=findViewById(R.id.scrollStats);
        pieChart=findViewById(R.id.piechart);


        fetchData();


    }

    private void fetchData() {

        String url="https://corona.lmao.ninja/v2/all";
        simpleArcLoader.start();
        StringRequest request=new StringRequest(StringRequest.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response.toString());

                            tvcases.setText(jsonObject.getString("cases"));
                            tvRecovered.setText(jsonObject.getString("recovered"));
                            tvCritical.setText(jsonObject.getString("critical"));
                            tvActive.setText(jsonObject.getString("active"));
                            tvTodayCases.setText(jsonObject.getString( "todayCases"));
                            tvTodayDeaths.setText(jsonObject.getString("todayDeaths"));
                            tvTotalDeaths.setText(jsonObject.getString("deaths"));
                            tvAffectedContries.setText(jsonObject.getString(  "affectedCountries"));


                            pieChart.addPieSlice(new PieModel("cases",Integer.parseInt(tvcases.getText().toString()), Color.parseColor("#FFA726")));
                            pieChart.addPieSlice(new PieModel("recoverd",Integer.parseInt(tvRecovered.getText().toString()), Color.parseColor("#66BB6A")));
                            pieChart.addPieSlice(new PieModel("deaths",Integer.parseInt(tvTotalDeaths.getText().toString()), Color.parseColor("#EF5350")));
                            pieChart.addPieSlice(new PieModel("active",Integer.parseInt(tvActive.getText().toString()), Color.parseColor("#29B6F6")));
                            pieChart.startAnimation();
                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View. GONE);
                            scrollView.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View. GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View. GONE);
                scrollView.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);

            }

    public void goTrackCountries(View View){

    startActivity(new Intent(getApplicationContext(),AffectedCountry.class));
    }
}
