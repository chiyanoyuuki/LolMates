package chiya.lolmates;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;

import java.util.ArrayList;

import chiya.lolmates.requetes.Match;
import chiya.lolmates.requetes.PlayerStats;

public class FindPlayer extends Fragment implements View.OnClickListener
{
    private AlphaAnimation animation;
    private TextView buttonsearch, message, search, pseudo, score, win, lose, honor, scoreclass, classement;
    private FrameLayout frame;
    private ArrayList<Match> matchs;
    private String player, summonerId, summonerName;
    private ImageView champion;

    private Historique histo;
    private FrameLayout historique;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.search, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        Accueil main = (Accueil)getActivity();
        histo = new Historique(main);

        message     = view.findViewById(R.id.message2);
        historique  = view.findViewById(R.id.historique2);
        buttonsearch = view.findViewById(R.id.buttonsearch);
        champion    = view.findViewById(R.id.schampion);
        search      = view.findViewById(R.id.search);
        frame       = view.findViewById(R.id.framelay);
        pseudo      = view.findViewById(R.id.spseudo);
        score       = view.findViewById(R.id.sscore);
        win         = view.findViewById(R.id.swin);
        lose        = view.findViewById(R.id.slose);
        honor       = view.findViewById(R.id.shonor);
        scoreclass  = view.findViewById(R.id.sscore2);
        classement  = view.findViewById(R.id.sclassement);

        historique.addView(histo);
        buttonsearch.setOnClickListener(this);
        frame.setOnClickListener(this);

        try{main.start2();}catch(JSONException e){e.printStackTrace();}
    }

    public void initHisto(String pseudo) throws JSONException
    {
        Accueil main = (Accueil)getActivity();
        histo.removeAllViews();
        matchs = new ArrayList<>();

        summonerId = main.db().parties().selectPlayer(pseudo);

        Cursor cursor = main.db().parties().selectFromPlayer(summonerId);
        while(cursor.moveToNext())
        {
            initTop();
            Match match = new Match(cursor);
            match.chargeData(main.db());
            matchs.add(match);
            histo.addGame(match,summonerId);
        }
        cursor.close();
        checkHisto();
    }

    private void initTop() throws JSONException
    {
        Accueil main = (Accueil)getActivity();

        PlayerStats stats = main.db().stats().selectPlayer(summonerId);

        champion.setImageResource(main.getResources().getIdentifier("champ_"+stats.champion(),"drawable",main.getPackageName()));
        pseudo.setText(stats.summonerName());
        double wins = stats.wins();
        double loses = stats.loses();
        double games = wins+loses;
        double kills = stats.kills();
        double deaths = stats.deaths();
        double assists = stats.assists();
        score.setText(String.format("%.1f",kills/games)+"/"+String.format("%.1f",deaths/games)+"/"+String.format("%.1f",assists/games));
        win.setText(String.format("%.0f",wins)+"W");
        lose.setText(String.format("%.0f",loses)+"L");
        honor.setText(String.format("%.0f",stats.honors())+" Honors");
        scoreclass.setText(String.format("%.2f",stats.score())+"/100");
        classement.setText(stats.POS()+"/"+stats.NB());
    }

    public void initHisto2(String summonerName) throws JSONException
    {
        Accueil main = (Accueil)getActivity();
        histo.removeAllViews();
        matchs = new ArrayList<>();

        search.setText(summonerName);

        summonerId = main.db().parties().selectPlayer(summonerName);
        player = summonerName;

        Cursor cursor = main.db().parties().selectFromPlayer(summonerId);
        while(cursor.moveToNext())
        {
            initTop();
            Match match = new Match(cursor);
            match.chargeData(main.db());
            matchs.add(match);
            histo.addGame(match,summonerId);
        }
        cursor.close();
        checkHisto();
    }

    public void checkHisto()
    {
        message.setVisibility(View.INVISIBLE);
        if(matchs.size()==0)
        {
            message.setText("We have not found any games with the player "+player+"...");
            message.setVisibility(View.VISIBLE);
        }
    }

    public void honor() throws JSONException
    {
        Accueil main = (Accueil)getActivity();
        histo.removeAllViews();
        matchs = new ArrayList<>();

        Cursor cursor = main.db().parties().selectFromPlayer(summonerId);
        while(cursor.moveToNext())
        {
            initTop();
            Match match = new Match(cursor);
            match.chargeData(main.db());
            matchs.add(match);
            histo.addGame(match,summonerId);
        }
        cursor.close();
        checkHisto();
    }

    @Override
    public void onClick(View view)
    {
        Accueil main = (Accueil)getActivity();

        if(view!=search)
        {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if(view==buttonsearch)
        {
            player = ""+search.getText();
            try{initHisto(""+search.getText());}catch(JSONException e){e.printStackTrace();}
        }
    }
}
