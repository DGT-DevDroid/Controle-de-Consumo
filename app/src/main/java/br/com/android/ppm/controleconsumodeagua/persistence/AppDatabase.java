package br.com.android.ppm.controleconsumodeagua.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.com.android.ppm.controleconsumodeagua.dao.ConsumoDAO;
import br.com.android.ppm.controleconsumodeagua.entity.ConsumoEntity;


//
//@Database(entities = { ListaPaletesEncaminhadosEntity.class, DadosPedidoEncaminhamentoEntity.class, ListaEncomendasRecebidasEntity.class, ListaPaletesRecebidosEntity.class, ConferenciaListaPaletesEntity.class, DadosVeiculoEntity.class,MotivosBaixaExternaEntity.class,
//                        ListaEntregaExternaEntity.class, ParadasVeiculoEntity.class,
//                        PaleteEnglobadoEntity.class},
//                        version = 20)

@Database(entities = {ConsumoEntity.class},
        version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
           instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "consumo-db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }

        return instance;
    }

    public abstract ConsumoDAO consumoDAO();

}
