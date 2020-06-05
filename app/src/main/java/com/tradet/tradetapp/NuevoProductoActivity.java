package com.tradet.tradetapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tradet.excepciones.ExcepcionTradeT;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NuevoProductoActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private LocationManager locManager;
    private Location loc;
    ArrayList<Categoria> listaCategorias = null;
    private Spinner spinnerCategorias;
    private EditText nombre, descripcion, precio;
    private ImageView image;
    private Button selImagen, borrarImagen, confirmar;
    final int COD_MARCADA = 10;
    final int COD_FOTO = 20;
    String path = null;
    byte[] inputData;
    MarkerOptions marcador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_producto);
        inicializarSpinner();
        inicializarComponentes();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

    }

    private void inicializarSpinner() {
        spinnerCategorias = findViewById(R.id.spinnerNProductoCategoria);
        ComunicacionServidor comunicacionServidor = new ComunicacionServidor();

        try {
            listaCategorias = comunicacionServidor.leerCategorias();
        } catch (ExcepcionTradeT excepcionTradeT) {
            excepcionTradeT.printStackTrace();
        }
        ArrayAdapter<Categoria> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner, listaCategorias);
        spinnerCategorias.setAdapter(arrayAdapter);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "La aplicación no funcionará correctamente sin permisos de ubicaicón", Toast.LENGTH_LONG).show();
            return;
        } else {

            locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                    0, mLocationListener);
            loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        mMap = googleMap;

        if (loc != null) {
            LatLng sydney = new LatLng(loc.getLatitude(), loc.getLongitude());
            marcador = new MarkerOptions().position(sydney).title("Ubicación del producto").draggable(true);
            mMap.addMarker(marcador);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

    }
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            if(loc == null) {
                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                marcador = new MarkerOptions().position(sydney).title("Ubicación del producto").draggable(true);
                mMap.addMarker(marcador);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void inicializarComponentes(){
        nombre = findViewById(R.id.editTextNProductoNombre);
        descripcion = findViewById(R.id.editTextNProductoDescr);
        precio = findViewById(R.id.editTextNProductoPrecio);
        image = findViewById(R.id.imageViewNProducto);
        selImagen = findViewById(R.id.buttonNProductoSImagen);
        borrarImagen = findViewById(R.id.buttonNProductoBImagen);
        confirmar = findViewById(R.id.buttonNProductoConfirmar);
    }

    private void cargarFoto() {
        final CharSequence[] opc = {"Hacer Foto", "Cargar Foto", "Cancelar"};
        final AlertDialog.Builder alerta_Opc = new AlertDialog.Builder((this));
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
        Intent chooserIntent = Intent.createChooser(getIntent(), "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        startActivityForResult(chooserIntent, COD_MARCADA);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == COD_MARCADA && resultCode == RESULT_OK) {
            Uri miPath = data.getData();
            Glide.with(getBaseContext()).load(miPath).into(image);
            try {
                InputStream iStream = getContentResolver().openInputStream(miPath);
                inputData = getBytes(iStream);
                System.out.println(inputData);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(requestCode == COD_FOTO && resultCode == RESULT_OK){
            MediaScannerConnection.scanFile(this, new String[]{path}, null,
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

            Glide.with(getBaseContext()).load(rotatedBitmap).into(image);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
            inputData = stream.toByteArray();
        }
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }




    private void hacerFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // cerramos el activity principal y ponemos la camara
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
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
                Uri photoURI = FileProvider.getUriForFile(this,
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
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        path = image.getAbsolutePath(); //Variable global
        return image;
    }

    public void seleccionarImagen(View view) {
        cargarFoto();
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void confirmar(View view) {
        ComunicacionServidor c = new ComunicacionServidor();
        String nombreS = nombre.getText().toString();
        String descrS = descripcion.getText().toString();
        BigDecimal precioF = null;
        if(!precio.getText().toString().isEmpty()) {
            precioF = BigDecimal.valueOf(Long.parseLong(precio.getText().toString()));
        }
        Double latitud = marcador.getPosition().latitude;
        Double longitud = marcador.getPosition().longitude;
        Categoria categoria = listaCategorias.get(spinnerCategorias.getSelectedItemPosition());
        if(!nombreS.isEmpty() && !descrS.isEmpty() && precioF != null && inputData != null){
            Producto producto = new Producto();
            producto.setNombre(nombreS);
            producto.setDescripcion(descrS);
            producto.setPrecio(precioF);
            producto.setLatitud(latitud);
            producto.setLongitud(longitud);
            producto.setFoto(inputData);
            producto.setEstado("DISPONIBLE");
            producto.setCategoria(categoria);
            producto.setUsuario(Sesion.activeUser);

            try {
                c.insertarProducto(producto);
                finish();
            } catch (ExcepcionTradeT excepcionTradeT) {
                Toast.makeText(getApplicationContext(), excepcionTradeT.getMensajeUsuario(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "No puede haber ningún campo vacío", Toast.LENGTH_SHORT).show();
        }
    }

    public void borrarImagen(View view) {
        inputData = null;
        image.setImageResource(R.drawable.avatar);
    }

}
