package com.example.latihanstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnRegister;
    EditText eUsername, ePassword, eEmail, eName, eAsalSekolah, eAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");

        btnRegister = (Button) findViewById(R.id.btnRegister);
        eUsername = (EditText) findViewById(R.id.eUsername);
        ePassword = (EditText) findViewById(R.id.ePassword);
        eEmail = (EditText) findViewById(R.id.eEmail);
        eName = (EditText) findViewById(R.id.eAsalSekolah);
        eAlamat = (EditText) findViewById(R.id.eAlamat);

        btnRegister.setOnClickListener(this);
    }

    private boolean isValidation(){
        if (eUsername.getText().toString().equals("") ||
        ePassword.getText().toString().equals("") ||
        eEmail.getText().toString().equals("") ||
        eName.getText().toString().equals("") ||
        eAsalSekolah.getText().toString().equals("") ||
        eAlamat.getText().toString().equals("")){
            return false;
        }else{
            return true;
        }
    }

    void simpanFile(){
        String isiFile = eUsername.getText().toString()+";"+
                ePassword.getText().toString()+";"+
                eEmail.getText().toString()+";"+
                eName.getText().toString()+":"+
                eAsalSekolah.getText().toString()+":"+
                eAlamat.getText().toString();
        File file = new File(getFilesDir(), eUsername.getText().toString());
        FileOutputStream fileOutputStream = null;
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file, false);
            fileOutputStream.write(isiFile.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        Toast.makeText(this, "Register berhasil!", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                if (isValidation()){
                    simpanFile();
                }else{
                    Toast.makeText(this, "Mohon lengkapi seluruh data!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
