package com.tradet.tradetapp;

import android.content.Context;

import com.tradet.excepciones.ExcepcionTradeT;

import java.util.ArrayList;

public class Sesion {
    public static Usuario activeUser;

    public static boolean iniciarSesion(Context context, String nombre, String contrasena){

        ComunicacionServidor cs = new ComunicacionServidor();

        try {
            ArrayList<Usuario> listaUsuarios = cs.leerUsuariosNoImagen();
            System.out.println(ComunicacionServidor.getMD5(contrasena));

            System.out.println(listaUsuarios);
            for (Usuario u : listaUsuarios) {

                if (u.getNombre().equals(nombre) && u.getContrasena().equals(ComunicacionServidor.getMD5(contrasena))) {
                    activeUser = u;
                    return true;
                }

            }
        } catch (ExcepcionTradeT excepcionTradeT) {

        }

        return false;
    }

    public static void cerrarSesion(){
        activeUser = null;
    }


}
