package com.tradet.tradetapp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

public class Producto implements Serializable {

    private Integer productoId;
    private Categoria categoria;
    private Usuario usuario;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private double latitud;
    private double longitud;
    private byte[] foto;
    private String estado;

    public Producto() {
    }

    public Producto(Integer productoId) {
        this.productoId = productoId;
    }

    public Producto(Integer productoId, String nombre, String descripcion, BigDecimal precio, double latitud, double longitud, byte[] foto, String estado, Categoria categoria, Usuario usuario) {
        this.productoId = productoId;
        this.categoria = categoria;
        this.usuario = usuario;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.latitud = latitud;
        this.longitud = longitud;
        this.foto = foto;
        this.estado = estado;
    }

    public Producto(HashMap map) {
        if(map.containsKey("id"))
            productoId = ((Integer) map.get("id"));
        else
            productoId = null;
        nombre = ((String) map.get("nombre"));
        descripcion = ((String) map.get("descripcion"));
        precio = ((BigDecimal) map.get("precio"));
        latitud = ((Double) map.get("latitud"));
        longitud = ((Double) map.get("longitud"));
        foto = ((byte[]) map.get("foto"));
        estado = ((String) map.get("estado"));
        usuario = (new Usuario((Integer)((HashMap)map.get("usuario")).get("id"),(String) (((HashMap)map.get("usuario")).get("nombre")), (String) (((HashMap)map.get("usuario")).get("telefono")), (String) (((HashMap)map.get("usuario")).get("email"))));
        categoria = (new Categoria((Integer)((HashMap)map.get("categoria")).get("id"), (String) (((HashMap)map.get("categoria")).get("nombre"))));
    }

    public HashMap toHash() {
        HashMap map = new HashMap();
        map.put("id", productoId);
        map.put("nombre", nombre);
        map.put("descripcion", descripcion);
        map.put("precio", precio);
        map.put("latitud", latitud);
        map.put("longitud", longitud);
        map.put("foto", foto);
        map.put("estado", estado);
        map.put("usuario", usuario.toHash());
        map.put("categoria", categoria.toHash());
        return map;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "productoId=" + productoId +
                ", categoria=" + categoria +
                ", usuario=" + usuario +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", latitud=" + latitud +
                ", longitud=" + longitud +

                ", estado='" + estado + '\'' +
                '}';
    }
}
