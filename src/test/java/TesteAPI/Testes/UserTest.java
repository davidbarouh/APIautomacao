package TesteAPI.Testes;

import org.junit.Test;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import TesteAPI.Dominio.Usuario;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

public class UserTest extends BaseTest {

    private static final String LISTA_USUARIOS_ENDPOINT = "/users";
    private static final String CRIA_USUARIOS_ENDPOINT = "/user";
    private static final String REGISTRA_USUARIO_ENDPOINT ="/register";

    /**
     * Caso de teste para verificar se a lista de usuários é não nula
     */
    @Test
    public void PageListTest() {
        given().
                params("page","2").
        when().
                get(LISTA_USUARIOS_ENDPOINT).
        then().
                statusCode(HttpStatus.SC_OK).
                body("page", is(2)).
                body("data", is(notNullValue()));
    }

    /**
     * Caso de teste para testar que o registro está sendo feito com sucesso e com as informações requisitadas.
     */
    @Test
    public void CreateUserSucessfullyTest() {
        Usuario usuario = new Usuario("David","SW Engineer Test","teste@email.com","pistol");

        given().
                contentType(ContentType.JSON).
                body(usuario).
        when().
                post(CRIA_USUARIOS_ENDPOINT).
        then().
                statusCode(HttpStatus.SC_CREATED).
                body("name", is("David")).
                body("job", is("SW Engineer Test"));
    }
    /**
     * Caso de teste para garantir que quando um usuário for criado sem senha, retorne uma mensagem de erro e o status code 400.
     */
    @Test
    public void invalidRegistration(){
        Usuario usuario = new Usuario();
        usuario.setEmail("emaiL@teste.com");

        given().
                body(usuario).
        when().
                post(REGISTRA_USUARIO_ENDPOINT).
        then().
                statusCode(HttpStatus.SC_BAD_REQUEST).
                body("error", is("Missing password"));
    }

    /**
     * Esse caso de teste valida que o usuario será registrado e ao criar irá retornar sua ID e token.
     */
    @Test
    public void validRegistration(){
        Usuario usuario = new Usuario();
        usuario.setEmail("eve.holt@reqres.in");
        usuario.setPassword("pistol");

        given().
                contentType(ContentType.JSON).
                body(usuario).
        when().
                post(REGISTRA_USUARIO_ENDPOINT).
        then().
                statusCode(HttpStatus.SC_OK).
                body("id", is(4)).
                body("token", is("QpwL5tke4Pnpja7X4"));
    }
}
