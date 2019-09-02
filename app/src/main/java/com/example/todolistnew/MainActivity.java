package com.example.todolistnew;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    final Context context = this;
    Button btRemove, btEdit, btAdd, btMore;
    EditText addTaskEdit, etDate;
    final Calendar myCalendar = Calendar.getInstance();
    ListView listView;
    ArrayList<Boolean> doneList = new ArrayList<>();
    List<String> taskList = new ArrayList<String>();
    List<String> dateList = new ArrayList<String>();
    int idEdRem;
    String editDate = "";
    //Boolean editBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btAdd = (Button) findViewById(R.id.btAdd);
        btRemove = (Button) findViewById(R.id.buttonRemove);
        btEdit = (Button) findViewById(R.id.buttonEdit);
        btMore = (Button) findViewById(R.id.buttonMore);
        btRemove.setEnabled(false);
        btEdit.setEnabled(false);
        btMore.setEnabled(false);
        addTaskEdit = (EditText) findViewById(R.id.addTaskText);
        listView = (ListView) findViewById(R.id.listViewTask);

        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.listview_layout, taskList);
        listView.setAdapter(arrayAdapter);
        etDate = (EditText) findViewById(R.id.etData);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addTaskEdit.length() == 0){
                    Toast.makeText(getApplicationContext(), "Brak wpisanego zadania", Toast.LENGTH_LONG).show();
                }
                if(etDate.length() == 0){
                    Toast.makeText(getApplicationContext(), "Nie podano daty", Toast.LENGTH_LONG).show();
                }
                if(addTaskEdit.length() != 0 && etDate.length() != 0){
                    taskList.add(addTaskEdit.getText().toString());
                    dateList.add(etDate.getText().toString());
                    doneList.add(false);
                    addTaskEdit.setText("");
                    etDate.setText("");
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });

        btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Potwierdzenie usunięcia");
                alertDialogBuilder
                        .setMessage("Czy chcesz usunąć zadanie " + taskList.get(idEdRem) + "?")
                        .setCancelable(false)
                        .setPositiveButton("Tak",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                taskList.remove(idEdRem);
                                doneList.remove(idEdRem);
                                dateList.remove(idEdRem);
                                blockButtons();
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Nie",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                //Toast.makeText(getApplicationContext(), taskList.toString(), Toast.LENGTH_LONG).show();
            }
        });

        btMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Szczegóły zadania");
                alertDialogBuilder
                        .setMessage("Zadanie należy wykonać do dnia: " + dateList.get(idEdRem))
                        .setCancelable(false)
                        .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                blockButtons();
            }
        });

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //editBool = true;
                LayoutInflater li = LayoutInflater.from(context);
                View view = li.inflate(R.layout.layout_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view);
                final EditText etTask = (EditText) view.findViewById(R.id.etTask);
                final EditText etEditDate = (EditText) view.findViewById(R.id.etEditDate);
                etTask.setText(taskList.get(idEdRem));
                etEditDate.setText(dateList.get(idEdRem));

                builder
                        .setTitle("Edycja zadania")
                        .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Potwierdź", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                taskList.set(idEdRem,etTask.getText().toString());
                                dateList.set(idEdRem,etEditDate.getText().toString());
                                arrayAdapter.notifyDataSetChanged();

                            }
                        })
                        .create()
                        .show();
                //editBool = false;
                blockButtons();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String task_string;
                task_string = taskList.get(position);
                if(doneList.get(position) == false){
                    task_string = task_string + (" - zrobione");
                    doneList.set(position, true);
                }
                else{
                    task_string = task_string.substring(0, task_string.length() - 11);
                    doneList.set(position, false);
                }
                taskList.set(position, task_string);
                arrayAdapter.notifyDataSetChanged();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                idEdRem = position;
                btRemove.setEnabled(true);
                btEdit.setEnabled(true);
                btMore.setEnabled(true);
                return true;
            }
        });
    }

    public void blockButtons(){
        btRemove.setEnabled(false);
        btEdit.setEnabled(false);
        btMore.setEnabled(false);
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);
        etDate.setText(sdf.format(myCalendar.getTime()));

    }
}

