package chiya.lolmates;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import chiya.lolmates.requetes.Match;

public class Historique extends LinearLayout
{
    private Accueil main;
    private ArrayList<HistoriquePartie> parties;

    public Historique(Accueil main)
    {
        super(main);
        this.main = main;
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.setOrientation(VERTICAL);
        this.setGravity(Gravity.BOTTOM);

        parties = new ArrayList<>();
    }

    public void addGame(Match match)
    {
        HistoriquePartie partie = new HistoriquePartie(main,match);
        this.addView(partie);
    }

    public void addGame(Match match, String summonerId)
    {
        HistoriquePartie partie = new HistoriquePartie(main,match,summonerId);
        this.addView(partie);
    }
}
