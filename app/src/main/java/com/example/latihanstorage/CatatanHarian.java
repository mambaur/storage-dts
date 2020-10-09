package com.example.latihanstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CatatanHarian extends AppCompatActivity {
    public static final int REQUEST_CODE_STORAGE = 100;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catatan_harian);

        listView = (ListView) findViewById(R.id.listView);
        getSupportActionBar().setTitle("Aplikasi Catatan Proyek 1");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CatatanHarian.this, InsertActivity.class);
                Map<String, Object> data = (Map<String, Object>) parent.getAdapter().getItem(position);
                intent.putExtra("filename", data.get("name").toString());
                Toast.makeText(CatatanHarian.this, "You clicked "+data.get("name").toString(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> data = (Map<String, Object>) parent.getAdapter().getItem(position);
                konfirmasiDialog(data.get("name").toString());
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23){
            if(checkPermission()){
                getListData();
            }
        }else{
            getListData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getListData();
                }
                break;
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

//    https://android-arsenal.com/search?q=alert

    void konfirmasiDialog(final String name){
        new AlertDialog.Builder(this)
                .setTitle("Hapus Catatan")
                .setMessage("Apakah anda yakin ingin menghapus "+name+"?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hapusFile(name);
                    }
                }).setNegativeButton(android.R.string.no, null).show();
    }

    void hapusFile(String filename){
        File file = new File(getFilesDir(), filename);
        if(file.exists()){
            file.delete();
        }
        getListData();
    }

    void getListData(){
//        String path = Environment.getExternalStorageDirectory().toString();
//        String path = getFilesDir().toString();
//        Toast.makeText(this, "Get List Data, path= "+path, Toast.LENGTH_SHORT).show();
        File file = new File(getFilesDir().toString());
        if(file.exists()){
            File[] files = file.listFiles();
            String[] fileNames = new String[files.length];
            String[] dateCreated = new String[files.length];
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM YYY HH:mm:ss");
            ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < files.length; i++){
                fileNames[i] = files[i].getName();
                Date lastModDate = new Date(files[i].lastModified());
                dateCreated[i] = simpleDateFormat.format(lastModDate);
                Map<String, Object> itemList = new HashMap<>();
                itemList.put("name", fileNames[i]);
                itemList.put("date", dateCreated[i]);
                arrayList.add(itemList);
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(this, arrayList, android.R.layout.simple_list_item_2, new String[] {"name, date"}, new int[] {android.R.id.text1, android.R.id.text2});
            listView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_tambah:
                Intent intent = new Intent(this, InsertActivity.class);
                startActivity(intent);
                break;
            case R.id.tes_menu:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
