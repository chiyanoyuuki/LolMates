package chiya.lolmates;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.*;

import chiya.lolmates.requetes.Match;
import chiya.lolmates.requetes.PartieData;

public class TABLEHistoriqueParties
{
    private BDD base;

    public TABLEHistoriqueParties(BDD base)
    {
        this.base = base;
    }

    public void init(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE historiqueParties     (" +
                "gameId TEXTL PRIMARY KEY, " +
                "participants TEXTL," +
                "participantIdentities TEXTL," +
                "teams TEXTL, " +
                "queueId TEXTL, " +
                "gameType TEXTL, " +
                "gameDuration TEXTL, " +
                "platformId TEXTL," +
                "gameCreation TEXTL, " +
                "seasonId TEXTL, " +
                "gameVersion TEXTL, " +
                "mapId TEXTL, " +
                "gameMode TEXTL);");
    }

    public void create(PartieData data)
    {
        SQLiteDatabase db = base.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("gameId"                ,data.gameId());
        content.put("participants"          ,data.getParticipants());
        content.put("participantIdentities" ,data.getParticipantIdentities());
        content.put("teams"                 ,data.getTeams());
        content.put("queueId"               ,data.queueId());
        content.put("gameType"              ,data.gameType());
        content.put("gameDuration"          ,data.gameDuration());
        content.put("platformId"            ,data.platformId());
        content.put("gameCreation"          ,data.gameCreation());
        content.put("seasonId"              ,data.seasonId());
        content.put("gameVersion"           ,data.gameVersion());
        content.put("mapId"                 ,data.mapId());
        content.put("gameMode"              ,data.gameMode());
        db.insert("historiqueParties",null,content);
    }

    public Cursor select(String gameId)
    {
        SQLiteDatabase db = base.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM historiqueParties WHERE gameId=\""+gameId+"\"",new String[]{});
        cursor.moveToNext();
        return cursor;
    }

    public Cursor findPlayer(String summonerId)
    {
        SQLiteDatabase db = base.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM historiqueParties h WHERE " +
                "participantIdentities LIKE '%\"summonerId\":\""+summonerId+"\"%'"
                ,new String[]{});
         //       " AND \""+queue+"\" = (SELECT queueId FROM parties WHERE gameId = h.gameId)",new String[]{});
        return cursor;
    }

    public void reset()
    {
        SQLiteDatabase db = base.getWritableDatabase();
        db.delete("historiqueParties","",new String[]{});
    }
}
