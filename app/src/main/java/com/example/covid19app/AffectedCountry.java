package com.example.covid19app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AffectedCountry extends AppCompatActivity {
    EditText edtsearch;
    ListView listView;
    SimpleArcLoader simpleArcLoader;
    public static List<CountryModel>countryModelsList=new ArrayList<>();
    CountryModel countryModel;
    MYCustomAdapter myCustomAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_country);
        edtsearch=findViewById(R.id.edtSearch);
        listView=findViewById(R.id.listView);
        simpleArcLoader=findViewById(R.id.loader);
        fetchData();
    }
    private void fetchData() {

        String url="https://corona.lmao.ninja/v2/countries";
        simpleArcLoader.start();
        StringRequest request=new StringRequest(StringRequest.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=jsonArray .getJSONObject(i);
                                String countryName=jsonObject.getString("country");
                                String cases=jsonObject.getString("cases");
                                String todaycases=jsonObject.getString("todaycases");
                                String deaths=jsonObject.getString("deaths");
                                String todayDeaths=jsonObject.getString("todayDeaths");
                                String recovered=jsonObject.getString("recovered");
                                String active=jsonObject.getString("active");
                                String  critical=jsonObject.getString("critical");
                                JSONObject object=jsonObject.getJSONObject("countryInfo");
                                String flagUrl=object.getString("flag");
                                countryModel =new CountryModel(flagUrl,countryName,cases,todaycases,deaths,todayDeaths,recovered,active,critical);
                                countryModelsList.add(countryModel);
                            }
                            myCustomAdapter=new MYCustomAdapter(AffectedCountry.this,countryModelsList);
                            listView.setAdapter(myCustomAdapter);
                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);

                Toast.makeText(AffectedCountry.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);

    }
}
