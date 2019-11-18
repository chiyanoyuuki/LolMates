package chiya.lolmates.requetes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public class Participant
{
    private HashMap<String,String> participant;
    private Stats stats;

    public Participant(HashMap<String,String> participant) throws JSONException
    {
        this.participant = participant;
        createStats();
    }

    private void createStats() throws JSONException
    {
        JSONObject obj = new JSONObject(participant.get("stats"));
        HashMap<String,String> results = new HashMap<>();
        Iterator<String> it = obj.keys();
        while(it.hasNext())
        {
            String key = it.next();
            results.put(key,obj.getString(key));
        }
        this.stats = new Stats(results);
    }

    public Stats stats(){return stats;}

    public String participantId(){return participant.get("participantId");}
    public String championId(){return participant.get("championId");}
    public String teamId(){return participant.get("teamId");}
    public String timeline(){return participant.get("timeline");}
    public String spell1Id(){return participant.get("spell1Id");}
    public String spell2Id(){return participant.get("spell2Id");}
}