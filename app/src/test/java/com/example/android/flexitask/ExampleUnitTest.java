package com.example.android.flexitask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.provider.CalendarContract;

import com.example.android.flexitask.data.taskContract;

import org.junit.Test;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

    }



    /**Tests the date checker in task cursor adaptor that determines if a task is due or overdue*/
    @Test
    public void testTaskCursorAdapterNotverDue(){

        Calendar todayDateC = Calendar.getInstance();
        long todayDate = todayDateC.getTimeInMillis();
        Calendar dueDateC = (Calendar) todayDateC.clone();
        //due IN 7 days
        dueDateC.add(Calendar.DAY_OF_MONTH,7);
        dueDateC.add(Calendar.MINUTE,-1);
        String expectedMessage = "Due in 7 days";
        long dueDate = dueDateC.getTimeInMillis();

        String datemessage;

        if (dueDate >= todayDate) {
            long differenceMillisecond = dueDate - todayDate + 1;
            long differenceDays = TimeUnit.MILLISECONDS.toDays(differenceMillisecond) + 1;
            datemessage = "Due in " + String.valueOf(differenceDays);

            if (differenceDays == 0) {
                datemessage = "DUE TODAY!";
            } else if (differenceDays != 1) {
                datemessage += " days";
            } else {
                datemessage += " day";
            }
        } else {

            long differenceMillisecond = todayDate - dueDate;
            long differenceDays = TimeUnit.MILLISECONDS.toDays(differenceMillisecond)+1;
            datemessage = "Overdue by " + String.valueOf(differenceDays);
            if (differenceDays == 0) {
                datemessage = "DUE TODAY!";
            } else if (differenceDays != 1) {
                datemessage += " days";
            } else {
                datemessage += " day";
            }
        }

        assertEquals("Overdue method not working",datemessage,expectedMessage);

    }


    /**Tests the date checker in task cursor adaptor that determines if a task is due or overdue*/
    @Test
    public void testTaskCursorAdapterOverdue(){

        Calendar todayDateC = Calendar.getInstance();
        long todayDate = todayDateC.getTimeInMillis();
        Calendar dueDateC = (Calendar) todayDateC.clone();
        //over due by 7 days
        dueDateC.add(Calendar.DAY_OF_MONTH,-7);
        dueDateC.add(Calendar.MINUTE,-1);
        String expectedMessage = "Overdue by 7 days";
        long dueDate = dueDateC.getTimeInMillis();

        String datemessage;

        if (dueDate >= todayDate) {
            long differenceMillisecond = dueDate - todayDate + 1;
            long differenceDays = TimeUnit.MILLISECONDS.toDays(differenceMillisecond) + 1;
            datemessage = "Due in " + String.valueOf(differenceDays);

            if (differenceDays == 0) {
                datemessage = "DUE TODAY!";
            } else if (differenceDays != 1) {
                datemessage += " days";
            } else {
                datemessage += " day";
            }
        } else {

            long differenceMillisecond = todayDate - dueDate;
            long differenceDays = TimeUnit.MILLISECONDS.toDays(differenceMillisecond)+1;
            datemessage = "Overdue by " + String.valueOf(differenceDays);
            if (differenceDays == 0) {
                datemessage = "DUE TODAY!";
            } else if (differenceDays != 1) {
                datemessage += " days";
            } else {
                datemessage += " day";
            }
        }

        assertEquals("Overdue method not working",datemessage,expectedMessage);

    }




    /**Tests the priority checker in task cursor adaptor that determines a flexi task's priority*/
    @Test
    public void testTaskCursorAdapterFlexiPriority(){

        long dateLong = 0;
        Calendar cTodayDate = Calendar.getInstance();

        //sets lastcompleted day for a week ago

        cTodayDate.set(Calendar.HOUR_OF_DAY, 0);
        cTodayDate.set(Calendar.MINUTE, 0);
        cTodayDate.set(Calendar.SECOND, 0);
        cTodayDate.set(Calendar.MILLISECOND, 0);

        Calendar lastCompletedC = (Calendar) cTodayDate.clone();
        lastCompletedC.add(Calendar.WEEK_OF_YEAR,-1);
        long lastCompletedLong = lastCompletedC.getTimeInMillis();

        long todayDate = cTodayDate.getTimeInMillis();
        //recurring period = everyday
        int recurringPeriod = 1;

        long daysSinceTaskLastCompleted = ((todayDate - lastCompletedLong) / 86400000L) + 1;

        double priorityRating = (daysSinceTaskLastCompleted / (double) recurringPeriod);


        //log for testing purposes


        //if 75% until due,
        if (priorityRating < 0.75) {

            assertTrue(false);

        }
        //if between 75% and 150%
        else if (priorityRating > 0.75 && priorityRating < 1.5) {

            assertTrue(false);

        }
        //if between over 150%, (URGENT) priority. Because task is daily and is one week overdue
        //it is urgent
        else {

            assertTrue(true);

        }
    }




    /**Tests the alarm setter method in the flexitask editor*/
    @Test
    public void testReminderSetter(){

        Calendar reminderCalander = Calendar.getInstance();
        Calendar secondCalender = (Calendar) reminderCalander.clone();
        String mReminderUnit ="4";
        String mReminderUnitBefore = "Hours before";
        secondCalender.add(Calendar.HOUR_OF_DAY, -Integer.valueOf(mReminderUnit));
        long expectedResult = secondCalender.getTimeInMillis();

            //Makes sure there is a valid reminder date
            if (!(mReminderUnitBefore.equals("") || mReminderUnit.equals(""))) {

                switch (mReminderUnitBefore) {
                    case ("Minutes before"):
                        reminderCalander.add(Calendar.MINUTE, -Integer.valueOf(mReminderUnit));
                        break;
                    case ("Hours before"):
                        reminderCalander.add(Calendar.HOUR_OF_DAY, -Integer.valueOf(mReminderUnit));
                        break;
                    case ("Days before"):
                        reminderCalander.add(Calendar.DAY_OF_YEAR, -Integer.valueOf(mReminderUnit));
                        break;

                    case ("Weeks before"):
                        reminderCalander.add(Calendar.WEEK_OF_YEAR, -Integer.valueOf(mReminderUnit));
                        break;
                }
                assertEquals ("Alarm not set correctly",expectedResult,reminderCalander.getTimeInMillis());
            }
        }
    }
