package com.personal.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public  final class DateUtil {

    /*截取日期数组*/
    public static List<String> getDateList(Date start, Date end) {
        String kprqqTYPE;
        String kprqzTYPE;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //【date区间查询数数组】
        List<String> dateListString = new ArrayList<>();
        if(start.after(end)){ //前后顺序自动转换
            Date k = start;
            start = end;
            end = k;
        }
        if (!StringUtil.isNullOrEmpty(start) && !StringUtil.isNullOrEmpty(end)) {
            kprqqTYPE = format.format(start);
            kprqzTYPE = format.format(end);
            try {
                dateListString = getDayByDay(kprqqTYPE, kprqzTYPE);//Days 顺序数组
            } catch (ParseException e) {
            }
        } else {
            dateListString.add(format.format(new Date()));
        }
        return dateListString;
    }
    private static List<String> getDayByDay(String startDate, String endDate) throws ParseException {
        List<String> days = new ArrayList<>();
        DateFormat dateFormatd = new SimpleDateFormat("yyyy-MM-dd");
        Date start = dateFormatd.parse(startDate);
        Date end = dateFormatd.parse(endDate);
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        tempEnd.add(Calendar.DATE, +1);
        while (tempStart.before(tempEnd)) {
            days.add((dateFormatd.format(tempStart.getTime())));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return days;
    }
    public static Date parseDate(String date){
        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd");
        try{
            return df.parse(date);
        }catch (Exception e){
            return new Date();
        }
    }
}
