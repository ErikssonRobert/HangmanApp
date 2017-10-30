package roberteriksson12.gmail.com.hangmanapp.hangman;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import roberteriksson12.gmail.com.hangmanapp.R;

/**
 * Created by Robert on 2017-10-25.
 */

public class HangmanAdapter extends Hangman {

    public HangmanAdapter(Context context){
        super(context);
    }

    /**
     * Saves variables to Bundle to not lose them when orientation is changed
     * @param outState
     */
    public void saveInformation(Bundle outState){
        outState.putString("mysteryWord", getMysteryWord());
        outState.putString("randomWord", getRandWord());
        outState.putInt("guesses", getGuesses());
        outState.putString("pastGuesses", getPastGuesses());
    }

    /**
     * Sends saved information to Hangman class to restore the game to the state it was.
     * @param context
     * @param outState
     * @param triesLeft
     * @param pastGuessField
     * @param word
     * @param hangmanImg
     * @param images
     */
    public void restoreInformation(Context context, Bundle outState, TextView triesLeft, TextView pastGuessField, TextView word, ImageView hangmanImg, TypedArray images){
        restoreHangman(context, outState.getString("mysteryWord"), outState.getString("randomWord"), outState.getInt("guesses"), outState.getString("pastGuesses"));
        triesLeft.setText(context.getString(R.string.tries) + " " + getGuesses());
        pastGuessField.setText(getPastGuesses());
        word.setText(getMysteryWord());
        hangmanImg.setImageDrawable(images.getDrawable(getGuesses()));
    }
}
