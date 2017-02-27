package br.ufba.engc50.bluelock.local;

import br.ufba.engc50.bluelock.model.Usuario;

/**
 * Created by raffaello on 26/02/17.
 */

public final class Global {
    private static final String BASE_ENDPOINT = "http://192.168.25.12/~raffaello/bluelock-ws/index.php?r=site/";

    public static final String ENDPOINT_LOGIN = BASE_ENDPOINT + "autenticar&matricula={matricula}&senha={senha}";
    public static final String ENDPOINT_USUARIO_LISTAR = BASE_ENDPOINT + "listarUsuarios";
    public static final String ENDPOINT_USUARIO_ADICIONAR = BASE_ENDPOINT + "adicionarUsuario";
    public static final String ENDPOINT_USUARIO_EDITAR = BASE_ENDPOINT + "editarUsuario";
    public static final String ENDPOINT_USUARIO_REMOVER = BASE_ENDPOINT + "deletarUsuario";
    public static final String ENDPOINT_ACAO_REGISTRAR = BASE_ENDPOINT + "registrarAcesso";
    public static final String ENDPOINT_ACAO_RELATORIO = BASE_ENDPOINT + "relatorioAcesso";

    public static final String LOGIN_CALLBACK = "br.ufba.engc50.bluelock.local.LOGIN_CALLBACK";
    public static final String USUARIO_LISTAR_CALLBACK = "br.ufba.engc50.bluelock.local.USUARIO_LISTAR_CALLBACK";
    public static final String USUARIO_ADICIONAR_CALLBACK = "br.ufba.engc50.bluelock.local.USUARIO_ADICIONAR_CALLBACK";
    public static final String USUARIO_EDITAR_CALLBACK = "br.ufba.engc50.bluelock.local.USUARIO_EDITAR_CALLBACK";
    public static final String USUARIO_REMOVER_CALLBACK = "br.ufba.engc50.bluelock.local.USUARIO_REMOVER_CALLBACK";
    public static final String ACAO_REGISTRAR_CALLBACK = "br.ufba.engc50.bluelock.local.ACAO_REGISTRAR_CALLBACK";
    public static final String ACAO_RELATORIO_CALLBACK = "br.ufba.engc50.bluelock.local.ACAO_RELATORIO_CALLBACK";

    private static Usuario usuarioLogado = null;
    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
    public static void logIn(Usuario usuario){
        usuarioLogado = usuario;
    }
    public static void logOut(){
        usuarioLogado = null;
    }

}
