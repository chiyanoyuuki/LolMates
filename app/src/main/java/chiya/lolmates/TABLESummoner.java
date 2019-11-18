package chiya.lolmates;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import chiya.lolmates.requetes.Summoner;

public class TABLESummoner
{
    private BDD base;

    public TABLESummoner(BDD base)
    {
        this.base = base;
    }

    public void init(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE summoner     (" +
                "accountId TEXTL PRIMARY KEY, " +
                "profileIconId TEXTL," +
                "revisionDate TEXTL," +
                "name TEXTL, " +
                "puuid TEXTL, " +
                "id TEXTL, " +
                "summonerLevel TEXTL, " +
                "region TEXTL);");
    }

    public void create(Summoner data)
    {
        SQLiteDatabase db = base.getWritableDatabase();

        db.delete("summoner","",new String[]{});

        ContentValues content = new ContentValues();
        content.put("accountId"         ,data.accountId());
        content.put("profileIconId"     ,data.profileIconId());
        content.put("revisionDate"      ,data.revisionDate());
        content.put("name"              ,data.name());
        content.put("puuid"             ,data.puuid());
        content.put("id"                ,data.id());
        content.put("summonerLevel"     ,data.summonerLevel());
        content.put("region"            ,data.region());
        db.insert("summoner",null,content);
    }

    public Cursor select()
    {
        SQLiteDatabase db = base.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM summoner",new String[]{});
        return cursor;
    }
}
