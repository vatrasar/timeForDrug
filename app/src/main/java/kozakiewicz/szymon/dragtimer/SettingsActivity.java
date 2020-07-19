package kozakiewicz.szymon.dragtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void onSaveSettings(View view) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        EditText txtHoursNumber=findViewById(R.id.txtHoursNumber);
        int hoursNumber=Integer.parseInt(txtHoursNumber.getText().toString());
        SharedPreferences.Editor editor= sharedPref.edit();
        editor.putInt("hoursNumber",hoursNumber);
        editor.commit();
        int timeInterval=0;
        timeInterval=sharedPref.getInt("hoursNumber",timeInterval);
        finish();
    }
}