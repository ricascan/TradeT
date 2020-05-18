package com.tradet.tradetapp;

import java.util.HashMap;

public class Valoracion {

    private Usuario usuarioByUsuarioValoradoId;
    private Usuario usuarioByUsuarioValoradorId;
    private int puntuacion;
    private String comentario;

    public Valoracion() {
    }



    public Valoracion(Usuario usuarioByUsuarioValoradoId, Usuario usuarioByUsuarioValoradorId, int puntuacion) {

        this.usuarioByUsuarioValoradoId = usuarioByUsuarioValoradoId;
        this.usuarioByUsuarioValoradorId = usuarioByUsuarioValoradorId;
        this.puntuacion = puntuacion;
    }

    public Valoracion(Usuario usuarioByUsuarioValoradoId, Usuario usuarioByUsuarioValoradorId, int puntuacion, String comentario) {

        this.usuarioByUsuarioValoradoId = usuarioByUsuarioValoradoId;
        this.usuarioByUsuarioValoradorId = usuarioByUsuarioValoradorId;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
    }






    public Usuario getUsuarioByUsuarioValoradoId() {
        return this.usuarioByUsuarioValoradoId;
    }

    public void setUsuarioByUsuarioValoradoId(Usuario usuarioByUsuarioValoradoId) {
        this.usuarioByUsuarioValoradoId = usuarioByUsuarioValoradoId;
    }


    public Usuario getUsuarioByUsuarioValoradorId() {
        return this.usuarioByUsuarioValoradorId;
    }

    public void setUsuarioByUsuarioValoradorId(Usuario usuarioByUsuarioValoradorId) {
        this.usuarioByUsuarioValoradorId = usuarioByUsuarioValoradorId;
    }

    public int getPuntuacion() {
        return this.puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }


    public String getComentario() {
        return this.comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "Valoracion{" +"usuarioByUsuarioValoradoId=" + usuarioByUsuarioValoradoId + ", usuarioByUsuarioValoradorId=" + usuarioByUsuarioValoradorId + ", puntuacion=" + puntuacion + ", comentario=" + comentario + '}';
    }

    public Valoracion(HashMap map) {

        usuarioByUsuarioValoradoId = (new Usuario((Integer)((HashMap)map.get("usuario valorado")).get("id"), (String)((HashMap)map.get("usuario valorado")).get("nombre")));
        usuarioByUsuarioValoradorId = (new Usuario((Integer)((HashMap)map.get("usuario valorador")).get("id"), (String)((HashMap)map.get("usuario valorador")).get("nombre")));
        puntuacion = ((Integer) map.get("puntuacion"));
        comentario = ((String) map.get("comentario"));
    }

    public HashMap toHash() {
        HashMap map = new HashMap();
        map.put("usuario valorado", usuarioByUsuarioValoradoId.toHash());
        map.put("usuario valorador", usuarioByUsuarioValoradorId.toHash());
        map.put("puntuacion", puntuacion);
        map.put("comentario", comentario);
        return map;
    }
}
