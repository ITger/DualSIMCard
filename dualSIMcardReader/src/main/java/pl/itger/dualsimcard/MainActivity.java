package pl.itger.dualsimcard;

/**
 * Created by piotrz on 7/23/15.
 * Copyright 2015 Piotr Zerynger ITger
 */


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static pl.itger.dualsimcard.R.id.action_settings;


public class MainActivity extends Activity {

    private static Context context;

    public static Context getMActContext() {
        return MainActivity.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GsmCellLocation cl = (GsmCellLocation) CellLocation.getEmpty();
        CellLocation.requestLocationUpdate();
        System.out.println("GsmCellLocation " + cl.toString());
        MainActivity.context = getApplicationContext();
        setContentView(R.layout.activity_dual_sim_card_main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        clockwise(findViewById(R.id.listView));
        //readSims();
    }

    private void readSims() {

        //  LogUtils.logW("Network");
        TelInfo telephonyInfo = TelInfo.getInstance();//getMActContext());
        //System.out.println("..." + telephonyInfo.toString());

        final int listView1 = R.id.listView;
        final ListView lv = (ListView) findViewById(listView1);

        //DualSIM_adapter ad = new DualSIM_adapter(this, R.layout.simci, new ArrayList<Sci>(Arrays.asList(telephonyInfo.scitems)));
        DualSIM_adapter ad = new DualSIM_adapter(this, telephonyInfo.scitemsArr);
        lv.setAdapter(ad);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dual_sim_card_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clockwise(View view) {
        ImageView image = (ImageView) findViewById(R.id.dualSIMimageView);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
        image.startAnimation(animation);
        readSims();
    }


}
