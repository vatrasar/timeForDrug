package kozakiewicz.szymon.dragtimer;

import androidx.appcompat.app.AppCompatActivity;

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
        EditText txtHoursNumber=findViewById(R.id.txtHoursNumber);
       txtHoursNumber.setText("3");
    }

    public void onSaveSettings(View view) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        EditText txtHoursNumber=findViewById(R.id.txtHoursNumber);
        EditText txtMinutesNumber=findViewById(R.id.txtMinutesNumber);
        EditText txtSecoundsNumber=findViewById(R.id.txtSecounds);
        int hoursNumber=Integer.parseInt(txtHoursNumber.getText().toString());
        int minutesNumber=Integer.parseInt(txtMinutesNumber.getText().toString());
        int secoundsNumber=Integer.parseInt(txtSecoundsNumber.getText().toString());
        int timeInterval=1000*(minutesNumber*60+hoursNumber*60*60+secoundsNumber);

        SharedPreferences.Editor editor= sharedPref.edit();
        editor.putLong("timeInterval",timeInterval);
        editor.commit();

        finish();
    }
}