package com.tradet.tradetapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private EditText editTextNombre;
    private EditText editTextContrasena;
    private Button buttonIniciarSesion;
    private Button buttonRegistrarse;
    public static boolean hayPermisos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextNombre = findViewById(R.id.loginNombreText);
        editTextContrasena = findViewById(R.id.loginContrasenaText);
        buttonIniciarSesion = findViewById(R.id.loginButton);
        buttonRegistrarse = findViewById(R.id.registroButton);
        buttonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextNombre.getText().toString().trim().isEmpty() && !editTextContrasena.getText().toString().trim().isEmpty()) {
                    String nombre = editTextNombre.getText().toString().trim();
                    String contrasena = editTextContrasena.getText().toString().trim();
                    if(Sesion.iniciarSesion(getApplicationContext(), nombre, contrasena)) {
                        Toast.makeText(getApplicationContext(), "Correcto", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), NavigationActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Incorrecto", Toast.LENGTH_SHORT).show();

                }
            }
        });
        buttonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), RegistroActivity.class);
                startActivity(intent);
            }
        });
        if(validarPermisos()){
            hayPermisos = true;
        }
    }

    private boolean validarPermisos() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if ((checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }
        if ((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))) {
            cargarDialogo();
        } else { //primera vez
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
        }
        return false;
    }

    private void cargarDialogo() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder((MainActivity.this));
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe dar permisos para que funcione la aplicación");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
                }
            }
        });
        dialogo.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                hayPermisos = true;
            } else {
                pedirPermisosManual();
            }
        }
    }

    private void pedirPermisosManual() {
        final CharSequence[] opc = {"si", "no"};
        final AlertDialog.Builder alerta_Opc = new AlertDialog.Builder((MainActivity.this));
        alerta_Opc.setTitle("¿Desea dar permisos de forma manual?");
        alerta_Opc.setItems(opc, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opc[which].equals("si")) {
                    Intent miInten = new Intent();
                    miInten.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri miUri = Uri.fromParts("package", getPackageName(), null);
                    miInten.setData(miUri);
                    startActivity(miInten);
                    /////////////////////////////////////////////
                    //Para comprobar de nuevo si se activaron permisos de forma manual y así activar botón
                    if (validarPermisos()) {
                        hayPermisos = true;}
                    /////////////////////////////////////////////
                } else {
                    Toast.makeText(getApplicationContext(), "Los permisos no fueron aceptados", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });
        alerta_Opc.show();
    }
}
