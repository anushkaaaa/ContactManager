package Data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;

import com.example.anu.contactmanager.MainActivity;

import java.util.ArrayList;
import java.util.List;

import Model.Contact;
import Utils.Util;

/**
 * Created by Anushka on 29-Dec-17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }
    public DatabaseHandler(Context context) {
        super(context,Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    //To create table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //SQL Command to create table
        String CREATE_CONTACT_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "(" + Util.KEY_ID +" INTEGER PRIMARY KEY," + Util.KEY_NAME + " TEXT," + Util.KEY_PHONENUMBER+ " TEXT"+ ")";
        // To execute that command
        sqLiteDatabase.execSQL(CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Delete the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ Util.TABLE_NAME);
        //Create table again
        onCreate(sqLiteDatabase);
    }

    //CRUD OPERATION : Create, read, update, delete
    public void add(Contact contact){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(Util.KEY_NAME, contact.getName());
        value.put(Util.KEY_PHONENUMBER, contact.getPhoneNumber());
        // id isn't added because it will be incremented
        sqLiteDatabase.insert(Util.TABLE_NAME,null,value);
        sqLiteDatabase.close();
    }

    public Contact get(int id){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(Util.TABLE_NAME, new String[]{Util.KEY_ID,Util.KEY_NAME,Util.KEY_PHONENUMBER}, Util.KEY_ID + "=?", new String[] {String.valueOf(id)},null,null,null,null);
        if(cursor != null)
            cursor.moveToFirst();
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2));
        // i is the column index
        return contact;
    }

    //retrieve all contacts

    public List<Contact> getAll() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<Contact> contactList = new ArrayList<>();
        // using select all queries
        String selectAll = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(selectAll,null);
        // loop
        if(cursor.moveToFirst()){
            do{
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                //add to list
                contactList.add(contact);
            }while(cursor.moveToNext());
        }
        return contactList;
    }

    //update
    public int update(Contact contact){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, contact.getName());
        values.put(Util.KEY_PHONENUMBER, contact.getPhoneNumber());
        //update row
        return sqLiteDatabase.update(Util.TABLE_NAME,values,Util.KEY_ID+"=?",new String[]{String.valueOf(contact.getId())});
    }
    //delete
    public void delete(Contact contact){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(Util.TABLE_NAME,Util.KEY_ID+"=?",new String[]{String.valueOf(contact.getId())});
        sqLiteDatabase.close();
    }

    //count
    public int count(){
        String count = "SELECT * FROM " + Util.TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(count,null);
        return cursor.getCount();
    }

}
