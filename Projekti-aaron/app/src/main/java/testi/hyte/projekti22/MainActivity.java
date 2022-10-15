package testi.hyte.projekti22;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * MainActivity, josta pääsee joko kaavaan tai kyselyyn (kutsuu kyselyn itse ensimmäisen kerran, kun sovellus käynnistetään)
 * @author Amin ja Aaron
 */
public class MainActivity extends AppCompatActivity {
    private int hoursOfSleep, hoursToSleep, whenWakeUp, wakeUpGoal;
    private final AI aiForAlarm= new AI();
    private String startingDate;

    boolean quizDone = false;

    private SharedPreferences preference;
    private SharedPreferences.Editor prefEditor;

    private int today;

    public static final String MESSAGE_HOURSOFSLEEP = "com.example.testi.hyte.projekti22.MESSAGE_HOURSOFSLEEP";
    public static final String MESSAGE_HOURSTOSLEEP = "com.example.testi.hyte.projekti22.MESSAGE_HOURSTOSLEEP";
    public static final String MESSAGE_WHENWAKEUP = "com.example.testi.hyte.projekti22.MESSAGE_WHENWAKEUP";
    public static final String MESSAGE_WAKEUPGOAL= "com.example.testi.hyte.projekti22.MESSAGE_WAKEUPGOAL";
    public static final String MESSAGE_STARTINGDATE= "com.example.testi.hyte.projekti22.MESSAGE_STARTINGDATE";

    //Tarkistaa, onko muuttujat tulleet kyselystä ja tallentaa ne SharedPreferenceen, sitten kutsuu herätyskellon
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        preference = getSharedPreferences("Pref", MainActivity.MODE_PRIVATE);
        prefEditor = preference.edit();

        quizDone = intent.getBooleanExtra(Quiz.MESSAGE_QUIZDONE, false);
        if (!quizDone){
            quizDone = preference.getBoolean("quizDone", false);
            if (!quizDone){
                Intent quiz = new Intent(this, Quiz.class);
                startActivity(quiz);
            }
        }

        hoursOfSleep = preference.getInt("hoursOfSleep", 0);
        hoursToSleep = preference.getInt("hoursToSleep", 0);
        whenWakeUp = preference.getInt("whenWakeUp", 0);
        wakeUpGoal = preference.getInt("wakeUpGoal", 0);
        startingDate = preference.getString("startDate", LocalDate.now().toString());

        if(hoursOfSleep == 0) {

            hoursOfSleep = intent.getIntExtra(Quiz.MESSAGE_HOURSOFSLEEP, 0);
            hoursToSleep = intent.getIntExtra(Quiz.MESSAGE_HOURSTOSLEEP, 0);
            whenWakeUp = intent.getIntExtra(Quiz.MESSAGE_WHENWAKEUP, 0);
            wakeUpGoal = intent.getIntExtra(Quiz.MESSAGE_WAKEUPGOAL, 0);
            startingDate = LocalDate.now().toString();
        }

        prefEditor.putInt("hoursOfSleep", hoursOfSleep);
        prefEditor.putInt("hoursToSleep", hoursToSleep);
        prefEditor.putInt("whenWakeUp", whenWakeUp);
        prefEditor.putInt("wakeUpGoal", wakeUpGoal);
        prefEditor.putString("startingDate", startingDate);
        prefEditor.putBoolean("quizDone", quizDone);
        prefEditor.apply();

        if(hoursOfSleep+hoursToSleep+whenWakeUp+wakeUpGoal != 0) {
            setAlarm();
        }
    }

    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor prefEditor = preference.edit();
        prefEditor.putInt("hoursOfSleep", hoursOfSleep);
        prefEditor.putInt("hoursToSleep", hoursToSleep);
        prefEditor.putInt("whenWakeUp", whenWakeUp);
        prefEditor.putInt("wakeUpGoal", wakeUpGoal);
        prefEditor.putString("startingDate", startingDate);
        prefEditor.apply();

    }

    /**
     * Funktio, joka aktivoi kaavan (GraphActivity), ja lähettää kaavaa varten tarvittavat muuttujat
     * @param v View, josta funktio aktivoituu
     */
    public void buttonPressed(View v){

        Intent intent = new Intent(this, GraphActivity.class);

        intent.putExtra(MESSAGE_HOURSOFSLEEP, hoursOfSleep);
        intent.putExtra(MESSAGE_HOURSTOSLEEP, hoursToSleep);
        intent.putExtra(MESSAGE_WHENWAKEUP, whenWakeUp);
        intent.putExtra(MESSAGE_WAKEUPGOAL, wakeUpGoal);
        intent.putExtra(MESSAGE_STARTINGDATE, startingDate);

        startActivity(intent);
    }

    /**
     * Funktio, joka tyhjentää muistin ja antaa käyttäjälle kyselyn uudestaan
     * @param v View, josta funktio aktivoituu
     */
    public void resetQuiz(View v){

        hoursOfSleep = 0;
        hoursToSleep = 0;
        whenWakeUp = 0;
        wakeUpGoal = 0;
        startingDate = LocalDate.now().toString();

        Intent quiz = new Intent(this, Quiz.class);
        startActivity(quiz);

    }

    //Asettaa herätyskellon(Yksi nukkumaanmenolle ja yksi herätykselle)
    private void setAlarm(){

        LocalTime zeroTime = LocalTime.MIN;

        aiForAlarm.Schedule(zeroTime.plusMinutes(hoursOfSleep), zeroTime.plusMinutes(hoursToSleep), zeroTime.plusMinutes(whenWakeUp),zeroTime.plusMinutes(wakeUpGoal), LocalDate.parse(startingDate).minusDays(0));

        today = aiForAlarm.dayCounter(aiForAlarm.getCurrentDate(0))-1;

        LocalTime wakeUpTime = LocalTime.parse(aiForAlarm.getWhenWakeUp(today));
        LocalTime sleepTime = LocalTime.parse(aiForAlarm.getWhenSleep(today));

        alarm(wakeUpTime.getHour(), wakeUpTime.getMinute(), false, "Herätys!");
        alarm(sleepTime.getHour(), sleepTime.getMinute(), true, "Nukkumaan!");

    }

    //Luo herätyskellon android-puhelimen omaan sovelluksen
    private void alarm(int hour, int minute, boolean silentAlarm, String title) {

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);

        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, title);

        if(silentAlarm) {
            intent.putExtra(AlarmClock.EXTRA_RINGTONE, "silent");
        }

        if (hour <= 24 && minute <= 60) {

            startActivity(intent);
        }
    }

}
