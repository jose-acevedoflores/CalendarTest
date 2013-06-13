package msrp.aida.calendartest;

import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView tvMain = (TextView) findViewById(R.id.tvMain);

		this.getCalendars();

		tvMain.setText("Woot Woot");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@SuppressLint("NewApi")
	private void getCalendars()
	{
		String projection[] = {
				Calendars._ID,
				Calendars.CALENDAR_DISPLAY_NAME,
				Calendars.VISIBLE
		};

		final int PROJECTION_CALENDAR_ID = 0;
		final int PROJECTION_CALENDAR_DISP_NAME = 1;

		//This line solicits the ContentProvider manager for the calendars table {Calendars.CONTENT_URI} 

		// Projection tells the query that we only want the column with: calendar Name and calendar display name 
		Cursor c  = getContentResolver().query(Calendars.CONTENT_URI, projection, null, null, null);
		
		// Boundaries for next event look up
		long currentTime = new Date().getTime();
		long topTime = currentTime +  6 * DateUtils.HOUR_IN_MILLIS;

		while(c.moveToNext())
		{
			String str = c.getString(PROJECTION_CALENDAR_ID);
			if(str != null)
				System.out.println("ID "+str);

			str = c.getString(PROJECTION_CALENDAR_DISP_NAME);
			if(str != null)
				System.out.println("Display name "+str);

			str = c.getString(2);
			if(str != null)
				System.out.println("Visible "+str);				
			
		}
		
		Uri.Builder uriBuilder = Uri.parse("content://com.android.calendar/instances/when").buildUpon();
		ContentUris.appendId(uriBuilder, currentTime);
		ContentUris.appendId(uriBuilder, topTime);				
		
		Cursor eventCursor = getContentResolver().query(uriBuilder.build(),
				new String[] { CalendarContract.Events.TITLE,
			CalendarContract.Events.DTSTART, 
			CalendarContract.Events.DESCRIPTION,
			CalendarContract.Events.EVENT_LOCATION,
			CalendarContract.Events.ACCOUNT_NAME,
			CalendarContract.Events.CALENDAR_ID}, 
			null  ,
			null, null); 

		System.out.println("-------------------------");
		while(eventCursor.moveToNext())
		{
			System.out.println("Title "  + eventCursor.getString(0));
			System.out.println("Calendar ID " + eventCursor.getString(5));
			System.out.println("Account name " + eventCursor.getString(4));
			System.out.println("-------------------------");
		}
		
		
		System.out.println("END");
	}

}
