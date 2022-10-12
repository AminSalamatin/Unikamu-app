package testi.hyte.projekti22;

import static java.time.temporal.ChronoUnit.MINUTES;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


//Tämä luokka käsittelee logiikan, jolla unirytmi lasketaan ja päivät näytetään kaavassa

public class AI {

    private LocalDate dateNow = LocalDate.now();
    private LocalTime timeNow = LocalTime.now();

    private double hoursOfSleep;
    private LocalTime whenSleep, whenWakeUp;
    private LocalDate currentDate;

    private List<AI> listForGraph;


    int daysTotal = 0;


    //Tämä kutsutaan
    public AI(){
        listForGraph = new ArrayList<>();

    }

    //Konstruktori kaavan elementeille
    private AI(double hoursOfSleep, LocalTime whenWakeUp, LocalTime whenSleep, LocalDate currentDate){

        this.hoursOfSleep = hoursOfSleep;
        this.whenSleep = whenSleep;
        this.whenWakeUp = whenWakeUp;
        this.currentDate = currentDate;

    }

    //Räätälöi listan päivistä
    private void graphBuilder(LocalTime hoursOfSleep, LocalTime hoursToSleep, LocalTime whenWakeUp, LocalTime wakeUpGoal, LocalDate currentDate){

        Log.d("MAIN_VALUES", "hoursOfSleep: " + hoursOfSleep + " hoursToSleep: "+ hoursToSleep+" whenWakeUp: "+whenWakeUp+" wakeUpGoal: "+wakeUpGoal+" currentDate: "+currentDate);


        double minsOfSleep = hoursOfSleep.getMinute()*1+(hoursOfSleep.getHour()*60);


        int i = 0;
        daysTotal = dayCounter(currentDate);

        float timeDiffNotNull = 1;

        if(timeDiffInMins(whenWakeUp,wakeUpGoal, 1) != 0){
            timeDiffNotNull = timeDiffInMins(whenWakeUp,wakeUpGoal, i);
        }

        while(i<daysTotal+7){


            listForGraph.add(new AI((minsOfSleep-(timeDiffInMins(hoursOfSleep,hoursToSleep, i)))/60, (whenWakeUp.minusMinutes((long)(timeDiffInMins(whenWakeUp,wakeUpGoal, i)))) , (whenWakeUp.minusMinutes((long)(((minsOfSleep-(timeDiffInMins(hoursOfSleep,hoursToSleep, i)))+timeDiffInMins(whenWakeUp,wakeUpGoal, i))))), currentDate.plusDays(i)));

            Log.d("TIMEDIFF_CHECK", "value: " + (whenWakeUp.minusMinutes((long)(timeDiffInMins(whenWakeUp,wakeUpGoal, i)))) + " No "+i);

            i++;


        }

    }

    //Järjestyksen logiikka käsitellään täällä
    public void Schedule(LocalTime hoursOfSleep, LocalTime hoursToSleep, LocalTime whenWakeUp, LocalTime wakeUpGoal, LocalDate date){

        graphBuilder(hoursOfSleep, hoursToSleep, whenWakeUp, wakeUpGoal, date);
    }

    //Laskee päiviä

    public int dayCounter(LocalDate firstDay){
        int day= 1;
        LocalDate dayCount = firstDay;

        while(dayCount.isBefore(dateNow)){

            day++;
            dayCount = dayCount.plusDays(1);

            Log.d("DAY_COUNT", "Day: " + day);
        }
        return day;

    }

    //Laskee aikavälin logiikan päivien välillä
    private float timeDiffInMins(LocalTime current, LocalTime goal, int modifier){
        float timeDiff, timeDiffLimit;


            timeDiff = ((current.getMinute() + current.getHour()*60 - goal.getMinute() - goal.getHour()*60) / 6)*(modifier);


            timeDiffLimit =  (current.getMinute() + current.getHour()*60 - goal.getMinute() - goal.getHour()*60);





        if(timeDiffLimit <= timeDiff){
            timeDiff = timeDiffLimit;
        }

        return timeDiff;
    }

    //Hakee koko listan
    public List<AI> getListForGraph() {
        return listForGraph;
    }

    //Pitää viikkovälin kaavaa varten
    public int weekRange(int today){
        int dayDiff;
        if(today>=7){
            dayDiff = 7;
        } else{

            dayDiff = today;
        }

        return dayDiff;
    }

    //Get -komennot, jotka palauttavat halutun arvon listan elementiltä
    public double getHoursOfSleep(int i){
        return listForGraph.get(i).hoursOfSleep;
    }

    public String getWhenWakeUp(int i){
        return listForGraph.get(i).whenWakeUp.toString();
    }

    public String getWhenSleep(int i){
        return listForGraph.get(i).whenSleep.toString();
    }

    public String getCurrentDateFormatted(int i){
        return listForGraph.get(i).currentDate.format(DateTimeFormatter.ofPattern("EEE dd"));

    }

    public LocalDate getCurrentDate(int i){
        return listForGraph.get(i).currentDate;

    }


    //Nykyinen päivämäärä ja aika
    public LocalDate getDateNow(){ return dateNow;}

    public LocalTime getTimeNow(){ return timeNow;}


}
