package com.example.nxnnshoes.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nxnnshoes.R;
import com.example.nxnnshoes.adapter.CustomAdapter;
import com.example.nxnnshoes.db.MyDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PesananActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addButton;
    ImageView empty_imageview;
    TextView no_data;

    SearchView searchView;

    MyDatabaseHelper myDB;
    ArrayList<String> no_id, nama_pemesan, tlp_pemesan, merek_sepatu, warna_sepatu, ukuran_sepatu, imagePathList;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan);

        recyclerView = findViewById(R.id.recyclerView);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);
        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PesananActivity.this, TambahPesananActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(PesananActivity.this);
        no_id = new ArrayList<>();
        nama_pemesan = new ArrayList<>();
        tlp_pemesan = new ArrayList<>();
        merek_sepatu = new ArrayList<>();
        warna_sepatu = new ArrayList<>();
        ukuran_sepatu = new ArrayList<>();
        imagePathList = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(PesananActivity.this, this, no_id, nama_pemesan,
                tlp_pemesan, merek_sepatu, warna_sepatu, ukuran_sepatu, imagePathList);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(PesananActivity.this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()) {
                no_id.add(cursor.getString(0));
                nama_pemesan.add(cursor.getString(1));
                tlp_pemesan.add(cursor.getString(2));
                merek_sepatu.add(cursor.getString(3));
                warna_sepatu.add(cursor.getString(4));
                ukuran_sepatu.add(cursor.getString(5));
                imagePathList.add(cursor.getString(6));
            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete_all){
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(PesananActivity.this);
                myDB.deleteAllData();
                //Refresh Activity
                Intent intent = new Intent(PesananActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}
