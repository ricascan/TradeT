package com.tradet.tradetapp.ui.perfil;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tradet.excepciones.ExcepcionTradeT;
import com.tradet.tradetapp.ComunicacionServidor;
import com.tradet.tradetapp.R;
import com.tradet.tradetapp.Sesion;
import com.tradet.tradetapp.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1Fragment extends Fragment {


    private EditText nombre, contra, email, numero;
    private Button aplicar, eliminar;
    public Tab1Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_tab1, container, false);


        nombre = root.findViewById(R.id.editTextNombreUsuarioEditarPerfil);
        contra = root.findViewById(R.id.editTextContrasenaEditarPerfil);
        email = root.findViewById(R.id.editTextEmailEditarPerfil);
        numero = root.findViewById(R.id.editTextTelefonoEditarPerfil);
        aplicar = root.findViewById(R.id.buttonAplicarEditarPerfil);
        eliminar = root.findViewById(R.id.buttonBorrarEditarPerfil);

        nombre.setText(Sesion.activeUser.getNombre());
        email.setText(Sesion.activeUser.getEmail());
        numero.setText(Sesion.activeUser.getTelefono());

        aplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nombre.getText().toString().isEmpty() && !contra.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !nombre.getText().toString().isEmpty()) {
                    String sNombre = nombre.getText().toString();
                    String sContra = ComunicacionServidor.getMD5(contra.getText().toString());
                    String sEmail = email.getText().toString();
                    String sNumero = numero.getText().toString();

                    ComunicacionServidor comunicacionServidor = new ComunicacionServidor();
                    Sesion.activeUser.setNombre(sNombre);
                    Sesion.activeUser.setContrasena(sContra);
                    Sesion.activeUser.setEmail(sEmail);
                    Sesion.activeUser.setTelefono(sNumero);

                    try {
                        comunicacionServidor.modificarUsuario(Sesion.activeUser);
                        Toast.makeText(getActivity(), "Datos actualizados correctamente.", Toast.LENGTH_SHORT).show();
                    } catch (ExcepcionTradeT excepcionTradeT) {
                        Toast.makeText(getActivity(), excepcionTradeT.getMensajeUsuario(), Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Ningún campo puede quedar vacío.", Toast.LENGTH_LONG).show();
                }
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarPerfil();
            }
        });

        return root;
    }
    private void eliminarPerfil(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        ComunicacionServidor comunicacionServidor = new ComunicacionServidor();
                        try {
                            comunicacionServidor.borrarUsuario(Sesion.activeUser);
                            getActivity().finish();
                        } catch (ExcepcionTradeT excepcionTradeT) {
                            Toast.makeText(getActivity(), excepcionTradeT.getMensajeUsuario(), Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Seguro que deseas borrar tu perfil?").setPositiveButton("Sí", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
}
