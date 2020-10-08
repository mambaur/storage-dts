package com.example.latihanstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

import static android.provider.Telephony.Mms.Part.FILENAME;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnCreate, btnRead, btnUpdate, btnDelete;
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnRead = (Button) findViewById(R.id.btnRead);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        txtResult = (TextView) findViewById(R.id.txtResult);

        btnCreate.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCreate:
                createFile();
                break;
            case R.id.btnUpdate:
                updateFile();
                break;
            case R.id.btnRead:
                readFile();
                break;
            case R.id.btnDelete:
                deleteFile();
                break;
        }
    }

    void createFile(){
        String fileValue = "Nama Saya Mambaur ";
        File file = new File(getFilesDir(), FILENAME);

        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file, true);
            outputStream.write(fileValue.getBytes());
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        readFile();
    }

    void updateFile(){
        File file = new File(getFilesDir(), FILENAME);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file, true);
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
            streamWriter.write("Nama saya Roziq (Update) ");
            streamWriter.close();
            outputStream.close();
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        readFile();
    }

    void readFile(){
        File file = new File(getFilesDir(), FILENAME);
        if (file.exists()){
            StringBuilder text = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null){
                    text.append(line);
                    line = reader.readLine();
                }
                reader.close();
            }catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            txtResult.setText(text.toString());
        }else{
            txtResult.setText("File belum dibuat");
        }
    }

    void deleteFile(){
        File file = new File(getFilesDir(), FILENAME);
        if (file.exists()){
            file.delete();
            txtResult.setText("File berhasil dihapus");
        }
    }
}
