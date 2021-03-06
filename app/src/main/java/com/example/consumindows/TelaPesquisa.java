package com.example.consumindows;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import modelo.Cliente;
import modelo.Empresa;
import modelo.RetrofitConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TelaPesquisa extends AppCompatActivity {
    ListView listView;
    Empresa empresa;
    Cliente cliente;
    String status;
    Empresa empresaLogada;

    public void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_pesquisa);

        listView = findViewById(R.id.lvPesquisa);

        if (getIntent().getStringExtra("status").equals("logado")) {
            cliente = (Cliente) getIntent().getSerializableExtra("cliente");
            status = "logado";
        } else if (getIntent().getStringExtra("status").equals("empresa")) {
            status = "empresa";
        } else status = "convidado";
        final SearchView pesquisar = (SearchView) findViewById(R.id.search);
        pesquisar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s != null) {
                    Call<List<Empresa>> call = new RetrofitConfig().getWigService().pesquisa(s);
                    call.enqueue(new Callback<List<Empresa>>() {
                        @Override
                        public void onResponse(Call<List<Empresa>> call, Response<List<Empresa>> response) {
                            if (response.code() == 200 || response.isSuccessful()) {
                                final List<Empresa> lista = response.body();

                                final String[] array = new String[lista.size()];
                                for (int i = 0; i < lista.size(); i++) {
                                    array[i] = lista.get(i).getLogin();
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TelaPesquisa.this,
                                        android.R.layout.simple_list_item_1, array);
                                listView.setAdapter(adapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        empresa = new Empresa();
                                        empresa = lista.get(position);
                                        String nome = empresa.getLogin();

                                        Intent telaEmrpes = new Intent(TelaPesquisa.this, telaEmpresa.class);
                                        telaEmrpes.putExtra("empresa", empresa);
                                        telaEmrpes.putExtra("empresaNome", nome);
                                        telaEmrpes.putExtra("status", status);

                                        if (status.equals("logado")) {
                                            telaEmrpes.putExtra("cliente", cliente);
                                        } else if (status.equals("empresa")) {
                                            // caso seja uma empresa que esteja logada
                                            empresaLogada = (Empresa) getIntent()
                                                    .getSerializableExtra("empresa");
                                            String nomeEmpresa = getIntent().getStringExtra("loginEmpresaLogada");
                                            Log.e("wig", "loginEmpresa " + nomeEmpresa);
                                            telaEmrpes.putExtra("loginEmpresaLogada", nomeEmpresa);
                                            telaEmrpes.putExtra("empresaLogada", empresaLogada);
                                        }
                                        startActivity(telaEmrpes);
                                    }
                                });

                            }

                        }

                        @Override
                        public void onFailure(Call<List<Empresa>> call, Throwable t) {
                            Log.e("wig", "erro ao se comunicar com o WS: " + t.getMessage());
                        }
                    });
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


    }

}
