package testi.hyte.projekti22;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Color;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.time.LocalDate;
import java.time.LocalTime;


//Aktiviteetti, jossa tapahtuu kaavan piirto
public class GraphActivity extends AppCompatActivity {

    private final AI aiForGraph = new AI();

    private int days, weekMax;

    //Käynnistyessä ottaa MainActivitysta muuttujat, joilla luo kaavan taulukkoon
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Intent intent = getIntent();

        int hoursOfSleep = intent.getIntExtra(MainActivity.MESSAGE_HOURSOFSLEEP, 0);
        int hoursToSleep = intent.getIntExtra(MainActivity.MESSAGE_HOURSTOSLEEP, 0);
        int whenWakeUp = intent.getIntExtra(MainActivity.MESSAGE_WHENWAKEUP, 0);
        int wakeUpGoal = intent.getIntExtra(MainActivity.MESSAGE_WAKEUPGOAL, 0);

        String startingDate = intent.getStringExtra(MainActivity.MESSAGE_STARTINGDATE);

        LocalTime zeroTime = LocalTime.MIN;

        aiForGraph.Schedule(zeroTime.plusMinutes(hoursOfSleep), zeroTime.plusMinutes(hoursToSleep), zeroTime.plusMinutes(whenWakeUp),zeroTime.plusMinutes(wakeUpGoal), LocalDate.parse(startingDate).minusDays(0));

        days = aiForGraph.dayCounter(aiForGraph.getCurrentDate(0));

        weekMax = aiForGraph.weekRange(days);

        drawGraph();

        setTextUi(days-1);

    }

    //Asettaa päivät kaavaa varten
    private String[] setDays(){
        String[] dayString = new String[7];
        int i=0;
        while (7 > i){
            dayString[i] = " "+aiForGraph.getCurrentDateFormatted(i)+" ";
            i++;

        }
        return dayString;

    }

    //Funktio vastaa kaavan piirtämisestä
    private void drawGraph(){

        DataPoint[] DataPoints= new DataPoint[weekMax];

        GraphView graph = findViewById(R.id.graph);

        for (int i = 0; i < weekMax;i++){

            DataPoints[i] = new DataPoint(i+1, aiForGraph.getHoursOfSleep(days-weekMax+i));

        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(DataPoints);

        graph.setTitle("Tunteja nukuttu/Päivä");

        graph.addSeries(series);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setColor(Color.rgb(100,0,250));

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);

        staticLabelsFormatter.setVerticalLabels(new String[] {"0h", "1h", "2h","3h","4h","5h", "6h", "7h","8h","9h","10h", "11h", "12h","13h","14h","15h","16h","17h","18h","19h","20h","21h","22h","23h","24h"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(24);

        staticLabelsFormatter.setHorizontalLabels(setDays());
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(7);

        graph.getGridLabelRenderer().setTextSize(20);
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(25);
        graph.getGridLabelRenderer().reloadStyles();
    }


    //Asettaa tekstin kaavan alapuolelle, jossa seuraava nukkumaanmeno ja herätys
    private void setTextUi(int day){

        TextView wakeUpText = (TextView)findViewById(R.id.textView1);
        TextView sleepText = (TextView)findViewById(R.id.textView2);

        wakeUpText.setText("Seuraava herätys klo: "+aiForGraph.getWhenWakeUp(day));
        sleepText.setText("Seuraava nukkumaan meno klo: "+aiForGraph.getWhenSleep(day));

    }



}
