package chiya.lolmates.requetes;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Live
{
    private HashMap<String,String> live;
    private ArrayList<LiveParticipant> players;
    private ArrayList<String> bans1, bans2;

    public Live(HashMap<String,String> live) throws JSONException
    {
        this.live = live;
        createPlayers();
        createBans();
    }

    public Live(Cursor data) throws JSONException
    {
        this.live = new HashMap<>();
        for(int i=0;i<data.getColumnCount();i++)
        {
            this.live.put(data.getColumnName(i),data.getString(i));
        }
        data.close();
        createPlayers();
        createBans();
    }

    private void createPlayers() throws JSONException
    {
        players = new ArrayList<>();
        JSONArray array = new JSONArray(live.get("participants"));
        for(int i=0;i<array.length();i++)
        {
            HashMap<String,String> results = new HashMap<>();

            JSONObject obj = array.getJSONObject(i);
            Iterator<String> it = obj.keys();
            while(it.hasNext())
            {
                String key = it.next();
                results.put(key,obj.getString(key));
            }

            players.add(new LiveParticipant(results));
        }
    }

    private void createBans() throws JSONException
    {
        bans1 = new ArrayList<>();
        bans2 = new ArrayList<>();
        JSONArray array = new JSONArray(live.get("bannedChampions"));
        for(int i=0;i<array.length();i++)
        {
            JSONObject obj = array.getJSONObject(i);
            String team = obj.getString("teamId");
            if(team.equals("100"))bans1.add(obj.getString("championId"));
            else bans2.add(obj.getString("championId"));
        }
    }

    public LiveParticipant getPlayer(int i){return players.get(i);}
    public ArrayList<LiveParticipant> getPlayers(){return players;}

    public String participants(){return live.get("participants");}
    public String gameId(){return live.get("gameId");}
    public String gameType(){return live.get("gameType");}
    public String gameStartTime(){return live.get("gameStartTime");}
    public String mapId(){return live.get("mapId");}
    public String platformId(){return live.get("platformId");}
    public String gameLength(){return live.get("gameLength");}
    public String gameMode(){return live.get("gameMode");}
    public String bannedChampions(){return live.get("bannedChampions");}
    public String observers(){return live.get("observers");}
    public String gameQueueConfigId(){return live.get("gameQueueConfigId");}
    public ArrayList<String>bans1(){return bans1;}
    public ArrayList<String>bans2(){return bans2;}
    public String date(){return live.get("date");}
}