package chiya.lolmates;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Type;

public class HistoriquePlayer extends FrameLayout
{
    private ImageView winorlose, champion;
    private TextView pseudo, score;

    public HistoriquePlayer(String gameId, Accueil main, String champion, String pseudo, int kills, int deaths, int assists ,boolean green, String summId, String summonerId)
    {
        super(main);

        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40));

        boolean honored = main.db().honors().honored(gameId,summId);

        if(honored)
        {
            this.setBackgroundColor(Color.parseColor("#2050FF50"));
        }

        String score = kills+"/"+deaths+"/"+assists;

        LinearLayout layout = (LinearLayout) LayoutInflater.from(main).inflate(R.layout.joueur,null);
        this.winorlose = layout.findViewById(R.id.winorlose);
        this.champion = layout.findViewById(R.id.champion);
        this.pseudo = layout.findViewById(R.id.pseudo);
        this.score = layout.findViewById(R.id.score);

        if(green)this.winorlose.setBackgroundResource(R.drawable.win);
        else this.winorlose.setBackgroundResource(R.drawable.lose);

        int id = main.getResources().getIdentifier("champ_"+champion,"drawable",main.getPackageName());

        if(id==0){System.out.println(pseudo +" <==> " +champion);}

        this.champion.setImageResource(id);
        this.pseudo.setText(pseudo);
        this.score.setText(score);

        if(main.summoner().id().equals(summId))
        {
            this.pseudo.setTextColor(Color.parseColor("#ffffbb33"));
            this.pseudo.setTypeface(this.pseudo.getTypeface(), Typeface.BOLD_ITALIC);

            this.score.setTextColor(Color.parseColor("#ffffbb33"));
            this.score.setTypeface(this.pseudo.getTypeface(), Typeface.BOLD_ITALIC);
        }
        else if(summonerId!=null&&summonerId.equals(summId))
        {
            this.pseudo.setTextColor(Color.parseColor("#ffbb77ff"));
            this.pseudo.setTypeface(this.pseudo.getTypeface(), Typeface.BOLD_ITALIC);

            this.score.setTextColor(Color.parseColor("#ffbb77ff"));
            this.score.setTypeface(this.pseudo.getTypeface(), Typeface.BOLD_ITALIC);
        }
        else
        {
            float ratio = (kills*1.0f + assists*1.0f)/(deaths*1.0f);
            if(ratio<1)
            {
                this.score.setTextColor(Color.parseColor("#FF9090"));
            }
            else if(ratio>4)
            {
                this.score.setTextColor(Color.parseColor("#90FF90"));
            }
        }

        this.addView(layout);
    }
}
