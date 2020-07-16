package com.dayat.submission3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SettingActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int notificationDaily = sharedPreferences.getInt("notification", 0);
        Switch aSwitch = findViewById(R.id.switch_daily_reminder);
        if (notificationDaily == 1){
            aSwitch.setChecked(true);
        }else {
            aSwitch.setChecked(false);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    setDailyReminderOn(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("notification",1);
                    editor.apply();
                    Toast.makeText(SettingActivity.this, getString(R.string.status_reminder_active), Toast.LENGTH_SHORT).show();
                }else {
                    setDailyReminderOff(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("notification",0);
                    editor.apply();
                    Toast.makeText(SettingActivity.this, getString(R.string.status_reminder_not_active), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void setDailyReminderOn(Context applicationContext) {
        Intent intent = new Intent(applicationContext, MyReceiver.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,9);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        AlarmManager alarmManager = (AlarmManager) applicationContext.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(applicationContext, 46, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        if (alarmManager != null){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        }
    }
    private void setDailyReminderOff(Context applicationContext) {
        Intent intent = new Intent(SettingActivity.this, MyReceiver.class);

        AlarmManager alarmManager = (AlarmManager) applicationContext.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(applicationContext, 46, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }
    }
}