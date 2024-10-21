package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.adapter.EtudiantAdapter;
import com.example.myapplication.beans.Etudiant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShowActivity extends AppCompatActivity {
    List<Etudiant> etudiantList = new ArrayList<>();
    RecyclerView recyclerView;
    private EtudiantAdapter etudiantAdapter = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show);

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getEtudiantList();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (etudiantAdapter != null) {
                    etudiantAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void getEtudiantList() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.108/Nouveau dossier/get.php";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Type type = new TypeToken<Collection<Etudiant>>() {}.getType();
                            Collection<Etudiant> etudiants = new Gson().fromJson(response, type);
                            if (etudiants != null) {
                                etudiantList.addAll(etudiants);
                                etudiantAdapter = new EtudiantAdapter(ShowActivity.this, etudiantList);
                                recyclerView.setAdapter(etudiantAdapter);
                                etudiantAdapter.notifyDataSetChanged();
                            } else {
                                Log.e("Erreur", "Aucun étudiant trouvé.");
                            }
                        } catch (Exception e) {
                            Log.e("Erreur JSON", "Erreur lors du parsing : " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Erreur Volley", "Erreur de requête : " + error.getMessage());
                    }
                }
        );

        requestQueue.add(request);
    }
}
