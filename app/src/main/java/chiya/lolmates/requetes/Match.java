package chiya.lolmates.requetes;

import android.database.Cursor;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import chiya.lolmates.BDD;

public class Match
{
    private PartieData data;
    private HashMap<String,String> match;

    public Match(HashMap<String,String> match)
    {
        this.match = match;
    }

    public Match(Cursor data)
    {
        this.match = new HashMap<>();
        for(int i=0;i<data.getColumnCount();i++)
        {
            this.match.put(data.getColumnName(i),data.getString(i));
        }
    }

    public void setData(HashMap<String,String> data, BDD db) throws JSONException
    {
        PartieData datas = new PartieData(data);
        db.parties().create(this);
        db.historiqueParties().create(datas);

        boolean win = datas.teams().get("100").win().equals("Win");

        HashMap<String,Player> players = datas.players();
        HashMap<String,Participant> participants = datas.participants();

        Iterator<String> it = players.keySet().iterator();
        while(it.hasNext())
        {
            String key          = it.next();
            Player player       = players.get(key);
            Participant part    = participants.get(key);
            int w = 0;
            int l = 0;
            String team = part.teamId();
            if(team.equals("100"))
            {
                if(win)w++;
                else l++;
            }
            else
            {
                if(win)l++;
                else w++;
            }

            //System.out.println(player.summonerName()+":("+part.teamId()+":"+datas.teams().get("100").win()+"):("+w+"="+l+"):"+part.stats().kills()+"/"+part.stats().deaths()+"/"+part.stats().assists());
            db.stats().update(part,player,w,l);
        }

        this.data = datas;
    }

    public void chargeData(BDD db) throws JSONException
    {
        this.data = new PartieData(db.historiqueParties().select(gameId()));
    }

    public ArrayList<String> bans1(){return data.bans1();}
    public ArrayList<String> bans2(){return data.bans2();}

    public String gameId(){return match.get("gameId");}
    public String role(){return match.get("role");}
    public String season(){return match.get("season");}
    public String platformId(){return match.get("platformId");}
    public String champion(){return match.get("champion");}
    public String queue(){return match.get("queue");}
    public String lane(){return match.get("lane");}
    public String timestamp(){return match.get("timestamp");}

    public PartieData data(){return data;}
}