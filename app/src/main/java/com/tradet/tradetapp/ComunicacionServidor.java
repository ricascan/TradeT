package com.tradet.tradetapp;


import com.tradet.excepciones.ExcepcionTradeT;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class ComunicacionServidor {
    private final String IP = "192.168.1.59";
    private final Integer PUERTO = 5557;
    private  Object resultado;
    private  ArrayList<HashMap> listaHash;
    private  ExcepcionTradeT excepcionTradeT;
    private HashMap objetoRespuesta;
    private String respuesta;

    public ComunicacionServidor() {

    }

    public String insertarUsuario(final Usuario u) throws ExcepcionTradeT {
        new Thread(new Runnable() {

            @Override
            public void run() {
                HashMap peticion = new HashMap();
                u.setContrasena(getMD5(u.getContrasena()));

                peticion.put("peticion", "insertar usuario");
                peticion.put("argumento", u.toHash());
                resultado = null;
                try {
                    Socket socket = new Socket(IP, PUERTO);
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
                    flujoSalida.writeObject(peticion);
                    flujoSalida.flush();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
                    resultado = flujoEntrada.readObject();
                    if(resultado instanceof String){
                        respuesta = (String) resultado;
                    }else if (resultado instanceof ExcepcionTradeT){
                        excepcionTradeT = (ExcepcionTradeT) resultado;
                    }
                    flujoEntrada.close();
                    flujoSalida.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while (excepcionTradeT == null && respuesta == null) {
            System.out.printf("");
        }
        if (excepcionTradeT != null)
            throw excepcionTradeT;


        return respuesta;
    }

    public String modificarUsuario(final Usuario u) throws ExcepcionTradeT {
        System.out.println(u.getContrasena());
        System.out.println(getMD5(u.getContrasena()));
        new Thread(new Runnable() {

            @Override
            public void run() {
                HashMap peticion = new HashMap();
                peticion.put("peticion", "modificar usuario");
                peticion.put("argumento", u.toHash());
                resultado = null;
                try {
                    Socket socket = new Socket(IP, PUERTO);
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
                    flujoSalida.writeObject(peticion);
                    flujoSalida.flush();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
                    resultado = flujoEntrada.readObject();
                    if(resultado instanceof String){
                        respuesta = (String) resultado;
                    }else if (resultado instanceof ExcepcionTradeT){
                        excepcionTradeT = (ExcepcionTradeT) resultado;
                    }
                    flujoEntrada.close();
                    flujoSalida.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while (excepcionTradeT == null && respuesta == null) {
            System.out.printf("");
        }
        if (excepcionTradeT != null)
            throw excepcionTradeT;


        return respuesta;
    }

    public String borrarUsuario(final Usuario u) throws ExcepcionTradeT{
        new Thread(new Runnable() {

            @Override
            public void run() {
                HashMap peticion = new HashMap();

                peticion.put("peticion", "eliminar usuario");
                peticion.put("argumento", u.getUsuarioId());
                resultado = null;
                try {
                    Socket socket = new Socket(IP, PUERTO);
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
                    flujoSalida.writeObject(peticion);
                    flujoSalida.flush();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
                    resultado = flujoEntrada.readObject();
                    if(resultado instanceof String){
                        respuesta = (String) resultado;
                    }else if (resultado instanceof ExcepcionTradeT){
                        excepcionTradeT = (ExcepcionTradeT) resultado;
                    }
                    flujoEntrada.close();
                    flujoSalida.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while (excepcionTradeT == null && respuesta == null) {
            System.out.printf("");
        }
        if (excepcionTradeT != null)
            throw excepcionTradeT;


        return respuesta;
    }

    public ArrayList<Usuario> leerUsuarios() throws ExcepcionTradeT {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap peticion = new HashMap();
                peticion.put("peticion", "leer usuarios");
                try {
                    Socket socket = new Socket(IP, PUERTO);
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
                    flujoSalida.writeObject(peticion);
                    flujoSalida.flush();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
                    resultado = flujoEntrada.readObject();
                    if(resultado instanceof ArrayList){
                        listaHash = (ArrayList<HashMap>) resultado;
                    }else if (resultado instanceof ExcepcionTradeT){
                        excepcionTradeT = (ExcepcionTradeT) resultado;
                    }
                    flujoEntrada.close();
                    flujoSalida.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }).start();
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        while (excepcionTradeT == null && listaHash == null) {
            System.out.printf("");
        }
            if (excepcionTradeT != null)
                throw excepcionTradeT;

            for (HashMap map : listaHash) {
                Usuario u = new Usuario(map);
                listaUsuarios.add(u);
            }

        return listaUsuarios;
    }


    public ArrayList<Usuario> leerUsuarios(final String filtro) throws ExcepcionTradeT {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap peticion = new HashMap();
                peticion.put("peticion", "leer usuarios filtro");
                peticion.put("argumento", filtro);
                try {
                    Socket socket = new Socket(IP, PUERTO);
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
                    flujoSalida.writeObject(peticion);
                    flujoSalida.flush();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
                    resultado = flujoEntrada.readObject();
                    if(resultado instanceof ArrayList){
                        listaHash = (ArrayList<HashMap>) resultado;
                    }else if (resultado instanceof ExcepcionTradeT){
                        excepcionTradeT = (ExcepcionTradeT) resultado;
                    }
                    flujoEntrada.close();
                    flujoSalida.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }).start();
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        while (excepcionTradeT == null && listaHash == null) {
            System.out.printf("");
        }
        if (excepcionTradeT != null)
            throw excepcionTradeT;

        for (HashMap map : listaHash) {
            Usuario u = new Usuario(map);
            listaUsuarios.add(u);
        }

        return listaUsuarios;
    }



    public Usuario leerUsuario() throws ExcepcionTradeT {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap peticion = new HashMap();
                peticion.put("peticion", "leer usuario");
                try {
                    Socket socket = new Socket(IP, PUERTO);
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
                    flujoSalida.writeObject(peticion);
                    flujoSalida.flush();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
                    resultado = flujoEntrada.readObject();
                    if(resultado instanceof HashMap){
                        objetoRespuesta = (HashMap) resultado;
                    }else if (resultado instanceof ExcepcionTradeT){
                        excepcionTradeT = (ExcepcionTradeT) resultado;
                    }
                    flujoEntrada.close();
                    flujoSalida.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }).start();
        while (excepcionTradeT == null && listaHash == null) {
            System.out.printf("");
        }
        if (excepcionTradeT != null)
            throw excepcionTradeT;

        return new Usuario(objetoRespuesta);
    }

    public String insertarProducto(final Producto p) throws ExcepcionTradeT {

        new Thread(new Runnable() {

            @Override
            public void run() {
                HashMap peticion = new HashMap();

                peticion.put("peticion", "insertar producto");
                peticion.put("argumento", p.toHash());
                resultado = null;
                try {
                    Socket socket = new Socket(IP, PUERTO);
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
                    flujoSalida.writeObject(peticion);
                    flujoSalida.flush();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
                    resultado = flujoEntrada.readObject();
                    if(resultado instanceof String){
                        respuesta = (String) resultado;
                    }else if (resultado instanceof ExcepcionTradeT){
                        excepcionTradeT = (ExcepcionTradeT) resultado;
                    }
                    flujoEntrada.close();
                    flujoSalida.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while (excepcionTradeT == null && respuesta == null) {
            System.out.printf("");
        }
        if (excepcionTradeT != null)
            throw excepcionTradeT;


        return respuesta;
    }

    public String borrarProducto(final Producto p) throws ExcepcionTradeT{
        new Thread(new Runnable() {

            @Override
            public void run() {
                HashMap peticion = new HashMap();

                peticion.put("peticion", "eliminar producto");
                peticion.put("argumento", p.getProductoId());
                resultado = null;
                try {
                    Socket socket = new Socket(IP, PUERTO);
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
                    flujoSalida.writeObject(peticion);
                    flujoSalida.flush();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
                    resultado = flujoEntrada.readObject();
                    if(resultado instanceof String){
                        respuesta = (String) resultado;
                    }else if (resultado instanceof ExcepcionTradeT){
                        excepcionTradeT = (ExcepcionTradeT) resultado;
                    }
                    flujoEntrada.close();
                    flujoSalida.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while (excepcionTradeT == null && respuesta == null) {
            System.out.printf("");
        }
        if (excepcionTradeT != null)
            throw excepcionTradeT;


        return respuesta;
    }

    public String modificarProducto(final Producto p) throws ExcepcionTradeT {
        new Thread(new Runnable() {

            @Override
            public void run() {
                HashMap peticion = new HashMap();

                peticion.put("peticion", "modificar producto");
                peticion.put("argumento", p.toHash());
                resultado = null;
                try {
                    Socket socket = new Socket(IP, PUERTO);
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
                    flujoSalida.writeObject(peticion);
                    flujoSalida.flush();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
                    resultado = flujoEntrada.readObject();
                    if(resultado instanceof String){
                        respuesta = (String) resultado;
                    }else if (resultado instanceof ExcepcionTradeT){
                        excepcionTradeT = (ExcepcionTradeT) resultado;
                    }
                    flujoEntrada.close();
                    flujoSalida.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while (excepcionTradeT == null && respuesta == null) {
            System.out.printf("");
        }
        if (excepcionTradeT != null)
            throw excepcionTradeT;


        return respuesta;
    }

    public ArrayList<Producto> leerProductos() throws ExcepcionTradeT {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap peticion = new HashMap();
                peticion.put("peticion", "leer productos");
                try {
                    Socket socket = new Socket(IP, PUERTO);
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
                    flujoSalida.writeObject(peticion);
                    flujoSalida.flush();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
                    resultado = flujoEntrada.readObject();
                    if(resultado instanceof ArrayList){
                        listaHash = (ArrayList<HashMap>) resultado;
                    }else if (resultado instanceof ExcepcionTradeT){
                        excepcionTradeT = (ExcepcionTradeT) resultado;
                    }
                    flujoEntrada.close();
                    flujoSalida.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }).start();
        ArrayList<Producto> listaProductos = new ArrayList<>();
        while (excepcionTradeT == null && listaHash == null) {
            System.out.printf("");
        }
        if (excepcionTradeT != null)
            throw excepcionTradeT;

        for (HashMap map : listaHash) {
            Producto p = new Producto(map);
            listaProductos.add(p);
        }

        return listaProductos;
    }



    public ArrayList<Producto> leerProductos(final String filtro) throws ExcepcionTradeT {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap peticion = new HashMap();
                peticion.put("peticion", "leer productos filtro");
                peticion.put("argumento", filtro);
                try {
                    Socket socket = new Socket(IP, PUERTO);
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
                    flujoSalida.writeObject(peticion);
                    flujoSalida.flush();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
                    resultado = flujoEntrada.readObject();
                    if(resultado instanceof ArrayList){
                        listaHash = (ArrayList<HashMap>) resultado;
                    }else if (resultado instanceof ExcepcionTradeT){
                        excepcionTradeT = (ExcepcionTradeT) resultado;
                    }
                    flujoEntrada.close();
                    flujoSalida.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }).start();
        ArrayList<Producto> listaProductos = new ArrayList<>();
        while (excepcionTradeT == null && listaHash == null) {
            System.out.printf("");
        }
        if (excepcionTradeT != null)
            throw excepcionTradeT;

        for (HashMap map : listaHash) {
            Producto p = new Producto(map);
            listaProductos.add(p);
        }

        return listaProductos;
    }

    public ArrayList<Categoria> leerCategorias() throws ExcepcionTradeT {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap peticion = new HashMap();
                peticion.put("peticion", "leer categorias");
                try {
                    Socket socket = new Socket(IP, PUERTO);
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
                    flujoSalida.writeObject(peticion);
                    flujoSalida.flush();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
                    resultado = flujoEntrada.readObject();
                    if(resultado instanceof ArrayList){
                        listaHash = (ArrayList<HashMap>) resultado;
                    }else if (resultado instanceof ExcepcionTradeT){
                        excepcionTradeT = (ExcepcionTradeT) resultado;
                    }
                    flujoEntrada.close();
                    flujoSalida.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }).start();
        ArrayList<Categoria> listaCategorias = new ArrayList<>();
        while (excepcionTradeT == null && listaHash == null) {
            System.out.printf("");
        }
        if (excepcionTradeT != null)
            throw excepcionTradeT;

        for (HashMap map : listaHash) {
            Categoria c = new Categoria(map);
            listaCategorias.add(c);
        }

        return listaCategorias;
    }

    public String insertarValoracion(final Valoracion v) throws ExcepcionTradeT {

        new Thread(new Runnable() {

            @Override
            public void run() {
                HashMap peticion = new HashMap();

                peticion.put("peticion", "insertar valoracion");
                peticion.put("argumento", v.toHash());
                resultado = null;
                try {
                    Socket socket = new Socket(IP, PUERTO);
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
                    flujoSalida.writeObject(peticion);
                    flujoSalida.flush();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
                    resultado = flujoEntrada.readObject();
                    if(resultado instanceof String){
                        respuesta = (String) resultado;
                    }else if (resultado instanceof ExcepcionTradeT){
                        excepcionTradeT = (ExcepcionTradeT) resultado;
                    }
                    flujoEntrada.close();
                    flujoSalida.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while (excepcionTradeT == null && respuesta == null) {
            System.out.printf("");
        }
        if (excepcionTradeT != null)
            throw excepcionTradeT;


        return respuesta;
    }

    public String borrarValoracion(final Valoracion v) throws ExcepcionTradeT{
        new Thread(new Runnable() {

            @Override
            public void run() {
                HashMap peticion = new HashMap();

                peticion.put("peticion", "eliminar valoracion");
                HashMap valoracionId = new HashMap();
                valoracionId.put("usuario valorado", v.getUsuarioByUsuarioValoradoId().getUsuarioId());
                valoracionId.put("usuario valorador", v.getUsuarioByUsuarioValoradorId().getUsuarioId());
                peticion.put("argumento", valoracionId);
                resultado = null;
                try {
                    Socket socket = new Socket(IP, PUERTO);
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
                    flujoSalida.writeObject(peticion);
                    flujoSalida.flush();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
                    resultado = flujoEntrada.readObject();
                    if(resultado instanceof String){
                        respuesta = (String) resultado;
                    }else if (resultado instanceof ExcepcionTradeT){
                        excepcionTradeT = (ExcepcionTradeT) resultado;
                    }
                    flujoEntrada.close();
                    flujoSalida.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while (excepcionTradeT == null && respuesta == null) {
            System.out.printf("");
        }
        if (excepcionTradeT != null)
            throw excepcionTradeT;


        return respuesta;
    }

    public ArrayList<Valoracion> leerValoraciones(final String filtro) throws ExcepcionTradeT {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap peticion = new HashMap();
                peticion.put("peticion", "leer valoraciones filtro");
                peticion.put("argumento", filtro);
                try {
                    Socket socket = new Socket(IP, PUERTO);
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
                    flujoSalida.writeObject(peticion);
                    flujoSalida.flush();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
                    resultado = flujoEntrada.readObject();
                    if(resultado instanceof ArrayList){
                        listaHash = (ArrayList<HashMap>) resultado;
                    }else if (resultado instanceof ExcepcionTradeT){
                        excepcionTradeT = (ExcepcionTradeT) resultado;
                    }
                    flujoEntrada.close();
                    flujoSalida.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }).start();
        ArrayList<Valoracion> listaValoraciones = new ArrayList<>();
        while (excepcionTradeT == null && listaHash == null) {
            System.out.printf("");
        }
        if (excepcionTradeT != null)
            throw excepcionTradeT;

        for (HashMap map : listaHash) {
            Valoracion v = new Valoracion(map);
            listaValoraciones.add(v);
        }

        return listaValoraciones;
    }

    public ArrayList<Sorteo> leerSorteos() throws ExcepcionTradeT {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap peticion = new HashMap();
                peticion.put("peticion", "leer sorteos");
                try {
                    Socket socket = new Socket(IP, PUERTO);
                    ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
                    flujoSalida.writeObject(peticion);
                    flujoSalida.flush();
                    ObjectInputStream flujoEntrada = new ObjectInputStream(socket.getInputStream());
                    resultado = flujoEntrada.readObject();
                    if(resultado instanceof ArrayList){
                        listaHash = (ArrayList<HashMap>) resultado;
                    }else if (resultado instanceof ExcepcionTradeT){
                        excepcionTradeT = (ExcepcionTradeT) resultado;
                    }
                    flujoEntrada.close();
                    flujoSalida.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }).start();
        ArrayList<Sorteo> listaSorteos = new ArrayList<>();
        while (excepcionTradeT == null && listaHash == null) {
            System.out.printf("");
        }
        if (excepcionTradeT != null)
            throw excepcionTradeT;

        for (HashMap map : listaHash) {
            Sorteo s = new Sorteo(map);
            listaSorteos.add(s);
        }

        return listaSorteos;
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }




}
