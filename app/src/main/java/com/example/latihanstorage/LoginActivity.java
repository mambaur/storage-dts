package com.example.latihanstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import static android.provider.Telephony.Mms.Part.FILENAME;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin, btnRegister;
    EditText eUsername, ePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        eUsername = (EditText) findViewById(R.id.eUsername);
        ePassword = (EditText) findViewById(R.id.ePassword);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    void login(){
        File file = new File(getFilesDir(), eUsername.getText().toString());
        if(file.exists()){
            StringBuilder stringBuilder = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                while (line != null){
                    stringBuilder.append(line);
                    line = br.readLine();
                }
                br.close();
            }catch (Exception e){
                e.printStackTrace();
            }

            String data = stringBuilder.toString();
            String[] dataUser = data.split(";");
            if (dataUser[1].equals(ePassword.getText().toString())){
                simpanFileLogin();
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, "Password sesuai!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "User tidak ditemukan!", Toast.LENGTH_SHORT).show();
        }
    }

    void simpanFileLogin(){
        String isiFile = eUsername.getText().toString()+";"+ePassword.getText().toString();
        File file = new File(getFilesDir(), FILENAME);
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
        Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                login();
                break;
            case R.id.btnRegister:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }
}
