package peem.michaelkim.padevomaterialmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class homescreen extends AppCompatActivity {

    // Widget declarations.
    Button toMonsterBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        toMonsterBox = (Button) findViewById(R.id.toMonsterBox);
        toMonsterBox.bringToFront();

        // When the monster box button is pressed, proceed to the monster box screen.
        toMonsterBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), monsterBoxScreen.class));
            }
        });
    }
}
