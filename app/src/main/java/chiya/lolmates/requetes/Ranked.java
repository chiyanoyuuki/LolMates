package chiya.lolmates.requetes;

import android.database.Cursor;

import org.json.JSONException;

import java.util.HashMap;

public class Ranked
{
    private HashMap<String,String> league;

    public Ranked(HashMap<String,String> league)
    {
        this.league = league;
    }

    public Ranked(Cursor data) throws JSONException
    {
        this.league = new HashMap<>();
        for(int i=0;i<data.getColumnCount();i++)
        {
            this.league.put(data.getColumnName(i),data.getString(i));
        }
        data.close();
    }

    public String wins(){return league.get("wins");}
    public String freshBlood(){return league.get("freshBlood");}
    public String summonerName(){return league.get("summonerName");}
    public String leaguePoints(){return league.get("leaguePoints");}
    public String losses(){return league.get("losses");}
    public String inactive(){return league.get("inactive");}
    public String tier(){return league.get("tier");}
    public String veteran(){return league.get("veteran");}
    public String leagueId(){return league.get("leagueId");}
    public String hotStreak(){return league.get("hotStreak");}
    public String queueType(){return league.get("queueType");}
    public String rank(){return league.get("rank");}
    public String summonerId(){return league.get("summonerId");}
}