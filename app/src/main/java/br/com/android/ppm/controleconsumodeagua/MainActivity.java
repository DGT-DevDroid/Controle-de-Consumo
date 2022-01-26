package br.com.android.ppm.controleconsumodeagua;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private EditText edtMedia, edtMediaDiaria, edtLeituraAnterior, edtMeta;
    private Double mediaConsumo =0.00;
    private Double mediaConsumoDiario = 0.00;
    private Double leituraAnteior = 0.00;
    private Double meta = 0.00;
    private SharedPreferences preferences;
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
        edtLeituraAnterior = (EditText) findViewById(R.id.idLeituraAnterior);
        edtMeta = (EditText) findViewById(R.id.idMeta);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add:
                //addSomething();
                abreJanelaDeCadastro();
                return true;
            case R.id.action_settings:
                //startSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        SharedPreferences preferences = getSharedPreferences("UltimaLeitura", MODE_PRIVATE);
        String qtd = preferences.getString("Qtd", "");
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
            mediaConsumo = ((double) somaConsumo/numDias) *30;
            mediaConsumoDiario = dadosConsumoDAO.maior() - getTop2();
        }else{
            listviewConsumo.setVisibility(View.GONE);
        }

        edtMedia.setText(String.valueOf(df2.format(mediaConsumo)));
        edtMediaDiaria.setText(String.valueOf(df2.format(mediaConsumoDiario)));
        edtLeituraAnterior.setText(qtd);
        Double meta = Double.parseDouble(qtd) + 14.00;
        edtMeta.setText(String.valueOf(meta));
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
    public void abreJanelaDeCadastro(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewDialog = layoutInflater.inflate(R.layout.dialog_signin, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(viewDialog);

        TextView tvAdicionar = viewDialog.findViewById(R.id.tvAdicionar);
        TextView tvFechar = viewDialog.findViewById(R.id.tvFechar);

        final EditText editQtdUltimaLeitura = viewDialog.findViewById(R.id.editQtdUltimaLeitura);
        final EditText editDataUltimaLeitura = viewDialog.findViewById(R.id.editDataUltimaLeitura);

        //editQtdUltimaLeitura.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(9)});

        AlertDialog dialog = alertDialogBuilder.create();

        dialog.setCancelable(false);

        tvAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarUltimaLeitura(editQtdUltimaLeitura.getText().toString(), editDataUltimaLeitura.getText().toString());
                dialog.dismiss();
            }
        });


        tvFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //refreshInformacoes();
            }
        });

        dialog.show();
    }
    private void salvarUltimaLeitura(String editQtdUltimaLeitura, String editDataUltimaLeitura) {
        preferences = getSharedPreferences("UltimaLeitura", MODE_PRIVATE);
        SharedPreferences.Editor dados = preferences.edit();
        dados.putString("Qtd", String.valueOf(editQtdUltimaLeitura));
        dados.putString("Data", String.valueOf(editDataUltimaLeitura));
        dados.apply();
    }
}