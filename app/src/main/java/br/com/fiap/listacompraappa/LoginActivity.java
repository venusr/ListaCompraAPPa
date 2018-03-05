package br.com.fiap.listacompraappa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.fiap.listacompraappa.api.LoginAPI;
import br.com.fiap.listacompraappa.api.ProdutoAPI;
import br.com.fiap.listacompraappa.model.Login;
import br.com.fiap.listacompraappa.model.Produto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etSenha;
    public List<Produto> produtosMain;
    private static Login login;

    public static Login getLogin() {
        return LoginActivity.login;
    }

    public static void setLogin(Login login) {
        LoginActivity.login = login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        produtosMain = new ArrayList<Produto>();



    }


    private boolean validaDados(EditText etUsuario, EditText etSenha) {
        if (etUsuario.getText().toString().length() == 0) {
            etUsuario.setError(getString(R.string.digite_usuario));
            return false;
        }
        if (etSenha.getText().toString().length() == 0) {
            etSenha.setError(getString(R.string.digite_senha));
            return false;
        }

        return true;
    }

    public void criarUsuario(View v) {
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etSenha = (EditText) findViewById(R.id.etSenha);

        Intent proximaTela = new Intent(LoginActivity.this,
                CriarLoginActivity.class);

        proximaTela.putExtra("usuario", etUsuario.getText().toString());
        startActivity(proximaTela);
        LoginActivity.this.finish();

    }


    public void verificarSenha(View v) {
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etSenha = (EditText) findViewById(R.id.etSenha);

        if (!validaDados(etUsuario,etSenha)) return;

        final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "",
                getString(R.string.validando_usuario), true);

        dialog.show();

        LoginAPI api = getRetrofit().create(LoginAPI.class);


        System.out.println("Usuario " + etUsuario.getText().toString() );

        Login login = new Login();
        login.setSenha(etSenha.getText().toString());
        login.setUsuario(etUsuario.getText().toString());

        api.verSenha(etUsuario.getText().toString(), etSenha.getText().toString())
                .enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> verSenha, Response<Login> response) {
                        dialog.dismiss();
                        if(response.isSuccessful()) {
//                            Toast.makeText(LoginActivity.this,
//                                    "Usuário e Senha Validados com Sucesso.", Toast.LENGTH_SHORT).show();
                            Login loginResponse = response.body();

                            LoginActivity.login = loginResponse;

                            SharedPreferences prefs = getSharedPreferences("pref_lista_compras",0);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean("usuarioLogado", true);
                            editor.putString("idLogin", loginResponse.getId());
                            editor.putString("nomeCompleto", loginResponse.getNome());
                            editor.putString("usuario", loginResponse.getUsuario());
                            editor.commit();

                            Intent proximaTela = new Intent(LoginActivity.this,
                                    MainActivity.class);

                            System.out.println("etusuario LoginActivity: " + etUsuario.getText().toString());
                            proximaTela.putExtra("usuario", etUsuario.getText().toString());
                            startActivity(proximaTela);
                            LoginActivity.this.finish();


                        }

                    }

                    @Override
                    public void onFailure(Call<Login> verSenha, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this,
                                R.string.usuario_senha_invalido, Toast.LENGTH_LONG).show();
                    }
                });
    }


/*    private List<Produto> buscarListaProdutos(Login login) {
        ProdutoAPI apiProduto = getRetrofit().create(ProdutoAPI.class);
        List<Produto> produtos;



    final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "",
                "Buscando os produtos de sua lista, por favor aguarde.", true);


        System.out.println("listarProduto");
        apiProduto.listaProdutos(login.getId())
                .enqueue(new Callback<List<Produto>>() {
                    @Override
                    public void onResponse(Call<List<Produto>> salvar, Response<List<Produto>> response) {
                        dialog.dismiss();

//                        Toast.makeText(LoginActivity.this,
//                                "Lista de Produtos sendo gerada.", Toast.LENGTH_LONG).show();

                        if(response.isSuccessful())
                            System.out.println("response lista produto com sucesso");
                        List<Produto> produtos = response.body();
                        int i = 0;
                        for (i=0; i < produtos.size() ; i++) {
                            System.out.println("Listando Produtos: " + produtos.get(i).getNome());
                        }
                        produtosMain = produtos;


                    }

                    @Override
                    public void onFailure(Call<List<Produto>> salvar, Throwable t) {
                        System.out.println("produto failure");
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this,
                                "Ocorreu um erro ao tentar buscar os produtos.", Toast.LENGTH_LONG).show();
                    }
                });
        return produtosMain;

    }
*/
    public void sair(DialogInterface dialog, int wich) {
        finish();
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://listacompravenus.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
