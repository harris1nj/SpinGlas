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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.util.Log;
import android.view.InputEvent;
import com.haddondroyd.spinglas.SpinGlas;


/**
 * Created by Jim Harris on 4/21/2014.
 */
public class SpinGlasPlayer {

    // number if spins left, initially 15
    public int spinPool;
    // player's current score, initially 0
    public int score;
    // 1 - White player, 2 - Black player
    public int color;
    // points to deduct from the other player due to flip (usually 0 or 1)
    public int deduct_from_other_player;
    // real Color
    private int realColor;
    // 0 - not this player's turn, 1 - player's turn in process
    public int turn;
    // number of moves left in a player's turn
    public int moves_left;
    // current bond
    public int bond;
    // enables player to change a link to the selected bond
    public boolean bondEnabled;
    // constructor
    public SpinGlasPlayer(int passed_in_color) {
        spinPool = 15;
        score = 0;
        deduct_from_other_player = 0;
        color = passed_in_color;
        // white player goes first
        if (color == 1) {
            turn = 1;
            realColor = Color.WHITE;
        }
        else {
            turn = 0;
            realColor = Color.BLACK;
        }
        // start out with 3 moves for every turn
        moves_left = 3;
        bond = 0;
        bondEnabled = false;
    }
    int touchedSite(ImageView iv, Context c) {
        int result;
        if ((moves_left > 0)&&(turn == 1)) {
            // determine if site is empty or player is attempting a flip
            SpinGlasSite site = (SpinGlasSite) iv.getTag();
            if (site.color == this.color) { // do nothing
            }
            else if (site.color == 0) { // site is empty, change site to player's color
                if (spinPool > 0) {
                    site.color = this.color;
                    spinPool--;
                    moves_left--;
                    ShapeDrawable s = new ShapeDrawable(new OvalShape());
                    s.getPaint().setColor(realColor);
                    s.setIntrinsicHeight((int) c.getResources().getDimension(R.dimen.site_image_height));
                    s.setIntrinsicWidth((int) c.getResources().getDimension(R.dimen.site_image_width));
                    iv.setImageDrawable(s); // change the color
                    this.score += 1;
                }
            }
            else { // attempt flip
                if (site.energy >= 0) {
                    if (spinPool > 0) {
                        site.color = this.color;
                        spinPool--;
                        moves_left--;
                        ShapeDrawable s = new ShapeDrawable(new OvalShape());
                        s.getPaint().setColor(realColor);
                        s.setIntrinsicHeight((int) c.getResources().getDimension(R.dimen.site_image_height));
                        s.setIntrinsicWidth((int) c.getResources().getDimension(R.dimen.site_image_width));
                        iv.setImageDrawable(s); // change the color
                        this.score += 1;
                        this.deduct_from_other_player = 1;
                    }
                }
            }
        }
        if (turn == 0) {
            result = -1; // not this player's turn
        }
        else if (spinPool == 0) {
            moves_left = 0; // all done for this player
            result = 0;
        }
        else result = moves_left;
        return result;
    }
    // when a player touches a link,
    // return values are -1 for error, 0 for no moves left, or number of moves left
    int touchedLink(ImageView iv, Context c) {
        int result = -1;

        if ((moves_left > 0) && (turn == 1)) {
            // determine if link is empty
            SpinGlasLink link = (SpinGlasLink) iv.getTag();
            if ((link.color == 0)&&(bondEnabled)) { // empty
                ShapeDrawable linkShape = new ShapeDrawable(new RectShape());
                if (bond == SpinGlas.REDBOND) {
                    linkShape.getPaint().setColor(Color.RED);
                }
                else if (bond == SpinGlas.BLUEBOND) {
                    linkShape.getPaint().setColor(Color.BLUE);
                }
                linkShape.setIntrinsicHeight((int) c.getResources().getDimension(R.dimen.link_image_height));
                linkShape.setIntrinsicWidth((int) c.getResources().getDimension(R.dimen.link_image_width));
                iv.setImageDrawable(linkShape); // change the color
                link.color = bond;
                moves_left--;
                result = moves_left;
//                Log.d("JRH","TL: "+Integer.toString(link.color));
            }
        }
        bondEnabled = false;
        return result;
    }
    int getRealColor()
    {
        return realColor;
    }
}
