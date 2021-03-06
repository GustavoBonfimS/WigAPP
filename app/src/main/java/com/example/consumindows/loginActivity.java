package com.example.consumindows;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import modelo.RetrofitConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class loginActivity extends AppCompatActivity {

    private EditText etLogin;
    private EditText etSenha;
    TextView convidado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // esconde barra de titulo
        setContentView(R.layout.activity_login);

        etLogin = findViewById(R.id.etLogin);
        etSenha = findViewById(R.id.etSenha);
        convidado = findViewById(R.id.tvConvidado);

        final Button btnLogin = findViewById(R.id.btnLogin);
        final TextView cadastrar = findViewById(R.id.tvCadastrar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaIndex = new Intent(loginActivity.this, Index.class);
                Bundle param = new Bundle();
                param.putString("login", etLogin.getText().toString());

                telaIndex.putExtras(param);
                startActivity(telaIndex);
                /*
                if (!validaCampos()) {

                    Call<String> call = new RetrofitConfig().getWigService()
                            .validarLogin(etLogin.getText().toString()
                                    , etSenha.getText().toString());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            if (response.body().equals("true") && response.isSuccessful()) {
                                Intent telaIndex = new Intent(loginActivity.this, Index.class);
                                Bundle param = new Bundle();
                                param.putString("login", etLogin.getText().toString());

                                telaIndex.putExtras(param);
                                startActivity(telaIndex);
                            } else {
                                Toast.makeText(loginActivity.this, "Nome de usuairo ou senha incorretos",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("wig", "erro ao se comunicar com o WS: " + t.getMessage());
                        }
                    });
                }

                 */
            }
        });

        // click no texto de cadastrar-se
        cadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent telaCadastro = new Intent(loginActivity.this, CadastrarUsuario.class);
                startActivity(telaCadastro);
            }
        });

        convidado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telaIndex = new Intent(loginActivity.this, Index.class);
                Bundle param = new Bundle();
                param.putString("login", "convidado");
                telaIndex.putExtras(param);
                startActivity(telaIndex);
            }
        });

    }


    private boolean validaCampos() {
        boolean res = false;
        String login = etLogin.getText().toString();
        String senha = etSenha.getText().toString();

        if (res = isCampoVazio(login)) {
            etLogin.requestFocus();
        } else if (res = isCampoVazio(senha)) {
            etSenha.requestFocus();
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

    // sera usado na classe de cadastro
    private boolean isEmailValido(String email) {
        boolean resultado = (!isCampoVazio(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        return resultado;
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}