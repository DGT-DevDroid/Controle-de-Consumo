package br.com.android.ppm.controleconsumodeagua;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.android.ppm.controleconsumodeagua.dao.ConsumoDAO;
import br.com.android.ppm.controleconsumodeagua.entity.ConsumoEntity;
import br.com.android.ppm.controleconsumodeagua.persistence.AppDatabase;

public class CadastroConsumoActivity extends AppCompatActivity {
    private ConsumoDAO listaConsumoDAO;
    final Calendar myCalendar= Calendar.getInstance();
    private TextView txtqtd;
    private TextView txtdata;
    private Button btnCadastrar;
    EditText idData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_consumo);
        idData=(EditText) findViewById(R.id.idData);
        listaConsumoDAO = AppDatabase.getInstance(this).consumoDAO();
        txtqtd       = findViewById(R.id.idConsumo);
        txtdata      = findViewById(R.id.idData);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gravaDadosConsumo(Double.parseDouble(txtqtd.getText().toString()), txtdata.getText().toString());
                //limpaCamposCadastro(txtqtd, txtdata);
                Toast.makeText(CadastroConsumoActivity.this, getString(R.string.cadastro_com_sucesso), Toast.LENGTH_SHORT).show();
                goMainActivity();
            }
        });


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        idData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CadastroConsumoActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
    private void limpaCamposCadastro(TextView txtqtd, TextView txtdata) {
        txtqtd.setText(null);
        txtdata.setText(null);
    }

    private void gravaDadosConsumo(Double qtd, String data) {
        //SimpleDateFormat fmtData = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        ConsumoEntity dadosConsumo = new ConsumoEntity();
        //int id =  +listaConsumoDAO.lista().size();

        dadosConsumo.setQtd(qtd);
        dadosConsumo.setDataConsumo(data.toString());
        int id = listaConsumoDAO.ultimoId() + 1;
        dadosConsumo.setIdConsumo(id);
        listaConsumoDAO.adiciona(dadosConsumo);
    }
    public void goMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

        idData.setText(sdf.format(myCalendar.getTime()));
    }
}