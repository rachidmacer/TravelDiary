package com.example.kellihe_emil.traveldiary;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ListView;
import android.app.ActionBar;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Locale;

public class TravelLog extends Activity implements OnItemClickListener, OnInitListener {

    private ListView listview;
    private ArrayList<String> items = new ArrayList<String>();
    private EditText edittext;
    private TextView textview;
    private ArrayAdapter<String> aa;
    private TextToSpeech speaker;
    private int selected;
    private int index;
    private String fileName;
    private static final String tag = "TravelLog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_log);

        listview = (ListView)findViewById(R.id.list);
        listview.setOnItemClickListener(this);  //set listener on widget
        edittext = (EditText)findViewById(R.id.edit);
        textview = (TextView)findViewById(R.id.text2);
        aa = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,    //Android supplied List item format
                items);

        listview.setAdapter(aa);

        //hide title and icon in action bar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);

        //Initialize Text to Speech engine (context, listener object)
        speaker = new TextToSpeech(this, this);

        fileName = "travel.txt";

        try {
            InputStream in = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(isr);
            String str;

            while ((str = reader.readLine()) != null) {
                items.add(str);
                index = items.indexOf(str); // find position in ArrayList
                String newItem = (index + 1) + ". " + str;
                items.set(index, newItem); // add item with number
                aa.notifyDataSetChanged();
            }
            reader.close();
        } catch (IOException e) {}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu); // inflate menu
        return true;
    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        String text = items.get(position);
        int space = text.indexOf(" ");
        String sub = text.substring(space+1);
        edittext.setText(text); // when you click on a list item, it displays text in the edittext
        textview.setText(sub); // when you click on a list item, it adds to the text view for later use
        selected = position; // for later use
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.Day:
                // add new day to listview
                String toAdd = "Day: " + edittext.getText().toString();
                items.add(toAdd);
                //index = items.indexOf(toAdd); // find position in ArrayList
                //String newItem = (index+1) +". " + toAdd;
                //items.set(index, newItem); // add item with number
                aa.notifyDataSetChanged();
                edittext.setText(""); // set edittext to empty after add
                speak(toAdd + " added.");
                return true;

            case R.id.Hotel:
                //add new hotel to listview
                String hotel = "Hotel: " + edittext.getText().toString();
                items.add(hotel);
                aa.notifyDataSetChanged();
                edittext.setText("");
                speak(hotel + " added.");
                return true;

            case R.id.Transport:
                //add new transport to listview
                String transport = "Transport: " + edittext.getText().toString();
                items.add(transport);
                aa.notifyDataSetChanged();
                edittext.setText("");
                speak(transport + " added.");
                return true;

            case R.id.Attraction:
                //add new attraction to listview
                String attract = "Attraction: " + edittext.getText().toString();
                items.add(attract);
                aa.notifyDataSetChanged();
                edittext.setText("");
                speak(attract + " added.");
                return true;

            case R.id.delete:
                // delete item from list
                String del = edittext.getText().toString();
                //String toDelete = (selected+1) + ". " + del;
                index = items.indexOf(del);
                items.remove(index);
                /*for(String s:items) {
                    int pos = items.indexOf(s); // get the position of the item in the ArrayList
                    int sp = s.indexOf(" "); // get the index of the space for that item
                    String i = s.substring(sp+1); // get rid of the numbering
                    String newNum = (pos+1) + ". " + i; // add back new numbering
                    items.set(pos,newNum); // update ArrayList with new numbers
                }*/
                aa.notifyDataSetChanged();
                edittext.setText(""); // set edittext to empty after delete
                speak(del + " deleted.");
                return true;

            case R.id.update:
                // update list item
                String toUpdate = edittext.getText().toString();
                //String update = (selected+1) + ". " + toUpdate;
                items.set(selected, toUpdate);
                aa.notifyDataSetChanged();
                edittext.setText(""); // set edittext to empty after update
                return true;

            case R.id.save:
                // save list to list.txt
                saveList();
                return true;

            case R.id.close:
                // save list and exit app
                saveList();
                finish();
                return true;

            default: super.onOptionsItemSelected(item);
        }
        return false;
    }

    public void saveList() {
        // save list to list.txt
        try {
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            OutputStreamWriter out = new OutputStreamWriter(fos);
            for (String s:items) {
                int space = s.indexOf(" ");
                String i = s.substring(space+1); // get rid of number before writing to text file
                out.write(i + "\n");
            }
            out.close();
        }
        catch(IOException e) {}
    }

    public void speak(String output){
        //speaks the contents of output
        speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null, "Id 0");
    }

    // Implements TextToSpeech.OnInitListener
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // If a language is not be available, the result will indicate it.
            int result = speaker.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language data is missing or the language is not supported.
                Log.e(tag, "Language is not available.");
            } else {
                // The TTS engine has been successfully initialized
                Log.i(tag, "TTS Initialization successful.");
            }
        } else {
            // Initialization failed.
            Log.e(tag, "Could not initialize TextToSpeech.");
        }
    }

    // on destroy
    public void onDestroy(){

        // shut down TTS engine
        if(speaker != null){
            speaker.stop();
            speaker.shutdown();
        }
        super.onDestroy();
    }

}
