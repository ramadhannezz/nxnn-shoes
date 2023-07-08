package com.example.nxnnshoes.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nxnnshoes.R;
import com.example.nxnnshoes.db.MyDatabaseHelper;

public class EditPesananActivity extends AppCompatActivity {

    EditText uploadNama, uploadTlp, uploadMerek, uploadWarna, uploadUkuran;
    Button updateButton, deleteButton;
    Button chooseImageButton;

    String id, nama, tlp, merek, warna, ukuran;
    String imagePath;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_PICK_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pesanan);

        uploadNama = findViewById(R.id.uploadNama2);
        uploadTlp = findViewById(R.id.uploadTlp2);
        uploadMerek = findViewById(R.id.uploadMerek2);
        uploadWarna = findViewById(R.id.uploadWarna2);
        uploadUkuran = findViewById(R.id.uploadUkuran2);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        chooseImageButton = findViewById(R.id.chooseImageButton);

        // First we call this
        getAndSetIntentData();

        // Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(nama);
        }

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check runtime permission for storage access
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {
                        // Permission not granted, request it
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        // Permission already granted
                        pickImageFromGallery();
                    }
                } else {
                    // System OS is less than Marshmallow
                    pickImageFromGallery();
                }
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // And only then we call this
                MyDatabaseHelper myDB = new MyDatabaseHelper(EditPesananActivity.this);
                nama = uploadNama.getText().toString().trim();
                tlp = uploadTlp.getText().toString().trim();
                merek = uploadMerek.getText().toString().trim();
                warna = uploadWarna.getText().toString().trim();
                ukuran = uploadUkuran.getText().toString().trim();
                myDB.updateData(id, nama, tlp, merek, warna, ukuran, imagePath);
                finish();
            }
        });

        // Ini harusnya menampilkan dialog konfirmasi sebelum menghapus data
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("nama") && getIntent().hasExtra("tlp") &&
                getIntent().hasExtra("merek") && getIntent().hasExtra("warna") && getIntent().hasExtra("ukuran")) {
            // Getting Data from Intent
            id = getIntent().getStringExtra("id");
            nama = getIntent().getStringExtra("nama");
            tlp = getIntent().getStringExtra("tlp");
            merek = getIntent().getStringExtra("merek");
            warna = getIntent().getStringExtra("warna");
            ukuran = getIntent().getStringExtra("ukuran");

            // Setting Intent Data
            uploadNama.setText(nama);
            uploadTlp.setText(tlp);
            uploadMerek.setText(merek);
            uploadWarna.setText(warna);
            uploadUkuran.setText(ukuran);
            Log.d("stev", nama + " " + tlp + " " + merek + " " + warna + " " + ukuran);
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + nama + " ?");
        builder.setMessage("Are you sure you want to delete " + nama + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(EditPesananActivity.this);
                myDB.deleteOneRow(id);
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

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                pickImageFromGallery();
            } else {
                // Permission was denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Handle image pick result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                imagePath = getRealPathFromURI(imageUri);
                Log.d("Image Path", imagePath);
            }
        }
    }

    // Open gallery to choose an image
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    // Get the real path of the image from its URI
    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        } else {
            return uri.getPath();
        }
    }
}
