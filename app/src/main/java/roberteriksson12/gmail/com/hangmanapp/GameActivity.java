package roberteriksson12.gmail.com.hangmanapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    private Random random;
    private TextView word;
    private String randWord;
    private TextView triesLeft;
    private int guesses;
    private String pastGuesses;
    private TextView pastGuessField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setTitle("Hangman game");
        guessField = (EditText)findViewById(R.id.guessField);
        hangmanImg = (ImageView)findViewById(R.id.hangmanImg);
        hangmanImg.setImageResource(R.drawable.hang10);
        word = (TextView)findViewById(R.id.hiddenWord);
        triesLeft = (TextView)findViewById(R.id.triesLeft);
        pastGuessField = (TextView)findViewById(R.id.pastGuesses);
        guesses = 10;
        pastGuesses = "";
        random = new Random();
        createWords();  //puts a number of strings into the string array words
        randWord = words[random.nextInt(7)];
        mysteryWord = new char[randWord.length()];
        //for (int i = 0; i < randWord.length(); i++)
            //tempString += randWord.charAt(i);
        for (int i = 0; i < mysteryWord.length; i++){
            mysteryWord[i] = '*';
        }
        word.setText(mysteryWord, 0, mysteryWord.length);
    }

    private void createWords() {
        words = new String[7];
        words[0] = getString(R.string.word1);
        words[1] = getString(R.string.word2);
        words[2] = getString(R.string.word3);
        words[3] = getString(R.string.word4);
        words[4] = getString(R.string.word5);
        words[5] = getString(R.string.word6);
        words[6] = getString(R.string.word7);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

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
        char guess = guessField.getText().charAt(0);
        boolean correct = false;
        if (isLetter(guess)){
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
            toastHandler(3);
        }
        afterEveryGuess();
        showResult();
    }

    private boolean isLetter(char c){
        if (Character.isDigit(c))
            return false;

        return true;
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
        switch (guesses){
            case 9:
                hangmanImg.setImageResource(R.drawable.hang9);
                break;
            case 8:
                hangmanImg.setImageResource(R.drawable.hang8);
                break;
            case 7:
                hangmanImg.setImageResource(R.drawable.hang7);
                break;
            case 6:
                hangmanImg.setImageResource(R.drawable.hang6);
                break;
            case 5:
                hangmanImg.setImageResource(R.drawable.hang5);
                break;
            case 4:
                hangmanImg.setImageResource(R.drawable.hang4);
                break;
            case 3:
                hangmanImg.setImageResource(R.drawable.hang3);
                break;
            case 2:
                hangmanImg.setImageResource(R.drawable.hang2);
                break;
            case 1:
                hangmanImg.setImageResource(R.drawable.hang1);
                break;
        }
    }

    private void showResult(){
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("mystery", randWord);
        intent.putExtra("tries", guesses);
        if (isGameWon()){
            intent.putExtra("result", "You won!");
            startActivity(intent);
        }
        else if (guesses == 0){
            intent.putExtra("result", "You lost");
            startActivity(intent);
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
