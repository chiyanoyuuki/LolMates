package chiya.lolmates;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import chiya.lolmates.requetes.Live;

public class TABLEHonor
{
    private BDD base;

    public TABLEHonor(BDD base)
    {
        this.base = base;
    }

    public void init(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE honors     (" +
                "gameId TEXTL, " +
                "summonerId TEXTL);");
    }

    public void create(String gameId, String summonerId)
    {
        SQLiteDatabase db = base.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put("gameId"                ,gameId);
        content.put("summonerId"            ,summonerId);
        db.insert("honors",null,content);

        db.execSQL("UPDATE stats SET honors=honors+1 WHERE summonerId=\""+summonerId+"\"");
        base.stats().score(summonerId);
    }

    public void delete(String gameId, String summonerId)
    {
        SQLiteDatabase db = base.getWritableDatabase();
        db.delete("honors","gameId='"+gameId+"' AND summonerId='"+summonerId+"'",new String[]{});

        db.execSQL("UPDATE stats SET honors=honors-1 WHERE summonerId=\""+summonerId+"\"");
        base.stats().score(summonerId);
    }

    public Cursor getGame(String gameId)
    {
        SQLiteDatabase db = base.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM honors where gameId='"+gameId+"'",new String[]{});
        return cursor;
    }

    public Cursor getSummoner(String summonerId)
    {
        SQLiteDatabase db = base.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM honors where summonerId='"+summonerId+"'",new String[]{});
        return cursor;
    }

    public boolean honored(String gameId, String summonerId)
    {
        SQLiteDatabase db = base.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM honors where gameId=\""+gameId+"\" AND summonerId='"+summonerId+"'",new String[]{});
        return cursor.moveToNext();
    }

    public void reset()
    {
        SQLiteDatabase db = base.getWritableDatabase();
        db.delete("honors","",new String[]{});
    }
}
