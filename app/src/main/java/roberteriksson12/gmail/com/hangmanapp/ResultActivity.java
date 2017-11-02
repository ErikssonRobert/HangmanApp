package roberteriksson12.gmail.com.hangmanapp;

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle(getString(R.string.resultTitle));
        Intent intent = getIntent();
        showResult(intent);
        ImageView hangmanImage = (ImageView) findViewById(R.id.hangedMan);
        if (intent.getStringExtra("result").contains("lost"))
            hangmanImage.setImageResource(R.drawable.hang0);
    }

    public void returnToMenu(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showResult(Intent intent){
        TextView result = (TextView) findViewById(R.id.resultText);
        result.setText(intent.getStringExtra("result"));
        TextView mysteryWord = (TextView) findViewById(R.id.mysteryWord);
        String revealWord = getString(R.string.reveal) + intent.getStringExtra("mystery").toUpperCase();
        mysteryWord.setText(revealWord);
        TextView triesLeft = (TextView)findViewById(R.id.triesLeft);
        triesLeft.setText(getString(R.string.tries_left) + " " + intent.getIntExtra("tries", 0));
    }

    /**
     * Inflates toolbar to fit icons
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
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

        switch (id){
            case R.id.playTlbBtn:
                Intent game = new Intent(this, GameActivity.class);
                startActivity(game);
                break;
            case R.id.infoTlbBtn:
                Intent info = new Intent(this, InfoActivity.class);
                startActivity(info);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
