package chiya.lolmates.requetes;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PlayerStats
{
    private HashMap<String,String> stats;

    public PlayerStats(HashMap<String,String> live) throws JSONException
    {
        this.stats = live;
    }

    public PlayerStats(Cursor data) throws JSONException
    {
        this.stats = new HashMap<>();
        for(int i=0;i<data.getColumnCount();i++)
        {
            this.stats.put(data.getColumnName(i),data.getString(i));
        }
        data.close();
    }

    public PlayerStats(Cursor data, boolean b) throws JSONException
    {
        this.stats = new HashMap<>();
        for(int i=0;i<data.getColumnCount();i++)
        {
            this.stats.put(data.getColumnName(i),data.getString(i));
        }
    }

    public String POS(){return stats.get("POS");}
    public String NB(){return stats.get("NB");}
    public String summonerName(){return stats.get("summonerName");}
    public String summonerId(){return stats.get("summonerId");}
    public double wins()    {return Double.parseDouble(stats.get("wins"));}
    public double loses()   {return Double.parseDouble(stats.get("loses"));}
    public double honors()  {return Double.parseDouble(stats.get("honors"));}
    public double kills()   {return Double.parseDouble(stats.get("kills"));}
    public double deaths()  {return Double.parseDouble(stats.get("deaths"));}
    public double assists() {return Double.parseDouble(stats.get("assists"));}
    public double score()   {return Double.parseDouble(stats.get("score"));}
    public String champion(){return stats.get("champion");}
}