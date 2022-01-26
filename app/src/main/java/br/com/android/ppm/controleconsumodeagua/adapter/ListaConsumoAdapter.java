package br.com.android.ppm.controleconsumodeagua.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import br.com.android.ppm.controleconsumodeagua.MainActivity;
import br.com.android.ppm.controleconsumodeagua.R;
import br.com.android.ppm.controleconsumodeagua.dao.ConsumoDAO;
import br.com.android.ppm.controleconsumodeagua.entity.ConsumoEntity;
import br.com.android.ppm.controleconsumodeagua.modal.Consumo;
import br.com.android.ppm.controleconsumodeagua.persistence.AppDatabase;

public class ListaConsumoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Consumo> listaConsumo;
    private ConsumoDAO consumoDAO;

    public ListaConsumoAdapter(Context context, ArrayList<Consumo> listaConsumo){
        this.context = context;
        this.listaConsumo = listaConsumo;
    }


    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return listaConsumo.size();
    }

    @Override
    public Object getItem(int position) {
        return listaConsumo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private void excluir(String data){
        consumoDAO.delete(data);
        context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class));

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        consumoDAO = AppDatabase.getInstance(context.getApplicationContext()).consumoDAO();

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_row_consumo, null, true);

            holder.idQdtConsumo  = convertView.findViewById(R.id.idQdtConsumo);
            holder.idData = convertView.findViewById(R.id.idData);
            holder.mDelete = convertView.findViewById(R.id.id_delete);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.idQdtConsumo.setText(String.valueOf(listaConsumo.get(position).getQtd()));
        holder.idData.setText(listaConsumo.get(position).getData());
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialAlertDialogBuilder(context)
                        .setTitle("Atenção")
                        .setMessage("Deseja excluir o registro ")
                        .setNeutralButton(R.string.label_cancelar,(dialogInterface, i) -> {})
                        .setPositiveButton("Sim", (dialogInterface, i) -> {
                            excluir(listaConsumo.get(position).getData());
                            dialogInterface.dismiss();
                        })
                        .show();
            }
        });

        return convertView;
    }

    private class ViewHolder {

        protected TextView idQdtConsumo, idData;
        ImageView mDelete;

    }

    public ArrayList<Consumo> getListaConsumo() {
        return listaConsumo;
    }

    public void getListaConsumo(ArrayList<Consumo> listaConsumo) {
        this.listaConsumo = listaConsumo;
    }

}
