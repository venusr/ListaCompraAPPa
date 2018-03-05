package br.com.fiap.listacompraappa.api;

import java.util.List;

import br.com.fiap.listacompraappa.model.Login;
import br.com.fiap.listacompraappa.model.Produto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Cecilia_2 on 11/02/2018.
 */

public interface ProdutoAPI {

    @GET(value = "/produto/produtos/{idLogin}")
    Call<List<Produto>> listaProdutos(@Path(value = "idLogin") String idLogin);

    @POST("/produto")
    Call<Produto> salvar(@Body Produto produto );

    @DELETE(value = "/produto/nome/{nome}/{idLogin}")
    Call<Void> excluirProdutos(@Path(value = "nome") String nome,@Path(value = "idLogin") String idLogin);

    @POST(value = "/produto/altera")
    Call<Produto> alterarProduto(@Body Produto produto) ;

    @GET(value = "/produto/nome/{nome}/{idLogin}")
    Call<Produto> buscarProduto(@Path(value = "nome") String nome,@Path(value = "idLogin") String idLogin);


}
