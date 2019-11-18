package chiya.lolmates;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;

import chiya.lolmates.requetes.Participant;
import chiya.lolmates.requetes.Player;
import chiya.lolmates.requetes.PlayerStats;
import chiya.lolmates.requetes.Ranked;

public class TABLEStats
{
    private BDD base;

    private final String formula =
            "(CASE WHEN deaths=0 AND kills+assists >= 6 THEN 45 " +
                    "WHEN ((kills*1.0+assists*1.0)/(deaths*1.0)) >= 6                 THEN 45 " +
                    "WHEN deaths=0 THEN (kills+assists)*(45.0/6.0) " +
                    "ELSE ((kills*1.0+assists*1.0)/(deaths*1.0))*(45.0/6.0)         END) +" +
            "(CASE WHEN honors >= ((wins*1.0+loses*1.0)/2.0)    AND wins+loses>3    THEN 20 " +
                    "ELSE ((honors*1.0)*(20.0/((wins*1.0+loses*1.0)/2.0)))   END) +" +
            "(CASE WHEN (wins*1.0)/(wins*1.0+loses*1.0) >= 0.75 AND wins+loses>3    THEN 25 " +
                    "WHEN wins+loses<4 THEN ((wins*1.0)/(wins*1.0+loses*1.0))*12.5 " +
                    "ELSE (((wins*1.0)/(wins*1.0+loses*1.0))+0.25)*25.0             END) +" +
            "(CASE WHEN wins+loses >= 5                                             THEN 10 " +
                    "ELSE (wins*1.0+loses*1.0)*2.0                           END)";

    public TABLEStats(BDD base)
    {
        this.base = base;
    }

    public void init(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE stats     (" +
                "summonerId TEXTL PRIMARY KEY, " +
                "summonerName TEXTL, " +
                "wins       NUMERIC, " +
                "loses      NUMERIC, " +
                "honors     NUMERIC, " +
                "kills      NUMERIC, " +
                "deaths     NUMERIC, " +
                "assists    NUMERIC, " +
                "score      NUMERIC, " +
                "champion   TEXTL);");
    }

    private void create(Participant part, Player player, int win, int lose)
    {
        SQLiteDatabase db = base.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put("summonerId"          ,player.summonerId());
        content.put("summonerName"        ,player.summonerName());
        content.put("wins"                ,win);
        content.put("loses"               ,lose);
        content.put("honors"              ,0);
        content.put("kills"               ,part.stats().kills());
        content.put("deaths"              ,part.stats().deaths());
        content.put("assists"             ,part.stats().assists());
        content.put("score"               ,0);
        content.put("champion"            ,part.championId());
        db.insert("stats",null,content);
    }

    public void update(Participant part, Player player, int win, int lose)
    {
        SQLiteDatabase db = base.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM stats WHERE summonerId=\""+player.summonerId()+"\"",new String[]{});
        if(cursor.moveToNext())
        {
            db.execSQL("UPDATE stats SET " +
                    "summonerName=\""+player.summonerName()+"\", " +
                    "kills=kills+"+part.stats().kills()+", " +
                    "deaths=deaths+"+part.stats().deaths()+", " +
                    "assists=assists+"+part.stats().assists()+", " +
                    "wins=wins+"+win+", " +
                    "loses=loses+"+lose+", " +
                    "champion=\""+part.championId() +"\""+
                    " WHERE summonerId=\""+player.summonerId()+"\"");
        }
        else
        {
            create(part,player,win,lose);
        }
        score(player.summonerId());
    }

    public void score(String summonerId)
    {
        SQLiteDatabase db = base.getWritableDatabase();
        db.execSQL("UPDATE stats SET score = "+formula+" WHERE summonerId=\""+summonerId+"\"",new String[]{});
    }

    public PlayerStats selectPlayer(String summonerId)
    {
        SQLiteDatabase db = base.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT (SELECT COUNT(*) FROM STATS s2 WHERE s2.score>s1.score)+1 AS POS, (SELECT COUNT(*) FROM STATS s3) AS NB, * FROM STATS s1 WHERE summonerId=\"" + summonerId + "\" ORDER BY POS ASC", new String[]{});

        try {if (cursor.moveToNext()) return new PlayerStats(cursor);}
        catch(JSONException e){e.printStackTrace();}
        return null;
    }

    public Cursor selectAll(String clicked)
    {
        String start = clicked.substring(0,clicked.length()-1);

        String tri = "POS";
        if(start.equals("name"))tri = "summonerName";
        else if(start.equals("kda"))tri = "KDA";
        else if(start.equals("winlose")) tri = "RATIO";
        else if(start.equals("honor")) tri = "honors";
        else if(start.equals("score")) tri = "score";

        String ord = "ASC";
        if(clicked.endsWith("-"))ord = "DESC";

        SQLiteDatabase db = base.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " +
                "((kills*1.0+assists*1.0)/deaths*1.0)                       as KDA, " +
                "((wins*1.0)/(wins*1.0+loses*1.0))                          as RATIO," +
                "(SELECT COUNT(*) FROM STATS s2 WHERE s2.score>s1.score)+1  AS POS, " +
                "* FROM STATS s1 ORDER BY "+tri+" "+ord+",POS ASC", new String[]{});
        return cursor;
    }

    public void reset()
    {
        SQLiteDatabase db = base.getWritableDatabase();
        db.delete("stats","",new String[]{});
    }
}
