package com.example.consumindows;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;

import modelo.Avaliacao;
import modelo.Cliente;
import modelo.Empresa;
import modelo.RetrofitConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TesteAvaliacao extends AppCompatActivity {

    Empresa empresa;
    Cliente cliente;
    // EditText etAutor; comentado no xml
    EditText etConteudo;
    CheckBox checkAnonimo;
    TextView tAnon;
    String strAnonimo = null;
    String clienteNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_teste_avaliacao);

        checkAnonimo = findViewById(R.id.checkAnonimo);
        // etAutor = findViewById(R.id.etAutor);
        tAnon = findViewById(R.id.tAnon);
        final Button btAvalia = findViewById(R.id.btnAvaliacao);
        final Button btnVoltar = findViewById(R.id.btnVoltar);
        etConteudo = findViewById(R.id.etConteudo);
        final Avaliacao a = new Avaliacao();

        clienteNome = getIntent().getStringExtra("clienteNome");

        empresa = (Empresa) getIntent().getSerializableExtra("empresa");
        cliente = (Cliente) getIntent().getSerializableExtra("cliente");

        checkAnonimo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    tAnon.setVisibility(View.GONE);
                }
                else{
                    tAnon.setVisibility(View.VISIBLE);
                    strAnonimo = "Anônimo";
                }

            }
        });

        btAvalia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validaCampos()) {

                    if (strAnonimo != null) {
                        a.setAutor(strAnonimo);
                    } else {
                        a.setAutor(clienteNome);
                    }
                    a.setConteudo(etConteudo.getText().toString());
                    a.setIdcliente(cliente.getIdcliente());
                    a.setIdempresa(empresa.getIdempresa());

                    // pega data atual do sistema
                    java.util.Date dataUtil = new java.util.Date();
                    Date data = new Date(dataUtil.getTime());
                    a.setData(data);

                    // pegar hora atual do sistema;
                    Time hora = new Time(dataUtil.getTime());
                    a.setHora(hora);

                    Call<Avaliacao> call = new RetrofitConfig().getWigService().inserirAvaliacao(a);
                    call.enqueue(new Callback<Avaliacao>() {
                        @Override
                        public void onResponse(Call<Avaliacao> call, Response<Avaliacao> response) {
                            if (response.code() == 200 || response.isSuccessful()) {
                                Toast.makeText(TesteAvaliacao.this, "Avaliado com sucesso!"
                                        , Toast.LENGTH_SHORT).show();
                                a.setIdavaliacao(response.body().getIdavaliacao());
                                a.setHora(response.body().getHora());
                                a.setData(response.body().getData());
                                btAvalia.setVisibility(View.GONE);
                                btnVoltar.setVisibility(View.VISIBLE);
                                //finish();

                            }
                        }

                        @Override
                        public void onFailure(Call<Avaliacao> call, Throwable t) {
                            Log.e("wig", "erro ao fazer request" + t.getMessage());
                        }
                    });

                }
                // Toast.makeText(getBaseContext(), "id= :" + a.getIdavaliacao(), Toast.LENGTH_SHORT).show();
            } // onClick ends

        });

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private boolean validaCampos() {
        boolean res = false;
        // String autor = etAutor.getText().toString();
        String conteudo = etConteudo.getText().toString();

        if (res = isCampoVazio(conteudo)) {
            etConteudo.requestFocus();
        }

        if (res) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Aviso");
            dlg.setMessage("Há campos vazios ou em branco");
            dlg.setNeutralButton("OK", null);
            dlg.show();
        }
        return res;
    }

    private boolean isCampoVazio(String valor) {
        boolean resultado = (TextUtils.isEmpty(valor)) || valor.trim().isEmpty();
        return resultado;
    }


}

