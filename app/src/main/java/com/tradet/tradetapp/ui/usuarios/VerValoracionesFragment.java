package com.tradet.tradetapp.ui.usuarios;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tradet.excepciones.ExcepcionTradeT;
import com.tradet.tradetapp.ComunicacionServidor;
import com.tradet.tradetapp.R;
import com.tradet.tradetapp.Usuario;
import com.tradet.tradetapp.Valoracion;
import com.tradet.tradetapp.adapters.AdapterDatosValoraciones;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerValoracionesFragment extends Fragment {

    private Usuario usuario;
    private RecyclerView recyclerView;
    private TextView notaMedia, nombre;

    public VerValoracionesFragment() {
        // Required empty public constructor
    }

    public VerValoracionesFragment(Usuario usuario){
        this.usuario = usuario;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_ver_valoraciones, container, false);

        recyclerView = root.findViewById(R.id.recyclerValoraciones1);
        notaMedia = root.findViewById(R.id.textViewNotaMedia1);
        nombre = root.findViewById(R.id.textViewNombreVerV);
        nombre.setText("Valoraciones del usuario: "+ usuario.getNombre());
        ComunicacionServidor comunicacionServidor = new ComunicacionServidor();
        try {
            ArrayList<Valoracion> listaValoraciones = comunicacionServidor.leerValoraciones(" where usuarioByUsuarioValoradoId.nombre = '" + usuario.getNombre()+"'");
            final AdapterDatosValoraciones adapter = new AdapterDatosValoraciones(listaValoraciones, getActivity().getApplicationContext(), false, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapter);

            if(listaValoraciones.size() > 0) {
                Integer suma = 0;
                float media;

                for (Valoracion v : listaValoraciones) {
                    suma += v.getPuntuacion();
                }

                media = suma / listaValoraciones.size();

                notaMedia.setText("Nota media: " + media);
            } else{
                notaMedia.setText("Este usuario aun no ha sido valorado por nadie.");
            }

        } catch (ExcepcionTradeT excepcionTradeT) {
            excepcionTradeT.printStackTrace();
        }

        return root;
    }

}
