package chiya.lolmates;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import chiya.lolmates.requetes.*;

public class Accueil extends FragmentActivity implements View.OnClickListener
{
    private final long timingRefresh = 30000;
    private boolean canRefresh = true, canLive = true;
    private long startTime;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {

    @Override
    public void run() {
        long millis = timingRefresh-(System.currentTimeMillis() - startTime);
        if(millis<0)
        {
            refresh.setTextColor(Color.parseColor("#ffffff"));
            refresh.setText("Refresh Data");
            canRefresh = true;
            handler.removeCallbacks(runnable);
        }
        else
        {
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            minutes = minutes % 60;
            refresh.setText("Refresh : " + String.format("%02d:%02d", minutes, seconds));
        }
        handler.postDelayed(this, 1000);
    }
};

    private String ecran, lastecran, summonerId;

    private API api;
    private String result;
    private BDD db;

    private int nbmatch;
    private ArrayList<Match> matchs;
    private Fragment fragment, fragment2;

    private AlphaAnimation animation;
    private TextView refresh, lp, wins, loses, livegame, history, show, search, leaderboard, settings;
    private ImageView rankedimg0, rankedimg1, profilicon;
    private LinearLayout layout;
    private TextView nom, acclvl;
    private String games;
    private FrameLayout loading, containfrag;
    private Match match;

    private Summoner summoner;
    private Ranked rksolo, rkflex;

    private InterstitialAd ad;
    private BillingClient mBillingClient;

    private boolean verifpay = false;
    private boolean payed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        ad = new InterstitialAd(this);
        ad.setAdUnitId("ca-app-pub-9108603776020618/9983236388");
        ad.loadAd(new AdRequest.Builder().build());
        ad.setAdListener(new AdListener() {
        @Override
        public void onAdClosed() {
            ad.loadAd(new AdRequest.Builder().build());
        }
    });


        layout = findViewById(R.id.layout);
        nom = findViewById(R.id.pseudo);
        refresh = findViewById(R.id.refreshgames);
        rankedimg0 = findViewById(R.id.rankedimg0);
        rankedimg1 = findViewById(R.id.rankedimg1);
        lp = findViewById(R.id.lp);
        wins = findViewById(R.id.wins);
        loses = findViewById(R.id.loses);
        livegame = findViewById(R.id.LiveGame);
        history = findViewById(R.id.matchhistory);
        show = findViewById(R.id.show2);
        profilicon = findViewById(R.id.profilicon);
        acclvl = findViewById(R.id.acclvl);
        loading = findViewById(R.id.loading);
        containfrag = findViewById(R.id.containfragment2);
        search = findViewById(R.id.findplayer);
        leaderboard = findViewById(R.id.leaderboard);
        settings = findViewById(R.id.settings);

        loading.setVisibility(View.INVISIBLE);

        search.setOnClickListener(this);
        refresh.setOnClickListener(this);
        livegame.setOnClickListener(this);
        history.setOnClickListener(this);
        leaderboard.setOnClickListener(this);
        settings.setOnClickListener(this);

        initBill();
        reinitialize();
    }

    private void initBill()
    {
        mBillingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult result, @Nullable List<Purchase> purchases) {
                if (result.getResponseCode() == BillingClient.BillingResponseCode.OK
                        && purchases != null) {

                    System.out.println("==============> ET LE PREMIER TEST");


                } else if (result.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                } else {
                }
            }
        }).build();
    }

    public void payed(){payed = true;}

    private void verifBill()
    {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult result) {

                Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
                List<Purchase> pu = purchasesResult.getPurchasesList();
                for(Purchase p:pu)
                {
                    if(p.getSku().equals("remove_ads.lolmates"))payed = true;
                }
                verifpay = true;
                showAd();
            }


            @Override
            public void onBillingServiceDisconnected() {}
        });
    }

    public void reinitialize()
    {
        fragment = new RefreshData();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.containfragment, fragment).commit();

        db = new BDD(this);
        api = new API(this);
        setShow();

        games = "420";
        ecran = "history";
        reinit();
        history.setTextColor(Color.parseColor("#404040"));
    }

    public void start()
    {
        try{initAcc();}catch(JSONException e){}
    }

    public void start2() throws JSONException
    {
        FindPlayer f = (FindPlayer)fragment;
        if(summonerId!=null) f.initHisto2(summonerId);
    }

    public void start3() throws JSONException
    {
        LeaderBoard l = (LeaderBoard)fragment;
        l.init();
    }

    private void initAcc() throws JSONException
    {
        Cursor cursor = db.summoner().select();
        if(cursor.moveToNext())
        {
            summoner = new Summoner(cursor);
            Cursor cursorsolo = db.ranked().select("RANKED_SOLO_5x5");
            if(cursorsolo.moveToNext())rksolo = new Ranked(cursorsolo);
            cursorsolo.close();
            Cursor cursorflex = db.ranked().select("RANKED_TEAM_5x5");
            if(cursorflex.moveToNext())rkflex = new Ranked(cursorflex);
            cursorflex.close();
            nom.setText(summoner.name());
            acclvl.setText(summoner.summonerLevel());
            profilicon.setBackgroundResource(getResources().getIdentifier("icone_"+summoner.profileIconId(),"drawable",getPackageName()));
            refreshElos();

            RefreshData tmp = (RefreshData)fragment;
            tmp.initHisto();
        }
        else
        {
            askSumm();
        }
        cursor.close();
    }

    public void askSumm()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.summ,null);
        builder.setView(view);

        final EditText input = view.findViewById(R.id.summedit);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        final Spinner spinner = view.findViewById(R.id.summspinn);

        builder.setPositiveButton("Validate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){tryname(input.getText().toString(),spinner.getSelectedItem().toString());}
        });
        /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){}
        });*/

        final AlertDialog dialog = builder.create();
        input.setBackgroundColor(Color.parseColor("#ffaaaa"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {}
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            @Override
            public void afterTextChanged(Editable Token)
            {
                int l = Token.toString().length();
                if(l<3||l>16)
                {
                    input.setBackgroundColor(Color.parseColor("#ffaaaa"));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
                else
                {
                    input.setBackgroundColor(Color.parseColor("#dddddd"));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
    }

    private String region(String region)
    {
        if(region.equals("BR"))region = "br1";
        else if(region.equals("EUN"))region = "eun1";
        else if(region.equals("EUW"))region = "euw1";
        else if(region.equals("JP"))region = "jp1";
        else if(region.equals("KR"))region = "kr";
        else if(region.equals("LAN"))region = "la1";
        else if(region.equals("LAS"))region = "la2";
        else if(region.equals("NA"))region = "na1";
        else if(region.equals("OC"))region = "oc1";
        else if(region.equals("TR"))region = "tr1";
        else if(region.equals("RU"))region = "ru";
        return region;
    }

    public void tryname(String summ, String region)
    {
        api.firstsummonerByName(summ,region(region));
    }

    public void result(HashMap<String,String> result, String type) throws JSONException
    {
        //System.out.println("[REQUETE]==> "+type);

        if(type.startsWith("summoner")||type.startsWith("firstsumm"))
        {
            summoner = new Summoner(result,db,type);
            api.region(type.substring(type.indexOf(":")+1));
            nom.setText(summoner.name());
            acclvl.setText(summoner.summonerLevel());
            profilicon.setBackgroundResource(getResources().getIdentifier("icone_"+summoner.profileIconId(),"drawable",getPackageName()));
            api.rankedStatsBySummonerId(summoner.id());
            RefreshData tmp = (RefreshData)fragment;
            tmp.initHisto();
        }
        else if(type.equals("matches"))
        {
            if(result.containsKey("matches"))
            {
                nbmatch = 0;
                matchs = new ArrayList<>();
                JSONArray arr = new JSONArray(result.get("matches"));
                int cpt = 0;
                for(int i=0;i<arr.length();i++)
                {
                    HashMap<String,String> results = new HashMap<>();
                    JSONObject obj = arr.getJSONObject(i);
                    Iterator<String> it = obj.keys();
                    while(it.hasNext())
                    {
                        String key = it.next();
                        results.put(key,obj.getString(key));
                    }
                    Match match = new Match(results);

                    boolean exists = db.parties().exists(match.gameId());

                    if(!exists)
                    {
                        matchs.add(match);
                        api.partieData(match.gameId(),cpt++);
                    }
                }
                if(matchs.size()==0)
                {
                    show(0);
                }
            }
        }
        else if(type.startsWith("partie_"))
        {
            if(nbmatch==0)showAd();
            nbmatch++;
            int i = Integer.parseInt(type.substring(type.indexOf("_")+1));
            Match match = matchs.get(i);
            match.setData(result,db);
            if(nbmatch==matchs.size())
            {
                show(nbmatch);
                if(ecran.equals("history"))
                {
                    RefreshData tmp = (RefreshData)fragment;
                    tmp.initHisto();
                }
            }
        }
        else if(type.equals("livegame"))
        {
            showAd();
            Live live = new Live(result);
            db.live().create(live);
            LiveGame tmp = (LiveGame)fragment;
            tmp.init();
            loading.setVisibility(View.INVISIBLE);
        }
    }

    private void showAd()
    {
        if (ad.isLoaded()) {
            if(!verifpay)verifBill();
            else if(!payed)ad.show();
        }
        else
        {
            error("Ad Loading Error");
        }
    }

    public void noLive()
    {
        show.setAnimation(animation);
        show.setText("Not currently in a game..");
        loading.setVisibility(View.INVISIBLE);
        animation.start();
    }

    public void result(ArrayList<HashMap<String,String>> result, String type) throws JSONException
    {
        if(type.equals("ranked"))
        {
            for(int i=0;i<result.size();i++)
            {
                HashMap<String,String> tmp = result.get(i);
                Ranked rk = new Ranked(tmp);
                if(rk.queueType().equals("RANKED_SOLO_5x5"))
                {
                    if(rksolo==null)db.ranked().create(rk);
                    else db.ranked().update(rk);
                    rksolo = rk;
                }
                else if(rk.queueType().equals("RANKED_TEAM_5x5"))
                {
                    if(rkflex==null)db.ranked().create(rk);
                    else db.ranked().update(rk);
                    rkflex = rk;
                }
            }
            refreshElos();
        }
    }

    public void refreshElos()
    {
        if(games.equals("420"))
        {
            if(rksolo==null){noelo();}
            else
            {
                rankedimg0.setImageResource(getResources().getIdentifier("elo_"+rksolo.tier().toLowerCase(),"drawable",getPackageName()));
                rankedimg1.setImageResource(getResources().getIdentifier("elo_"+rksolo.tier().toLowerCase(),"drawable",getPackageName()));
                rankedimg1.clearColorFilter();
                lp.setText(rksolo.rank()+" - "+rksolo.leaguePoints()+"LP");
                wins.setText(rksolo.wins()+"W");
                loses.setText(rksolo.losses()+"L");
            }
        }
        else if(games.equals("440"))
        {
            if(rkflex==null){noelo();}
            else
            {
                rankedimg0.setImageResource(getResources().getIdentifier("elo_"+rkflex.tier().toLowerCase(),"drawable",getPackageName()));
                rankedimg1.setImageResource(getResources().getIdentifier("elo_"+rkflex.tier().toLowerCase(),"drawable",getPackageName()));
                rankedimg1.clearColorFilter();
                lp.setText(rkflex.rank()+" - "+rkflex.leaguePoints()+"LP");
                wins.setText(rkflex.wins()+"W");
                loses.setText(rkflex.losses()+"L");
            }
        }
        else{noelo();}
    }

    private void noelo()
    {
        rankedimg0.setImageResource(R.drawable.elo_bronze);
        rankedimg1.setImageResource(R.drawable.elo_bronze);
        rankedimg1.setColorFilter(Color.parseColor("#000000"));
        if(!games.equals("420")&&!games.equals("440")) lp.setText("Normal Games");
        else lp.setText("Not yet Ranked");
        wins.setText("");
        loses.setText("");
    }

    private void reinit()
    {
        int color = Color.parseColor("#ffffff");
        if(canLive)livegame.setTextColor(color);
        history.setTextColor(color);
        search.setTextColor(color);
        leaderboard.setTextColor(color);
        settings.setTextColor(color);
    }

    @Override
    public void onClick(View view)
    {
        if(view!=refresh)
        {
            reinit();
            TextView tv = (TextView)view;
            if(!(view==livegame&&!canLive))tv.setTextColor(Color.parseColor("#404040"));
        }

            if(view==refresh&&canRefresh)
            {
                loading.setVisibility(View.VISIBLE);
                canRefresh = false;
                refresh.setTextColor(Color.parseColor("#ff5050"));
                startTime = System.currentTimeMillis();
                handler.postDelayed(runnable, 0);
                api.matchesByAccountId(summoner.accountId());
                api.rankedStatsBySummonerId(summoner.id());
            }
            else if(view==livegame&&!ecran.equals("live"))
            {
                if(ecran.equals("match"))stopMatch();
                ecran = "live";
                remove();
                fragment = new LiveGame();
                change();
            }
            else if(view==history&&!ecran.equals("history"))
            {
                if(ecran.equals("match"))
                {
                    if(!lastecran.equals("history"))stopMatch();
                }
                else
                {
                    games = "420";
                    ecran = "history";
                    remove();
                    fragment = new RefreshData();
                    change();
                }
            }
            else if(view==search&&!ecran.equals("search"))
            {
                if(ecran.equals("match"))
                {
                    if(!lastecran.equals("search"))stopMatch();
                }
                else
                {
                    ecran = "search";
                    remove();
                    fragment = new FindPlayer();
                    change();
                }
            }
            else if(view==leaderboard&&!ecran.equals("leader"))
            {
                if(ecran.equals("match"))stopMatch();
                ecran = "leader";
                remove();
                fragment = new LeaderBoard();
                change();
            }
            else if(view==settings&&!ecran.equals("settings"))
            {
                if(ecran.equals("match"))stopMatch();
                ecran = "settings";
                remove();
                fragment = new Settings();
                change();
            }
    }

    private void setShow()
    {
        animation = new AlphaAnimation(1f, 0f);
        animation.setDuration(3000);
        animation.setStartOffset(3000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                show.setAlpha(1f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                show.setAlpha(0f);
                show.clearAnimation();
            }
        });
    }

    private void show(int i)
    {
        show.setAnimation(animation);
        show.setText("Refreshed. "+i+" game"+(i>1?"s":"")+ " charged..");
        animation.start();
        loading.setVisibility(View.INVISIBLE);
    }

    public void error(String s)
    {
        show.setAnimation(animation);
        show.setText(s);
        animation.start();
        loading.setVisibility(View.INVISIBLE);
    }

    private void remove()
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(fragment).commit();
    }

    private void change()
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.containfragment, fragment).commit();
    }

    public void setGame(Match match)
    {
        lastecran = ecran;
        this.match = match;
        fragment2 = new Game();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.containfragment2, fragment2).commit();
        ecran = "match";
        containfrag.setVisibility(View.VISIBLE);
    }

    public Summoner summoner(){return summoner;}

    public BDD db(){return db;}
    public void setGames(String s){games = s;}
    public void partieData(String gameId, int i){api.partieData(gameId,i);}
    public String ecran(){return ecran;}
    public void liveGame(){loading.setVisibility(View.VISIBLE);api.livegame(summoner.id());}
    public Cursor getLive(){return db.live().get();}
    public Match getGame(){return match;}
    public void stopMatch(){containfrag.setVisibility(View.INVISIBLE);ecran=lastecran;}

    public void findPlayer(String summonerId)
    {
        containfrag.setVisibility(View.INVISIBLE);
        this.summonerId = summonerId;
        ecran = "search";
        reinit();
        search.setTextColor(Color.parseColor("#404040"));
        remove();
        fragment = new FindPlayer();
        change();
    }

    public void honor()
    {
        if(lastecran.equals("search"))
        {
            FindPlayer tmp = (FindPlayer)fragment;
            try{tmp.honor();}catch(JSONException e){e.printStackTrace();}
        }
    }

    public void reset()
    {
        db.historiqueParties().reset();
        db.honors().reset();
        db.live().reset();
        db.parties().reset();
        db.stats().reset();
    }

    public String summName(){return summoner.name();}

    public void changeSummoner(String name,String region)
    {
        reinitialize();
        api.summonerByName(name,region(region));
    }

    @Override
    public void onBackPressed(){if(ecran.equals("match")){stopMatch();}}
}
