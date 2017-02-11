package br.ufba.engc50.bluelock.remote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.ufba.engc50.bluelock.model.Usuario;

/**
 * Created by raffaello.salvetti on 10/02/2017.
 */
public class MicrocontrollerActions {
    public static int TIPO_USUARIO_ADMIN = 0;
    public static int TIPO_USUARIO_USER = 1;

    private static Usuario usuarioLogado = null;

    private List<Usuario> usuarios = new ArrayList<Usuario>(Arrays.asList(new Usuario[] {
        new Usuario(6666,6666,TIPO_USUARIO_ADMIN), new Usuario(1234,1234,TIPO_USUARIO_USER)
    }));

    public int autenticar(String matricula, String password) {
        for (Usuario u : usuarios) {
            if (u.getMatricula() == Integer.parseInt(matricula) && u.getSenha() == Integer.parseInt(password)) {
                usuarioLogado = u;
                return usuarioLogado.getTipoUsuario();
            }
        }
        this.usuarioLogado = null;
        return -1;
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
    public static void logOut(){
        usuarioLogado = null;
    }

    public List<Usuario> listarUsuarios() {
        return usuarios;
    }

    public boolean criarUsuario(Usuario usuario) {
        return usuarios.add(usuario);
    }

    public boolean modificarUsuario(Usuario usuario) {
        return false; //usuarios.add(usuario);
    }

    public boolean apagarUsuario(Usuario usuario) {
        return usuarios.remove(usuario);
    }
}
