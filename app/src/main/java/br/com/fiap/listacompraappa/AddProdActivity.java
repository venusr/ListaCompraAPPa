package br.com.fiap.listacompraappa;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import br.com.fiap.listacompraappa.adapter.ProdutoRecyclerAdapter;
import br.com.fiap.listacompraappa.api.LoginAPI;
import br.com.fiap.listacompraappa.api.ProdutoAPI;
import br.com.fiap.listacompraappa.model.Login;
import br.com.fiap.listacompraappa.model.Produto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddProdActivity extends AppCompatActivity {

    private EditText etNome;
    private EditText etQtde;
    private ConstraintLayout layoutContentAddProd;
    private Produto produto;
    private EditText etUsuario;
    private String usuario;
    public ProdutoRecyclerAdapter mAdapter;
    private Produto produtoIncluir;
    public Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prod);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        etNome = (EditText) findViewById(R.id.etNome);
        etQtde = (EditText) findViewById(R.id.etQtde);
        //recuperar campo vindo da tela anterior
        Bundle bundle = getIntent().getExtras();
        usuario = bundle.getString("usuario");

        mAdapter = MainActivity.getAdapter();

        //  layoutContentAddProd = (ConstraintLayout)findViewById(R.id.Layout_content_add_prod);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu_incluir_prod, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.action_ok:
                confirmar();
                break;
            case R.id.action_cancelar:
                finish();
                break;


        }

        return super.onOptionsItemSelected(item);

    }

    private void confirmar() {
        System.out.println("Confirmar");

        if (validaCampos() == true) {
//            pesquisaProduto() // verifica se produto ja existe, para nao deixar criar outro igual

            incluirProduto();
        }

    }

    private void pesquisaProduto() {
        ProdutoAPI apiProduto = getRetrofit().create(ProdutoAPI.class);
        Produto produto = new Produto();
        login = LoginActivity.getLogin();

        produto.setNome(etNome.getText().toString());
        produto.setQtde(etQtde.getText().toString());
        produto.setLogin(login);

        produtoIncluir = produto;

        System.out.println(etNome.getText().toString() + login.getId());
        apiProduto.buscarProduto(etNome.getText().toString(), login.getId())
                .enqueue(new Callback<Produto>() {
                    @Override
                    public void onResponse(Call<Produto> buscarProduto, Response<Produto> response) {
                        if(response.isSuccessful())
                            System.out.println("response produto com sucesso: " + mAdapter);

                        mAdapter.updateList(produtoIncluir);
                        Toast.makeText(AddProdActivity.this,
                                R.string.gravado_sucesso, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Produto> salvar, Throwable t) {
                        System.out.println("produto failure");

                        Toast.makeText(AddProdActivity.this,
                                R.string.erro_salvar_produto, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void incluirProduto() {
        ProdutoAPI apiProduto = getRetrofit().create(ProdutoAPI.class);
        Produto produto = new Produto();
        login = LoginActivity.getLogin();

        produto.setNome(etNome.getText().toString());
        produto.setQtde(etQtde.getText().toString());
        produto.setLogin(login);

        produtoIncluir = produto;

        System.out.println("incluirProduto");
        apiProduto.salvar(produto)
                .enqueue(new Callback<Produto>() {
                    @Override
                    public void onResponse(Call<Produto> salvar, Response<Produto> response) {
                        Produto produtoResponse = response.body();
                        System.out.println("produto com erro numero1: " + produtoResponse.getNome());

                        if(response.isSuccessful()) {
                            System.out.println("response produto com sucesso: " + mAdapter);

                            mAdapter.updateList(produtoIncluir);
                            MainActivity.setProdutoSearch(mAdapter.getListaProdutos()); //para o Search
                            Toast.makeText(AddProdActivity.this,
                                    R.string.gravado_sucesso, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(AddProdActivity.this,
                                    R.string.produto_existe, Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Produto> salvar, Throwable t) {
                        System.out.println("produto failure");

                        Toast.makeText(AddProdActivity.this,
                                R.string.erro_ja_existe, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private boolean validaCampos() {
        String nome = etNome.getText().toString();

        if (isCampoVazio(nome)) {
            etNome.requestFocus();

            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setMessage(R.string.digite_produto);
            dlg.setNeutralButton("OK", null);
            dlg.show();
            return false;
        }

        return true;
    }

    private boolean isCampoVazio(String valor) {
        boolean resultado = (TextUtils.isEmpty(valor) || valor.trim().isEmpty());
        return resultado;
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://listacompravenus.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
