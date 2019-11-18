package chiya.lolmates;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import chiya.lolmates.requetes.Live;
import chiya.lolmates.requetes.LiveParticipant;
import chiya.lolmates.requetes.Match;
import chiya.lolmates.requetes.Participant;
import chiya.lolmates.requetes.PartieData;
import chiya.lolmates.requetes.Player;
import chiya.lolmates.requetes.Stats;
import chiya.lolmates.requetes.Team;

public class Game extends Fragment implements View.OnClickListener
{
    private String name;
    private Match match;
    private LinearLayout team1, team2, bans1, bans2;
    private TextView mode, desc, date;
    private ImageView map, retour;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.game, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        team1  = view.findViewById(R.id.liveteam12);
        team2  = view.findViewById(R.id.liveteam22);
        map    = view.findViewById(R.id.livemap2);
        mode   = view.findViewById(R.id.livemode2);
        desc   = view.findViewById(R.id.livedesc2);
        bans1  = view.findViewById(R.id.livebans12);
        bans2  = view.findViewById(R.id.livebans22);
        retour = view.findViewById(R.id.retourgame);
        date   = view.findViewById(R.id.gamedate2);

        retour.setOnClickListener(this);

        Accueil main = (Accueil) getActivity();
        match = main.getGame();
        try{init();}catch(JSONException e){e.printStackTrace();}
    }

    private void initTop()
    {
        Accueil main = (Accueil)getActivity();

        ArrayList<String> bans = match.bans1();
        for(String champ:bans)
        {
            ImageView image = new ImageView(main);
            image.setImageResource(main.getResources().getIdentifier("champ_"+champ,"drawable",main.getPackageName()));
            image.setLayoutParams(new LinearLayout.LayoutParams(60,60));
            bans1.addView(image);
        }

        bans = match.bans2();
        for(String champ:bans)
        {
            ImageView image = new ImageView(main);
            image.setImageResource(main.getResources().getIdentifier("champ_"+champ,"drawable",main.getPackageName()));
            image.setLayoutParams(new LinearLayout.LayoutParams(60,60));
            bans2.addView(image);
        }

        map.setImageResource(main.getResources().getIdentifier("map_"+match.data().mapId(),"drawable",main.getPackageName()));
        mode.setText(match.data().gameMode());

        String queue = match.data().queueId();
        if(queue.equals("420"))desc.setText("5v5 Ranked Solo games");
        else if(queue.equals("400"))desc.setText("5v5 Draft Pick games");
        else if(queue.equals("430"))desc.setText("5v5 Blind Pick games");
        else if(queue.equals("440"))desc.setText("5v5 Ranked Flex games");
        else if(queue.equals("450"))desc.setText("5v5 ARAM games");
        else if(queue.equals("460"))desc.setText("3v3 Blind Pick games");
        else if(queue.equals("470"))desc.setText("3v3 Ranked Flex games");

        Team team = match.data().team("100");
        boolean win = team.win().equals("Win");
        if(win)
        {
            team1.setBackgroundColor(Color.parseColor("#0D32FF32"));
            team2.setBackgroundColor(Color.parseColor("#0DFF3232"));
        }
        else
        {
            team2.setBackgroundColor(Color.parseColor("#0D32FF32"));
            team1.setBackgroundColor(Color.parseColor("#0DFF3232"));
        }

        long l = Long.parseLong(match.data().gameDuration());
        date.setText(date(Long.parseLong(match.data().gameCreation()))+" - "+String.format("%02d",l/3600)+":"+String.format("%02d",l/60)+":"+String.format("%02d",l%60));
    }

    private String date(long date)
    {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = new Date(date);
        return f.format(d);
    }

    public void init() throws JSONException
    {
        Accueil main = (Accueil)getActivity();
        initTop();

        ArrayList<String> honors = new ArrayList<>();
        Cursor cursor = main.db().honors().getGame(match.gameId());
        while(cursor.moveToNext())
        {
            honors.add(cursor.getString(1));
        }
        cursor.close();

        HashMap<String, Participant> participants = match.data().participants();
        HashMap<String, Player> players = match.data().players();

        Iterator<String> it = participants.keySet().iterator();

        while(it.hasNext())
        {
            String key = it.next();
            Participant participant = participants.get(key);
            Player player = players.get(key);

            int team = Integer.parseInt(participant.teamId());
            LinearLayout layout = (LinearLayout) LayoutInflater.from(main).inflate(R.layout.gameplayer,null);
            TextView pseudo     = layout.findViewById(R.id.livepseudo2);
            ImageView champion  = layout.findViewById(R.id.livechamp2);
            ImageView summ1     = layout.findViewById(R.id.summ1);
            ImageView summ2     = layout.findViewById(R.id.summ2);
            ImageView item1     = layout.findViewById(R.id.item1);
            ImageView item2     = layout.findViewById(R.id.item2);
            ImageView item3     = layout.findViewById(R.id.item3);
            ImageView item4     = layout.findViewById(R.id.item4);
            ImageView item5     = layout.findViewById(R.id.item5);
            ImageView item6     = layout.findViewById(R.id.item6);
            TextView score      = layout.findViewById(R.id.livescore2);
            TextView honor      = layout.findViewById(R.id.honorable);

            pseudo.setTag("pseudo:"+player.summonerName());
            pseudo.setOnClickListener(this);

            Cursor cursor2 = main.db().historiqueParties().findPlayer(player.summonerId());
            int cpt = 0;
            while(cursor2.moveToNext())
            {
                PartieData data = new PartieData(cursor2,player.summonerId());
                cpt++;
            }
            cursor2.close();

            if(honors.contains(player.summonerId()))
            {
                honor.setText("Honored");
                honor.setTextColor(Color.parseColor("#80FF80"));
            }
            honor.setTag(new String[]{match.gameId(),player.summonerId()});

            layout.setPadding(20,20,20,0);
            champion.setImageResource(main.getResources().getIdentifier("champ_"+participant.championId(),"drawable",main.getPackageName()));

            score.setText(participant.stats().kills()+"/"+participant.stats().deaths()+"/"+participant.stats().assists());
            summ1.setImageResource(main.getResources().getIdentifier("spell_"+participant.spell1Id(),"drawable",main.getPackageName()));
            summ2.setImageResource(main.getResources().getIdentifier("spell_"+participant.spell2Id(),"drawable",main.getPackageName()));
            if(player.summonerId().equals(main.summoner().id()))
            {
                pseudo.setText(player.summonerName());
                honor.setText("-");
                pseudo.setTextColor(Color.parseColor("#bbbb00"));
                score.setTextColor(Color.parseColor("#bbbb00"));
            }
            else
            {
                pseudo.setText(player.summonerName()+" ("+cpt+")");
                honor.setOnClickListener(this);
                int kills = Integer.parseInt(participant.stats().kills());
                int deaths = Integer.parseInt(participant.stats().deaths());
                int assists = Integer.parseInt(participant.stats().assists());
                float ratio = (kills*1.0f + assists*1.0f)/(deaths*1.0f);
                if(ratio<1)
                {
                    score.setTextColor(Color.parseColor("#FF9090"));
                }
                else if(ratio>4)
                {
                    score.setTextColor(Color.parseColor("#90FF90"));
                }
            }
            item(participant.stats().item0(),item1);
            item(participant.stats().item1(),item2);
            item(participant.stats().item2(),item3);
            item(participant.stats().item3(),item4);
            item(participant.stats().item4(),item5);
            item(participant.stats().item5(),item6);

            if(team==100)   team1.addView(layout);
            else            team2.addView(layout);
        }
    }

    private void item(String item, ImageView img)
    {
        Accueil main = (Accueil)getActivity();
        img.setImageResource(main.getResources().getIdentifier("item_"+item,"drawable",main.getPackageName()));
    }

    @Override
    public void onClick(View view)
    {
        Accueil main = (Accueil)getActivity();
        if(view==retour)
        {
            main.stopMatch();
        }
        else if(view.getTag().toString().startsWith("pseudo:"))
        {
            String pseudo = view.getTag().toString();
            pseudo = pseudo.substring(pseudo.indexOf(":")+1);
            main.findPlayer(pseudo);
        }
        else
        {
            String[] tmp = (String[])view.getTag();
            TextView honor = (TextView)view;
            boolean state = honor.getText().equals("Honor");

            if(state)
            {
                main.db().honors().create(tmp[0],tmp[1]);
                honor.setText("Honored");
                honor.setTextColor(Color.parseColor("#80FF80"));
                main.honor();
            }
            else
            {
                main.db().honors().delete(tmp[0], tmp[1]);
                honor.setText("Honor");
                honor.setTextColor(Color.parseColor("#FFFFFF"));
                main.honor();
            }
        }
    }
}