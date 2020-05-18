package com.tradet.tradetapp.ui.perfil;


import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tradet.excepciones.ExcepcionTradeT;
import com.tradet.tradetapp.ComunicacionServidor;
import com.tradet.tradetapp.R;
import com.tradet.tradetapp.Sesion;
import com.tradet.tradetapp.Valoracion;
import com.tradet.tradetapp.adapters.AdapterDatosValoraciones;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab3Fragment extends Fragment {

    private Spinner spinner;
    private RecyclerView recyclerView;
    private TextView notaMedia;
    private SwipeRefreshLayout swipeRefreshLayout;
    public Tab3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root = inflater.inflate(R.layout.fragment_tab3, container, false);

       spinner = root.findViewById(R.id.spinnerTab3);
       recyclerView = root.findViewById(R.id.recyclerValoraciones2);
       notaMedia = root.findViewById(R.id.textViewNotaMediaTab3);
       swipeRefreshLayout = root.findViewById(R.id.swipeV);

       ArrayList<String> filtros = new ArrayList<>();
       Resources res = getActivity().getResources();
       filtros.addAll(Arrays.asList(res.getStringArray(R.array.filtros2)));

       ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner, filtros);
       spinner.setAdapter(arrayAdapter1);

       swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               if (spinner.getSelectedItemPosition() == 0){
                   verValoracionHechasPorMi();
               }else{
                   verValoracionesHechasHaciaMi();
               }
               swipeRefreshLayout.setRefreshing(false);
           }
       });
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if (position == 0){
                   verValoracionHechasPorMi();
               }else{
                   verValoracionesHechasHaciaMi();
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

       return root;
    }

    private void verValoracionHechasPorMi(){
        ComunicacionServidor comunicacionServidor = new ComunicacionServidor();
        ArrayList<Valoracion> listaValoraciones = null;
        try {
            listaValoraciones = comunicacionServidor.leerValoraciones(" where usuarioByUsuarioValoradorId.nombre = '" + Sesion.activeUser.getNombre()+"'");
        } catch (ExcepcionTradeT excepcionTradeT) {
            excepcionTradeT.printStackTrace();
        }
        final AdapterDatosValoraciones adapter = new AdapterDatosValoraciones(listaValoraciones, getActivity().getApplicationContext(), true, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        notaMedia.setText("");
    }

    private void verValoracionesHechasHaciaMi(){
        ComunicacionServidor comunicacionServidor = new ComunicacionServidor();
        ArrayList<Valoracion> listaValoraciones = null;
        try {
            listaValoraciones = comunicacionServidor.leerValoraciones(" where usuarioByUsuarioValoradoId.nombre = '" + Sesion.activeUser.getNombre()+"'");
        } catch (ExcepcionTradeT excepcionTradeT) {
            excepcionTradeT.printStackTrace();
        }
        final AdapterDatosValoraciones adapter = new AdapterDatosValoraciones(listaValoraciones, getActivity().getApplicationContext(), false, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        if(listaValoraciones.size() > 0) {
            Integer suma = 0;
            float media = 0;

            for (Valoracion v : listaValoraciones) {
                suma += v.getPuntuacion();
            }

            media = suma / listaValoraciones.size();

            notaMedia.setText("Nota media: " + media);
        } else{
            notaMedia.setText("Todavía no has sido valorado por nadie.");
        }
    }

}
