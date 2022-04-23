package pan.bo.yu.my_2022_04_20_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JumpTurn();
        JumpTurn();JumpTurn();JumpTurn();JumpTurn();JumpTurn();

    }

    private void JumpTurn() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    startActivity(new Intent().setClass(MainActivity.this, MainActivity2.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}