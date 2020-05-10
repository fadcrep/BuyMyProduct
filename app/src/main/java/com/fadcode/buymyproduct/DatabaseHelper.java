package com.fadcode.buymyproduct;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.fadcode.buymyproduct.Model.Product;
import com.fadcode.buymyproduct.Variable.Variable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String NOM_TABLE = "product";

    private static final int NUMERO_VERSION = 1 ;

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String FILENAME = "filename";


    public DatabaseHelper(@Nullable Context context) {
        super(context, Variable.NOM_BASE_DE_DONNEES, null, NUMERO_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+NOM_TABLE+ "(ID TEXT PRIMARY KEY, TITLE TEXT, FILENAME TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+NOM_TABLE);
    }

    public ArrayList<Product> productListFromSQLite(){
        ArrayList<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+NOM_TABLE, null);
        if (cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String productId = cursor.getString(0);
                String titleProduct = cursor.getString(1);
                String filenameProduct = cursor.getString(2);
                products.add(new Product(productId, titleProduct, filenameProduct));
            }
        }
        return products;
    }

    public boolean addProductFromSQLite(ArrayList<Product> products){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        for( Product product : products) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, product.getId());
            contentValues.put(TITLE, product.getTitle());
            contentValues.put(FILENAME, product.getFilename());

            result = db.insert(NOM_TABLE, null, contentValues);
        }
       db.close();
       return !(result == -1);
    }
}
