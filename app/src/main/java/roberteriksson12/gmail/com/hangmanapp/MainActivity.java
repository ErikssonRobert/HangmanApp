package roberteriksson12.gmail.com.hangmanapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.mainMenu);
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

    public void playBtnPressed(View view){
        Intent game = new Intent(this, GameActivity.class);
        startActivity(game);
    }

    public void aboutBtnPressed(View view){
        Intent about = new Intent(this, AboutActivity.class);
        startActivity(about);
    }
}
