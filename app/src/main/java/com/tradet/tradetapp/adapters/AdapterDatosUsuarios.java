package com.tradet.tradetapp.adapters;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tradet.tradetapp.R;
import com.tradet.tradetapp.Usuario;
import com.tradet.tradetapp.ui.usuarios.UsuarioFragment;

import java.util.ArrayList;

public class AdapterDatosUsuarios extends RecyclerView.Adapter<AdapterDatosUsuarios.ViewHolderDatos> {
    private ArrayList<Usuario> listaUsuarios;
    private Context context;
    private Fragment fragment;

    public AdapterDatosUsuarios(ArrayList<Usuario> listaUsuarios, Context context, Fragment fragment) {
        this.listaUsuarios = listaUsuarios;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_usuarios, null, false);
        return new AdapterDatosUsuarios.ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos(listaUsuarios.get(position));
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    class ViewHolderDatos extends RecyclerView.ViewHolder {
        private View view;
        private TextView nombreUsuario;
        private ImageView imagen;
        ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            nombreUsuario = view.findViewById(R.id.textViewNombreListaUsuarios);
            imagen = view.findViewById(R.id.imageViewListaUsuarios);
        }

        void asignarDatos(final Usuario usuario) {
            if(usuario.getFoto() != null) {

                Glide.with(context).load(usuario.getFoto()).fitCenter().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imagen);
            }
            nombreUsuario.setText(usuario.getNombre());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = fragment.getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    UsuarioFragment usuarioFragment = new UsuarioFragment(usuario);
                    fragmentTransaction.addToBackStack("xyz");
                    fragmentTransaction.hide(fragment);
                    fragmentTransaction.add(R.id.nav_host_fragment, usuarioFragment);
                    fragmentTransaction.commit();
                }
            });
        }
    }
}
