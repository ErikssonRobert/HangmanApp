package roberteriksson12.gmail.com.hangmanapp;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import roberteriksson12.gmail.com.hangmanapp.hangman.HangmanAdapter;

public class GameActivity extends AppCompatActivity {
    private HangmanAdapter hangmanAdapter;
    private EditText guessField;
    private ImageView hangmanImg;
    private TextView word;
    private TextView triesLeft;
    private TextView pastGuessField;
    private TypedArray images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setTitle(getString(R.string.gameTitle));
        hangmanAdapter = new HangmanAdapter(this);
        initialize();
        if (savedInstanceState != null)
            hangmanAdapter.restoreInformation(this, savedInstanceState, triesLeft, pastGuessField, word, hangmanImg, images);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        hangmanAdapter.saveInformation(outState);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialize(){
        guessField = (EditText)findViewById(R.id.guessField);
        hangmanImg = (ImageView)findViewById(R.id.hangmanImg);
        images = getResources().obtainTypedArray(R.array.images);
        word = (TextView)findViewById(R.id.hiddenWord);
        triesLeft = (TextView)findViewById(R.id.triesLeft);
        pastGuessField = (TextView)findViewById(R.id.pastGuesses);
        word.setText(hangmanAdapter.getMysteryWord());
        hangmanAdapter.changeImage(hangmanImg, images);
    }

    /**
     * Runs after guess button is pressed. Checks if guessed letter was correct or wrong.
     * @param view
     */
    public void guessBtnPressed(View view) {
        String guess = guessField.getText().toString().toLowerCase();
        hangmanAdapter.handleGuess(guess, pastGuessField, hangmanImg, images, triesLeft);
        hangmanAdapter.afterEveryGuess(word, guessField);
        showResult();
    }

    private void showResult(){
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("mystery", hangmanAdapter.getRandWord());
        intent.putExtra("tries", hangmanAdapter.getGuesses());
        if (hangmanAdapter.isGameWon()){
            intent.putExtra("result", "You won!");
            startActivity(intent);
            finish();
        }
        else if (hangmanAdapter.getGuesses() == 0){
            intent.putExtra("result", "You lost");
            startActivity(intent);
            finish();
        }
    }
}
