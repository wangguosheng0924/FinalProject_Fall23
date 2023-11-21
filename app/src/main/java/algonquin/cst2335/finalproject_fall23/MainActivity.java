package algonquin.cst2335.finalproject_fall23;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void onDictionaryButtonClick(View view){
        Intent intent = new Intent(this, DictionaryActivity.class);
        startActivity(intent);
    }
}