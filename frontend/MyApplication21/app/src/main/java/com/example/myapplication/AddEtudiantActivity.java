package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddEtudiantActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE = 100;
    private ImageButton pickImage;
    private EditText nom;
    private EditText prenom;
    private Spinner ville;
    private RadioButton homme;
    private RadioButton femme;
    private EditText CNI;
    private EditText CNE;
    private Button button;
    private String base64Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_etudiant);
        init();
        pickImage.setOnClickListener(v -> openGallery());
        button.setOnClickListener(this);
    }

    // Initilisation
    public void init() {
        pickImage = findViewById(R.id.photo);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        ville = findViewById(R.id.ville);
        homme = findViewById(R.id.m);
        femme = findViewById(R.id.f);
        CNE = findViewById(R.id.CNE);
        CNI = findViewById(R.id.CNI);
        button = findViewById(R.id.add);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            pickImage.setImageURI(imageUri);
            base64Image = uploadImage(imageUri);
        }
    }

    private String uploadImage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            inputStream.close();
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        if (nom.getText().toString().isEmpty() || prenom.getText().toString().isEmpty() || base64Image == null) {
            Toast.makeText(this, "Veuillez remplir tous les champs et sélectionner une image.", Toast.LENGTH_LONG).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.108/Nouveau dossier/register.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("res", s);
                if (s.contains("ok")) {
                    Toast.makeText(AddEtudiantActivity.this, "L'étudiant a été ajouté avec succès.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddEtudiantActivity.this, "Erreur dans l'ajout.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("res", volleyError.toString());
                Toast.makeText(AddEtudiantActivity.this, "Erreur réseau.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                String sex = homme.isChecked() ? "homme" : "femme";
                params.put("nom", nom.getText().toString());
                params.put("prenom", prenom.getText().toString());
                params.put("ville", ville.getSelectedItem().toString());
                params.put("sexe", sex);
                params.put("image", "data:image/png;base64," + base64Image);
                return params;
            }
        };

        requestQueue.add(request);
    }
}
