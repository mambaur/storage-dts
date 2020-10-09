package com.example.latihanstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.Buffer;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSimpan;
    EditText eUsername, ePassword, eEmail, eName, eAsalSekolah, eAlamat;
    public static final String FILENAME = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("Halaman Home");

        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        eUsername = (EditText) findViewById(R.id.eUsername);
        ePassword = (EditText) findViewById(R.id.ePassword);
        eEmail = (EditText) findViewById(R.id.eEmail);
        eName = (EditText) findViewById(R.id.eName);
        eAsalSekolah = (EditText) findViewById(R.id.eAsalSekolah);
        eAlamat = (EditText) findViewById(R.id.eAlamat);

        btnSimpan.setVisibility(View.GONE);
        eUsername.setEnabled(false);
        ePassword.setVisibility(View.GONE);
        eEmail.setEnabled(false);
        eName.setEnabled(false);
        eAsalSekolah.setEnabled(false);
        eAlamat.setEnabled(false);

        btnSimpan.setOnClickListener(this);
        bacaFileLogin();
    }

    void bacaFileLogin(){
        File file = new File(getFilesDir(), FILENAME);
        if (file.exists()){
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                while(line != null){
                    text.append(line);
                    line = br.readLine();
                }
                br.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            String data = text.toString();
            String[] dataUser = data.split(";");
            bacaDataUser(dataUser[0]);
        }
    }

    void bacaDataUser(String filename){
        File file = new File(getFilesDir(), filename);
        if (file.exists()){
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                while(line != null){
                    text.append(line);
                    line = br.readLine();
                }
                br.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            String data = text.toString();
            String[] dataUser = data.split(";");

            eUsername.setText(dataUser[0]);
            eEmail.setText(dataUser[2]);
            eName.setText(dataUser[3]);
            eAsalSekolah.setText(dataUser[4]);
            eAlamat.setText(dataUser[5]);
        }else{
            Toast.makeText(this, "User tidak ditemukan!", Toast.LENGTH_SHORT).show();
        }
    }

    void logout(){
        File file = new File(getFilesDir(), FILENAME);
        if (file.exists()){
            file.delete();
        }
    }

    void konfirmasiDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Apakah anda yakin ingin logout?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                konfirmasiDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSimpan:
                break;
        }
    }
}
