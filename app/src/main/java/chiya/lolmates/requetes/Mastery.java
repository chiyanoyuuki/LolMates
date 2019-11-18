package chiya.lolmates.requetes;

import java.util.HashMap;

public class Mastery
{
    private HashMap<String,String> mastery;
    
    public Mastery(HashMap<String,String> mastery)
    {
        this.mastery = mastery;
    }
    
    public String championPointsUntilNextLevel(){return mastery.get("championPointsUntilNextLevel");}
    public String chestGranted(){return mastery.get("chestGranted");}
    public String tokensEarned(){return mastery.get("tokensEarned");}
    public String championId(){return mastery.get("championId");}
    public String lastPlayTime(){return mastery.get("lastPlayTime");}
    public String summonerId(){return mastery.get("summonerId");}
    public String championLevel(){return mastery.get("championLevel");}
    public String championPoints(){return mastery.get("championPoints");}
    public String championPointsSinceLastLevel(){return mastery.get("championPointsSinceLastLevel");}
}
