package com.tradet.tradetapp.ui.tienda;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tradet.excepciones.ExcepcionTradeT;
import com.tradet.tradetapp.Categoria;
import com.tradet.tradetapp.ComunicacionServidor;
import com.tradet.tradetapp.Producto;
import com.tradet.tradetapp.R;
import com.tradet.tradetapp.adapters.AdapterDatosProductos;

import java.util.ArrayList;
import java.util.Arrays;

public class TiendaFragment extends Fragment {

    private Spinner spinnerCategorias, spinnerOtrosFiltros;
    private EditText editTextBusqueda;
    private Button buttonAplicarFiltros;
    private RecyclerView recyclerView;
    private ArrayList<Categoria> listaCategorias = null;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tienda, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewProductos);
        buttonAplicarFiltros = root.findViewById(R.id.buttonAplicarFiltros);
        spinnerCategorias = root.findViewById(R.id.spinnerCategorias);
        spinnerOtrosFiltros = root.findViewById(R.id.spinnerFiltros);
        editTextBusqueda = root.findViewById(R.id.editTextBuscarProducto);
        swipeRefreshLayout = root.findViewById(R.id.refreshProductos);


        inicializarSpinners();

        buttonAplicarFiltros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sentencia = "";
                if (!editTextBusqueda.getText().toString().isEmpty()) {
                    sentencia = " where p.nombre like '%" + editTextBusqueda.getText().toString() + "%'";
                }
                if(spinnerCategorias.getSelectedItemPosition() != 0) {
                    if (!sentencia.equals("")) {
                        sentencia += " and p.categoria.categoriaId = " + listaCategorias.get(spinnerCategorias.getSelectedItemPosition()).getCategoriaId();

                    } else {
                        sentencia += " where p.categoria.categoriaId = " + listaCategorias.get(spinnerCategorias.getSelectedItemPosition()).getCategoriaId();
                    }
                }
                if (!sentencia.equals("")) {
                    sentencia += " and p.estado like 'DISPONIBLE'";

                } else {
                    sentencia += " where p.estado like 'DISPONIBLE'";
                }

                Integer posicion = spinnerOtrosFiltros.getSelectedItemPosition();
                switch (posicion) {
                    case 0:
                        sentencia += " order by precio asc";
                        break;
                    case 1:
                        sentencia += " order by precio desc";
                        break;
                    default:
                        break;

                }
                actualizarLista(sentencia);

            }
        });
        actualizarLista(" where p.estado like 'DISPONIBLE'");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                String sentencia = "";
                if (!editTextBusqueda.getText().toString().isEmpty()) {
                    sentencia = " where p.nombre like '%" + editTextBusqueda.getText().toString() + "%'";
                }
                if(spinnerCategorias.getSelectedItemPosition() != 0) {
                    if (!sentencia.equals("")) {
                        sentencia += " and p.categoria.categoriaId = " + listaCategorias.get(spinnerCategorias.getSelectedItemPosition()).getCategoriaId();

                    } else {
                        sentencia += " where p.categoria.categoriaId = " + listaCategorias.get(spinnerCategorias.getSelectedItemPosition()).getCategoriaId();
                    }
                }
                if (!sentencia.equals("")) {
                    sentencia += " and p.estado like 'DISPONIBLE'";

                } else {
                    sentencia += " where p.estado like 'DISPONIBLE'";
                }
                Integer posicion = spinnerOtrosFiltros.getSelectedItemPosition();
                switch (posicion) {
                    case 0:
                        sentencia += " order by precio asc";
                        break;
                    case 1:
                        sentencia += " order by precio desc";
                        break;
                    default:
                        break;

                }
                actualizarLista(sentencia);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return root;
    }

    private void actualizarLista(String filtro) {
        ComunicacionServidor comunicacionServidor = new ComunicacionServidor();
        ArrayList<Producto> listaProductos = null;
        ArrayList<Producto> listaProductos2 = new ArrayList<>();
        try {
            if (filtro == null) {
                listaProductos = comunicacionServidor.leerProductos();
            } else {
                listaProductos = comunicacionServidor.leerProductos(filtro);
            }
        } catch (ExcepcionTradeT excepcionTradeT) {
            excepcionTradeT.printStackTrace();
        }
        for(int i=0;i<listaProductos.size();i++){
            if(i%5==0)
            {
                listaProductos2.add(null);
            }
            listaProductos2.add(listaProductos.get(i));
        }

        final AdapterDatosProductos adapter = new AdapterDatosProductos(listaProductos2, getActivity().getApplicationContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

    }

    private void inicializarSpinners(){

        ComunicacionServidor comunicacionServidor = new ComunicacionServidor();

        try {
            listaCategorias = comunicacionServidor.leerCategorias();
        } catch (ExcepcionTradeT excepcionTradeT) {
            excepcionTradeT.printStackTrace();
        }
        listaCategorias.add(0, new Categoria("Ninguna"));
        ArrayAdapter<Categoria> arrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner, listaCategorias);
        spinnerCategorias.setAdapter(arrayAdapter);
        Resources res = getActivity().getResources();
        ArrayList<String> filtros = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.filtros)));

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner, filtros);
        spinnerOtrosFiltros.setAdapter(arrayAdapter1);
    }
}