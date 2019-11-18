package chiya.lolmates.requetes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Team
{
    private HashMap<String,String> match;
    private ArrayList<String> bans;

    public Team(HashMap<String,String> match) throws JSONException
    {
        this.match = match;
        createBans();
    }

    private void createBans() throws JSONException
    {
        bans = new ArrayList<>();
        JSONArray array =  new JSONArray(match.get("bans"));

        for(int i=0;i<array.length();i++)
        {
            JSONObject obj = array.getJSONObject(i);
            bans.add(obj.getString("championId"));
        }
    }

    public String towerKills(){return match.get("towerKills");}
    public String riftHeraldKills(){return match.get("riftHeraldKills");}
    public String firstBlood(){return match.get("firstBlood");}
    public String inhibitorKills(){return match.get("inhibitorKills");}
    public ArrayList<String> bans(){return bans;}
    public String firstBaron(){return match.get("firstBaron");}
    public String firstDragon(){return match.get("firstDragon");}
    public String dominionVictoryScore(){return match.get("dominionVictoryScore");}
    public String dragonKills(){return match.get("dragonKills");}
    public String baronKills(){return match.get("baronKills");}
    public String firstInhibitor(){return match.get("firstInhibitor");}
    public String firstTower(){return match.get("firstTower");}
    public String vilemawKills(){return match.get("vilemawKills");}
    public String firstRiftHerald(){return match.get("firstRiftHerald");}
    public String teamId(){return match.get("teamId");}
    public String win(){return match.get("win");}
}