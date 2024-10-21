package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.beans.Etudiant;
import com.example.myapplication.info_;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder> implements Filterable {

    private Context context;
    private List<Etudiant> etudiantList;      // Original list
    private List<Etudiant> etudiantsFilter;   // Filtered list
    private NewFilter mFilter;
    private static final String TAG = "EtudiantAdapter";

    public EtudiantAdapter(Context context, List<Etudiant> etudiantList) {
        this.context = context;
        this.etudiantList = etudiantList;

        // Initialize filtered list with original data
        this.etudiantsFilter = new ArrayList<>(etudiantList);

        Log.d(TAG, "Adapter initialized with " + etudiantList.size() + " students");
        this.mFilter = new NewFilter();
    }

    @NonNull
    @Override
    public EtudiantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        final  EtudiantViewHolder holder=new EtudiantViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, info_.class);
                intent.putExtra("nom",holder.nom.getText());
                intent.putExtra("prenom",holder.prenon.getText());
                intent.putExtra("id",holder.id.getText());
                intent.putExtra("img",holder.img.getTag().toString());
                intent.putExtra("ville",holder.ville.getText());
                intent.putExtra("sexe",holder.sexe.getText());
                context.startActivity(intent);
            }
        });
        return  holder;

    }

    @Override
    public void onBindViewHolder(@NonNull EtudiantViewHolder holder, int position) {
        Etudiant etudiant = etudiantsFilter.get(position);
        String url="http://192.168.1.108/Nouveau dossier/" + etudiant.getPhoto();
        Picasso.get()
                .load(url)
                .into(holder.img);
        holder.img.setTag(url);
        holder.nom.setText(etudiant.getNom());
        holder.prenon.setText(etudiant.getPrenom());
        holder.id.setText(String.valueOf(etudiant.getId()));
        holder.sexe.setText(etudiant.getSexe());
        holder.ville.setText(etudiant.getVille());
        Log.d(TAG, "Binding student: " + etudiant.getNom() + " " + etudiant.getPrenom());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert(holder).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        int size = etudiantsFilter.size();
        Log.d(TAG, "getItemCount: " + size);
        return size;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public static class EtudiantViewHolder extends RecyclerView.ViewHolder {
        TextView id, nom,prenon,sexe,ville;
        ImageView img;


        public EtudiantViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.Id);
            nom= itemView.findViewById(R.id.Name);
            prenon=itemView.findViewById(R.id.prenom);
            img = itemView.findViewById(R.id.StudentImage);
            sexe=itemView.findViewById(R.id.sexe);
            ville=itemView.findViewById(R.id.ville);

        }
    }

    private class NewFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Etudiant> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(etudiantList);  // Show all if no filter
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Etudiant e : etudiantList) {
                    if (e.getPrenom().toLowerCase().startsWith(filterPattern)) {
                        filteredList.add(e);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            etudiantsFilter.clear();
            etudiantsFilter.addAll((List<Etudiant>) results.values);
            notifyDataSetChanged();
            Log.d(TAG, "publishResults: Filter applied, size = " + etudiantsFilter.size());
        }
    }
    private AlertDialog alert(EtudiantViewHolder holder){
        View popup = LayoutInflater.from(context).inflate(R.layout.edite, null, false);
                       @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView nom=popup.findViewById(R.id.nom);
                       @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView prenom=popup.findViewById(R.id.prenom);
                       @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView ville=popup.findViewById(R.id.ville);
                       @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView sexe=popup.findViewById(R.id.sexe);

        return new AlertDialog.Builder(context)
                .setView(popup)
                .setPositiveButton("Modifier", (dialog, whi) -> {

                    holder.nom.setText(nom.getText());
                    holder.prenon.setText(prenom.getText());
                    holder.ville.setText(ville.getText());
                    holder.sexe.setText(sexe.getText());
                    update((String) holder.id.getText(), (String) nom.getText(), (String) prenom.getText(), (String) ville.getText(), (String) sexe.getText(),holder);

                })
                .setNegativeButton("Annuler", null)
                .create();
    }

    private void update(String id, String nom, String prenom, String sexe, String ville,EtudiantViewHolder holder) {
        String url = "http://192.168.1.108/Nouveau dossier/update.php";
        RequestQueue requestQueue = Volley.newRequestQueue(context);

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
                param.put("photo",holder.img.getTag().toString());
                return param;
            }
        };
        requestQueue.add(request);
    }
}
