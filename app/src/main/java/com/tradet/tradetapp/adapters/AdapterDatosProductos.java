package com.tradet.tradetapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.tradet.tradetapp.Producto;
import com.tradet.tradetapp.R;
import com.tradet.tradetapp.ui.tienda.ProductoFragment;


import java.util.ArrayList;

public class AdapterDatosProductos extends RecyclerView.Adapter<AdapterDatosProductos.ViewHolderDatos> {


    private ArrayList<Producto> listaProdutocs;
    private Context context;
    private Fragment fragment;

    public AdapterDatosProductos(ArrayList<Producto> listaProdutocs, Context context, Fragment fragment) {
        this.listaProdutocs = listaProdutocs;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_productos, null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos(listaProdutocs.get(position));
    }

    @Override
    public int getItemCount() {
        return listaProdutocs.size();
    }


    class ViewHolderDatos extends RecyclerView.ViewHolder {
        View view;
        TextView nombreProducto, precioProducto;
        ImageView imagenProducto;
        ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            nombreProducto = itemView.findViewById(R.id.textViewNombreProducto);
            precioProducto = itemView.findViewById(R.id.textViewPrecioProducto);
            imagenProducto = itemView.findViewById(R.id.imageViewProductoFoto);

        }

        void asignarDatos(final Producto producto) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(producto.getFoto(), 0, producto.getFoto().length);
            imagenProducto.setImageBitmap(bitmap);
            nombreProducto.setText(producto.getNombre());
            precioProducto.setText(producto.getPrecio() + " €");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = fragment.getFragmentManager();

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ProductoFragment productoFragment = new ProductoFragment(producto);
                    fragmentTransaction.addToBackStack("xyz");
                    fragmentTransaction.hide(fragment);
                    fragmentTransaction.add(R.id.nav_host_fragment, productoFragment);
                    fragmentTransaction.commit();
                }
            });
        }
    }
}
