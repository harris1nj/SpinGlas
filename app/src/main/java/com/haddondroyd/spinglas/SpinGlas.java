package com.haddondroyd.spinglas;



import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.PathShape;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.util.Log;
import android.view.InputEvent;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import com.haddondroyd.spinglas.SpinGlasPlayer;
import com.haddondroyd.spinglas.SpinGlasBondPool;
import com.haddondroyd.spinglas.SpinGlasSite;
//import com.haddondroyd.spinglas.;
import android.widget.Toast;

public class SpinGlas extends Activity {
	public ImageView sites[] = new ImageView[14];
    public TextView site_text[] = new TextView[14];
	public ImageView links[] = new ImageView[21];
    SpinGlasPlayer whitePlayer;
    SpinGlasPlayer blackPlayer;
    SpinGlasBondPool bondPool = new SpinGlasBondPool();
    int linkSite1[] = new int[36];
    int linkSite2[] = new int[36];
    public static final int BLUEBOND = 2;
    public static final int REDBOND = 1;
    private boolean game_over;
    private boolean turn_over_early;
    // a site was touched, take appropriate action
		public OnClickListener siteListener = new OnClickListener() {
		  public void onClick(View v) {
			  ImageView iv = (ImageView) v;
              int result;
              if (whitePlayer.turn == 1) {
                  result = whitePlayer.touchedSite(iv,getApplicationContext());
                  if (result == 0) { // change to other player's turn
                      blackPlayer.turn = 1;
                      blackPlayer.moves_left = 3;
                      whitePlayer.turn = 0;
                      // turn off White flashing
                      Button b = (Button) findViewById(R.id.Button4);
                      update_button_flashing(b,false);
                      // turn on Black flashing
                      b = (Button) findViewById(R.id.Button5);
                      update_button_flashing(b,true);
                  }
                  if (whitePlayer.deduct_from_other_player != 0) {
                      blackPlayer.score -= whitePlayer.deduct_from_other_player;
                      whitePlayer.deduct_from_other_player = 0;
                  }
              }
              else {
                  result = blackPlayer.touchedSite(iv,getApplicationContext());
                  if (result == 0) { // change to other player's turn
                      whitePlayer.turn = 1;
                      whitePlayer.moves_left = 3;
                      blackPlayer.turn = 0;
                      // turn off Black flashing
                      Button b = (Button) findViewById(R.id.Button5);
                      update_button_flashing(b,false);
                      // turn on White flashing
                      b = (Button) findViewById(R.id.Button4);
                      update_button_flashing(b,true);
                  }
                  if (blackPlayer.deduct_from_other_player != 0) {
                      whitePlayer.score -= blackPlayer.deduct_from_other_player;
                      blackPlayer.deduct_from_other_player = 0;
                  }
              }
              update_score();
              // update all sites
              for (int i=0; i < 14; i++) {
                  update_site_energy(sites[i]);
              }
              turn_over_early = determine_turn_over_early();
              if (turn_over_early) {
                  if (blackPlayer.turn == 1) {
                      whitePlayer.turn = 1;
                      whitePlayer.moves_left = 3;
                      blackPlayer.turn = 0;
                      Log.d("JRH","S Black player turn over early");
                  }
                  else {
                      blackPlayer.turn = 1;
                      blackPlayer.moves_left = 3;
                      whitePlayer.turn = 0;
                      Log.d("JRH","S White player turn over early");
                  }
              }
              game_over = determine_game_over();
              if (game_over) {
                  // determine who won or if it's a draw
                  if (whitePlayer.score > blackPlayer.score) {
                      Log.d("JRH","White player wins!");
                      Toast.makeText(getApplicationContext(),"White player wins!",Toast.LENGTH_LONG).show();
                  }
                  else if (whitePlayer.score < blackPlayer.score) {
                      Log.d("JRH","Black player wins!");
                      Toast.makeText(getApplicationContext(),"Black player wins!",Toast.LENGTH_LONG).show();
                  }
                  else {
                      Log.d("JRH","It's a draw!");
                      Toast.makeText(getApplicationContext(),"It's a draw!",Toast.LENGTH_LONG).show();
                  }
                  startGame(); // start another game
              }
		  }
		};
		public OnClickListener linkListener = new OnClickListener() {
			  public void onClick(View v) {
				  ImageView iv = (ImageView) v;
                  int result;
                  if (whitePlayer.turn == 1) {
                      result = whitePlayer.touchedLink(iv, getApplicationContext());
                      if (result == 0) { // change to other player's turn
                          blackPlayer.turn = 1;
                          blackPlayer.moves_left = 3;
                          whitePlayer.turn = 0;
                          // turn off White flashing
                          Button b = (Button) findViewById(R.id.Button4);
                          update_button_flashing(b,false);
                          // turn on Black flashing
                          b = (Button) findViewById(R.id.Button5);
                          update_button_flashing(b,true);
                      }
                  }
                  else {
                      result = blackPlayer.touchedLink(iv,getApplicationContext());
                      if (result == 0) { // change to other player's turn
                          whitePlayer.turn = 1;
                          whitePlayer.moves_left = 3;
                          blackPlayer.turn = 0;
                          // turn off Black flashing
                          Button b = (Button) findViewById(R.id.Button5);
                          update_button_flashing(b,false);
                          // turn on White flashing
                          b = (Button) findViewById(R.id.Button4);
                          update_button_flashing(b,true);
                      }
                  }
                  // update all sites
                  for (int i=0; i < 14; i++) {
                      update_site_energy(sites[i]);
                  }
                  turn_over_early = determine_turn_over_early();
                  if (turn_over_early) {
                      if (blackPlayer.turn == 1) {
                          whitePlayer.turn = 1;
                          whitePlayer.moves_left = 3;
                          blackPlayer.turn = 0;
                          Log.d("JRH","L Black player turn over early");
                      }
                      else {
                          blackPlayer.turn = 1;
                          blackPlayer.moves_left = 3;
                          whitePlayer.turn = 0;
                          Log.d("JRH","L White player turn over early");
                      }
                  }
                  game_over = determine_game_over();
                  if (game_over) {
                      // determine who won or if it's a draw
                      if (whitePlayer.score > blackPlayer.score) {
                          Log.d("JRH","White player wins!");
                          Toast.makeText(getApplicationContext(),"White player wins!",Toast.LENGTH_LONG).show();
                      }
                      else if (whitePlayer.score < blackPlayer.score) {
                          Log.d("JRH","Black player wins!");
                          Toast.makeText(getApplicationContext(),"Black player wins!",Toast.LENGTH_LONG).show();
                      }
                      else {
                          Log.d("JRH","It's a draw!");
                          Toast.makeText(getApplicationContext(),"It's a draw!",Toast.LENGTH_LONG).show();
                      }
                      startGame(); // start another game
                  }
              }
			};
		@Override
		public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        int i = 0;
        site_text[i++] = (TextView) findViewById(R.id.ImageViewText01);
        site_text[i++] = (TextView) findViewById(R.id.ImageViewText02);
        site_text[i++] = (TextView) findViewById(R.id.ImageViewText03);
        site_text[i++] = (TextView) findViewById(R.id.ImageViewText04);
        site_text[i++] = (TextView) findViewById(R.id.ImageViewText05);
        site_text[i++] = (TextView) findViewById(R.id.ImageViewText06);
        site_text[i++] = (TextView) findViewById(R.id.ImageViewText07);
        site_text[i++] = (TextView) findViewById(R.id.ImageViewText08);
        site_text[i++] = (TextView) findViewById(R.id.ImageViewText09);
        site_text[i++] = (TextView) findViewById(R.id.ImageViewText10);
        site_text[i++] = (TextView) findViewById(R.id.ImageViewText11);
        site_text[i++] = (TextView) findViewById(R.id.ImageViewText12);
        site_text[i++] = (TextView) findViewById(R.id.ImageViewText13);
        site_text[i] = (TextView) findViewById(R.id.ImageViewText14);
		i = 0;
		sites[i++] = (ImageView) findViewById(R.id.ImageView01);
		sites[i++] = (ImageView) findViewById(R.id.ImageView02);
		sites[i++] = (ImageView) findViewById(R.id.ImageView03);
		sites[i++] = (ImageView) findViewById(R.id.ImageView04);
		sites[i++] = (ImageView) findViewById(R.id.ImageView05);
		sites[i++] = (ImageView) findViewById(R.id.ImageView06);
		sites[i++] = (ImageView) findViewById(R.id.ImageView07);
		sites[i++] = (ImageView) findViewById(R.id.ImageView08);
		sites[i++] = (ImageView) findViewById(R.id.ImageView09);
		sites[i++] = (ImageView) findViewById(R.id.ImageView10);
		sites[i++] = (ImageView) findViewById(R.id.ImageView11);
		sites[i++] = (ImageView) findViewById(R.id.ImageView12);
		sites[i++] = (ImageView) findViewById(R.id.ImageView13);
		sites[i++] = (ImageView) findViewById(R.id.ImageView14);
		for (int j=0; j < sites.length; ++j) {
			sites[j].setOnClickListener(siteListener);
            SpinGlasSite sgs = new SpinGlasSite(j+1);
			sites[j].setTag((Object)sgs); // set the identifier for later use
			sites[j].bringToFront(); // bring the sites up front and the links in the back
            site_text[j].bringToFront();
			}
		i = 0;
		links[i] = (ImageView) findViewById(R.id.ImageView15); links[i++].setTag(15);
		links[i] = (ImageView) findViewById(R.id.ImageView16); links[i++].setTag(16);
		links[i] = (ImageView) findViewById(R.id.ImageView17); links[i++].setTag(17);
		links[i] = (ImageView) findViewById(R.id.ImageView18); links[i++].setTag(18);
		links[i] = (ImageView) findViewById(R.id.ImageView19); links[i++].setTag(19);
		links[i] = (ImageView) findViewById(R.id.ImageView20); links[i++].setTag(20);
		links[i] = (ImageView) findViewById(R.id.ImageView21); links[i++].setTag(21);
		links[i] = (ImageView) findViewById(R.id.ImageView22); links[i++].setTag(22);
		links[i] = (ImageView) findViewById(R.id.ImageView23); links[i++].setTag(23);
		links[i] = (ImageView) findViewById(R.id.ImageView24); links[i++].setTag(24);
		links[i] = (ImageView) findViewById(R.id.ImageView25); links[i++].setTag(25);
		links[i] = (ImageView) findViewById(R.id.ImageView26); links[i++].setTag(26);
		links[i] = (ImageView) findViewById(R.id.ImageView27); links[i++].setTag(27);
		links[i] = (ImageView) findViewById(R.id.ImageView28); links[i++].setTag(28);
		links[i] = (ImageView) findViewById(R.id.ImageView29); links[i++].setTag(29);
		links[i] = (ImageView) findViewById(R.id.ImageView30); links[i++].setTag(30);
		links[i] = (ImageView) findViewById(R.id.ImageView31); links[i++].setTag(31);
		links[i] = (ImageView) findViewById(R.id.ImageView32); links[i++].setTag(32);
		links[i] = (ImageView) findViewById(R.id.ImageView33); links[i++].setTag(33);
		links[i] = (ImageView) findViewById(R.id.ImageView34); links[i++].setTag(34);
		links[i] = (ImageView) findViewById(R.id.ImageView35); links[i++].setTag(35);
		for (int j=0; j < links.length; ++j) {
			links[j].setOnClickListener(linkListener);
            links[j].setTag((Object)new SpinGlasLink(j+1));
		}
	    startGame();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt("WHITE_SCORE", whitePlayer.score);
        //Log.d("JRH", "Saved white score: " + whitePlayer.score);
        savedInstanceState.putInt("BLACK_SCORE", blackPlayer.score);
        savedInstanceState.putInt("WHITE_MOVES", whitePlayer.moves_left);
        //Log.d("JRH", "Saved white moves left: " + whitePlayer.moves_left);
        savedInstanceState.putInt("BLACK_MOVES", blackPlayer.moves_left);
        savedInstanceState.putInt("WHITE_TURN", whitePlayer.turn);
        savedInstanceState.putInt("BLACK_TURN", blackPlayer.turn);
        for (int i=0; i < 14; i++) {
            SpinGlasSite site = (SpinGlasSite)sites[i].getTag();
            savedInstanceState.putInt("SITE"+i,site.color);
        }
        for (int i=0; i < 21; i++) {
            SpinGlasLink link = (SpinGlasLink) links[i].getTag();
            savedInstanceState.putInt("LINK"+i,link.color);
        }
        // Save the bond pool
        savedInstanceState.putInt("BONDINDEX", bondPool.getBondIndex());
        for (int i=0; i < bondPool.MAXINDEX; i++) savedInstanceState.putInt("BOND"+i,bondPool.getBondArrayValue(i));
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        Button button = (Button) findViewById(R.id.Button1);

        if (savedInstanceState != null) {
            // Restore state members from saved instance
            whitePlayer.score = savedInstanceState.getInt("WHITE_SCORE");
            //Log.d("JRH","Restored white score: "+whitePlayer.score);
            blackPlayer.score = savedInstanceState.getInt("BLACK_SCORE");
            whitePlayer.moves_left = savedInstanceState.getInt("WHITE_MOVES");
            //Log.d("JRH", "Restored white moves left: " + whitePlayer.moves_left);
            blackPlayer.moves_left = savedInstanceState.getInt("BLACK_MOVES");
            whitePlayer.turn = savedInstanceState.getInt("WHITE_TURN");
            blackPlayer.turn = savedInstanceState.getInt("BLACK_TURN");
            whitePlayer.getRealColor();
            update_score();
            for (int i=0; i < 14; i++) {
                SpinGlasSite site = (SpinGlasSite) sites[i].getTag();
                site.color = savedInstanceState.getInt("SITE" + i);
                if (site.color > 0) {
                    ShapeDrawable s = new ShapeDrawable(new OvalShape());
                    if (site.color == 1) s.getPaint().setColor(Color.WHITE);
                    else s.getPaint().setColor(Color.BLACK);
                    s.setIntrinsicHeight((int) getApplicationContext().getResources().getDimension(R.dimen.site_image_height));
                    s.setIntrinsicWidth((int) getApplicationContext().getResources().getDimension(R.dimen.site_image_width));
                    sites[i].setImageDrawable(s); // change the color
                }
            }
            for (int i=0; i < 21; i++) {
                SpinGlasLink link = (SpinGlasLink) links[i].getTag();
                link.color = savedInstanceState.getInt("LINK" + i);
                if (link.color > 0) {
                    ShapeDrawable linkShape = new ShapeDrawable(new RectShape());
                    if (link.color == SpinGlas.REDBOND) {
                        linkShape.getPaint().setColor(Color.RED);
                    }
                    else if (link.color == SpinGlas.BLUEBOND) {
                        linkShape.getPaint().setColor(Color.BLUE);
                    }
                    linkShape.setIntrinsicHeight((int) getApplicationContext().getResources().getDimension(R.dimen.link_image_height));
                    linkShape.setIntrinsicWidth((int) getApplicationContext().getResources().getDimension(R.dimen.link_image_width));
                    links[i].setImageDrawable(linkShape); // change the color
                }
            }
            for (int i=0; i < 14; i++) {
                update_site_energy(sites[i]);
            }
            // Restore the bond pool
            bondPool.setBondIndex(savedInstanceState.getInt("BONDINDEX"));
            for (int i=0; i < bondPool.MAXINDEX; i++)
                bondPool.setBondArrayValue(i,savedInstanceState.getInt("BOND"+i));
            if (bondPool.previousBond() == SpinGlas.REDBOND) button.setBackgroundColor(Color.RED);
            else if (bondPool.previousBond() == SpinGlas.BLUEBOND) button.setBackgroundColor(Color.BLUE);
            else button.setBackgroundColor(Color.GRAY);
            button.setText(Integer.toString(bondPool.remaining()));
            if (blackPlayer.turn == 1) {
                Button bb = (Button)  findViewById(R.id.Button5); // flash Black button, white button is currently flashing
                update_button_flashing(bb,true);
                Button wb = (Button)  findViewById(R.id.Button4); // stop flashing white button
                update_button_flashing(wb,false);
            }
        }

    }

    void startGame() {
        game_over = false;
        bondPool.init();
        whitePlayer = new SpinGlasPlayer(1);
        blackPlayer = new SpinGlasPlayer(2);
        // make the link to site 1 to site 2 linkage
        linkSite1[15] = 1; // link 15, site 1, site 4
        linkSite2[15] = 4; // link 15, site 1, site 4
        linkSite1[26] = 1; // link 26, site 1, site 5
        linkSite2[26] = 5; // link 26, site 1, site 5
        linkSite1[17] = 2; // link 17, site 2, site 5
        linkSite2[17] = 5; // link 17, site 2, site 5
        linkSite1[18] = 2; // link 18, site 2, site 3
        linkSite2[18] = 3; // link 18, site 2, site 3
        linkSite1[19] = 3; // link 19, site 3, site 7
        linkSite2[19] = 7; // link 19, site 3, site 7
        linkSite1[22] = 4; // link 22, site 4, site 5
        linkSite2[22] = 5; // link 22, site 4, site 5
        linkSite1[20] = 5; // link 20, site 5, site 6
        linkSite2[20] = 6; // link 20, site 5, site 6
        linkSite1[21] = 6; // link 21, site 6, site 7
        linkSite2[21] = 7; // link 21, site 6, site 7
        linkSite1[23] = 4; // link 23, site 4, site 11
        linkSite2[23] = 11; // link 23, site 4, site 11
        linkSite1[30] = 4; // link 30, site 4, site 8
        linkSite2[30] = 8; // link 30, site 4, site 8
        linkSite1[24] = 5; // link 24, site 5, site 9
        linkSite2[24] = 9; // link 24, site 5, site 9
        linkSite1[25] = 6; // link 25, site 6, site 10
        linkSite2[25] = 10; // link 25, site 6, site 10
        linkSite1[27] = 7; // link 27, site 7, site 14
        linkSite2[27] = 14; // link 27, site 7, site 14
        linkSite1[28] = 8; // link 28, site 8, site 9
        linkSite2[28] = 9; // link 28, site 8, site 9
        linkSite1[29] = 9; // link 29, site 9, site 10
        linkSite2[29] = 10; // link 29, site 9, site 10
        linkSite1[31] = 8; // link 31, site 8, site 11
        linkSite2[31] = 11; // link 31, site 8, site 11
        linkSite1[16] = 9; // link 16, site 9, site 12
        linkSite2[16] = 12; // link 16, site 9, site 12
        linkSite1[32] = 9; // link 32, site 9, site 13
        linkSite2[32] = 13; // link 32, site 9, site 13
        linkSite1[33] = 10; // link 33, site 10, site 14
        linkSite2[33] = 14; // link 33, site 10, site 14
        linkSite1[34] = 11; // link 34, site 11, site 12
        linkSite2[34] = 12; // link 34, site 11, site 12
        linkSite1[35] = 13; // link 35, site 13, site 14
        linkSite2[35] = 14; // link 35, site 13, site 14
        Button b = (Button)  findViewById(R.id.Button4); // flash White button
        update_button_flashing(b,true);

    }
    public void bondPoolClick(View view) {
        //
        int result;
        Button button = (Button) findViewById(R.id.Button1);

        result = bondPool.getBond();
        if (result == -1) {
            // do nothing
        }
        if (whitePlayer.turn == 1) {
            whitePlayer.bond = result;
            whitePlayer.bondEnabled = true;
        }
        else {
            blackPlayer.bond = result;
            blackPlayer.bondEnabled = true;
        }
        // set color of Bond Pool button to the color selected
        if (result == 1) button.setBackgroundColor(Color.RED);
        else if (result == 2) button.setBackgroundColor(Color.BLUE);
        else button.setBackgroundColor(Color.GRAY);
        button.setText(Integer.toString(bondPool.remaining()));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Help:
                Log.d("JRH","Help pressed");
                Intent hintent = new Intent(this, HelpActivity.class);
                startActivity (hintent);
                return true;
            case R.id.About:
                Log.d("JRH","About pressed");
                Intent aintent = new Intent(this, AboutActivity.class);
                startActivity (aintent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void update_score() {
        Button b1 = (Button) findViewById(R.id.Button3);
        b1.setText(Integer.toString(blackPlayer.score));
        Button b2 = (Button) findViewById(R.id.Button2);
        b2.setText(Integer.toString(whitePlayer.score));

    }
    private void update_site_energy(ImageView iv) {
        int i,j,other_site_id[] = new int[8];
        int tempLinks[] = new int[8]; // max of 8 links per site
        SpinGlasSite site = (SpinGlasSite) iv.getTag();
        // find all links associated with this site
        for (i=0; i < 8; i++) tempLinks[i] = 0;
        j = 0;
//        Log.d("JRH","Site: "+Integer.toString(site.id));
        for (i=15;i<=35;i++) {
            if (linkSite1[i] == site.id) {
                tempLinks[j] = i; // save the link id
                j++;
                if (j > 8) break;
//                Log.d("JRH","Link: "+Integer.toString(tempLinks[j-1]));
            }
            else if (linkSite2[i] == site.id) {
                tempLinks[j] = i; // save the link id
                j++;
                if (j > 8) break;
//                Log.d("JRH","Link: "+Integer.toString(tempLinks[j-1]));
            }
        }

        // for each link, find other site
        for (i=0; i < 8; i++) other_site_id[i] = 0;
        for (i=0; i < 8; i++) {
                if (tempLinks[i] == 0) break;
            if (linkSite2[tempLinks[i]] == site.id) {
                other_site_id[i] = linkSite1[tempLinks[i]];
//                Log.d("JRH","OS: "+Integer.toString(other_site_id[i]));
            }
            else {
                other_site_id[i] = linkSite2[tempLinks[i]];
//                Log.d("JRH","OS: "+Integer.toString(other_site_id[i]));
            }
        }
        // determine energy based on site/link/site combo
        site.energy = 0; // init value
        for (i=0; i < 8; i++) {
            if (tempLinks[i] == 0) break;
            // get reference to link
           SpinGlasLink sgl = (SpinGlasLink) links[tempLinks[i]-15].getTag();
            // get other site color
            ImageView tempIv = sites[other_site_id[i]-1];
            SpinGlasSite tempSite = (SpinGlasSite) tempIv.getTag();
//            Log.d("JRH","C: "+Integer.toString(site.color)+" "+Integer.toString(tempSite.color));
            if (site.color == tempSite.color) {
                // if link color is BLUE, then energy is -1
                // if link color is RED, then energy is +1
                if (sgl.color == BLUEBOND) {
                    site.energy -= 1;
//                    Log.d("JRH","E: -1");
                }
                else if (sgl.color == REDBOND) {
                    site.energy += 1;
//                    Log.d("JRH","E: +1");
                }

            }
            else if (tempSite.color == 0) {
                // not used yet so no changes
            }
            else { // different colors
                // if link color is BLUE, then energy is +1
                // if link color is RED, then energy is -1
                if (sgl.color == BLUEBOND) {
                    site.energy += 1;
//                    Log.d("JRH","E: +1");
                }
                else if (sgl.color == REDBOND) {
                    site.energy -= 1;
//                    Log.d("JRH","E: -1");
                }
            }
        }
//        Log.d("JRH","TE: "+Integer.toString(site.energy));

        if (site.color == 1) {
            site_text[site.id-1].setTextColor(Color.BLACK);
            site_text[site.id-1].setText(Integer.toString(site.energy));
        }
        else if (site.color == 2) {
            site_text[site.id-1].setTextColor(Color.WHITE);
            site_text[site.id-1].setText(Integer.toString(site.energy));
        }
    }
    // check to see if all sites and links have color. If so, game over
    private boolean determine_game_over() {
        boolean result = true; // assume game is over
        for (int i=0; i < sites.length; i++) {
            SpinGlasSite site = (SpinGlasSite) sites[i].getTag();
            if (site.color == 0) {
                result = false;
                break;
            }
            else if (site.energy >= 0) {
                // still flippable so game on
                result = false;
                break;
            }
        }
        for (int i=0; i < links.length; i++) {
            SpinGlasLink link = (SpinGlasLink) links[i].getTag();
            if (link.color == 0) {
                result = false;
                break;
            }
        }
        return result;
    }
    private void update_button_flashing(Button b, boolean on){
        Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
       if (on)  b.startAnimation(animation);
        else b.clearAnimation();
    }
    // determine if the current user's turn is over early
    private boolean determine_turn_over_early() {
        boolean result = true; // assume turn is over
        int other_color;
        if (whitePlayer.turn == 1) other_color = 2;
        else  other_color = 1;
        // all sites owned by the other player must be negative
        for (int i=0; i < sites.length; i++) {
            SpinGlasSite site = (SpinGlasSite) sites[i].getTag();
            if (site.color == 0) {
                result = false;
                break;
            }
            else if ((site.color == other_color)&&(site.energy >= 0)) {
                // still flippable so turn is still on
                result = false;
                break;
            }
        }
        // all links must be used
        for (int i=0; i < links.length; i++) {
            SpinGlasLink link = (SpinGlasLink) links[i].getTag();
            if (link.color == 0) {
                result = false;
                break;
            }
        }
        return result;
    }
}
