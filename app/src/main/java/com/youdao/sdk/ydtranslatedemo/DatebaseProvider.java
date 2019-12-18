package com.youdao.sdk.ydtranslatedemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatebaseProvider extends ContentProvider {
    public static final int WORD_DIR=0;
    public static final int WORD_ITEM=1;
    public static final String AUTHRITY="com.youdao.sdk.ydtranslatedemo.provider";
    public static UriMatcher uriMatcher;
    private MySQLite mySQLite;
    static {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHRITY,"word",WORD_DIR);
        uriMatcher.addURI(AUTHRITY,"word/#",WORD_ITEM);
    }
    public DatebaseProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        //throw new UnsupportedOperationException("Not yet implemented");
        SQLiteDatabase db=mySQLite.getWritableDatabase();
        int deletedRows=0;
        switch (uriMatcher.match(uri)){
            case WORD_DIR:
                deletedRows =db.delete("word",selection,selectionArgs);
                break;
            case WORD_ITEM:
                String wordId=uri.getPathSegments().get(1);
                deletedRows=db.delete("word","id=?",new String[]{wordId});
                default:break;
        }
        return deletedRows;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        //throw new UnsupportedOperationException("Not yet implemented");
        switch (uriMatcher.match(uri)){
            case WORD_DIR:
                return "vnd.android.cursor.dir/vnd.com.youdao.sdk.ydtranslatedemo.provider.word";
            case  WORD_ITEM:
                return "vnd.android.cursor.item/vnd.com.youdao.sdk.ydtranslatedemo.provider.word";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        //throw new UnsupportedOperationException("Not yet implemented");
        SQLiteDatabase db=mySQLite.getWritableDatabase();
        Uri uriReturn=null;
        switch (uriMatcher.match(uri)){
            case WORD_DIR:
            case WORD_ITEM:
                long newWordId=db.insert("word",null,values);
                uriReturn=Uri.parse("content://"+AUTHRITY+"/word/"+newWordId);
                break;
                default:break;
        }
        return uriReturn;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        mySQLite=new MySQLite(getContext(),"wordbank.db",null,1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        //throw new UnsupportedOperationException("Not yet implemented");
        SQLiteDatabase db=mySQLite.getReadableDatabase();
        Cursor cursor=null;
        switch (uriMatcher.match(uri)){
            case WORD_DIR:
                cursor=db.query("word",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case WORD_ITEM:
                String wordId=uri.getPathSegments().get(1);
                cursor=db.query("word",projection,"id=?",new String[]{wordId},null,null,sortOrder);
                break;
                default:break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        //throw new UnsupportedOperationException("Not yet implemented");
        SQLiteDatabase db=mySQLite.getWritableDatabase();
        int updatedRows=0;
        switch (uriMatcher.match(uri)){
            case WORD_DIR:
                updatedRows=db.update("word",values,selection,selectionArgs);
                break;
            case WORD_ITEM:
                String wordId=uri.getPathSegments().get(1);
                updatedRows=db.update("word",values,"id=?",new String[]{wordId});
                break;
        }
        return updatedRows;
    }
}
