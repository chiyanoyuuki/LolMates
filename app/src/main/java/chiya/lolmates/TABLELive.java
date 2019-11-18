package chiya.lolmates;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import chiya.lolmates.requetes.Live;

public class TABLELive
{
    private BDD base;

    public TABLELive(BDD base)
    {
        this.base = base;
    }

    public void init(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE live     (" +
                "gameId INTEGER PRIMARY KEY, " +
                "gameStartTime TEXTL, " +
                "platformId TEXTL, " +
                "gameMode TEXTL, " +
                "mapId TEXTL," +
                "gameType TEXTL, " +
                "gameQueueConfigId TEXTL, " +
                "observers TEXTL, " +
                "participants TEXTL, " +
                "gameLength TEXTL, " +
                "bannedChampions TEXTL," +
                "date DATETIME DEFAULT CURRENT_TIMESTAMP);");
    }

    public void create(Live match)
    {
        SQLiteDatabase db = base.getWritableDatabase();
        db.delete("live","",new String[]{});

        ContentValues content = new ContentValues();
        content.put("gameId"                ,match.gameId());
        content.put("gameStartTime"         ,match.gameStartTime());
        content.put("platformId"            ,match.platformId());
        content.put("gameMode"              ,match.gameMode());
        content.put("mapId"                 ,match.mapId());
        content.put("gameType"              ,match.gameType());
        content.put("gameQueueConfigId"     ,match.gameQueueConfigId());
        content.put("observers"             ,match.observers());
        content.put("participants"          ,match.participants());
        content.put("gameLength"            ,match.gameLength());
        content.put("bannedChampions"       ,match.bannedChampions());
        db.insert("live",null,content);
    }

    public Cursor get()
    {
        SQLiteDatabase db = base.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM live",new String[]{});
        return cursor;
    }

    public void reset()
    {
        SQLiteDatabase db = base.getWritableDatabase();
        db.delete("live","",new String[]{});
    }
}
