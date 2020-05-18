package com.tradet.tradetapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.tradet.tradetapp.R;
import com.tradet.tradetapp.Sorteo;
import com.tradet.tradetapp.ui.perfil.ModificarProductoFragment;
import com.tradet.tradetapp.ui.sorteos.SorteoFragment;

import java.util.ArrayList;

public class AdapterDatosSorteos extends RecyclerView.Adapter<AdapterDatosSorteos.ViewHolderDatos> {

    private ArrayList<Sorteo> listaSorteos;
    private Context context;
    private Fragment fragment;


    public AdapterDatosSorteos(ArrayList<Sorteo> listaSorteos, Context context, Fragment fragment) {
        this.listaSorteos = listaSorteos;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public AdapterDatosSorteos.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_sorteos, null, false);
        return new AdapterDatosSorteos.ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDatosSorteos.ViewHolderDatos holder, int position) {
        holder.asignarDatos(listaSorteos.get(position));
    }

    @Override
    public int getItemCount() {
        return listaSorteos.size();
    }

    class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView nombre;
        View view;
        ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.textViewNombreSorteoLista);
            this.view = itemView;
        }

        void asignarDatos(final Sorteo sorteo) {
            nombre.setText(sorteo.getNombre());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = fragment.getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    SorteoFragment sorteoFragment = new SorteoFragment(sorteo);
                    fragmentTransaction.addToBackStack("xyz");
                    fragmentTransaction.hide(fragment);
                    fragmentTransaction.add(R.id.nav_host_fragment, sorteoFragment);
                    fragmentTransaction.commit();
                }
            });
        }
    }
}
