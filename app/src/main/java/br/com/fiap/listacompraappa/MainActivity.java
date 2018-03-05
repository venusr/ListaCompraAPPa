package br.com.fiap.listacompraappa;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
//import android.widget.SearchView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.fiap.listacompraappa.adapter.ProdutoAdapter;
import br.com.fiap.listacompraappa.adapter.ProdutoRecyclerAdapter;
import br.com.fiap.listacompraappa.adapter.RecyclerOnItemClickListener;
import br.com.fiap.listacompraappa.api.LoginAPI;
import br.com.fiap.listacompraappa.api.ProdutoAPI;
import br.com.fiap.listacompraappa.model.Login;
import br.com.fiap.listacompraappa.model.Produto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static br.com.fiap.listacompraappa.adapter.ProdutoRecyclerAdapter.selected_item;

public class MainActivity extends AppCompatActivity {

//ver esse para filtrar no recycler
//    https://www.androidhive.info/2017/11/android-recyclerview-with-search-filter-functionality/
//  share componentes., internalicional, botao sair
    //e exceptions, menu, tratar volta das APIS


    private FloatingActionButton fab;
    private ConstraintLayout layoutContentMain;
    private EditText etUsuario;
    private String usuario;
    List<Produto> produtosMain;
    private static List<Produto> produtosSearch;
    public ProdutoRecyclerAdapter mAdapter;
    private static ProdutoRecyclerAdapter mAdapterProduto;  // para passar para o AddProd
    public View selectedView;
    public Integer positionList;
    public Login login;
    SearchView searchView;
    private Date horarioSelectItem;

    public static ProdutoRecyclerAdapter getAdapter() {
        return MainActivity.mAdapterProduto;
    }

    public static void setProdutoSearch(List<Produto> produtos) {
        produtosSearch = produtos;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        System.out.println("entrou oncreateMain");

        fab = (FloatingActionButton) findViewById(R.id.fab);
//        listar();


        //recuperar campo vindo da tela anterior
        Bundle bundle = getIntent().getExtras();
        usuario = bundle.getString("usuario");

        //       System.out.println("antes recuperalogion");

        login = LoginActivity.getLogin();
        listar(login);

        System.out.println("encerra oncreateMain");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu_main, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_pesquisar);
        searchView = (SearchView) myActionMenuItem.getActionView();

        System.out.println("entrou createmenuitem");

//        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(getResources().getColor(R.color.colorPrimary));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                System.out.println("entrou na queryTextChange : " + s);

                List<Produto> prod1 = mAdapter.getListaProdutos();
                for (Produto prod: prod1) {
                    System.out.println("Produto da mLista: " + prod.getNome());
                }

                final List<Produto> filtermodelist = filter(MainActivity.produtosSearch, s);
       //         mAdapter.setfilter(filtermodelist);
                for (Produto prod: filtermodelist) {
                     System.out.println("Produto do Filter: " + prod.getNome());
                }

                atualizaAdapter(filtermodelist);
                return false;
            }
        });
        System.out.println("saiu createmenuitem");


        return true;

    }

    private List<Produto> filter(List<Produto> p1, String query) {
        query = query.toUpperCase();
        final List<Produto> filteredModeList = new ArrayList<>();

        for (Produto model:p1) {
            final String text=model.getNome().toUpperCase();
            if (text.startsWith(query)) {
                filteredModeList.add(model);
            }
        }

        for (Produto model:filteredModeList) {
            System.out.println("Produto na FilteredModeList: " + model.getNome());
        }
        produtosMain = filteredModeList;

        return filteredModeList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {

            case R.id.action_sair:

                AlertDialog alerta;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.encerrar_sessao);
                builder.setMessage(R.string.pergunta_encerra_sessao);
                builder.setPositiveButton(R.string.title_menu_cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
          //              Toast.makeText(MainActivity.this, R.string.title_menu_cancelar + arg1, Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton(R.string.sim, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
            //            Toast.makeText(MainActivity.this, R.string.sim + arg1, Toast.LENGTH_SHORT).show();
                        SharedPreferences prefs = getSharedPreferences("pref_lista_compras",0);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("usuarioLogado", false);
                        editor.putString("idLogin", "");
                        editor.putString("nomeCompleto", "");
                        editor.putString("usuario", "" );
                        editor.commit();

                        Login login = new Login();

                        LoginActivity.setLogin(login);

                        Intent proximaTela = new Intent(MainActivity.this,
                                LoginActivity.class);
                        startActivity(proximaTela);
                        MainActivity.this.finish();

                    }
                });
                alerta = builder.create();
                alerta.show();

                break;
            case R.id.action_sobre:
                Intent proximaTela = new Intent(MainActivity.this,
                        SobreActivity.class);
                startActivity(proximaTela);

                System.out.println("saiu sobre");
//                return super.onOptionsItemSelected(item);

                break;

        }

        return super.onOptionsItemSelected(item);

    }


    private void listar(Login login) {
        ArrayList<Produto> lista = new ArrayList<>();

        ProdutoAPI apiProduto = getRetrofit().create(ProdutoAPI.class);
        List<Produto> produtos;

        Toast.makeText(MainActivity.this,
                R.string.gerando_lista, Toast.LENGTH_LONG).show();

        System.out.println("listarProduto");
        apiProduto.listaProdutos( login.getId())
                .enqueue(new Callback<List<Produto>>() {
                    @Override
                    public void onResponse(Call<List<Produto>> salvar, Response<List<Produto>> response) {
                        if(response.isSuccessful())
                            System.out.println("response lista produto com sucesso");
                        List<Produto> produtos = response.body();
                        int i = 0;
                        for (i=0; i < produtos.size() ; i++) {
                            System.out.println("Listando Produtos: " + produtos.get(i).getNome());
                        }
                        produtosMain = produtos;
                        produtosSearch = produtos;
                        atualizaAdapter(produtosMain);

                    }

                    @Override
                    public void onFailure(Call<List<Produto>> salvar, Throwable t) {
                        System.out.println("produto failure");

                        Toast.makeText(MainActivity.this,
                                R.string.erro_lista_prod, Toast.LENGTH_LONG).show();
                    }
                });


    }

    private void atualizaAdapter(List<Produto> produtos) {

        final RecyclerView recyclerViewProdutos = (RecyclerView) findViewById(R.id.lstProdutos);
        recyclerViewProdutos.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new ProdutoRecyclerAdapter(buscarProdutos());
        recyclerViewProdutos.setAdapter(mAdapter);

        mAdapterProduto = mAdapter; // para passar em AddProd

        System.out.println("AtualizaAdapter - onItemClicado");
        recyclerViewProdutos.addOnItemTouchListener(new RecyclerOnItemClickListener(this, new RecyclerOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onItemClicado(position, view);

            }
        }));


    }


    private ArrayList<Produto> buscarProdutos() {
        ArrayList<Produto> lista = new ArrayList<>();

        int i = 0;
        for (i=0;i < produtosMain.size();i++) {

            Produto produto = new Produto();
//            produto.setId(i);
            produto.setNome(produtosMain.get(i).getNome());
            produto.setQtde(produtosMain.get(i).getQtde());

            lista.add(produto);

        }

        return lista;
    }

    private void onItemClicado(int position, View view) {
        Produto produto = mAdapter.getItem(position);
        System.out.println("entrou onItemClicado");


        if (horarioSelectItem != null) {
            Calendar horaAtual = Calendar.getInstance();
            horaAtual.setTime(new Date());
            System.out.println("HoraAtual: " + horaAtual.getTime());

            Calendar horaSelectWait = Calendar.getInstance();
            horaSelectWait.setTime(horarioSelectItem);
            horaSelectWait.add(Calendar.SECOND,2);
            System.out.println("Data HoraSelect + 2seg: " + horaSelectWait.getTime());

            if (horaSelectWait.getTime().before(horaAtual.getTime())) {
                System.out.println("Sim - hora depois de 2seg - segue clique");
            }
            else {
                System.out.println("sai do clique sem fazer nada");
                return;
            }

        }

        horarioSelectItem = new Date();
        System.out.println("Data e Hora: " + horarioSelectItem);

        List<Produto> prods= mAdapter.getListaProdutos();
        for (Produto prod:prods ) {
            System.out.println("Lista da mAdapter: " + prod.getNome());
        }

//voltar        selected_item = position;

        System.out.println("Valor Inicial do selected_item: " + selected_item);

        if (selectedView != null) {
            System.out.println("SelectedView is not null - coloca false: " + position );
            selectedView.setSelected(false);
//            view.setSelected(false);
        //    selectedView.setBackgroundResource(R.color.background_splash);
        }
        if (selectedView == view & selected_item >= 0) {
            System.out.println("SelectedView = view - coloca false: " + position );
            view.setSelected(false);
//            view.setBackgroundResource(R.color.background_splash);
            selected_item = -1; //significa que nesse momento ele desmarcou o botáo e náo tem item selecionado para alterar ou excluir
        }
        else {
            System.out.println("SelectedView <> view coloca true: " + position );
            System.out.println("marcando item: " + selected_item);
  //          view.setBackgroundResource(R.color.background_selected);
            selected_item = position;
            view.setSelected(true);

        }
        selectedView = view;

        System.out.println("Valor Final do selected_item: " + selected_item);

        Toast.makeText(getApplicationContext(), produto.getNome(), Toast.LENGTH_LONG).show();


    }


    public void incluirProduto(View view) {
        Intent proximaTela = new Intent(MainActivity.this,
                AddProdActivity.class);

        System.out.println("usuarioMain: " + usuario);
        proximaTela.putExtra("usuario", usuario);
        proximaTela.putExtra( "mAdapter", mAdapter.getClass());
        startActivity(proximaTela);

    }

    public void excluirProduto(View view) {

        if (selected_item <0) {

            Toast.makeText(getApplicationContext(), (getString(R.string.selecionar_produto_excluir)) , Toast.LENGTH_LONG).show();
        }
        else {

            Produto produto = mAdapter.getItem(selected_item);

            final String nomeProduto = produto.getNome();

            System.out.println("excluir produto: " + produto.getNome());

            AlertDialog alerta;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.excluir_produto);
            builder.setMessage(getString(R.string.pergunta_excluir_produto) + produto.getNome() + "?");
            builder.setPositiveButton(R.string.cancelar_excluir, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
//                    Toast.makeText(MainActivity.this, R.string.cancelar_excluir + arg1, Toast.LENGTH_SHORT).show();

                }
            });
            builder.setNegativeButton(R.string.sim, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
  //                  Toast.makeText(MainActivity.this, R.string.sim + arg1, Toast.LENGTH_SHORT).show();

                    excluirBaseDados(nomeProduto, login.getId());

                }
            });
            alerta = builder.create();
            alerta.show();


        }

    }

    public void alterarProduto(View view) {

        if (selected_item < 0) {
            Toast.makeText(getApplicationContext(), (getString(R.string.selecionar_para_alterar)) , Toast.LENGTH_LONG).show();
        }
        else {
            Produto produto = mAdapter.getItem(selected_item);
            System.out.println("alterar produto: " + selected_item);

            //Toast.makeText(getApplicationContext(), (getString(R.string.alterar_prod) + produto.getNome()), Toast.LENGTH_LONG).show();

            Intent proximaTela = new Intent(MainActivity.this,
                    AltProdActivity.class);


            proximaTela.putExtra("nome", produto.getNome());
            proximaTela.putExtra("qtde", produto.getQtde());
            proximaTela.putExtra("position", selected_item);
            proximaTela.putExtra( "mAdapter", mAdapter.getClass());
            startActivity(proximaTela);

        }

    }



    private void excluirBaseDados(String nome, String id) {
        ProdutoAPI apiProduto = getRetrofit().create(ProdutoAPI.class);

        apiProduto.excluirProdutos(nome, id)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> excluirProdutos, Response<Void> response) {
                        if(response.isSuccessful()) {

                            System.out.println("Excluiu produto " + login.getUsuario());
                            mAdapter.deleteItem(selected_item);
                            produtosSearch = mAdapterProduto.getListaProdutos(); // atualiza para depois utilizar na busca

                        }
                        else {
                            System.out.println("not success response excluirproduto");
                        }


                    }

                    //nao trouxe objeto Login, entáo náo existe o usuario, pode criar
                    @Override
                    public void onFailure(Call<Void> excluirProdutos, Throwable t) {
                        System.out.println("passou failure do Login Main");

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