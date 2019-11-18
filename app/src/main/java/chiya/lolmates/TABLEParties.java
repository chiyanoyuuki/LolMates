package chiya.lolmates;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;

import chiya.lolmates.requetes.Match;
import chiya.lolmates.requetes.PartieData;

public class TABLEParties
{
    private BDD base;

    public TABLEParties(BDD base)
    {
        this.base = base;
    }

    public void init(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE parties     (" +
                "gameId INTEGER PRIMARY KEY, " +
                "role TEXTL, " +
                "season TEXTL, " +
                "platformId TEXTL, " +
                "champion TEXTL," +
                "queue TEXTL, " +
                "lane TEXTL, " +
                "timestamp TEXTL);");
    }

    public void create(Match match)
    {
        SQLiteDatabase db = base.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("gameId"        ,match.gameId());
        content.put("role"          ,match.role());
        content.put("season"        ,match.season());
        content.put("platformId"    ,match.platformId());
        content.put("champion"      ,match.champion());
        content.put("queue"         ,match.queue());
        content.put("lane"          ,match.lane());
        content.put("timestamp"     ,match.timestamp());
        db.insert("parties",null,content);
    }

    public boolean exists(String gameId)
    {
        SQLiteDatabase db = base.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM parties WHERE gameId=\""+gameId+"\"",new String[]{});
        return cursor.moveToNext();
    }

    public Cursor selectLast(String queues)
    {
        SQLiteDatabase db = base.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM parties WHERE QUEUE IN ("+queues+") ORDER BY TIMESTAMP DESC LIMIT 20",new String[]{});
        return cursor;
    }

    public String selectPlayer(String player)
    {
        SQLiteDatabase db = base.getReadableDatabase();

        String summId = "";

        Cursor cursor0 = db.rawQuery("SELECT * FROM historiqueParties h WHERE participantIdentities LIKE '%\"summonerName\":\""+player+"\"%'",new String[]{});
        if(cursor0.moveToNext())
        {
            try
            {
                PartieData data = new PartieData(cursor0);
                summId = data.getSummId(player);
            }
            catch(JSONException e){e.printStackTrace();}
            cursor0.close();
        }

        return summId;
    }

    public Cursor selectFromPlayer(String summId)
    {
        SQLiteDatabase db = base.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM parties WHERE gameId IN " +
                "(SELECT gameId FROM historiqueParties " +
                "WHERE participantIdentities LIKE '%\"summonerId\":\""+summId+"\"%') ORDER BY TIMESTAMP DESC LIMIT 20"
            ,new String[]{});
        return cursor;
    }

    public void reset()
    {
        SQLiteDatabase db = base.getWritableDatabase();
        db.delete("parties","",new String[]{});
    }
}
