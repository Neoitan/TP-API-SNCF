package com.mobile.tp_api_sncf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    // Défini le TAG qui rassemble les requêtes
    public static final String TAG = "REQUEST";

    // URL de base pour l'api SNCF
    String url = "https://api.sncf.com/v1/coverage/sncf/";

    // Clef d'authorisation de l'api SNCF
    final static String authoKey = "f7773b94-e980-472f-9128-882905f29d1c";

    // Queue de requêtes
    RequestQueue queue;
    // Résultat de la requête
    JSONObject jsonfound = new JSONObject();

    // Liste de données permettant l'affichage des cards du RecyclerView
    String[] data1, data2;

    // EditText permettant de voir une url soit même
    TextView tvURL;

    TextView tvFromMode2;
    EditText etFromMode2;

    TextView tvToMode2;
    EditText etToMode2;

    TextView tvToMode3;
    EditText etToMode3;

    Button bExecuteMode2;
    Button bExecuteMode3;

    // Initialisation du RecyclerView
    RecyclerView rvObject;

    // Permet de savoir quel mode de décodage JSON utiliser
    private int modeChoose;

    // Initialise la bar de chargement
    ProgressBar pbChargement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des composants du layout
        this.tvURL = findViewById(R.id.tvURL);
        this.rvObject = findViewById(R.id.rvObject);
        this.pbChargement = findViewById(R.id.pbChargement);

        this.tvFromMode2 = findViewById(R.id.tvFromMode2);
        this.etFromMode2 = findViewById(R.id.etFromMode2);

        this.tvToMode2 = findViewById(R.id.tvToMode2);
        this.etToMode2 = findViewById(R.id.etToMode2);

        this.etToMode3 = findViewById(R.id.etToMode3);
        this.tvToMode3 = findViewById(R.id.tvToMode3);

        this.bExecuteMode2 = findViewById(R.id.bExecuteMode2);
        this.bExecuteMode3 = findViewById(R.id.bExecuteMode3);
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    // Application des nouvelles données dans le RecyclerView
    public void setRV(){
        RecyclerAdapter ra = new RecyclerAdapter(this, this.data1, this.data2);
        this.rvObject.setAdapter(ra);
        this.rvObject.setLayoutManager(new LinearLayoutManager(this));
    }

    public void resetRV(){
        data1 = new String[0];
        data2 = new String[0];

        RecyclerAdapter ra = new RecyclerAdapter(this, data1, data2);
        rvObject.setAdapter(ra);
        rvObject.setLayoutManager(new LinearLayoutManager(this));
    }

    // Affichage des composants du mode 2
    // Dissimuler les composants du mode 3
    public void showMode2(View v){
        this.tvFromMode2.setVisibility(View.VISIBLE);
        this.etFromMode2.setVisibility(View.VISIBLE);

        this.tvToMode2.setVisibility(View.VISIBLE);
        this.etToMode2.setVisibility(View.VISIBLE);

        this.tvToMode3.setVisibility(View.INVISIBLE);
        this.etToMode3.setVisibility(View.INVISIBLE);

        this.bExecuteMode2.setVisibility(View.VISIBLE);
        this.bExecuteMode3.setVisibility(View.INVISIBLE);
    }

    // Affichage des composants du mode 3
    // Dissimuler les composants du mode 2
    public void showMode3(View v){
        this.tvFromMode2.setVisibility(View.INVISIBLE);
        this.etFromMode2.setVisibility(View.INVISIBLE);

        this.tvToMode2.setVisibility(View.INVISIBLE);
        this.etToMode2.setVisibility(View.INVISIBLE);

        this.tvToMode3.setVisibility(View.VISIBLE);
        this.etToMode3.setVisibility(View.VISIBLE);

        this.bExecuteMode2.setVisibility(View.INVISIBLE);
        this.bExecuteMode3.setVisibility(View.VISIBLE);
    }

    // Dissimuler les composants du mode 2
    // Dissimuler les composants du mode 3
    public void getCommercialModes(View v){
        this.tvFromMode2.setVisibility(View.INVISIBLE);
        this.etFromMode2.setVisibility(View.INVISIBLE);

        this.tvToMode2.setVisibility(View.INVISIBLE);
        this.etToMode2.setVisibility(View.INVISIBLE);

        this.tvToMode3.setVisibility(View.INVISIBLE);
        this.etToMode3.setVisibility(View.INVISIBLE);

        this.bExecuteMode2.setVisibility(View.INVISIBLE);
        this.bExecuteMode3.setVisibility(View.INVISIBLE);

        this.tvURL.setText("commercial_modes");

        getApiResponse();
        this.modeChoose = 0;
    }

    // Fonction d'appel du mode 2
    // Sert à récupérer les trajets entre 2 villes (code postaux entrés en paramètres)
    public void getJourneysBetween(View v){
        int from = Integer.parseInt(this.etFromMode2.getText().toString());
        int to = Integer.parseInt(this.etToMode2.getText().toString());

        // Définition de l'url
        this.tvURL.setText("journeys?from=admin:fr:"+from+"&to=admin:fr:"+to+"&datetime=20211229T145220");

        // Récupération de la requête
        getApiResponse();

        // Mise à jour du mode de requête
        this.modeChoose = 1;
    }

    // Fonction d'appel du mode 3
    // Sert à récupérer les trajets à partir d'une ville (code postal entré en paramètre)
    public void getJourneysFrom(View v){
        int to = Integer.parseInt(this.etToMode3.getText().toString());

        // Définition de l'url
        this.tvURL.setText("stop_areas/stop_area:SNCF:"+to+"/departures?datetime=20211229T145220");

        // Récupération de la requête
        getApiResponse();

        // Mise à jour du mode de requête
        this.modeChoose = 2;
    }

    // Appel de la requête
    public void getApiResponse() {
        this.pbChargement.setVisibility(View.VISIBLE);
        String urlComplete = this.url + this.tvURL.getText();

        queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlComplete, null, new Response.Listener<JSONObject>() {

                    // Selon le mode, on récupère les réponses des requêtes
                    @Override
                    public void onResponse(JSONObject response) {
                        jsonfound = response;
                        switch(modeChoose){
                            case 0:
                                setCommercialModesTextResponse();
                                break;
                            case 1:
                                setJourneysBetweenTextResponse();
                                break;
                            case 2:
                                setJourneysFromTextResponse();
                                break;
                        }
                        setRV();
                        pbChargement.setVisibility(View.INVISIBLE);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        resetRV();
                    }
                }
                ) {
            // Application des headers
            // Sert à y introduire la clef d'authorisation
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", MainActivity.authoKey);

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

    // Fonction qui récupère les résultats des requètes
    // Gestion des JSON
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