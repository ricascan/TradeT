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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.tabs.TabLayout;
import com.tradet.excepciones.ExcepcionTradeT;
import com.tradet.tradetapp.ComunicacionServidor;
import com.tradet.tradetapp.R;
import com.tradet.tradetapp.Sesion;
import com.tradet.tradetapp.adapters.ViewPagerAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PerfilFragment extends Fragment {

    private ImageView image;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private final int COD_MARCADA = 10;
    private final int COD_FOTO = 20;
    private String path = null;
    private byte[] inputData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        image = root.findViewById(R.id.imageViewPerfil);
        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        viewPagerAdapter.addFragment(new Tab1Fragment(), "Mis datos");
        viewPagerAdapter.addFragment(new Tab2Fragment(this), "Mis productos");
        viewPagerAdapter.addFragment(new Tab3Fragment(), "Mis valoraciones");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(0);

        if(Sesion.activeUser.getFoto() != null) {

            Glide.with(getActivity()).load(Sesion.activeUser.getFoto()).fitCenter().into(image);
        }


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFoto();

            }
        });



        return root;
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
        if(inputData != null) {
            ComunicacionServidor comunicacionServidor = new ComunicacionServidor();
            Sesion.activeUser.setFoto(inputData);
            try {
                comunicacionServidor.modificarUsuario(Sesion.activeUser);
                Toast.makeText(getActivity(), "Foto de perifl modificada correctamente.", Toast.LENGTH_SHORT).show();
                inputData = null;
            } catch (ExcepcionTradeT excepcionTradeT) {
                Toast.makeText(getActivity(), excepcionTradeT.getMensajeUsuario(), Toast.LENGTH_LONG).show();
            }
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

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


}