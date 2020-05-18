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

import com.tradet.tradetapp.Producto;
import com.tradet.tradetapp.R;
import com.tradet.tradetapp.ui.perfil.ModificarProductoFragment;


import java.util.ArrayList;

public class AdapterDatosProductos2 extends RecyclerView.Adapter<AdapterDatosProductos2.ViewHolderDatos> {
    ArrayList<Producto> listaProdutocs;
    Context context;
    Fragment fragment;

    public AdapterDatosProductos2(ArrayList<Producto> listaProdutocs, Context context, Fragment fragment) {
        this.listaProdutocs = listaProdutocs;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public AdapterDatosProductos2.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_productos2, null, false);
        return new AdapterDatosProductos2.ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDatosProductos2.ViewHolderDatos holder, int position) {
        holder.asignarDatos(listaProdutocs.get(position));
    }

    @Override
    public int getItemCount() {
        return listaProdutocs.size();
    }


    class ViewHolderDatos extends RecyclerView.ViewHolder {
        View view;
        TextView nombreProducto;

        ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            nombreProducto = itemView.findViewById(R.id.textViewNombreProducto2);


        }

        void asignarDatos(final Producto producto) {

            nombreProducto.setText(producto.getNombre());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = fragment.getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ModificarProductoFragment productoFragment = new ModificarProductoFragment(producto);
                    fragmentTransaction.addToBackStack("xyz");
                    fragmentTransaction.hide(fragment);
                    fragmentTransaction.add(R.id.nav_host_fragment, productoFragment);
                    fragmentTransaction.commit();
                }
            });
        }
    }
}
