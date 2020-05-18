package com.tradet.tradetapp.ui.usuarios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;



import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tradet.excepciones.ExcepcionTradeT;
import com.tradet.tradetapp.ComunicacionServidor;

import com.tradet.tradetapp.R;
import com.tradet.tradetapp.Sesion;
import com.tradet.tradetapp.Usuario;

import com.tradet.tradetapp.adapters.AdapterDatosUsuarios;

import java.util.ArrayList;

public class UsuariosFragment extends Fragment {


    private EditText editTextNombre;
    private RecyclerView recyclerView;
    private Button aplicarFiltros;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_usuarios, container, false);

        editTextNombre = root.findViewById(R.id.editTextBuscarUsuario);
        recyclerView = root.findViewById(R.id.recyclerUsuarios);
        aplicarFiltros = root.findViewById(R.id.buttonAplicarFiltrosUsuarios);

        actualizarLista(" where nombre not like '"+ Sesion.activeUser.getNombre()+"'");

        aplicarFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editTextNombre.getText().toString().isEmpty()){
                    actualizarLista(" where nombre like '" + editTextNombre.getText().toString()+"' and nombre not like '"+ Sesion.activeUser.getNombre()+"'");
                } else{
                    actualizarLista(" where nombre not like '"+ Sesion.activeUser.getNombre()+"'");
                }
            }
        });

        return root;
    }

    private void actualizarLista(String filtro){
        ComunicacionServidor comunicacionServidor = new ComunicacionServidor();
        ArrayList<Usuario> listaUsuarios = null;
        try {
            if (filtro == null) {
                listaUsuarios = comunicacionServidor.leerUsuarios();
            } else {
                listaUsuarios = comunicacionServidor.leerUsuarios(filtro);
            }
        } catch (ExcepcionTradeT excepcionTradeT) {
            excepcionTradeT.printStackTrace();
        }

        final AdapterDatosUsuarios adapter = new AdapterDatosUsuarios(listaUsuarios, getActivity().getApplicationContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}