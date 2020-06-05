package com.tradet.tradetapp.ui.perfil;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tradet.excepciones.ExcepcionTradeT;
import com.tradet.tradetapp.Categoria;
import com.tradet.tradetapp.ComunicacionServidor;
import com.tradet.tradetapp.Producto;
import com.tradet.tradetapp.R;
import com.tradet.tradetapp.Sesion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModificarProductoFragment extends Fragment {

    private Producto producto;
    private EditText editTextNombre, editTextDescripcion, editTextPrecio;
    private Switch switchEstado;
    private Spinner spinnerCategorias;
    private ImageView image;
    private Button buttonBorrarImagen, buttonSeleccionarImagen, buttonConfirmar, buttonBorrar;
    private ArrayList<Categoria> listaCategorias = null;
    private final int COD_MARCADA = 10;
    private final int COD_FOTO = 20;
    private String path = null;
    private byte[] inputData;
    private boolean isChecked;
    public ModificarProductoFragment() {
        // Required empty public constructor
    }

    public ModificarProductoFragment(Producto producto){
        this.producto = producto;
        System.out.println(producto);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_modificar_producto, container, false);

        editTextNombre = root.findViewById(R.id.editTextMProductoNombre);
        editTextDescripcion = root.findViewById(R.id.editTextMProductoDescripcion);
        editTextPrecio = root.findViewById(R.id.editTextMProductoPrecio);
        switchEstado = root.findViewById(R.id.switchMProductoEstado);
        image = root.findViewById(R.id.imageViewMProducto);
        buttonSeleccionarImagen = root.findViewById(R.id.buttonMProductoSImagen);
        buttonBorrarImagen = root.findViewById(R.id.buttonMProductoBImagen);
        buttonConfirmar = root.findViewById(R.id.buttonMProductoConfirmar);
        buttonBorrar = root.findViewById(R.id.buttonMProductoBorrar);
        spinnerCategorias = root.findViewById(R.id.spinnerMProductoCategoria);

        switchEstado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                comprobarSwitch(isChecked);

            }
        });

        editTextNombre.setText(producto.getNombre());
        editTextDescripcion.setText(producto.getDescripcion());
        inputData = producto.getFoto();
        Glide.with(getActivity()).load(producto.getFoto()).fitCenter().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(image);
        if(producto.getEstado().equals("DISPONIBLE")){
            switchEstado.setChecked(true);
        }else{
            switchEstado.setChecked(false);
        }

        inicializarSpinner();

        buttonSeleccionarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFoto();
            }
        });

        buttonBorrarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarImagen();
            }
        });

        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmar();
            }
        });

        buttonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                ComunicacionServidor comunicacionServidor = new ComunicacionServidor();
                                try {
                                    comunicacionServidor.borrarProducto(producto);
                                    getFragmentManager().popBackStackImmediate();
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
                builder.setMessage("¿Seguro que deseas borrar este producto?").setPositiveButton("Sí", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });

        return root;
    }

    private void comprobarSwitch(Boolean bool){
        isChecked = bool;
    }

    private void inicializarSpinner() {
        ComunicacionServidor comunicacionServidor = new ComunicacionServidor();

        try {
            listaCategorias = comunicacionServidor.leerCategorias();
        } catch (ExcepcionTradeT excepcionTradeT) {
            excepcionTradeT.printStackTrace();
        }
        ArrayAdapter<Categoria> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner, listaCategorias);
        spinnerCategorias.setAdapter(arrayAdapter);

    }

    private void confirmar() {
        ComunicacionServidor c = new ComunicacionServidor();
        String nombreS = editTextNombre.getText().toString();
        String descrS = editTextDescripcion.getText().toString();
        BigDecimal precioF = null;
        if(!editTextPrecio.getText().toString().isEmpty()) {
            precioF = BigDecimal.valueOf(Long.parseLong(editTextPrecio.getText().toString()));
        }
        String estado = "DISPONIBLE";
        if(!isChecked){
            estado = "VENDIDO";
        }


        Categoria categoria = listaCategorias.get(spinnerCategorias.getSelectedItemPosition());
        if(!nombreS.isEmpty() && !descrS.isEmpty() && precioF != null && inputData != null){
            Producto producto = new Producto();
            producto.setProductoId(this.producto.getProductoId());
            producto.setNombre(nombreS);
            producto.setDescripcion(descrS);
            producto.setPrecio(precioF);
            producto.setLatitud(this.producto.getLatitud());
            producto.setLongitud(this.producto.getLongitud());
            producto.setFoto(inputData);
            producto.setEstado(estado);
            producto.setCategoria(categoria);
            producto.setUsuario(Sesion.activeUser);
            System.out.println(producto);
            try {
                c.modificarProducto(producto);
                Toast.makeText(getActivity(), "Se ha modificado el producto correctamente", Toast.LENGTH_SHORT).show();
            } catch (ExcepcionTradeT excepcionTradeT) {
                Toast.makeText(getActivity(), excepcionTradeT.getMensajeUsuario(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "No puede haber ningún campo vacío", Toast.LENGTH_SHORT).show();
        }
    }
    private void cargarFoto() {
        final CharSequence[] opc = {"Hacer Foto", "Cargar Foto", "Cancelar"};
        final AlertDialog.Builder alerta_Opc = new AlertDialog.Builder((getActivity()));
        alerta_Opc.setTitle("Marque una opción");
        alerta_Opc.setItems(opc, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opc[which].equals("Hacer Foto")) {
                    hacerFoto();
                    //Toast.makeText(getApplicationContext(), "Hacer fotos", Toast.LENGTH_LONG).show();
                } else {
                    if (opc[which].equals("Cargar Foto")) {
                        seleccionaFoto();
                    } else {
                        dialog.dismiss();
                    }
                }
            }
        });
        alerta_Opc.show();
    }

    private void seleccionaFoto() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(getActivity().getIntent(), "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, COD_MARCADA);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == COD_MARCADA && resultCode == getActivity().RESULT_OK) {
            Uri miPath = data.getData();
            Glide.with(getActivity()).load(miPath).fitCenter().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(image);
            try {
                InputStream iStream = getActivity().getContentResolver().openInputStream(miPath);
                inputData = getBytes(iStream);
                System.out.println(inputData);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(requestCode == COD_FOTO && resultCode == getActivity().RESULT_OK){
            MediaScannerConnection.scanFile(getActivity(), new String[]{path}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("Ruta de almacenamiento", "path: " + path);
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });

            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }

            Glide.with(getActivity()).load(rotatedBitmap).fitCenter().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(image);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
            inputData = stream.toByteArray();
        }
    }
    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }




    private void hacerFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // cerramos el activity principal y ponemos la camara
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // verificamos si se realizó la foto previamente
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("OJO:", "Error en camara");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.tradet.tradetapp.fileprovider1",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, COD_FOTO); //REQUEST_TAKE_PHOTO);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        path = image.getAbsolutePath(); //Variable global
        return image;
    }


    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void borrarImagen() {

        inputData = null;
        image.setImageResource(R.drawable.avatar);
    }
}
