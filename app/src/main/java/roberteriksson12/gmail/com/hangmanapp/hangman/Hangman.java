package roberteriksson12.gmail.com.hangmanapp.hangman;

import android.content.Context;
import android.content.res.TypedArray;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import roberteriksson12.gmail.com.hangmanapp.R;

/**
 * Created by mrx on 2017-10-25.
 */

public class Hangman {
    private String[] words;
    private char[] mysteryWord;
    private String randWord;
    private int guesses;
    private String pastGuesses;
    private Context context;

    public Hangman(){}

    public Hangman(Context context){
        this.context = context;
        guesses = 10;
        pastGuesses = "";
        Random random = new Random();
        createWords();
        randWord = words[random.nextInt(7)];
        mysteryWord = new char[randWord.length()];
        for (int i = 0; i < mysteryWord.length; i++){
            mysteryWord[i] = '*';
        }
    }

    public void handleGuess(String guess, TextView pastGuessField, ImageView hangmanImg, TypedArray images, TextView triesLeft){
        boolean correct = false;
        if (isLetter(guess) && !isGuessed(guess) && isOneLetter(guess)) {
            for (int i = 0; i < mysteryWord.length; i++) {
                if (guess.charAt(0) == randWord.charAt(i)) {
                    mysteryWord[i] = guess.charAt(0);
                    correct = true;
                }
            }
            if (correct) {
                toastHandler(1);
            } else {
                toastHandler(2);
                afterWrongGuess(guess, pastGuessField, hangmanImg, images, triesLeft);
            }
        } else {
            chooseToast(guess);
        }
    }

    public void createWords() {
        words = context.getResources().getStringArray(R.array.wordList);
    }

    public void afterWrongGuess(String s, TextView pastGuessField, ImageView hangmanImg, TypedArray images, TextView triesLeft) {
        handlePastGuesses(s, pastGuessField);
        guesses--;
        changeImage(hangmanImg, images);
        triesLeft.setText(context.getString(R.string.tries) + " " + guesses);
    }

    public void handlePastGuesses(String s, TextView pastGuessField) {
        if (pastGuesses.length() > 0)
            pastGuesses += ", " + s;
        else
            pastGuesses += s;
        pastGuessField.setText(pastGuesses);
    }

    public boolean isOneLetter(String s) {
        if (s.length() > 1)
            return false;
        else
            return true;
    }

    public boolean isGuessed(String s) {
        for (int i = 0; i < pastGuesses.length(); i++){
            if (pastGuesses.charAt(i) == s.charAt(0))
                return true;
        }
        return false;
    }

    /**
     * checks if input is a letter or not
     * @param s
     * @return true if input is a letter
     */
    public boolean isLetter(String s){
        if (s.equals(""))
            return false;
        else{
            char c = s.charAt(0);
            if (Character.isDigit(c))
                return false;
            else if (Character.isLetter(c))
                return true;
            else
                return false;
        }
    }

    public void chooseToast(String s){
        if (!isLetter(s))
            toastHandler(3);
        else if (isGuessed(s))
            toastHandler(4);
        else if (!isOneLetter(s))
            toastHandler(5);
        else if (s.equals(""))
            toastHandler(6);
    }

    public void afterEveryGuess(TextView word, EditText guessField){
        word.setText(getMysteryWord());
        guessField.setText("");
    }

    public String getMysteryWord(){ return String.valueOf(mysteryWord); }

    /**
     * Changes the image of the hanged man
     * @param hangmanImg
     * @param images
     */
    public void changeImage(ImageView hangmanImg, TypedArray images){
        hangmanImg.setImageDrawable(images.getDrawable(guesses));
    }

    /**
     * checks if all the letters have been guessed
     * @return true is all letters are guessed
     */
    public boolean isGameWon(){
        int numLetters = mysteryWord.length;
        for (int i = 0; i < mysteryWord.length; i++){
            if (mysteryWord[i] != '*')
                numLetters--;
        }
        if (numLetters == 0)
            return true;
        else
            return false;
    }

    public void toastHandler(int i){
        switch (i){
            case 1:
                Toast toast1 = Toast.makeText(context, context.getString(R.string.correct), Toast.LENGTH_SHORT);
                toast1.show();
                break;
            case 2:
                Toast toast2 = Toast.makeText(context, context.getString(R.string.wrong), Toast.LENGTH_SHORT);
                toast2.show();
                break;
            case 3:
                Toast toast3 = Toast.makeText(context, context.getString(R.string.lettersOnly), Toast.LENGTH_SHORT);
                toast3.show();
                break;
            case 4:
                Toast toast4 = Toast.makeText(context, context.getString(R.string.guessedBefore), Toast.LENGTH_SHORT);
                toast4.show();
                break;
            case 5:
                Toast toast5 = Toast.makeText(context, context.getString(R.string.oneLetterOnly), Toast.LENGTH_SHORT);
                toast5.show();
                break;
            case 6:
                Toast toast6 = Toast.makeText(context, context.getString(R.string.oneLetterOnly), Toast.LENGTH_SHORT);
                toast6.show();
                break;
        }
    }

    public String getRandWord(){
        return randWord;
    }

    public int getGuesses(){
        return guesses;
    }
}
