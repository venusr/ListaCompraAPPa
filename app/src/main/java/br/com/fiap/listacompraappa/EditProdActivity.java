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
import android.widget.TextView;
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

public class EditProdActivity extends AppCompatActivity {
    private TextView etNome;
    private EditText etQtde;
    private ConstraintLayout layoutContentAddProd;
    private Produto produto;
    private EditText etUsuario;
    private String usuario;
    public ProdutoRecyclerAdapter mAdapter;
    private Produto produtoAlterar;
    public Login login;
    private Integer position;
    private String nomeAntigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prod);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //recuperar campo vindo da tela anterior
        Bundle bundle = getIntent().getExtras();
        String nome = bundle.getString("nome");
        nomeAntigo = nome; //captura o nome antigo do produto, para busca-lo na base de dados e poder altera-lo depois
        String qtde = bundle.getString("qtde");
        position = bundle.getInt("position");

        System.out.println("oncreate edit antes nome");

        etNome = (TextView) findViewById(R.id.etNome);
        etNome.setText(nome);

        System.out.println("oncreate edit antes qtde");

        etQtde = (EditText) findViewById(R.id.etQtde);
        etQtde.setText(qtde);

        mAdapter = MainActivity.getAdapter();

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

             alterarProduto();

    }


    private void alterarProduto() {
        ProdutoAPI apiProduto = getRetrofit().create(ProdutoAPI.class);
        Produto produto = new Produto();
        login = LoginActivity.getLogin();

        System.out.println("Pesquisar produto: "+ etNome.getText().toString());
        System.out.println("Pesquisar login: "+ login.getId());

/*
        //primeiro pesquisa se o produto alterado nao esta usando o mesmo nome de um produto ja existente
        if (!nomeAntigo.equals(etNome.getText().toString())) {
            System.out.println("pesquisaProduto");
            apiProduto.buscarProduto(etNome.getText().toString(), login.getId())
                    .enqueue(new Callback<Produto>() {
                        @Override
                        public void onResponse(Call<Produto> buscarProduto, Response<Produto> response) {
                            Produto produtoResponse = response.body();
                            System.out.println("Response da Pesquisa: "+ produtoResponse.getNome());

                            if(response.isSuccessful()) {
                                System.out.println("response busca produto: ");

                                //encontrou produto, entáo não pode incluir outro com o mesmo nome
                                AlertDialog.Builder dlg = new AlertDialog.Builder(EditProdActivity.this);
                                dlg.setMessage("Você já tem um outro Produto em sua lista com esse nome.");
                                dlg.setNeutralButton("OK", null);
                                dlg.show();

                            }
                            else {

                                //nao encontrou o produto com o mesmo nome, entao pode alterar
                                System.out.println("voltou sem encontrar produto " );
                                alterarBaseDados();
                            }
                        }

                        @Override
                        public void onFailure(Call<Produto> buscarProduto, Throwable t) {
                            System.out.println("busca failure");

                            Toast.makeText(EditProdActivity.this,
                                    "Ocorreu um erro ao pesquisa o produto.", Toast.LENGTH_SHORT).show();
                        }
                    });

        } */
  //      else // nome do produto náo foi alterado, é o mesmo existente, entáo pode ir alterar
    //    {
            alterarBaseDados();
//        }

    }

    private void alterarBaseDados() {
        ProdutoAPI apiProduto = getRetrofit().create(ProdutoAPI.class);
        Produto produto = new Produto();
        login = LoginActivity.getLogin();

        produto.setNome(etNome.getText().toString());
        produto.setQtde(etQtde.getText().toString());
        produto.setLogin(login);

        System.out.println("altera nome: " + produto.getNome());
        System.out.println("altera qtde: "+ produto.getQtde());
        System.out.println("nome antigo: "+ nomeAntigo);
        produtoAlterar = produto;

        System.out.println("alterarProduto");
        apiProduto.alterarProduto(produto)   //(nomeAntigo, produto, login.getId())
                .enqueue(new Callback<Produto>() {
                    @Override
                    public void onResponse(Call<Produto> alterarProduto, Response<Produto> response) {
                        Produto produtoResponse = response.body();

                        if(response.isSuccessful()) {
                            System.out.println("response produto alterar com sucesso: " + mAdapter);
                            System.out.println("produtoResponse alterar: " + produtoResponse.getNome());

                            mAdapter.updateItem(position, produtoResponse);
                            Toast.makeText(EditProdActivity.this,
                                    R.string.alterado_sucesso, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            System.out.println("voltou alterar com null " + mAdapter);
                            //System.out.println("produtoResponse alterar: " + produtoResponse.getNome());

                            Toast.makeText(EditProdActivity.this,
                                    R.string.erro_alterar, Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Produto> alterarProduto, Throwable t) {

                        System.out.println("produto alterado failure");

                        Toast.makeText(EditProdActivity.this,
                                R.string.erro_alterar, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://listacompravenus.herokuapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
