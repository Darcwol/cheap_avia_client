package jokidark.cheapavia.models;

import android.support.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Date {
    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Date(String date){
        Pattern yearPattern = Pattern.compile("\\d{4}");
        Matcher yearMatcher = yearPattern.matcher(date);
        Pattern dayPattern = Pattern.compile("\\d{1,2}-");
        Matcher dayMatcher = dayPattern.matcher(new StringBuilder(date).reverse());
        Pattern monthPattern = Pattern.compile("-\\d{1,2}-");
        Matcher monthMatcher = monthPattern.matcher(date);

        if (yearMatcher.find() && monthMatcher.find() && dayMatcher.find()) {
            int year = Integer.parseInt(yearMatcher.group());
            int month = Integer.parseInt(monthMatcher.group().substring(1, monthMatcher.group().length()-1));
            int day = Integer.parseInt(
                    new StringBuilder(dayMatcher.group().substring(0, dayMatcher.group().length()-1)).reverse().toString()
            );

            this.year = year;
            this.month = month;
            this.day = day;
        }
    }

    public Date(String date, boolean humanDate){
        if(humanDate){
            Pattern yearPattern = Pattern.compile("\\d{4}");
            Matcher yearMatcher = yearPattern.matcher(date);
            Pattern dayPattern = Pattern.compile("\\d{1,2}\\s");
            Matcher dayMatcher = dayPattern.matcher(date);
            Pattern monthPattern = Pattern.compile("\\s\\w{3}\\.?\\s");
            Matcher monthMatcher = monthPattern.matcher(date);

            if (yearMatcher.find() && monthMatcher.find() && dayMatcher.find()) {
                int year = Integer.parseInt(yearMatcher.group());
                String month_str = monthMatcher.group();
                int month = 0;
                switch (month_str){
                    case " Jan. ":
                        month = 1;
                        break;
                    case " Feb. ":
                        month = 2;
                        break;
                    case " Mar. ":
                        month = 3;
                        break;
                    case " Apr. ":
                        month = 4;
                        break;
                    case " May ":
                        month = 5;
                        break;
                    case " Jun. ":
                        month = 6;
                        break;
                    case " Jul. ":
                        month = 7;
                        break;
                    case " Aug. ":
                        month = 8;
                        break;
                    case " Sep. ":
                        month = 9;
                        break;
                    case " Oct. ":
                        month = 10;
                        break;
                    case " Nov. ":
                        month = 11;
                        break;
                    case " Dec. ":
                        month = 12;
                        break;
                }
                int day = Integer.parseInt(dayMatcher.group().substring(0, dayMatcher.group().length()-1));

                this.year = year;
                this.month = month;
                this.day = day;
            }
        }
    }

    private int year;
    private int month;
    private int day;

    @NonNull
    @Override
    public String toString() {
        return year+"-"+month+"-"+day;
    }

    public String toHumanString(){
        String month;
        switch (this.month) {
            case 1: month = " Jan. ";
                break;
            case 2: month = " Feb. ";
                break;
            case 3: month = " Mar. ";
                break;
            case 4: month = " Apr. ";
                break;
            case 5: month = " May ";
                break;
            case 6: month = " Jun. ";
                break;
            case 7: month = " Jul. ";
                break;
            case 8: month = " Aug. ";
                break;
            case 9: month = " Sep. ";
                break;
            case 10: month = " Oct. ";
                break;
            case 11: month = " Nov. ";
                break;
            case 12: month = " Dec. ";
                break;
                default: month = "";
        }
        return day + month + year;
    }
}
