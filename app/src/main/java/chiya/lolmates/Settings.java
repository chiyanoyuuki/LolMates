package chiya.lolmates;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import chiya.lolmates.requetes.Match;
import chiya.lolmates.requetes.Participant;
import chiya.lolmates.requetes.PartieData;
import chiya.lolmates.requetes.Player;
import chiya.lolmates.requetes.Team;

public class Settings extends Fragment implements View.OnClickListener
{
    private BillingClient mBillingClient;
    private String mAdRemovalPrice;
    private SharedPreferences mSharedPreferences;

    private TextView changesumm, reset, removeads, contact, about;
    private ScrollView abouttext;
    private LinearLayout linearabout;
    private boolean show;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.settings, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        Accueil main = (Accueil)getActivity();

        changesumm = view.findViewById(R.id.changesumm);
        reset = view.findViewById(R.id.reinit);
        removeads = view.findViewById(R.id.ads);
        contact = view.findViewById(R.id.contact);
        about = view.findViewById(R.id.about);
        abouttext = view.findViewById(R.id.aboutscroll);
        linearabout = view.findViewById(R.id.linearabout);

        changesumm.setOnClickListener(this);
        reset.setOnClickListener(this);
        removeads.setOnClickListener(this);
        contact.setOnClickListener(this);
        about.setOnClickListener(this);
        abouttext.setOnClickListener(this);
        linearabout.setOnClickListener(this);

        initBill();

        abouttext.setVisibility(View.INVISIBLE);
    }

    private void initBill()
    {
        final Accueil main = (Accueil)getActivity();
        mBillingClient = BillingClient.newBuilder(main).enablePendingPurchases().setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(BillingResult result, @Nullable List<Purchase> purchases) {
                if (result.getResponseCode() == BillingClient.BillingResponseCode.OK
                        && purchases != null) {

                    main.payed();


                } else if (result.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                } else {
                }
            }
        }).build();
    }

    private void abouttext()
    {
        System.out.println("click");
        if(!show){abouttext.setVisibility(View.VISIBLE);}
        else {abouttext.setVisibility(View.INVISIBLE);}
        show = !show;
    }

    private void changeSumm()
    {
        Accueil main = (Accueil)getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.summ,null);
        builder.setView(view);

        final EditText input = view.findViewById(R.id.summedit);
        final Spinner spinner = view.findViewById(R.id.summspinn);
        TextView title = view.findViewById(R.id.summtitle);
        TextView text = view.findViewById(R.id.summtext);

        title.setText("Change Summoner :");
        text.setText("What is the new Summoner Name ?");


        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){change(input.getText().toString(),spinner.getSelectedItem().toString());}
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){}
        });

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

    public void change(String name, String region)
    {
        Accueil main = (Accueil)getActivity();

        final String newName = name;
        final String newRegion = region;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm changing " + main.summName() + " to " + name + " (" + region+") ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){changeSummoner(newName,newRegion);}
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){}
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void changeSummoner(String name,String region)
    {
        Accueil main = (Accueil)getActivity();
        main.changeSummoner(name,region);
    }

    private void reset()
    {
        Accueil main = (Accueil)getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Are you sure you want to delete all stats from the app ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){reset2();}
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){}
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void reset2()
    {
        Accueil main = (Accueil)getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Every stats from any summoner ever linked will be erased, continue ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){resetall();}
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){}
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void contact()
    {
        Accueil main = (Accueil)getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("chiyanoyuuki.apps@gmail.com\nOr comment on the app store ! :)");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){}
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void resetall()
    {
        Accueil main = (Accueil)getActivity();
        main.reset();
    }

    @Override
    public void onClick(View view)
    {
        if(view==changesumm)
        {
            changeSumm();
        }
        else if(view==reset)
        {
            reset();
        }
        else if(view==contact)
        {
            contact();
        }
        else if(view==removeads)
        {
            removeAds();
        }
        else if(view==about||view==linearabout)
        {
            abouttext();
        }
    }

    private void removeAds()
    {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult result)
            {
                if (result.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                    List<String> skuList = new ArrayList<>();
                    skuList.add("remove_ads.lolmates");

                    SkuDetailsParams skuDetailsParams = SkuDetailsParams.newBuilder()
                            .setSkusList(skuList).setType(BillingClient.SkuType.INAPP).build();
                    mBillingClient.querySkuDetailsAsync(skuDetailsParams,
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(BillingResult result, List<SkuDetails> skuDetailsList) {

                                    BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                            .setSkuDetails(skuDetailsList.get(0))
                                            .build();
                                        int billingResponseCode = mBillingClient.launchBillingFlow(getActivity(), flowParams).getResponseCode();
                                        if (billingResponseCode == BillingClient.BillingResponseCode.OK) {
                                        System.out.println("============> ON TESTE HEIN");
                                    }
                                }
                            });
                }
            }


            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }
}