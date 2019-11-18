package chiya.lolmates.requetes;

import java.util.HashMap;

public class Player
{
    private HashMap<String,String> match;
    private String participantId;

    public Player(HashMap<String,String> match, String participantId)
    {
        this.match = match;
        this.participantId = participantId;
    }

    public String participantId(){return participantId;}
    public String profileIcon(){return match.get("profileIcon");}
    public String accountId(){return match.get("accountId");}
    public String matchHistoryUri(){return match.get("matchHistoryUri");}
    public String currentAccountId(){return match.get("currentAccountId");}
    public String currentPlatformId(){return match.get("currentPlatformId");}
    public String summonerName(){return match.get("summonerName");}
    public String summonerId(){return match.get("summonerId");}
    public String platformId(){return match.get("platformId");}
}
