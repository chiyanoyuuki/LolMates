package chiya.lolmates;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;

import java.util.ArrayList;

import chiya.lolmates.requetes.Match;
import chiya.lolmates.requetes.PlayerStats;

public class LeaderBoard extends Fragment implements View.OnClickListener
{
    private LinearLayout players;
    private String clicked;
    private TextView rankl, namel, kdal, wll, honorl, scorel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.leaderboard, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        Accueil main = (Accueil)getActivity();

        clicked = "rank+";

        players = view.findViewById(R.id.playersleaderboard);
        rankl = view.findViewById(R.id.rankl);
        namel = view.findViewById(R.id.namel);
        kdal = view.findViewById(R.id.kdal);
        wll = view.findViewById(R.id.winsloses);
        honorl = view.findViewById(R.id.classhonors);
        scorel = view.findViewById(R.id.classscore);

        rankl.setOnClickListener(this);
        namel.setOnClickListener(this);
        kdal.setOnClickListener(this);
        wll.setOnClickListener(this);
        honorl.setOnClickListener(this);
        scorel.setOnClickListener(this);

        try{main.start3();}catch(JSONException e){e.printStackTrace();}
    }

    public void init() throws JSONException
    {
        Accueil main = (Accueil)getActivity();

        players.removeAllViews();

        Cursor cursor = main.db().stats().selectAll(clicked);
        int cpt = 0;

        while(cursor.moveToNext())
        {
            PlayerStats stats = new PlayerStats(cursor, false);

            LinearLayout layout = (LinearLayout) LayoutInflater.from(main).inflate(R.layout.playerleader,null);
            TextView rank = layout.findViewById(R.id.leaderrank);
            TextView name = layout.findViewById(R.id.leadername);
            TextView kda = layout.findViewById(R.id.leaderkda);
            TextView winlose = layout.findViewById(R.id.winsloses2);
            TextView honors = layout.findViewById(R.id.classhonors2);
            TextView score = layout.findViewById(R.id.classscore2);
            LinearLayout layoutl = layout.findViewById(R.id.layoutleader);

            if((cpt++)%2==0)layoutl.setBackgroundColor(Color.parseColor("#40202020"));

            if(stats.summonerId().equals(main.summoner().id()))
            {
                name.setBackgroundResource(R.drawable.testbtn22);
                name.setTextColor(Color.parseColor("#ffffbb33"));
                rank.setTextColor(Color.parseColor("#ffffbb33"));
                kda.setTextColor(Color.parseColor("#ffffbb33"));
                winlose.setTextColor(Color.parseColor("#ffffbb33"));
                honors.setTextColor(Color.parseColor("#ffffbb33"));
                score.setTextColor(Color.parseColor("#ffffbb33"));
            }
            else
            {
                name.setTag("pseudo:"+stats.summonerName());
                name.setOnClickListener(this);
            }

            double wins = stats.wins();
            double loses = stats.loses();
            double kills = stats.kills();
            double deaths = stats.deaths();
            double assists = stats.assists();
            double honor = stats.honors();

            rank.setText(stats.POS());
            name.setText(stats.summonerName());
            kda.setText(String.format("%.1f",((kills+assists)/(deaths>0?deaths:1))));
            winlose.setText(String.format("%.0f",wins)+"/"+String.format("%.0f",loses));
            honors.setText(String.format("%.0f",stats.honors()));
            score.setText(String.format("%.2f",stats.score()));

            if((kills+assists)/deaths>=6)kda.setTextColor(Color.parseColor("#5AFF5A"));
            if((wins/(wins+loses))>=0.75&&wins+loses>3)winlose.setTextColor(Color.parseColor("#5AFF5A"));
            if((honor/(wins+loses))>=0.5&&wins+loses>3)honors.setTextColor(Color.parseColor("#5AFF5A"));

            players.addView(layout);
        }
        cursor.close();
    }

    @Override
    public void onClick(View view)
    {
        Accueil main = (Accueil)getActivity();

        if(view==rankl||view==namel||view==kdal||view==wll||view==honorl||view==scorel)
        {
            int col = Color.parseColor("#ffffff");
            rankl.setTextColor(col);
            namel.setTextColor(col);
            kdal.setTextColor(col);
            wll.setTextColor(col);
            honorl.setTextColor(col);
            scorel.setTextColor(col);

            if(view==rankl)
            {
                rankl.setTextColor(Color.parseColor("#ffffbb33"));
                if(clicked.startsWith("rank"))
                {
                    if(clicked.endsWith("+")) clicked = "rank-";
                    else clicked = "rank+";
                }
                else clicked = "rank+";
            }
            else if(view==namel)
            {
                namel.setTextColor(Color.parseColor("#ffffbb33"));
                if(clicked.startsWith("name"))
                {
                    if(clicked.endsWith("+")) clicked = "name-";
                    else clicked = "name+";
                }
                else clicked = "name+";
            }
            else if(view==kdal)
            {
                kdal.setTextColor(Color.parseColor("#ffffbb33"));
                if(clicked.startsWith("kda"))
                {
                    if(clicked.endsWith("+")) clicked = "kda-";
                    else clicked = "kda+";
                }
                else clicked = "kda-";
            }
            else if(view==wll)
            {
                wll.setTextColor(Color.parseColor("#ffffbb33"));
                if(clicked.startsWith("winlose"))
                {
                    if(clicked.endsWith("+")) clicked = "winlose-";
                    else clicked = "winlose+";
                }
                else clicked = "winlose-";
            }
            else if(view==honorl)
            {
                honorl.setTextColor(Color.parseColor("#ffffbb33"));
                if(clicked.startsWith("honor"))
                {
                    if(clicked.endsWith("+")) clicked = "honor-";
                    else clicked = "honor+";
                }
                else clicked = "honor-";
            }
            else if(view==scorel)
            {
                scorel.setTextColor(Color.parseColor("#ffffbb33"));
                if(clicked.startsWith("score"))
                {
                    if(clicked.endsWith("+")) clicked = "score-";
                    else clicked = "score+";
                }
                else clicked = "score-";
            }

            try{init();}catch(JSONException e){e.printStackTrace();}
        }
        else if(view.getTag().toString().startsWith("pseudo:"))
        {
            String pseudo = view.getTag().toString();
            pseudo = pseudo.substring(pseudo.indexOf(":")+1);
            main.findPlayer(pseudo);
        }
    }
}
