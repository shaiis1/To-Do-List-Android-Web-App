package com.shaybar.todolist.utils;

import com.google.gson.Gson;
import com.shaybar.todolist.entities.Task;
import com.shaybar.todolist.services.ExceptionMessage;
import com.shaybar.todolist.services.Message;
import com.shaybar.todolist.services.MessageType;
import com.shaybar.todolist.services.MessagesFactory;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppUtils {
    public static Gson gson = new Gson();

    /**
     * Convert object to json string
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * read json string into java object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * parse String date with his given format to Long
     */
    public static Long parseDate(String date, String format) throws ExceptionMessage {
        SimpleDateFormat f = new SimpleDateFormat(format);
        try {
            Date d = f.parse(date);
            return d.getTime();
        } catch (ParseException e) {
            throw new ExceptionMessage(new Message(MessageType.ERROR, MessagesFactory.INVALID_VALUE_IN_FORM, Task.DATE, format));
        }
    }

    /**
     * convert bits array to decimal number
     */
    public static Integer bitsToDecimal(boolean[] bits) {
        int result = 0;
        for (int i = 6; i >= 0; i--) {
            result = result * 2 + (bits[i] ? 1 : 0);
        }

        return result;
    }

    /**
     * convert decimal number to bits array
     */
    public static boolean[] decimalTobits(int decimal) {
        boolean[] bits = new boolean[7];
        for (int i = 6; i >= 0; i--) {
            bits[i] = (decimal & (1 << i)) != 0;
        }

        return bits;
    }

    /**
     * get current date as Long
     */
    public static Long getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}
