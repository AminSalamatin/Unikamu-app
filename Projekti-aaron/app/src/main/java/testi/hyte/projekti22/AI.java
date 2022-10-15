package testi.hyte.projekti22;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Luokka AI, käsittelee logiikan, jolla unirytmi lasketaan ja päivät näytetään kaavassa
 * @author Amin
 */
public class AI {

    private LocalDate dateNow = LocalDate.now();
    private LocalTime timeNow = LocalTime.now();

    private double hoursOfSleep;
    private LocalTime whenSleep, whenWakeUp;
    private LocalDate currentDate;

    private List<AI> listForGraph;

    int daysTotal = 0;

    /**
     * Funktio, joka luo listan kutsuessa
     */
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

    /**
     * Funktio, joka räätälöi järjestyksen, jonka mukaan käyttäjän pitäisi nukkua
     * @param hoursOfSleep LocalTime muodossa aika tunneissa, joita on nukuttu
     * @param hoursToSleep LocalTime muodossa aika tunneissa, joita haluttaisiin nukkua
     * @param whenWakeUp LocalTime muodossa aika, jolloin on herätty
     * @param wakeUpGoal LocalTime muodossa aika, jolloin haluttaisiin herätä
     * @param currentDate LocalDate muodossa päivämäärä, josta aloitetaan järjestys
     */
    public void Schedule(LocalTime hoursOfSleep, LocalTime hoursToSleep, LocalTime whenWakeUp, LocalTime wakeUpGoal, LocalDate currentDate){

        double minsOfSleep = hoursOfSleep.getMinute()*1+(hoursOfSleep.getHour()*60);

        int i = 0;
        daysTotal = dayCounter(currentDate);

        while(i<daysTotal+7){

            listForGraph.add(new AI((minsOfSleep-(timeDiffInMins(hoursOfSleep,hoursToSleep, i)))/60, (whenWakeUp.minusMinutes((long)(timeDiffInMins(whenWakeUp,wakeUpGoal, i)))) , (whenWakeUp.minusMinutes((long)(((minsOfSleep-(timeDiffInMins(hoursOfSleep,hoursToSleep, i)))+timeDiffInMins(whenWakeUp,wakeUpGoal, i))))), currentDate.plusDays(i)));

            i++;

        }
    }

    /**
     * Funktio, joka laskee kaikki päivät syöttämästä päivästä nykyiseen. Palauttaa arvoksi menneet päivät(int)
     * @param firstDay LocalDate muodossa päivä, josta aloitetaan lasku
     * Huom! syöttämän päivän pitää olla ennen nykypäivää, muuten arvoksi tulee 1
     */
    public int dayCounter(LocalDate firstDay){
        int day= 1;
        LocalDate dayCount = firstDay;

        while(dayCount.isBefore(dateNow)){

            day++;
            dayCount = dayCount.plusDays(1);

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

    //Get -komennot, jotka palauttavat halutun arvon listan elementiltä

    /**
     * Double, joka palauttaa nukuttujen tuntien määrän järjestyksen listalta
     * @param i int mones päivä, josta halutaan nukutut tunnit
     */
    public double getHoursOfSleep(int i){
        return listForGraph.get(i).hoursOfSleep;
    }

    /**
     * String, joka palauttaa heräämisajan järjestyksen listalta
     * @param i int mones päivä, josta halutaan heräämisaika
     */
    public String getWhenWakeUp(int i){
        return listForGraph.get(i).whenWakeUp.toString();
    }

    /**
     * String, joka palauttaa nukkumaanmenoajan järjestyksen listalta
     * @param i int mones päivä, josta halutaan nukkumaanmenoaika
     */
    public String getWhenSleep(int i){
        return listForGraph.get(i).whenSleep.toString();
    }

    /**
     * String, joka palauttaa päivämäärän järjestyksen listalta muodossa "Viikonpäivä(3 kirjainta) Kuukausi(kaksi numeroa)"
     * @param i int mones päivä, josta halutaan päivämäärä
     */
    public String getCurrentDateFormatted(int i){
        return listForGraph.get(i).currentDate.format(DateTimeFormatter.ofPattern("EEE dd"));

    }

    /**
     * LocalDate, joka palauttaa päivämäärän järjestyksen listalta
     * @param i int mones päivä, josta halutaan päivämäärä
     */
    public LocalDate getCurrentDate(int i){
        return listForGraph.get(i).currentDate;

    }

}
