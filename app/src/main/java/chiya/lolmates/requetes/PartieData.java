package chiya.lolmates.requetes;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PartieData
{
    private String summonerId;
    private Stats stats;
    private HashMap<String,String> data;
    private HashMap<String,Player> players;
    private HashMap<String,Team> teams;
    private HashMap<String,Participant> participants;
    private ArrayList<String> bans1, bans2;

    public PartieData(HashMap<String,String> match) throws JSONException
    {
        this.data = match;
        init();
    }

    public PartieData(Cursor data) throws JSONException
    {
        this.data = new HashMap<>();
        for(int i=0;i<data.getColumnCount();i++)
        {
            this.data.put(data.getColumnName(i),data.getString(i));
        }
        data.close();
        init();
    }

    public PartieData(Cursor data, String summonerId) throws JSONException
    {
        this.summonerId = summonerId;
        this.data = new HashMap<>();
        for(int i=0;i<data.getColumnCount();i++)
        {
            this.data.put(data.getColumnName(i),data.getString(i));
        }
        init();
    }

    private void init() throws JSONException
    {
        players = new HashMap<>();
        teams = new HashMap<>();
        participants = new HashMap<>();

        createPlayers();
        createTeams();
        createParticipants();
        createBans();
    }

    private void createPlayers() throws JSONException
    {
        JSONArray array = new JSONArray(data.get("participantIdentities"));
        for(int i=0;i<array.length();i++)
        {
            HashMap<String,String> results = new HashMap<>();

            JSONObject obj = array.getJSONObject(i);
            String participantId = obj.getString("participantId");
            obj = obj.getJSONObject("player");
            Iterator<String> it = obj.keys();
            while(it.hasNext())
            {
                String key = it.next();
                results.put(key,obj.getString(key));
            }

            players.put(participantId,new Player(results,gameId()));
        }
    }

    private void createTeams() throws JSONException
    {
        JSONArray array = new JSONArray(data.get("teams"));
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

            Team team = new Team(results);
            teams.put(team.teamId(),team);
        }
    }

    private void createParticipants() throws JSONException
    {
        JSONArray array = new JSONArray(data.get("participants"));
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

            Participant participant = new Participant(results);

            if(summonerId!=null&&summonerId.equals(players.get(participant.participantId()).summonerId())) stats = participant.stats();

            participants.put(participant.participantId(),participant);
        }
    }

    public String getSummId(String player)
    {
        Iterator<String> it = players.keySet().iterator();
        while(it.hasNext())
        {
            String key = it.next();
            Player p = players.get(key);
            if(p.summonerName().toUpperCase().equals(player.toUpperCase()))return p.summonerId();
        }
        return "";
    }

    private void createBans() throws JSONException
    {
        bans1 = new ArrayList<>();
        bans2 = new ArrayList<>();

        Iterator<String> it = teams.keySet().iterator();
        while(it.hasNext())
        {
            String key = it.next();
            Team team = teams.get(key);
            if(key.equals("100"))bans1 = team.bans();
            else bans2 = team.bans();
        }
    }

    public Participant participant(String s){return participants.get(s);}
    public HashMap<String,Participant> participants(){return participants;}

    public Team team(String s){return teams.get(s);}
    public HashMap<String,Team> teams(){return teams;}

    public Player player(String s){return players.get(s);}
    public HashMap<String,Player> players(){return players;}

    public String gameId(){return data.get("gameId");}
    public String queueId(){return data.get("queueId");}
    public String gameType(){return data.get("gameType");}
    public String gameDuration(){return data.get("gameDuration");}
    public String platformId(){return data.get("platformId");}
    public String gameCreation(){return data.get("gameCreation");}
    public String seasonId(){return data.get("seasonId");}
    public String gameVersion(){return data.get("gameVersion");}
    public String mapId(){return data.get("mapId");}
    public String gameMode(){return data.get("gameMode");}

    public String getParticipants() {return data.get("participants");}
    public String getParticipantIdentities(){return data.get("participantIdentities");}
    public String getTeams(){return data.get("teams");}

    public ArrayList<String> bans1(){return bans1;}
    public ArrayList<String> bans2(){return bans2;}

    public Stats get(){return stats;}
}