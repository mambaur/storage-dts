package com.example.latihanstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Output;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

public class InsertActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_CODE_STORAGE = 100;
    int eventID = 0;
    EditText eFileName, eNote;
    Button btnSave;
    boolean isEditable = false;
    String filename = "";
    String tempCatatan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        eFileName = (EditText) findViewById(R.id.eName);
        eNote = (EditText) findViewById(R.id.eNote);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            filename = extras.getString("filename");
            eFileName.setText(filename);
            getSupportActionBar().setTitle("Ubah catatan");
        }else{
            getSupportActionBar().setTitle("Tambah catatan");
        }
        eventID = 1;
        if (Build.VERSION.SDK_INT >= 23){
            if(checkPermission()){
                bacaFile();
            }
        }else{
            bacaFile();
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23){
            if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                return true;
            }else{
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
                return false;
            }
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    bacaFile();
                }
                break;
        }
    }

    void bacaFile(){
        File file = new File(getFilesDir(), eFileName.getText().toString());
        if (file.exists()){
            StringBuilder stringBuilder = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                while(line != null){
                    stringBuilder.append(line);
                    line = br.readLine();
                }
                br.close();
            }catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            tempCatatan = stringBuilder.toString();
            eNote.setText(tempCatatan);
        }
    }

    void buatDanUbah(){
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)){
            return;
        }
        String path = Environment.getExternalStorageDirectory().toString();
        File parent = new File(path);
        if(parent.exists()){
            File file = new File(path, eFileName.getText().toString());
            FileOutputStream outputStream = null;
            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                outputStreamWriter.append(eNote.getText());
                outputStreamWriter.flush();
                outputStreamWriter.close();
                outputStream.flush();
                outputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            parent.mkdir();
            File file = new File(path, eFileName.getText().toString());
            FileOutputStream outputStream = null;
            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file, false);
                outputStream.write(eNote.getText().toString().getBytes());
                outputStream.flush();
                outputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        onBackPressed();
    }

    void konfirmasiDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Simpan Catatan")
                .setMessage("Apakah anda yakin ingin menyimpan catatan ini?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buatDanUbah();
                    }
                }).setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onBackPressed() {
        if(!tempCatatan.equals(eNote.getText().toString())){
            konfirmasiDialog();
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                eventID = 2;
                if (!tempCatatan.equals(eNote.getText().toString())){
                    if (Build.VERSION.SDK_INT >= 23){
                        if(checkPermission()){
                          konfirmasiDialog();
                        }
                    }else{
                      konfirmasiDialog();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_insert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                onBackPressed();
                break;
    }
        return super.onOptionsItemSelected(item);
    }
}
