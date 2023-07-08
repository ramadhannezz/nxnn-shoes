package com.example.nxnnshoes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "CShoesPencucian.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "pesanan";
    private static final String COLUMN_ID = "no_id";
    private static final String COLUMN_NAMA = "nama_pemesan";
    private static final String COLUMN_TLP = "tlp_pemesan";
    private static final String COLUMN_MEREK = "merek_sepatu";
    private static final String COLUMN_WARNA = "warna_sepatu";
    private static final String COLUMN_UKURAN = "ukuran_sepatu";
    private static final String COLUMN_IMAGE_PATH = "image_path";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAMA + " TEXT, " +
                COLUMN_TLP + " TEXT, " +
                COLUMN_MEREK + " TEXT, " +
                COLUMN_WARNA + " TEXT, " +
                COLUMN_UKURAN + " TEXT, " +
                COLUMN_IMAGE_PATH + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addPesanan(String nama, String tlp, String merek, String warna, String ukuran, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAMA, nama);
        cv.put(COLUMN_TLP, tlp);
        cv.put(COLUMN_MEREK, merek);
        cv.put(COLUMN_WARNA, warna);
        cv.put(COLUMN_UKURAN, ukuran);
        cv.put(COLUMN_IMAGE_PATH, imagePath);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateData(String row_id, String nama, String tlp, String merek, String warna, String ukuran, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAMA, nama);
        cv.put(COLUMN_TLP, tlp);
        cv.put(COLUMN_MEREK, merek);
        cv.put(COLUMN_WARNA, warna);
        cv.put(COLUMN_UKURAN, ukuran);
        cv.put(COLUMN_IMAGE_PATH, imagePath);

        long result = db.update(TABLE_NAME, cv, "no_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    // Menghapus salah satu baris data
    public void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "no_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    void exportDatabase() {
        try {
            // Path penyimpanan lokal yang ditentukan
            String storagePath = "/storage/emulated/0/Download"; // Ubah path ini sesuai dengan lokasi yang diinginkan
            File storageDirectory = new File(storagePath);

            if (!storageDirectory.exists()) {
                storageDirectory.mkdirs();
            }

            String currentDBPath = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
            String backupDBPath = storagePath + File.separator + DATABASE_NAME;

            File currentDB = new File(currentDBPath);
            File backupDB = new File(backupDBPath);

            if (currentDB.exists()) {
                FileInputStream fis = new FileInputStream(currentDB);
                FileOutputStream fos = new FileOutputStream(backupDB);
                FileChannel src = fis.getChannel();
                FileChannel dst = fos.getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                fis.close();
                fos.close();

                Toast.makeText(context, "Database exported to " + backupDBPath, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Database does not exist", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Export failed", Toast.LENGTH_SHORT).show();
        }
    }
}