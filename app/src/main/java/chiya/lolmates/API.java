package chiya.lolmates;

import chiya.lolmates.requetes.Summoner;

public class API
{
    private Accueil main;
    private String REGION;
    private final String KEY = "RGAPI-2338a871-5468-40fd-a1c3-a320e031af3c";

    public API(Accueil main)
    {
        this.main = main;
    }

    public void region(String region)
    {
        REGION = region;
    }

    /* ===========================
     *
     * LIVE GAME
     *
     * ============================*/

    public void livegame(String summonerId)
    {
        String url = "https://"+REGION+".api.riotgames.com/lol/spectator/v4/active-games/by-summoner/"+summonerId+"?api_key="+KEY;
        Async api = new Async(main,"livegame");
        api.get(url);
    }

    /* ===========================
     *
     * PARTIE STATS
     *
     * ============================*/

    public void partieData(String matchId, int i)
    {
        String url = "https://"+REGION+".api.riotgames.com/lol/match/v4/matches/"+matchId+"?api_key="+KEY;
        Async api = new Async(main,"partie_"+i);
        api.get(url);
    }

    /* ===========================
     *
     * RANKED STATS
     *
     * ============================*/

    public void matchesByAccountId(String accountId)
    {
        String url = "https://"+REGION+".api.riotgames.com/lol/match/v4/matchlists/by-account/"+accountId+"?api_key="+KEY+"&beginIndex=0&endIndex=8" +
                "&queue=400&queue=420&queue=430&queue=440&queue=450&queue=460&queue=470";
        Async api = new Async(main,"matches");
        api.get(url);
    }

    /* ===========================
     *
     * RANKED STATS
     *
     * ============================*/

    public void rankedStatsBySummonerId(String summonerId)
    {
        String url = "https://"+REGION+".api.riotgames.com/lol/league/v4/entries/by-summoner/"+summonerId+"?api_key="+KEY;
        Async api = new Async(main,"ranked");
        api.get(url);
    }

    /* ===========================
     *
     * CHAMPION MASTERY
     *
     * ============================*/

    public void championMasteriesBySummonerId(String summonerId)
    {
        String url = "https://"+REGION+".api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/"+summonerId+"?api_key="+KEY;
        Async api = new Async(main,"masteryArray");
        api.get(url);
    }

    public void championMasteryBySummonerId(String summonerId, String championId)
    {
        String url = "https://"+REGION+".api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/"+summonerId+"/by-champion/"+championId+"?api_key="+KEY;
        Async api = new Async(main,"mastery");
        api.get(url);
    }

    /* ===========================
     *
     * SUMMONER
     *
     * ============================*/

    public void summonerByName(String name, String region)
    {
        String url = "https://"+region+".api.riotgames.com/lol/summoner/v4/summoners/by-name/"+name+"?api_key="+KEY;
        Async api = new Async(main,"summoner:"+region);
        api.get(url);
    }

    public void firstsummonerByName(String name, String region)
    {
        String url = "https://"+region+".api.riotgames.com/lol/summoner/v4/summoners/by-name/"+name+"?api_key="+KEY;
        Async api = new Async(main,"firstsumm:"+region);
        api.get(url);
    }

    public void summonerByAccountId(String accountId)
    {
        String url = "https://"+REGION+".api.riotgames.com/lol/summoner/v4/summoners/by-account/"+accountId+"?api_key="+KEY;
        Async api = new Async(main,"summoner");
        api.get(url);
    }

    public void summonerByPuuid(String puuid)
    {
        String url = "https://"+REGION+".api.riotgames.com/lol/summoner/v4/summoners/by-puuid/"+puuid+"?api_key="+KEY;
        Async api = new Async(main,"summoner");
        api.get(url);
    }

    public void summonerById(String id)
    {
        String url = "https://"+REGION+".api.riotgames.com/lol/summoner/v4/summoners/"+id+"?api_key="+KEY;
        Async api = new Async(main,"summoner");
        api.get(url);
    }
}
