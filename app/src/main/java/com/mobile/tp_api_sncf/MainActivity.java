package com.mobile.tp_api_sncf;

import androidx.appcompat.app.AppCompatActivity;

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

    TextView textView;
    EditText etURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textView = findViewById(R.id.text);

        this.etURL = findViewById(R.id.etURL);
    }

    public void getCommercialModes(View v){
        this.etURL.setText("commercial_modes");

        getApiResponse();
        setCommercialModesTextResponse();
    }

    public void getJourneysBetween(View v){
        this.etURL.setText("journeys?from=admin:fr:75056&to=admin:fr:69123&datetime=20211229T145220");

        getApiResponse();
        setJourneysBetweenTextResponse();
    }

    public void getJourneysFrom(View v){
        this.etURL.setText("stop_areas/stop_area:SNCF:87391003/departures?datetime=20211229T145220");

        getApiResponse();
        setJourneysFromTextResponse();
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
        ArrayList<ArrayList<String>> alS = new ArrayList<>();
        String display = "";

        try {
            JSONArray jsonArray = jsonfound.getJSONArray("commercial_modes");
            ArrayList<String> temp = new ArrayList<>();
            for(int i=0; i<jsonArray.length(); i++) {
                temp.add(jsonArray.getJSONObject(i).get("id").toString());
                temp.add(jsonArray.getJSONObject(i).get("name").toString());
                alS.add(temp);
                temp = new ArrayList<>();
            }


            for(ArrayList<String> al : alS) {
                display += "id   = " + al.get(0) + "\n";
                display += "name = " + al.get(1) + "\n";
                display += "\n\n\n";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.textView.setText(display);
    }

    private void setJourneysBetweenTextResponse() {
        ArrayList<ArrayList<String>> alS = new ArrayList<>();
        String display = "";

        try {
            JSONArray jsonArray = jsonfound.getJSONArray("journeys").getJSONObject(0).getJSONArray("sections");
            ArrayList<String> temp = new ArrayList<>();
            for(int i=0; i<jsonArray.length(); i++) {
                temp.add(jsonArray.getJSONObject(i).getJSONObject("from").get("name").toString());
                temp.add(jsonArray.getJSONObject(i).getJSONObject("to").get("name").toString());
                alS.add(temp);
                temp = new ArrayList<>();
            }


            for(ArrayList<String> al : alS) {
                display += "id   = " + al.get(0) + "\n";
                display += "name = " + al.get(1) + "\n";
                display += "\n\n\n";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.textView.setText(display);
    }

    private void setJourneysFromTextResponse() {
        ArrayList<ArrayList<String>> alS = new ArrayList<>();
        String display = "";

        try {
            JSONArray jsonArray = jsonfound.getJSONArray("departures");
            ArrayList<String> temp = new ArrayList<>();
            for(int i=0; i<jsonArray.length(); i++) {
                temp.add("Montparnasse");
                temp.add(jsonArray.getJSONObject(i).getJSONObject("display_informations").get("direction").toString());
                alS.add(temp);
                temp = new ArrayList<>();
            }


            for(ArrayList<String> al : alS) {
                display += "from = " + al.get(0) + "\n";
                display += "to   = " + al.get(1) + "\n";
                display += "\n\n\n";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.textView.setText(display);
    }
}