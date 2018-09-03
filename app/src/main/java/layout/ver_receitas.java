package layout;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dany.stockhome.DbHelper;
import com.example.dany.stockhome.R;
import com.example.dany.stockhome.Receita;
import com.example.dany.stockhome.RecyclerAdapterReceitas;
import com.example.dany.stockhome.VerticalSpaceItemDecoration;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ver_receitas extends Fragment {

    RecyclerView.Adapter adapter;
    ArrayList<Receita> arrayList = new ArrayList<>();
        String x ;
    ArrayList<Receita> procura = new ArrayList<>();
    private static final int VERTICAL_ITEM_SPACE =48;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        try{
            Bundle bundle = this.getArguments();
            x = bundle.getString("Nome");
        }catch (Exception e){
            e.printStackTrace();
        }
        return inflater.inflate(R.layout.ver_receitas_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        DbHelper dbHelper;
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        dbHelper = new DbHelper(getActivity().getApplicationContext());

        //da me as receitas que posso fazer
        Cursor cursor = dbHelper.getReceitas();

        //ve se nao é uma pesquisa
        if (x == null) {
            //verifica se ha receitas na base de daos
            if (cursor.getCount() > 0) {
                arrayList = dbHelper.getReceita();
                //verifica se ha receitas possiveis de fazer
               if (arrayList.size() != 0) {
                    recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
                    adapter = new RecyclerAdapterReceitas(arrayList);
                    recyclerView.setAdapter(adapter);
                } else {
                    //se nao hover receitas possiveis de se fazer
                    TextView textView = (TextView) view.findViewById(R.id.textView7);
                    textView.setVisibility(View.VISIBLE);

                }
            } else {
                //se nao hover receitas na base de dados
                TextView textView = (TextView) view.findViewById(R.id.textView7);
                textView.setVisibility(View.VISIBLE);
            }
            //se nao for uma pesquisa
        }else{
            //ve se ha receitas na base de dados
            if (cursor.getCount() > 0) {
                arrayList = dbHelper.getReceita();
                if (arrayList.contains(x)){

                    for (int i = 0; i < arrayList.size() ; i++) {
                        if (arrayList.get(i).toString() == x){
                            procura.add(arrayList.get(i));
                            arrayList = procura;
                        }
                    }
                }else{
                    TextView textView = (TextView) view.findViewById(R.id.textView7);
                    textView.setVisibility(View.VISIBLE);

                }
            }


            recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));

            adapter = new RecyclerAdapterReceitas(arrayList);
            recyclerView.setAdapter(adapter);
        }
    }
}