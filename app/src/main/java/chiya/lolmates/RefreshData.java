package chiya.lolmates;

import android.database.Cursor;
import android.os.*;
import android.view.*;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import chiya.lolmates.requetes.Match;

public class RefreshData extends Fragment implements View.OnClickListener
{
    private AlphaAnimation animation;
    private TextView normalgames, soloq, flex, message;
    private String games;
    private ArrayList<Match> matchs;

    private Historique histo;
    private FrameLayout historique;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.refreshdata, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        Accueil main = (Accueil)getActivity();
        histo = new Historique(main);
        games = "420";

        normalgames = view.findViewById(R.id.normalgames);
        soloq       = view.findViewById(R.id.soloqgames);
        flex        = view.findViewById(R.id.flexgames);
        message     = view.findViewById(R.id.message);
        historique  = view.findViewById(R.id.historique);

        normalgames.setOnClickListener(this);
        soloq.setOnClickListener(this);
        flex.setOnClickListener(this);

        historique.addView(histo);

        main.start();
    }

    public void initHisto() throws JSONException
    {
        Accueil main = (Accueil)getActivity();
        histo.removeAllViews();
        matchs = new ArrayList<>();
        Cursor cursor = null;
        cursor = main.db().parties().selectLast(games);
        while(cursor.moveToNext())
        {
            Match match = new Match(cursor);
            match.chargeData(main.db());
            matchs.add(match);
            histo.addGame(match);
        }
        cursor.close();
        checkHisto();
    }

    public void checkHisto()
    {
        message.setVisibility(View.INVISIBLE);
        if(matchs.size()==0)
        {
            message.setText("No game charged yet, refresh games for checking the 8 last games played..");
            message.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view)
    {
        Accueil main = (Accueil)getActivity();
        try
        {
            if(view==soloq&&!games.equals("420"))
            {
                soloq.setBackgroundResource(R.drawable.none);
                flex.setBackgroundResource(R.drawable.black);
                normalgames.setBackgroundResource(R.drawable.black);
                games="420";
                main.setGames(games);
                initHisto();
                main.refreshElos();
            }
            else if(view==normalgames&&!games.equals("400")&&!games.equals("430")&&!games.equals("450"))
            {
                soloq.setBackgroundResource(R.drawable.black);
                flex.setBackgroundResource(R.drawable.black);
                normalgames.setBackgroundResource(R.drawable.none);
                games="400,430,450";
                main.setGames(games);
                initHisto();
                main.refreshElos();
            }
            else if(view==flex&&!games.equals("440"))
            {
                soloq.setBackgroundResource(R.drawable.black);
                flex.setBackgroundResource(R.drawable.none);
                normalgames.setBackgroundResource(R.drawable.black);
                games="440";
                main.setGames(games);
                initHisto();
                main.refreshElos();
            }
        }
        catch(JSONException e){e.printStackTrace();}
    }
}
