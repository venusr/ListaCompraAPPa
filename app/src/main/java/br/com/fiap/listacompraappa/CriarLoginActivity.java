package br.com.fiap.listacompraappa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.fiap.listacompraappa.api.LoginAPI;
import br.com.fiap.listacompraappa.model.Login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CriarLoginActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etSenha;
    private EditText etConfirmaSenha;
    private EditText etNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_login);

        //recuperar campo vindo da tela anterior
        Bundle bundle = getIntent().getExtras();
        String usuario = bundle.getString("usuario");

        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etUsuario.setText(usuario);


    }

    private boolean validaDados(EditText etUsuario, EditText etSenha, EditText etConfirmaSenha, EditText etNome) {
        if (etUsuario.getText().toString().length() == 0) {
            etUsuario.setError(getString(R.string.digite_usuario));
            return false;
        }
        if (etNome.getText().toString().length() == 0) {
            etNome.setError(getString(R.string.digite_nome));
            return false;
        }
        if (etSenha.getText().toString().length() == 0) {
            etSenha.setError(getString(R.string.digite_senha));
            return false;
        }

        System.out.println("antes Senha: "+ etSenha.getText().toString());
        System.out.println("Confirma Senha: " + etConfirmaSenha.getText().toString());

        if (!etSenha.getText().toString().equals(etConfirmaSenha.getText().toString()) ) {
            etConfirmaSenha.setError(getString(R.string.confirme_senha));
            return false;
        }

        System.out.println("depois Senha: "+ etSenha.getText().toString());
        System.out.println("Confirma Senha: " + etConfirmaSenha.getText().toString());

        return true;
    }

    public void validarLogin(final View v) {

        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etSenha = (EditText) findViewById(R.id.etSenha);
        etConfirmaSenha = (EditText) findViewById(R.id.etConfirmaSenha);
        etNome = (EditText) findViewById(R.id.etNome);

        if (!validaDados(etUsuario,etSenha,etConfirmaSenha,etNome)) return;

        System.out.println("validarlogin");

        final ProgressDialog dialog = ProgressDialog.show(CriarLoginActivity.this, "",
                getString(R.string.verifica_usuario), true);

        dialog.show();

        LoginAPI api = getRetrofit().create(LoginAPI.class);
        Login login = new Login();

        login.setNome(etNome.getText().toString());
        login.setSenha(etSenha.getText().toString());
        login.setUsuario(etUsuario.getText().toString());

        api.verUsuario(etUsuario.getText().toString())
                .enqueue(new Callback<Login>() {

                    @Override
                    public void onResponse(Call<Login> verUsuario, Response<Login> response) {
                        dialog.dismiss();
                        //Encontrou usuario com o mesmo nome
                        if(response.isSuccessful()) {
                            Toast.makeText(CriarLoginActivity.this,
                                    R.string.usuario_ja_existe, Toast.LENGTH_LONG).show();
                            return;
                        }
                        else {
                            System.out.println("passou null validarusuario");
                            dialog.dismiss();
                            criarLogin(v);
                        }


                    }
                    //nao trouxe objeto Login, entáo náo existe o usuario, pode criar
                    @Override
                    public void onFailure(Call<Login> verUsuario, Throwable t) {
                        dialog.dismiss();
                        System.out.println("passou failure");
                        dialog.dismiss();
                        criarLogin(v);

                    }
                });

    }

    public void criarLogin(View v) {

        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etSenha = (EditText) findViewById(R.id.etSenha);
        etConfirmaSenha = (EditText) findViewById(R.id.etConfirmaSenha);
        etNome = (EditText) findViewById(R.id.etNome);

        System.out.println("criarloginNovo");

        final ProgressDialog dialog = ProgressDialog.show(CriarLoginActivity.this, "",
                getString(R.string.criando_usuario), true);

        dialog.show();

        LoginAPI api = getRetrofit().create(LoginAPI.class);
        Login login = new Login();

        login.setNome(etNome.getText().toString());
        login.setSenha(etSenha.getText().toString());
        login.setUsuario(etUsuario.getText().toString());

        api.salvar(login)
                .enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> salvar, Response<Login> response) {
                        Login loginResponse = response.body();

                        System.out.println(loginResponse.getId());
                        LoginActivity.setLogin(loginResponse);

                        SharedPreferences prefs = getSharedPreferences("pref_lista_compras",0);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("usuarioLogado", true);
                        editor.putString("idLogin", loginResponse.getId());
                        editor.putString("nomeCompleto", loginResponse.getNome());
                        editor.putString("usuario", loginResponse.getUsuario());
                        editor.commit();

                        if(response.isSuccessful()) {
                            dialog.dismiss();

                            System.out.println("loginResponse : " + loginResponse.getNome());
                            Toast.makeText(CriarLoginActivity.this,
                                    R.string.usuario_gravado_sucesso, Toast.LENGTH_SHORT).show();
                            chamarListaProdutos();
                        }
                        else {
                            System.out.println("entrou no else do criarlogin");

                            dialog.dismiss();
                            Toast.makeText(CriarLoginActivity.this,
                                    R.string.erro_salvar_usuario, Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Login> salvar, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(CriarLoginActivity.this,
                                R.string.erro_salvar_usuario, Toast.LENGTH_LONG).show();
                    }
                });

    }


    private void chamarListaProdutos() {

        System.out.println("indo para tela de Lista de Produtos");
        Intent proximaTela = new Intent(CriarLoginActivity.this,
                MainActivity.class);

        proximaTela.putExtra("usuario", etUsuario.getText().toString());
        startActivity(proximaTela);
        CriarLoginActivity.this.finish();


    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://listacompravenus.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


}
