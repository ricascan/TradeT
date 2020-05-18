package com.tradet.tradetapp.ui.usuarios;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.tradet.excepciones.ExcepcionTradeT;
import com.tradet.tradetapp.ComunicacionServidor;
import com.tradet.tradetapp.R;
import com.tradet.tradetapp.Sesion;
import com.tradet.tradetapp.Usuario;
import com.tradet.tradetapp.Valoracion;

/**
 * A simple {@link Fragment} subclass.
 */
public class InsertarValoracionFragment extends Fragment {

    private RadioGroup radioGroup;
    private EditText editTextComentario;
    private Button button;
    private Usuario usuario;
    public InsertarValoracionFragment() {
        // Required empty public constructor
    }

    public InsertarValoracionFragment(Usuario usuario){
        this.usuario = usuario;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_insertar_valoracion, container, false);

        button = root.findViewById(R.id.buttonRegistrarValoracion);
        editTextComentario = root.findViewById(R.id.editTextComentario);
        radioGroup = root.findViewById(R.id.radioPuntuacion);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarValoracion();
            }
        });


        return root;
    }

    private void registrarValoracion(){
        ComunicacionServidor comunicacionServidor = new ComunicacionServidor();
        Valoracion valoracion = new Valoracion();
        valoracion.setUsuarioByUsuarioValoradoId(usuario);
        valoracion.setUsuarioByUsuarioValoradorId(Sesion.activeUser);
        if(!editTextComentario.getText().toString().isEmpty()) {
            valoracion.setComentario(editTextComentario.getText().toString());
        }
        switch(radioGroup.getCheckedRadioButtonId()){
            case R.id.radioButton:
                valoracion.setPuntuacion(0);
                break;
            case R.id.radioButton2:
                valoracion.setPuntuacion(1);
                break;
            case R.id.radioButton3:
                valoracion.setPuntuacion(2);
                break;
            case R.id.radioButton4:
                valoracion.setPuntuacion(3);
                break;
            case R.id.radioButton5:
                valoracion.setPuntuacion(4);
                break;
            case R.id.radioButton6:
                valoracion.setPuntuacion(5);
                break;
                default:
                    break;
        }
        try {
            comunicacionServidor.insertarValoracion(valoracion);
            getFragmentManager().popBackStackImmediate();
        } catch (ExcepcionTradeT excepcionTradeT) {
            Toast.makeText(getActivity(), excepcionTradeT.getMensajeUsuario(), Toast.LENGTH_LONG).show();
        }
    }

}
