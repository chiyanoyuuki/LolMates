package chiya.lolmates;

import android.content.Context;
import android.database.sqlite.*;
import android.util.Log;

public class BDD extends SQLiteOpenHelper
{
    protected final static int VERSION = 2;
    protected final static String NOM = "LolMates.db";
    private boolean update;
    private Context context;

    private TABLEParties parties;
    private TABLEHistoriqueParties historiqueParties;
    private TABLESummoner summoner;
    private TABLERanked ranked;
    private TABLELive live;
    private TABLEHonor honors;
    private TABLEStats stats;

    public BDD(Context context)
    {
        super(context, NOM, null, VERSION);
        this.context = context;
        init();
    }

    private void init()
    {
        parties             = new TABLEParties                  (this);
        historiqueParties   = new TABLEHistoriqueParties        (this);
        summoner            = new TABLESummoner                 (this);
        ranked              = new TABLERanked                   (this);
        live                = new TABLELive                     (this);
        honors              = new TABLEHonor                    (this);
        stats               = new TABLEStats                    (this);
    }

    public void onCreate(SQLiteDatabase db)
    {
        Log.w("CREATING","Creating Database");
        if(!update){initBDDCompte(db);}
        initBDDInfos(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        update = true;
        Log.w("UPGRADE","Upgrading Version of the Database. V" + oldVersion + " to V" + newVersion);
        onCreate(db);
    }

    private void initBDDCompte(SQLiteDatabase db)
    {
        parties.init(db);
        historiqueParties.init(db);
        summoner.init(db);
        ranked.init(db);
        live.init(db);
        honors.init(db);
        stats.init(db);
    }

    private void initBDDInfos(SQLiteDatabase db)
    {

    }

    public TABLEParties parties()                           {return parties;}
    public TABLEHistoriqueParties historiqueParties()       {return historiqueParties;}
    public TABLESummoner summoner()                         {return summoner;}
    public TABLERanked ranked()                             {return ranked;}
    public TABLELive live()                                 {return live;}
    public TABLEHonor honors()                              {return honors;}
    public TABLEStats stats()                               {return stats;}
}