package com.example.psp20segundos;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "zxy";
    ProgressBar progressBar, progressBarAsync;
    private int seg, segAsync=0;
    private TextView tvPorcentaje, tvPorcentajeAsync;
    Thread tiempo;
    Button btIniciar, btIniciarAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initComponents();
        initEvents();


        Log.v(TAG, "asdf");

        tiempo = new Thread(){
            @Override
            public void run() {
                Log.v(TAG, "HOLA");
                try {

                    for (seg=0; seg<=100; seg++){
                        Log.v(TAG, String.valueOf("Thread: "+seg));
                        tiempo.sleep(50);


                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressBar.setProgress(seg);
                                tvPorcentaje.setText(""+seg);

                                if (seg == 100){
                                    Intent intentThread = new Intent(MainActivity.this, ThreadActivity.class);
                                    startActivity(intentThread);
                                }
                            }
                        });
                    }

                } catch(Exception e){

                    e.printStackTrace();
                    Log.v(TAG, String.valueOf(e));
                }

            }
        };//fin


           }

    class hebraAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {

            int porcentaje=0;

            for(int segAsync = params[0]; segAsync<=100; segAsync++) {
                Log.v(TAG, String.valueOf("ASYNCTAKC: "+segAsync));
                porcentaje = segAsync;
                try {
                    Thread.sleep( 50 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                publishProgress(segAsync);


                if(isCancelled())
                    break;
            }

            return porcentaje;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();

            progressBarAsync.setProgress(progreso);
            tvPorcentajeAsync.setText(String.valueOf(progreso));
        }


        @Override
        protected void onPreExecute() {
            progressBarAsync.setMax(100);
            progressBarAsync.setProgress(0);
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == 100){

                Intent intentAsyncTask = new Intent(MainActivity.this, AsyncTaskActivity.class);
                startActivity(intentAsyncTask);
            }

        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MainActivity.this, "Tarea cancelada!",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void initEvents() {

        btIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tiempo.start();
            }
        });

        btIniciarAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new hebraAsyncTask().execute(0);
            }
        });
    }

    private void initComponents() {

        btIniciarAsync = findViewById(R.id.btAsyncTask);
        tvPorcentajeAsync = findViewById(R.id.tvPorcentajeAsyncTask);
        progressBarAsync = findViewById(R.id.pbAsyncTask);

        btIniciar = findViewById(R.id.button);
        progressBar = findViewById(R.id.pbThread);
        tvPorcentaje = findViewById(R.id.tvPorcentaje);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
