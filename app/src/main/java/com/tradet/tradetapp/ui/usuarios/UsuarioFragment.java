package com.tradet.tradetapp.ui.usuarios;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tradet.tradetapp.R;
import com.tradet.tradetapp.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsuarioFragment extends Fragment {

    private Usuario usuario;
    private TextView textViewNombre, textViewEmail, textViewTelefono;
    private ImageView imageView;
    private Button buttonValorar, buttonVerValoraciones;

    public UsuarioFragment() {
        // Required empty public constructor
    }

    public UsuarioFragment(Usuario usuario){
        this.usuario = usuario;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_usuario, container, false);

        textViewNombre = root.findViewById(R.id.textViewNombreUsuario);
        textViewEmail = root.findViewById(R.id.textViewEmailUsuario);
        textViewTelefono = root.findViewById(R.id.textViewTelefonoUsuario);
        imageView = root.findViewById(R.id.imageViewUsuario);
        buttonValorar = root.findViewById(R.id.buttonValorar);
        buttonVerValoraciones = root.findViewById(R.id.buttonVerValoracionesUsuario);


        textViewNombre.setText(usuario.getNombre());
        textViewTelefono.setText(usuario.getTelefono());
        textViewEmail.setText(usuario.getEmail());
        if(usuario.getFoto() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(usuario.getFoto(), 0, usuario.getFoto().length);
            imageView.setImageBitmap(bitmap);
        }
        buttonValorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valorar();
            }
        });
        buttonVerValoraciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verValoraciones();
            }
        });
        return root;
    }

    private void valorar(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        InsertarValoracionFragment insertarValoracionFragment = new InsertarValoracionFragment(usuario);
        fragmentTransaction.addToBackStack("xyz");
        fragmentTransaction.hide(this);
        fragmentTransaction.add(R.id.nav_host_fragment, insertarValoracionFragment);
        fragmentTransaction.commit();
    }

    private void verValoraciones(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        VerValoracionesFragment verValoracionesFragment = new VerValoracionesFragment(usuario);
        fragmentTransaction.addToBackStack("xyz");
        fragmentTransaction.hide(this);
        fragmentTransaction.add(R.id.nav_host_fragment, verValoracionesFragment);
        fragmentTransaction.commit();
    }
}
