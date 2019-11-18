package chiya.lolmates;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import chiya.lolmates.requetes.Match;
import chiya.lolmates.requetes.Participant;
import chiya.lolmates.requetes.Player;
import chiya.lolmates.requetes.Team;

public class HistoriquePartie extends FrameLayout implements View.OnClickListener
{
    private Accueil main;
    private LinearLayout team1, team2;
    private Match match;
    private TextView date, desc;
    private boolean team1win;
    private String summonerId;

    public HistoriquePartie(Accueil main, Match match)
    {
        super(main);
        this.main = main;
        this.match = match;
        init();
    }

    public HistoriquePartie(Accueil main, Match match, String summonerId)
    {
        super(main);
        this.main = main;
        this.match = match;
        this.summonerId = summonerId;
        init();
    }

    private void init()
    {
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250));
        this.setBackgroundResource(R.drawable.none);
        this.setOnClickListener(this);

        LinearLayout layout = (LinearLayout) LayoutInflater.from(main).inflate(R.layout.histo,null);
        team1 = layout.findViewById(R.id.team1);
        team1.setOrientation(LinearLayout.VERTICAL);
        team2 = layout.findViewById(R.id.team2);
        date = layout.findViewById(R.id.date02);
        desc = layout.findViewById(R.id.date0);

        long l = Long.parseLong(match.data().gameDuration());
        date.setText(date(Long.parseLong(match.data().gameCreation()))+" - "+String.format("%02d",l/3600)+":"+String.format("%02d",l/60)+":"+String.format("%02d",l%60));

        String queue = match.queue();

        if(queue.equals("420"))desc.setText("5v5 Ranked Solo games");
        else if(queue.equals("400"))desc.setText("5v5 Draft Pick games");
        else if(queue.equals("430"))desc.setText("5v5 Blind Pick games");
        else if(queue.equals("440"))desc.setText("5v5 Ranked Flex games");
        else if(queue.equals("450"))desc.setText("5v5 ARAM games");
        else if(queue.equals("460"))desc.setText("3v3 Blind Pick games");
        else if(queue.equals("470"))desc.setText("3v3 Ranked Flex games");

        this.addView(layout);

        HashMap<String,Team> teams = match.data().teams();
        Iterator<String> it = teams.keySet().iterator();
        while(it.hasNext())
        {
            Team team = teams.get(it.next());
            if(team.teamId().equals("100"))
            {
                if(team.win().equals("Win")) team1win = true;
                else team1win = false;
            }
        }
        addPlayers();
    }

    private String date(long date)
    {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = new Date(date);
        return f.format(d);
    }

    private void addPlayers()
    {
        HashMap<String,Player> players = match.data().players();
        Iterator<String> it = players.keySet().iterator();
        while(it.hasNext())
        {
            String participantId = it.next();
            Participant participant = match.data().participant(participantId);
            Player player = players.get(participantId);

            String pseudo = player.summonerName();
            int kills = Integer.parseInt(participant.stats().kills());
            int deaths = Integer.parseInt(participant.stats().deaths());
            int assists = Integer.parseInt(participant.stats().assists());
            String champion = participant.championId();
            boolean green = (participant.teamId().equals("100")&&team1win)||(!participant.teamId().equals("100")&&!team1win);

            HistoriquePlayer histoplayer = new HistoriquePlayer(match.gameId(),main,champion,pseudo,kills,deaths,assists,green,player.summonerId(),summonerId);

            if(participant.teamId().equals("100"))team1.addView(histoplayer);
            else team2.addView(histoplayer);
        }
    }

    @Override
    public void onClick(View v)
    {
        main.setGame(match);
    }
}
