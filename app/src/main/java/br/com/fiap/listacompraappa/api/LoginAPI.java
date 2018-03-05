package br.com.fiap.listacompraappa.api;


import br.com.fiap.listacompraappa.model.Login;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Cecilia_2 on 11/02/2018.
 */

public interface LoginAPI {

//    @GET("/login/validarsenha/{usuario}/{senha}")
//    Call<Login> validarSenha(@Path("usuario") String usuario, @Path("senha") String senha );

    @POST("/login")
    Call<Login> salvar(@Body Login login );

    @GET(value = "/login/validarsenha/{usuario}/{senha}")
    Call<Login> verSenha(@Path(value = "usuario") String usuario, @Path(value = "senha") String senha);

    @GET(value = "/login/usuario/{usuario}")
    Call<Login> verUsuario(@Path(value = "usuario") String usuario);

}
