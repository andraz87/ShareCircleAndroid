package com.example.sharecircleandroid;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView skupine;

    // Spremeni za storitev v oblaku ali emulator
    private String url = "https://sharecircle-is-esehh8ccc5dgaqee.italynorth-01.azurewebsites.net/api/v1/Skupina";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        skupine = findViewById(R.id.skupine);
    }

    public void prkaziSkupine(View view) {
        if (view != null){
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener)
            {
                @Override
                public Map<String,String> getHeaders() throws AuthFailureError
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("ApiKey", "SecretKey");
                    return params;
                }
            };
            requestQueue.add(request);
        }
    }

    private Response.Listener<JSONArray> jsonArrayListener = response -> {
        skupine.setText("Sezam skupin:\n\n"); // Počisti prejšnje podatke
        ArrayList<String> data = new ArrayList<>();

        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject object = response.getJSONObject(i);
                String name = object.getString("imeSkupine");
                String datumNastanka = object.getString("datumNastanka");
                data.add(name + "\n" + datumNastanka.substring(0,19));
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Napaka pri parsiranju JSON", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        for (String row : data) {
            skupine.append(row + "\n\n");
        }
    };

    private Response.ErrorListener errorListener = error -> {
        Log.d("REST error", error.getMessage());
        Toast.makeText(MainActivity.this, "Napaka pri pridobivanju podatkov: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    };

    public static final String EXTRA_MESSAGE = "com.example.universityapp.MESSAGE";

    public void addSkupinaActivity (View view) {
        Intent intent = new Intent(this, AddSkupinaMainActivity.class);
        startActivity(intent);
    }

}
