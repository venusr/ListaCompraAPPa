package br.com.fiap.listacompraappa;

import android.support.v7.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import br.com.fiap.listacompraappa.api.LoginAPI;
import br.com.fiap.listacompraappa.api.ProdutoAPI;
import br.com.fiap.listacompraappa.model.Login;
import br.com.fiap.listacompraappa.model.Produto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.Serializable;


public class ListarComprasActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etIdLogin;

    RecyclerView mRecyclerView;
    //private LIneAdapter mAdapter;

    private void setupRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

      //  mAdapter = new LineAdapter(new ArrayList<>(0));
       // mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
    }


    //
// material io -- VER BOAS PRATICAS E LAYOUTS DE TELAS
//    https://material.io/guidelines/components/#
//    https://material.io/guidelines/components/bottom-navigation.html

//exemplos de lista recycler
//    https://developer.android.com/training/material/lists-cards.html?hl=pt-br

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_compras);

        //recuperar campo vindo da tela anterior
        Bundle bundle = getIntent().getExtras();
        String usuario = bundle.getString("usuario");


        final ProgressDialog dialog = ProgressDialog.show(ListarComprasActivity.this, "",
                getString(R.string.carregando_lista), true);

        dialog.show();

        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etIdLogin = (EditText) findViewById(R.id.etIdLogin);

        LoginAPI api = getRetrofit().create(LoginAPI.class);

        api.verUsuario(usuario)
                .enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> verUsuario, Response<Login> response) {
                        //Encontrou usuario
                        if(response.isSuccessful()) {
                            Login login = response.body();
                            etUsuario.setText(login.getUsuario());
                            etIdLogin.setText(login.getId());

                            listarProdutos(login.getId());
                        }
                        dialog.dismiss();


                    }

                    //nao trouxe objeto Login, entáo náo existe o usuario
                    @Override
                    public void onFailure(Call<Login> verUsuario, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(ListarComprasActivity.this,
                                R.string.erro_carga_lista, Toast.LENGTH_LONG).show();


                    }
                });

    }

    private void listarProdutos(String idLogin) {

        ProdutoAPI api = getRetrofit().create(ProdutoAPI.class);

        api.listaProdutos(idLogin)
                .enqueue(new Callback<List<Produto>>() {
                    @Override
                    public void onResponse(Call<List<Produto>> listaProdutos, Response<List<Produto>> response) {
                        if(response.isSuccessful()) {
                           System.out.println("sucesso busca lista");

                           List<Produto> listaProduto = response.body();

                            for (int i=0; i<listaProduto.size();i++) {
                                System.out.println("Produto: " + listaProduto.get(i).getId() + "|" + listaProduto.get(i).getNome() + "|" + listaProduto.get(i).getQtde() + "|" );
                            }
                            exibirListaProdutos(listaProduto);

                        }
                    }

                    //nao trouxe objeto Login, entáo náo existe o usuario
                    @Override
                    public void onFailure(Call<List<Produto>> listaProdutos, Throwable t) {
                        Toast.makeText(ListarComprasActivity.this,
                                R.string.erro_carga_lista_compra, Toast.LENGTH_LONG).show();


                    }
                });


    }

    private void exibirListaProdutos(List<Produto> listaProdutos) {

    }


    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://listacompravenus.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }



}
