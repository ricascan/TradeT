package com.tradet.tradetapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.tradet.excepciones.ExcepcionTradeT;
import com.tradet.tradetapp.ComunicacionServidor;
import com.tradet.tradetapp.R;
import com.tradet.tradetapp.Valoracion;

import java.util.ArrayList;

public class AdapterDatosValoraciones extends RecyclerView.Adapter<AdapterDatosValoraciones.ViewHolderDatos> {
    private ArrayList<Valoracion> listaValoraciones;
    private Context context;
    private boolean borrar;
    private Fragment fragment;

    public AdapterDatosValoraciones(ArrayList<Valoracion> listaValoraciones, Context context, boolean borrar, Fragment fragment) {
        this.listaValoraciones = listaValoraciones;
        this.context = context;
        this.borrar = borrar;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public AdapterDatosValoraciones.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_valoraciones, null, false);
        return new AdapterDatosValoraciones.ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDatosValoraciones.ViewHolderDatos holder, int position) {
        holder.asignarDatos(listaValoraciones.get(position));
    }

    @Override
    public int getItemCount() {
        return listaValoraciones.size();
    }

    class ViewHolderDatos extends RecyclerView.ViewHolder {
        View view;
        TextView puntuacion, comentario, usuarioValorado, usuarioValorador;
        ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            puntuacion = view.findViewById(R.id.textViewPuntuacionLista);
            comentario = view.findViewById(R.id.textViewComentarioLista);
            usuarioValorado = view.findViewById(R.id.textViewNombreUsuarioValoradoLista);
            usuarioValorador = view.findViewById(R.id.textViewNombreUsuarioValoradorLista);
        }

        void asignarDatos(final Valoracion valoracion) {
            puntuacion.setText("Puntuación: " + String.valueOf(valoracion.getPuntuacion()));
            comentario.setText(valoracion.getComentario());
            usuarioValorado.setText("Hacia: " + valoracion.getUsuarioByUsuarioValoradoId().getNombre());
            usuarioValorador.setText("De: " + valoracion.getUsuarioByUsuarioValoradorId().getNombre());
            if(borrar){
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        ComunicacionServidor comunicacionServidor = new ComunicacionServidor();
                                        try {
                                            comunicacionServidor.borrarValoracion(valoracion);
                                            fragment.getFragmentManager().popBackStackImmediate();
                                        } catch (ExcepcionTradeT excepcionTradeT) {
                                            Toast.makeText(fragment.getActivity(), excepcionTradeT.getMensajeUsuario(), Toast.LENGTH_SHORT).show();
                                        }
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
                        builder.setMessage("¿Seguro que deseas borrar esta valoración?").setPositiveButton("Sí", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                });
            }
        }
    }
}
