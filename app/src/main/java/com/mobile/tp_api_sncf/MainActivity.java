package com.mobile.tp_api_sncf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "REQUEST";
    String url = "https://api.sncf.com/v1/coverage/sncf/";

    RequestQueue queue;
    JSONObject jsonfound = new JSONObject();
    ArrayList<String> jsonToDisplay = new ArrayList<>();
    String[] data1, data2;

    TextView textView;
    EditText etURL;

    RecyclerView rvObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textView = findViewById(R.id.text);
        this.etURL = findViewById(R.id.etURL);
        this.rvObject = findViewById(R.id.rvObject);

//        RecyclerAdapter ra = new RecyclerAdapter(this, this.jsonToDisplay);

    }

    public void setRV(){
        RecyclerAdapter ra = new RecyclerAdapter(this, this.data1, this.data2);
        this.rvObject.setAdapter(ra);
        this.rvObject.setLayoutManager(new LinearLayoutManager(this));
    }

    public void getCommercialModes(View v){
        this.etURL.setText("commercial_modes");

        getApiResponse();
        setCommercialModesTextResponse();
        setRV();
    }

    public void getJourneysBetween(View v){
        this.etURL.setText("journeys?from=admin:fr:75056&to=admin:fr:69123&datetime=20211229T145220");

        getApiResponse();
        setJourneysBetweenTextResponse();
        setRV();
    }

    public void getJourneysFrom(View v){
        this.etURL.setText("stop_areas/stop_area:SNCF:87391003/departures?datetime=20211229T145220");

        getApiResponse();
        setJourneysFromTextResponse();
        setRV();
    }

    public void getApiResponse() {
        String urlComplete = this.url + this.etURL.getText();

        queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlComplete, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        jsonfound = response;
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                }
                ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "f7773b94-e980-472f-9128-882905f29d1c");

                return params;
            }
        };
        jsonObjectRequest.setTag(TAG);
        queue.add(jsonObjectRequest);
    }

    private void setCommercialModesTextResponse() {
        try {
            JSONArray jsonArray = jsonfound.getJSONArray("commercial_modes");

            this.data1 = new String[jsonArray.length()];
            this.data2 = new String[jsonArray.length()];

            for(int i=0; i<jsonArray.length(); i++) {
                this.data1[i] = jsonArray.getJSONObject(i).get("id").toString();

                this.data2[i] = jsonArray.getJSONObject(i).get("name").toString();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setJourneysBetweenTextResponse() {
        try {
            JSONArray jsonArray = jsonfound.getJSONArray("journeys").getJSONObject(0).getJSONArray("sections");

            this.data1 = new String[jsonArray.length()];
            this.data2 = new String[jsonArray.length()];

            for(int i=0; i<jsonArray.length(); i++) {
                this.data1[i] = jsonArray.getJSONObject(i).getJSONObject("from").get("name").toString();
                this.data2[i] = jsonArray.getJSONObject(i).getJSONObject("to").get("name").toString();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setJourneysFromTextResponse() {
        try {
            JSONArray jsonArray = jsonfound.getJSONArray("departures");

            this.data1 = new String[jsonArray.length()];
            this.data2 = new String[jsonArray.length()];

            for(int i=0; i<jsonArray.length(); i++) {
                this.data1[i] = "Montparnasse";
                this.data2[i] = jsonArray.getJSONObject(i).getJSONObject("display_informations").get("direction").toString();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}