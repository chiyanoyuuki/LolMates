package chiya.lolmates.requetes;

import java.util.HashMap;

public class LiveParticipant
{
    private HashMap<String,String> player;

    public LiveParticipant(HashMap<String,String> spectator)
    {
        this.player = spectator;
    }

    public String championId(){return player.get("championId");}
    public String profileIconId(){return player.get("profileIconId");}
    public String bot(){return player.get("bot");}
    public String teamId(){return player.get("teamId");}
    public String summonerName(){return player.get("summonerName");}
    public String spell1Id(){return player.get("spell1Id");}
    public String summonerId(){return player.get("summonerId");}
    public String spell2Id(){return player.get("spell2Id");}
    public String perks(){return player.get("perks");}
    public String gameCustomizationObjects(){return player.get("gameCustomizationObjects");}
}