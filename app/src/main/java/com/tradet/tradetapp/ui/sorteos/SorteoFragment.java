package com.tradet.tradetapp.ui.sorteos;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tradet.tradetapp.R;
import com.tradet.tradetapp.Sorteo;

/**
 * A simple {@link Fragment} subclass.
 */
public class SorteoFragment extends Fragment {

    private Sorteo sorteo;
    private TextView nombre, descripcion;
    public SorteoFragment() {
        // Required empty public constructor
    }

    public SorteoFragment(Sorteo sorteo) {
        this.sorteo = sorteo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_sorteo, container, false);

        nombre = root.findViewById(R.id.textViewNombreSorteo);
        descripcion = root.findViewById(R.id.textViewDescripcionSorteo);

        nombre.setText(sorteo.getNombre());
        descripcion.setText("Descripcion: \n\t" + sorteo.getDescripcion());


        return root;
    }

}
