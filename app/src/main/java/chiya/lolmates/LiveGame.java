package chiya.lolmates;

import android.database.Cursor;
import android.graphics.Color;
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

import chiya.lolmates.requetes.Live;
import chiya.lolmates.requetes.LiveParticipant;
import chiya.lolmates.requetes.PartieData;
import chiya.lolmates.requetes.PlayerStats;
import chiya.lolmates.requetes.Stats;

public class LiveGame  extends Fragment implements View.OnClickListener
{
    private final long timingLive = 10000;
    private boolean canLive;

    private long startTime2;
    private View view;
    private Live live;
    private LinearLayout team1, team2, bans1, bans2;
    private TextView mode, desc, charge, avert, wins, scoreclass, date;
    private ImageView map;

    private Handler handler2 = new Handler();
    private Runnable runnable2 = new Runnable() {

        @Override
        public void run() {
            long millis = timingLive-(System.currentTimeMillis() - startTime2);
            if(millis<0)
            {
                charge.setTextColor(Color.parseColor("#ffffff"));
                charge.setText("Charge active game");
                canLive = true;
                handler2.removeCallbacks(runnable2);
            }
            else
            {
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                minutes = minutes % 60;
                charge.setText("Charge : " + String.format("%02d:%02d", minutes, seconds));
            }
            handler2.postDelayed(this, 1000);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.livegame, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        team1  = view.findViewById(R.id.liveteam1);
        team2  = view.findViewById(R.id.liveteam2);
        map    = view.findViewById(R.id.livemap);
        mode   = view.findViewById(R.id.livemode);
        desc   = view.findViewById(R.id.livedesc);
        bans1  = view.findViewById(R.id.livebans1);
        bans2  = view.findViewById(R.id.livebans2);
        charge = view.findViewById(R.id.chargeGame);
        avert  = view.findViewById(R.id.avert);
        date   = view.findViewById(R.id.livedate);

        canLive = true;

        charge.setOnClickListener(this);

        Accueil main = (Accueil) getActivity();
        Cursor cursor = main.getLive();
        if(cursor.moveToNext())
        {
            try{live = new Live(cursor);init();}catch(JSONException e){e.printStackTrace();}
        }
        else
        {
            avert.setVisibility(View.VISIBLE);
        }
    }

    private void initTop()
    {
        Accueil main = (Accueil)getActivity();

        ArrayList<String> bans = live.bans1();
        for(String champ:bans)
        {
            ImageView image = new ImageView(main);
            image.setImageResource(main.getResources().getIdentifier("champ_"+champ,"drawable",main.getPackageName()));
            image.setLayoutParams(new LinearLayout.LayoutParams(60,60));
            bans1.addView(image);
        }

        bans = live.bans2();
        for(String champ:bans)
        {
            ImageView image = new ImageView(main);
            image.setImageResource(main.getResources().getIdentifier("champ_"+champ,"drawable",main.getPackageName()));
            image.setLayoutParams(new LinearLayout.LayoutParams(60,60));
            bans2.addView(image);
        }

        map.setImageResource(main.getResources().getIdentifier("map_"+live.mapId(),"drawable",main.getPackageName()));
        mode.setText(live.gameMode());

        String queue = live.gameQueueConfigId();
        if(queue.equals("420"))desc.setText("5v5 Ranked Solo games");
        else if(queue.equals("400"))desc.setText("5v5 Draft Pick games");
        else if(queue.equals("430"))desc.setText("5v5 Blind Pick games");
        else if(queue.equals("440"))desc.setText("5v5 Ranked Flex games");
        else if(queue.equals("450"))desc.setText("5v5 ARAM games");
        else if(queue.equals("460"))desc.setText("3v3 Blind Pick games");
        else if(queue.equals("470"))desc.setText("3v3 Ranked Flex games");
    }

    public void init() throws JSONException
    {
        avert.setVisibility(View.INVISIBLE);
        bans1.removeAllViews();
        bans2.removeAllViews();
        team1.removeAllViews();
        team2.removeAllViews();
        Accueil main = (Accueil) getActivity();
        Cursor cursor = main.db().live().get();
        cursor.moveToNext();
        this.live = new Live(cursor);
        initTop();

        System.out.println(live.date());
        date.setText(live.date());

        ArrayList<LiveParticipant> players = live.getPlayers();
        for(LiveParticipant player : players)
        {
            int team = Integer.parseInt(player.teamId());
            LinearLayout layout = (LinearLayout) LayoutInflater.from(main).inflate(R.layout.liveplayer,null);
            TextView pseudo     = layout.findViewById(R.id.livepseudo);
            ImageView champion  = layout.findViewById(R.id.livechamp);
            TextView together   = layout.findViewById(R.id.livetogether);
            TextView score      = layout.findViewById(R.id.livescore);
            wins                = layout.findViewById(R.id.lwin);
            scoreclass          = layout.findViewById(R.id.lscore);
            String summonerId   = player.summonerId();

            /*================*/

            PlayerStats stats = main.db().stats().selectPlayer(summonerId);
            double games = 0;
            if(stats!=null)
            {
                double wins = stats.wins();
                double loses = stats.loses();
                games = wins+loses;
                double kills = stats.kills();
                double deaths = stats.deaths();
                double assists = stats.assists();

                score.setText(String.format("%.1f",kills/games)+"/"+String.format("%.1f",deaths/games)+"/"+String.format("%.1f",assists/games));
                this.wins.setText(String.format("%.0f",wins)+"W");
                together.setText(String.format("%.0f",loses)+"L");
                scoreclass.setText(String.format("%.2f",stats.score())+"/100");
            }


            /*================*/

            layout.setPadding(20,20,20,0);
            champion.setImageResource(main.getResources().getIdentifier("champ_"+player.championId(),"drawable",main.getPackageName()));


            if(player.summonerId().equals(main.summoner().id()))
            {
                pseudo.setTextColor(Color.parseColor("#ffffbb33"));
                pseudo.setText(player.summonerName());
                pseudo.setBackgroundResource(R.drawable.testbtn22);
            }
            else
            {
                pseudo.setText(player.summonerName() + " ("+String.format("%.0f",games)+")");
                if(games>0)
                {
                    pseudo.setTag("pseudo:"+player.summonerName());
                    pseudo.setOnClickListener(this);
                }
                else
                {
                    pseudo.setBackgroundResource(R.drawable.testbtn22);
                    pseudo.setTextColor(Color.parseColor("#505050"));
                }
            }

            if(team==100)   team1.addView(layout);
            else            team2.addView(layout);
        }
    }

    @Override
    public void onClick(View view)
    {
        Accueil main = (Accueil)getActivity();
        if(view==charge)
        {
            if(canLive)
            {
                canLive = false;
                charge.setTextColor(Color.parseColor("#ff5050"));
                startTime2 = System.currentTimeMillis();
                handler2.postDelayed(runnable2, 0);
                main.liveGame();
            }
        }
        else if(view.getTag().toString().startsWith("pseudo:"))
        {
            String pseudo = view.getTag().toString();
            pseudo = pseudo.substring(pseudo.indexOf(":")+1);
            main.findPlayer(pseudo);
        }
    }
}