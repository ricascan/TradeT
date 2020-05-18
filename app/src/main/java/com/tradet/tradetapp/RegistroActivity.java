package com.tradet.tradetapp;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.tradet.excepciones.ExcepcionTradeT;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistroActivity extends AppCompatActivity {

    EditText textNombre;
    EditText textContrasena;
    EditText textEmail;
    EditText textTelefono;
    ImageView image;
    Button buttonElegirImagen;
    Button buttonBorrarImagen;
    Button buttonConfirmar;
    final int COD_MARCADA = 10;
    final int COD_FOTO = 20;
    String path = null;
    byte[] inputData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        textNombre = findViewById(R.id.registroNombreText);
        textContrasena = findViewById(R.id.registroContrasenaText);
        textEmail = findViewById(R.id.registroEmailText);
        textTelefono = findViewById(R.id.registroTelefonoText);
        image = findViewById(R.id.registroFoto);
        buttonElegirImagen = findViewById(R.id.registroFotoButton);
        buttonBorrarImagen = findViewById(R.id.registroDelFotoButton);
        buttonConfirmar = findViewById(R.id.registroAceptarButton);
    }

    private void cargarFoto() {
        final CharSequence[] opc = {"Hacer Foto", "Cargar Foto", "Cancelar"};
        final AlertDialog.Builder alerta_Opc = new AlertDialog.Builder((RegistroActivity.this));
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
            rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
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
        String nombre = textNombre.getText().toString();
        String contrasena = textContrasena.getText().toString();
        String email = textEmail.getText().toString();
        String telefono = textTelefono.getText().toString();
        if(!nombre.isEmpty() && !contrasena.isEmpty() && !email.isEmpty() && !telefono.isEmpty()){
            Usuario user = new Usuario();
            user.setNombre(nombre);
            user.setContrasena(contrasena);
            user.setEmail(email);
            user.setTelefono(telefono);
            user.setFoto(inputData);
            try {
                c.insertarUsuario(user);
                Toast.makeText(getApplicationContext(), "Usuario registrado correctamente.", Toast.LENGTH_SHORT).show();
            } catch (ExcepcionTradeT excepcionTradeT) {
                Toast.makeText(getApplicationContext(), excepcionTradeT.getMensajeUsuario(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "La imagen es opcional pero ningún campo puede quedar vacío.", Toast.LENGTH_SHORT).show();
        }
    }

    public void borrarImagen(View view) {
        inputData = null;
        image.setImageResource(R.drawable.avatar);
    }
}
