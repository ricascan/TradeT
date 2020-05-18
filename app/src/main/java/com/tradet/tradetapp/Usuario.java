/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tradet.tradetapp;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author ricar
 */
public class Usuario implements Serializable{

    private Integer usuarioId;
    private String nombre;
    private String contrasena;
    private String email;
    private String telefono;
    private byte[] foto;

    public Usuario() {
    }

    public Usuario(Integer usuarioId, String nombre, String telefono, String email) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    public Usuario(Integer usuarioId, String nombre) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
    }

    public Usuario(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public Integer getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public Usuario(Integer usuarioId, String nombre, String contrasena, String email, String telefono, byte[] foto) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.email = email;
        this.telefono = telefono;
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Usuario{" + "usuarioId=" + usuarioId + ", nombre=" + nombre + ", contrasena=" + contrasena + ", email=" + email + ", telefono=" + telefono + ", foto=" + foto + '}';
    }

    public Usuario (HashMap map){
        if(map.containsKey("id")){
            usuarioId = (Integer) map.get("id");
        }else{
            usuarioId = null;
        }
        nombre = (String) map.get("nombre");
        contrasena = (String) map.get("contrasena");
        email = (String) map.get("email");
        telefono = (String) map.get("telefono");
        foto = (byte[]) map.get("foto");
    }

    public HashMap toHash(){
        HashMap map = new HashMap();
        map.put("id", usuarioId);
        map.put("nombre", nombre);
        map.put("contrasena", contrasena);
        map.put("email", email);
        map.put("telefono", telefono);
        map.put("foto", foto);
        return map;
    }

    
}
