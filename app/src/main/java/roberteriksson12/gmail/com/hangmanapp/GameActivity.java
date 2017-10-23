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
        setTitle("Hangman game");
        initialize();
        word.setText(mysteryWord, 0, mysteryWord.length);
    }

    private void initialize(){
        guessField = (EditText)findViewById(R.id.guessField);
        hangmanImg = (ImageView)findViewById(R.id.hangmanImg);
        hangmanImg.setImageResource(R.drawable.hang10);
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
    public void guessBtnPressed(View view){
        String guess2;
        char guess;
        boolean correct = false;
        if (!guessField.getText().toString().equals("")){
            guess2 = guessField.getText().toString();
            guess = guessField.getText().charAt(0);
            if (isLetter(guess) && !isGuessed(guess) && isOneLetter(guess2)){
                guess = Character.toLowerCase(guess);
                for (int i = 0; i < mysteryWord.length; i++){
                    if (guess == randWord.charAt(i)){
                        mysteryWord[i] = guess;
                        correct = true;
                    }
                }
                if (correct){
                    toastHandler(1);
                }
                else{
                    toastHandler(2);
                    if (pastGuesses.length() > 0)
                        pastGuesses += ", " + guess;
                    else
                        pastGuesses += guess;
                    pastGuessField.setText(pastGuesses);
                    guesses--;
                    changeImage();
                }
            }
            else{
                if (!isLetter(guess))
                    toastHandler(3);
                else if (isGuessed(guess))
                    toastHandler(4);
                else if (!isOneLetter(guess2))
                    toastHandler(5);
            }
        }
        else
            toastHandler(6);

        afterEveryGuess();
        showResult();
    }

    private boolean isOneLetter(String guess2) {
        if (guess2.length() > 1)
            return false;
        else
            return true;
    }

    private boolean isGuessed(char c) {
        for (int i = 0; i < pastGuesses.length(); i++){
            if (pastGuesses.charAt(i) == c)
                return true;
        }
        return false;
    }

    /**
     * checks if input is a letter or not
     * @param c
     * @return true if input is a letter
     */
    public boolean isLetter(char c){
        if (Character.isDigit(c))
            return false;
        else if (Character.isLetter(c))
            return true;
        else
            return false;
    }

    private void afterEveryGuess(){
        triesLeft.setText(getString(R.string.tries) + " " + guesses);
        word.setText(mysteryWord, 0, mysteryWord.length);
        guessField.setText("");
        dismissKeyboard(this);
    }

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
        }
    }

    /**
     * Hides the keyboard after the guess button is pressed
     * @param activity
     */
    public void dismissKeyboard(Activity activity){
        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            mgr.hideSoftInputFromWindow(guessField.getWindowToken(), 0);
    }
}
