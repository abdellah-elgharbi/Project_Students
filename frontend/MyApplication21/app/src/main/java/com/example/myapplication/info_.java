package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class info_ extends AppCompatActivity {
    private TextView nom,prenom ,ville,sexe;
    private ImageView img;
    private ImageButton imgbN;
    private ImageButton imgbP;
    private ImageButton imgbM;
    private ImageButton imgbV;
    private ImageButton imgbS;
    private Button button;

    public void init() {
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        img = findViewById(R.id.profile);
        ville=findViewById(R.id.ville);
        sexe=findViewById(R.id.sexe);
       // button = findViewById(R.id.button);
        imgbN = findViewById(R.id.imageButton1);
        imgbP = findViewById(R.id.imageButton2);
        imgbV=findViewById(R.id.imageButton3);
        imgbS=findViewById(R.id.imageButton4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Récupération des Intent Extras
        String name = getIntent().getStringExtra("nom");
        String prenomStr = getIntent().getStringExtra("prenom");
        String id = getIntent().getStringExtra("id");
        String imgUrl = getIntent().getStringExtra("img");
        String vill=getIntent().getStringExtra("ville");
        String sex=getIntent().getStringExtra("sexe");

        init();
        nom.setText(name);
        prenom.setText(prenomStr);
        ville.setText(vill);
        sexe.setText(sex);

        Picasso.get().load(imgUrl).into(img);

        // Bouton de suppression
        button.setOnClickListener(v -> {
            update(id,nom.getText().toString(),prenom.getText().toString(),sexe.getText().toString(),ville.getText().toString());
            Intent intent = new Intent(info_.this, ShowActivity.class);
            startActivity(intent);
        });

        updateText();
    }

    private void suprimEtudiant(String id) {
        String url = "http://192.168.1.108/Nouveau dossier/sup.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Log.d("Response", response),
                error -> Log.e("Error", error.toString())) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("id", id);
                return param;
            }
        };
        requestQueue.add(request);
    }

    private void update(String id, String nom, String prenom, String sexe, String ville) {
        String url = "http://192.168.1.108/Nouveau dossier/update.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> Log.d("Response", response),
                error -> Log.e("Error", error.toString())) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("id", id);
                param.put("nom", nom);
                param.put("prenom", prenom);
                param.put("ville", ville);
                param.put("sexe", sexe);
                param.put("photo",getIntent().getStringExtra("img").replace("http://192.168.1.108/Nouveau dossier/",""));
                return param;
            }
        };
        requestQueue.add(request);
    }

    private void updateText() {
        imgbN.setOnClickListener(v -> alert(nom).show());
        imgbP.setOnClickListener(v -> alert(prenom).show());
        imgbV.setOnClickListener(v->alert(ville).show());
        imgbS.setOnClickListener(v->alert(sexe).show());
    }

    private AlertDialog alert(TextView textView) {
        View popup = LayoutInflater.from(this).inflate(R.layout.add_alert, null, false);
        EditText text = popup.findViewById(R.id.text);

        return new AlertDialog.Builder(this)
                .setView(popup)
                .setPositiveButton("Modifier", (dialog, which) -> {
                    textView.setText(text.getText().toString());
                })
                .setNegativeButton("Annuler", null)
                .create();
    }
}
