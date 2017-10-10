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
        guesses = 10;
        random = new Random();
        words = new String[5];
        words[0] = "Variable";
        words[1] = "Method";
        words[2] = "Constructor";
        words[3] = "String";
        words[4] = "Integer";
        randWord = words[random.nextInt(5)];
        mysteryWord = new char[randWord.length()];
        //for (int i = 0; i < randWord.length(); i++)
            //tempString += randWord.charAt(i);
        for (int i = 0; i < mysteryWord.length; i++){
            mysteryWord[i] = '*';
            word.setText(mysteryWord, 0, mysteryWord.length);
        }
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

    public void guessBtnPressed(View view){
        char guess = guessField.getText().charAt(0);
        boolean correct = false;
        for (int i = 0; i < mysteryWord.length; i++){
            if (guess == randWord.charAt(i)){
                mysteryWord[i] = guess;
                correct = true;
            }
        }

        if (correct){
            Toast toast = Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT);
            toast.show();
            guesses--;
        }
        triesLeft.setText(getString(R.string.triesleft) + " " + guesses);
        word.setText(mysteryWord, 0, mysteryWord.length);
        guessField.setText("");
        dismissKeyboard(this);
    }

    public void dismissKeyboard(Activity activity){
        InputMethodManager mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            mgr.hideSoftInputFromWindow(guessField.getWindowToken(), 0);
    }
}
