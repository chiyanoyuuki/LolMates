package chiya.lolmates;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Async
{
    private Accueil main;
    private String type;

    public Async(Accueil main, String type)
    {
        this.main = main;
        this.type = type;
    }

    public void get(String url)
    {
        new GetJSONTask().execute(url);
    }

    private class GetJSONTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            try {
                return Utility.downloadDataFromUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve data. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            ArrayList<HashMap<String,String>> results2 = new ArrayList<>();
            HashMap<String,String> results = new HashMap<>();

            //if(type.equals("livegame"))result = new Tmp().getTmp();

            //System.out.println(result);

            try
            {
                if(result.startsWith("["))
                {
                    JSONArray arr = new JSONArray(result);
                    for(int i=0;i<arr.length();i++)
                    {
                        results = new HashMap<>();
                        JSONObject obj = arr.getJSONObject(i);
                        Iterator<String> it = obj.keys();
                        while(it.hasNext())
                        {
                            String key = it.next();
                            results.put(key,obj.getString(key));
                        }
                        results2.add(results);
                    }
                    main.result(results2, type);
                }
                else if(result.startsWith("{"))
                {
                    JSONObject obj = new JSONObject(result);
                    Iterator<String> it = obj.keys();
                    while(it.hasNext())
                    {
                        String key = it.next();
                        results.put(key,obj.getString(key));
                    }
                    main.result(results, type);
                }
                else
                {
                    if(type.equals("livegame"))main.noLive();
                    else if(type.equals("summoner"))main.error("Summoner not found..");
                    else if(type.equals("firstsumm")){main.error("Summoner not found...");main.askSumm();}
                    else main.error("A problem occured..");
                }
            }
            catch(JSONException e){e.printStackTrace();}
        }
    }
}
