package com.tradet.tradetapp.ui.perfil;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tradet.excepciones.ExcepcionTradeT;
import com.tradet.tradetapp.ComunicacionServidor;
import com.tradet.tradetapp.Producto;
import com.tradet.tradetapp.R;
import com.tradet.tradetapp.Sesion;
import com.tradet.tradetapp.adapters.AdapterDatosProductos2;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2Fragment extends Fragment {

    private Fragment parent;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    public Tab2Fragment() {
        // Required empty public constructor
    }
    public Tab2Fragment(Fragment parent){
        this.parent = parent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tab2, container, false);

        swipeRefreshLayout = root.findViewById(R.id.swipeMProducto);
        recyclerView = root.findViewById(R.id.recyclerViewPerfilProductos);
        ComunicacionServidor comunicacionServidor = new ComunicacionServidor();
        ArrayList<Producto> listaProductos = new ArrayList<>();
        try {
            listaProductos = comunicacionServidor.leerProductos("where p.usuario.usuarioId = " + Sesion.activeUser.getUsuarioId());
        } catch (ExcepcionTradeT excepcionTradeT) {
            excepcionTradeT.printStackTrace();
        }
        AdapterDatosProductos2 adapterDatosProductos2 = new AdapterDatosProductos2(listaProductos, getActivity(), parent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapterDatosProductos2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ComunicacionServidor comunicacionServidor = new ComunicacionServidor();
                ArrayList<Producto> listaProductos = new ArrayList<>();
                try {
                    listaProductos = comunicacionServidor.leerProductos("where p.usuario.usuarioId = " + Sesion.activeUser.getUsuarioId());
                } catch (ExcepcionTradeT excepcionTradeT) {
                    excepcionTradeT.printStackTrace();
                }
                AdapterDatosProductos2 adapterDatosProductos2 = new AdapterDatosProductos2(listaProductos, getActivity(), parent);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adapterDatosProductos2);
                swipeRefreshLayout.setRefreshing(false);
            }

        });

        return root;
    }

}
