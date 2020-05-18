package com.tradet.tradetapp.ui.sorteos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tradet.excepciones.ExcepcionTradeT;
import com.tradet.tradetapp.ComunicacionServidor;
import com.tradet.tradetapp.R;
import com.tradet.tradetapp.Sorteo;
import com.tradet.tradetapp.adapters.AdapterDatosSorteos;

import java.util.ArrayList;

public class SorteosFragment extends Fragment {

    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sorteos, container, false);

        recyclerView = root.findViewById(R.id.recyclerSorteos);

        ComunicacionServidor comunicacionServidor = new ComunicacionServidor();
        ArrayList<Sorteo> listaSorteos = new ArrayList<>();
        try {
            listaSorteos = comunicacionServidor.leerSorteos();
        } catch (ExcepcionTradeT excepcionTradeT) {
            excepcionTradeT.printStackTrace();
        }

        final AdapterDatosSorteos adapter = new AdapterDatosSorteos(listaSorteos, getActivity().getApplicationContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        return root;
    }
}