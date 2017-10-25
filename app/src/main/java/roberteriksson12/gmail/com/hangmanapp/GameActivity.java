package roberteriksson12.gmail.com.hangmanapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private EditText guessField;
    private ImageView hangmanImg;
    private String[] words;
    private char[] mysteryWord;
    private TextView word;
    private String randWord;
    private TextView triesLeft;
    private int guesses;
    private String pastGuesses;
    private TextView pastGuessField;
    private TypedArray images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setTitle(getString(R.string.gameTitle));
        initialize();
    }

    private void initialize(){
        guessField = (EditText)findViewById(R.id.guessField);
        hangmanImg = (ImageView)findViewById(R.id.hangmanImg);
        images = getResources().obtainTypedArray(R.array.images);
        word = (TextView)findViewById(R.id.hiddenWord);
        triesLeft = (TextView)findViewById(R.id.triesLeft);
        pastGuessField = (TextView)findViewById(R.id.pastGuesses);
        guesses = 10;
        pastGuesses = "";
        Random random = new Random();
        createWords();  //puts a number of strings into the string array words
        randWord = words[random.nextInt(7)];
        mysteryWord = new char[randWord.length()];
        for (int i = 0; i < mysteryWord.length; i++){
            mysteryWord[i] = '*';
        }
        word.setText(getMysteryWord());
        changeImage();
    }

    private void createWords() {
        words = getResources().getStringArray(R.array.wordList);
    }

    /**
     * Inflates toolbar to fit icons
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Checks which toolbar icon was pressed
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.infoTlbBtn){
            Intent info = new Intent(this, InfoActivity.class);
            startActivity(info);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Runs after guess button is pressed. Checks if guessed letter was correct or wrong.
     * @param view
     */
    public void guessBtnPressed(View view) {

        String guess = guessField.getText().toString().toLowerCase();
        handleGuess(guess);
        afterEveryGuess();
        showResult();
    }

    private void handleGuess(String guess){
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
                afterWrongGuess(guess);
            }
        } else {
            chooseToast(guess);
        }
    }

    private void afterWrongGuess(String s) {
        handlePastGuesses(s);
        guesses--;
        changeImage();
        triesLeft.setText(getString(R.string.tries) + " " + guesses);
    }

    private void handlePastGuesses(String s) {
        if (pastGuesses.length() > 0)
            pastGuesses += ", " + s;
        else
            pastGuesses += s;
        pastGuessField.setText(pastGuesses);
    }

    private boolean isOneLetter(String s) {
        if (s.length() > 1)
            return false;
        else
            return true;
    }

    private boolean isGuessed(String s) {
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

    private void chooseToast(String s){
        if (!isLetter(s))
            toastHandler(3);
        else if (isGuessed(s))
            toastHandler(4);
        else if (!isOneLetter(s))
            toastHandler(5);
        else if (s.equals(""))
            toastHandler(6);
    }

    private void afterEveryGuess(){
        word.setText(getMysteryWord());
        guessField.setText("");
    }

    private String getMysteryWord(){ return String.valueOf(mysteryWord); }

    /**
     * Changes the image of the hanged man
     */
    public void changeImage(){
        hangmanImg.setImageResource(images.getResourceId(guesses, -1));
    }

    private void showResult(){
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("mystery", randWord);
        intent.putExtra("tries", guesses);
        if (isGameWon()){
            intent.putExtra("result", "You won!");
            startActivity(intent);
            finish();
        }
        else if (guesses == 0){
            intent.putExtra("result", "You lost");
            startActivity(intent);
            finish();
        }
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

    private void toastHandler(int i){
        switch (i){
            case 1:
                Toast toast1 = Toast.makeText(this, getString(R.string.correct), Toast.LENGTH_SHORT);
                toast1.show();
                break;
            case 2:
                Toast toast2 = Toast.makeText(this, getString(R.string.wrong), Toast.LENGTH_SHORT);
                toast2.show();
                break;
            case 3:
                Toast toast3 = Toast.makeText(this, getString(R.string.lettersOnly), Toast.LENGTH_SHORT);
                toast3.show();
                break;
            case 4:
                Toast toast4 = Toast.makeText(this, getString(R.string.guessedBefore), Toast.LENGTH_SHORT);
                toast4.show();
                break;
            case 5:
                Toast toast5 = Toast.makeText(this, getString(R.string.oneLetterOnly), Toast.LENGTH_SHORT);
                toast5.show();
                break;
            case 6:
                Toast toast6 = Toast.makeText(this, getString(R.string.oneLetterOnly), Toast.LENGTH_SHORT);
                toast6.show();
                break;
        }
    }
}
