package testi.hyte.projekti22;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Kyselyaktiviteetti, jossa käyttäjä valitsee ajat sovellksen toimintaa varten
 * @author Amin
 */
public class Quiz extends AppCompatActivity {

    private TimePicker picker;
    private TextView textView;
    private Button button;

    private Intent intent;

    private int i=0;

    public static final String MESSAGE_HOURSOFSLEEP = "com.example.testi.hyte.projekti22.MESSAGE_HOURSOFSLEEP";
    public static final String MESSAGE_HOURSTOSLEEP = "com.example.testi.hyte.projekti22.MESSAGE_HOURSTOSLEEP";
    public static final String MESSAGE_WHENWAKEUP = "com.example.testi.hyte.projekti22.MESSAGE_WHENWAKEUP";
    public static final String MESSAGE_WAKEUPGOAL= "com.example.testi.hyte.projekti22.MESSAGE_WAKEUPGOAL";
    public static final String MESSAGE_QUIZDONE= "com.example.testi.hyte.projekti22.MESSAGE_QUIZDONE";

    //Asettaa alkumuuttujat kyselylle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        intent = new Intent(this, MainActivity.class);

        picker = (TimePicker)findViewById(R.id.timePicker1);
        textView = (TextView)findViewById(R.id.textView1);
        button = (Button) findViewById(R.id.button);
        picker.setIs24HourView(true);
        textView.setText("Monta tuntia olet nukkunut tänään?");
        button.setText("Jatka");

    }

    /**
     * Lähettää kyselystä saadut tiedot eteenpäin ja sulkee kyselyn
     * @param v View, josta funktio aktivoituu
     */
    public void sendInfo(View v){

        switch (i){

            case 0: intent.putExtra(MESSAGE_HOURSOFSLEEP, ((picker.getHour()*60)+picker.getMinute()));
                textView.setText("Monta tuntia haluaisit nukkua päivässä?");
                i++;
                break;
            case 1: intent.putExtra(MESSAGE_HOURSTOSLEEP, ((picker.getHour()*60)+picker.getMinute()));
                textView.setText("Monelta heräsit tänään?");
                button.setText("Jatka");
                i++;
                break;
            case 2: intent.putExtra(MESSAGE_WHENWAKEUP, ((picker.getHour()*60)+picker.getMinute()));
                textView.setText("Monelta haluaisit herätä joka päivä?");
                button.setText("Valmis");
                i++;
                break;
            case 3: intent.putExtra(MESSAGE_WAKEUPGOAL, ((picker.getHour()*60)+picker.getMinute()));
                intent.putExtra(MESSAGE_QUIZDONE, true);
                startActivity(intent);
                break;

        }
    }
}
