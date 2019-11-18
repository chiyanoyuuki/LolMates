package chiya.lolmates;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import chiya.lolmates.requetes.Ranked;
import chiya.lolmates.requetes.Summoner;

public class TABLERanked
{
    private BDD base;

    public TABLERanked(BDD base)
    {
        this.base = base;
    }

    public void init(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE ranked     (" +
                "queueType TEXTL PRIMARY KEY, " +
                "summonerName TEXTL," +
                "hotStreak TEXTL," +
                "wins TEXTL, " +
                "veteran TEXTL, " +
                "losses TEXTL, " +
                "rank TEXTL, " +
                "tier TEXTL, " +
                "inactive TEXTL, " +
                "freshBlood TEXTL, " +
                "leagueId TEXTL, " +
                "summonerId TEXTL, " +
                "leaguePoints TEXTL);");
    }

    public void create(Ranked data)
    {
        SQLiteDatabase db = base.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("queueType"     ,data.queueType());
        content.put("summonerName"  ,data.summonerName());
        content.put("hotStreak"     ,data.hotStreak());
        content.put("wins"          ,data.wins());
        content.put("veteran"       ,data.veteran());
        content.put("losses"        ,data.losses());
        content.put("rank"          ,data.rank());
        content.put("tier"          ,data.tier());
        content.put("inactive"      ,data.inactive());
        content.put("freshBlood"    ,data.freshBlood());
        content.put("leagueId"      ,data.leagueId());
        content.put("summonerId"    ,data.summonerId());
        content.put("leaguePoints"  ,data.leaguePoints());
        db.insert("ranked",null,content);
    }

    public void update(Ranked data)
    {
        SQLiteDatabase db = base.getWritableDatabase();
        db.execSQL("UPDATE ranked SET " +
                "wins=\""+data.wins()+"\", " +
                "losses=\""+data.losses()+"\", " +
                "rank=\""+data.rank()+"\"," +
                "tier=\""+data.tier()+"\"," +
                "leaguePoints=\""+data.leaguePoints()+"\" " +
                "WHERE queueType=\""+data.queueType()+"\"");
    }

    public Cursor select(String queue)
    {
        SQLiteDatabase db = base.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ranked WHERE queueType=\""+queue+"\"",new String[]{});
        return cursor;
    }
}
