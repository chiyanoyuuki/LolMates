package chiya.lolmates.requetes;

import android.database.Cursor;

import org.json.JSONException;

import java.util.HashMap;

import chiya.lolmates.BDD;

public class Summoner
{
    private HashMap<String,String> summoner;

    public Summoner(HashMap<String,String> summoner, BDD db)
    {
        this.summoner = summoner;
        db.summoner().create(this);
    }

    public Summoner(HashMap<String,String> summoner, BDD db, String s)
    {
        this.summoner = summoner;
        s = s.substring(s.indexOf(":")+1);
        this.summoner.put("region",s);
        db.summoner().create(this);
    }

    public Summoner(Cursor data) throws JSONException
    {
        this.summoner = new HashMap<>();
        for(int i=0;i<data.getColumnCount();i++)
        {
            this.summoner.put(data.getColumnName(i),data.getString(i));
        }
        data.close();
    }

    public String accountId(){return summoner.get("accountId");}
    public String profileIconId(){return summoner.get("profileIconId");}
    public String revisionDate(){return summoner.get("revisionDate");}
    public String name(){return summoner.get("name");}
    public String puuid(){return summoner.get("puuid");}
    public String id(){return summoner.get("id");}
    public String summonerLevel(){return summoner.get("summonerLevel");}
    public String region(){return summoner.get("region");}
}