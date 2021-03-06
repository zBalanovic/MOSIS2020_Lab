package zeljkobalanovic.mosis.elfak.rs;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.strictmode.IntentReceiverLeakedViolation;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        if(!sharedPreferences.getBoolean("isLogged", false)){
            Intent i = new Intent(MainActivity.this, GetStartedActivity.class);
            startActivity(i);
            finish();
        }
        //Koristimo broadcast da bi zatvorili (finish) sve Avtivity-je koji su bili otvoreni a ne bis smeli ostati kad se izlogujemo
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ACTION_LOGOUT");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        }, intentFilter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, EditMyPlaceActivity.class);
                startActivityForResult(i, NEW_PLACE);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    static int NEW_PLACE = 1;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.show_map_item) {
            Intent i = new Intent(this, MyPlacesMapsActivity.class);
            i.putExtra("state", MyPlacesMapsActivity.SHOW_MAP);
            startActivity(i);
        } else if(id == R.id.new_place_item){
            Intent i = new Intent(this, EditMyPlaceActivity.class);
            startActivityForResult(i, NEW_PLACE);
        } else if(id == R.id.my_places_list_item){
            Intent i = new Intent(this, MyPlacesList.class);
            startActivity(i);
        } else if(id == R.id.about_item){
            Intent i = new Intent(this, About.class);
            startActivity(i);
        } else if(id == R.id.logout_item){
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("ACTION_LOGOUT");
            sendBroadcast(broadcastIntent);
            sharedPreferences.edit().putBoolean("isLogged", false).apply();
            Intent i = new Intent(MainActivity.this, GetStartedActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "New Place Added", Toast.LENGTH_SHORT).show();
        }
    }
}
