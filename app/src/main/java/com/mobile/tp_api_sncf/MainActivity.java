package com.mobile.tp_api_sncf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
    String url = "https://api.sncf.com/v1/coverage/sncf/commercial_modes";

    RequestQueue queue;
    JSONObject jsonfound = new JSONObject();

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textView = findViewById(R.id.text);

    }


    public void getApi(View view) {
        queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

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

        setTextResponse();
    }

    private void setTextResponse() {
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
}