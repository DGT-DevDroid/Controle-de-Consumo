package br.com.android.ppm.controleconsumodeagua;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.android.ppm.controleconsumodeagua.adapter.ListaConsumoAdapter;
import br.com.android.ppm.controleconsumodeagua.dao.ConsumoDAO;
import br.com.android.ppm.controleconsumodeagua.entity.ConsumoEntity;
import br.com.android.ppm.controleconsumodeagua.modal.Consumo;
import br.com.android.ppm.controleconsumodeagua.persistence.AppDatabase;

public class MainActivity extends AppCompatActivity {
    private ConsumoDAO dadosConsumoDAO;
    private List<String> listaPaletesLidas;
    private ListView listviewConsumo;
    private EditText edtMedia;
    private EditText edtMediaDiaria;
    private Double mediaConsumo =0.00;
    private Double mediaConsumoDiario = 0.00;
    private Double somaConsumo = 0.00;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private Context context;
    private Double maiorValor;
    private Double menorValor;
    private int maiorData;
    private int menorData;
    private int numDias;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listviewConsumo = (ListView) findViewById(R.id.idLancamentoConsumo);
        edtMedia = (EditText) findViewById(R.id.idMediaConsumo);
        edtMediaDiaria = (EditText) findViewById(R.id.idConsumoDiario);
        dadosConsumoDAO = AppDatabase.getInstance(this).consumoDAO();
        List<ConsumoEntity> listaPaletesPersistidos = this.dadosConsumoDAO.lista();
        refreshInformacoes(listaPaletesPersistidos);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goCadastroConsumo();
            }
        });
    }

    public void goCadastroConsumo(){
        Intent intent = new Intent(this, CadastroConsumoActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    private void refreshInformacoes(List<ConsumoEntity> listaPaletesPersistidos){

        ArrayList<Consumo> listaConsumoASeremExibidos = new ArrayList<>();

        this.listaPaletesLidas = new ArrayList<>();


        if(listaPaletesPersistidos.size() > 0) {
            for (ConsumoEntity listaConsumo : listaPaletesPersistidos) {
                Consumo consumoASerExibido = new Consumo();
                consumoASerExibido.setQtd(listaConsumo.getQtd());
                consumoASerExibido.setData(listaConsumo.getDataConsumo());
                listaConsumoASeremExibidos.add(consumoASerExibido);
            }
            ListaConsumoAdapter listaAdapter = new ListaConsumoAdapter(this, listaConsumoASeremExibidos);
            listviewConsumo.setAdapter(listaAdapter);
            listviewConsumo.setVisibility(View.VISIBLE);
            somaConsumo = dadosConsumoDAO.maior() - dadosConsumoDAO.menor();
            menorData = dadosConsumoDAO.menorData();
            maiorData = dadosConsumoDAO.maiorData();
            //numDias = dadosConsumoDAO.maiorData();

            for(i = menorData; i < maiorData; i++){
                numDias++;
            }
        }else{
            listviewConsumo.setVisibility(View.GONE);
        }
//        getTop2();
        mediaConsumo = ((double) somaConsumo/numDias) *30;
        mediaConsumoDiario = dadosConsumoDAO.maior() - getTop2();
        edtMedia.setText(String.valueOf(df2.format(mediaConsumo)));
        edtMediaDiaria.setText(String.valueOf(df2.format(mediaConsumoDiario)));
    }


    public Double getTop2(){

        List<ConsumoEntity> lista;
        lista = (ArrayList<ConsumoEntity>) dadosConsumoDAO.top2();
        Double maior=0.00, menor=0.00;
        for (ConsumoEntity l : lista){
           menor = l.getQtd();
        }


        return menor;
    }



}