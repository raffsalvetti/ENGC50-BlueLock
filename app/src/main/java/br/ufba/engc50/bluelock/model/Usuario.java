package br.ufba.engc50.bluelock.model;

import br.ufba.engc50.bluelock.remote.MicrocontrollerActions;

/**
 * Created by raffaello.salvetti on 10/02/2017.
 */
public class Usuario {
    private int matricula;
    private int senha;
    private int tipoUsuario;

    public Usuario(){}

    public Usuario(int matricula, int senha, int tipoUsuario) {
        this.matricula = matricula;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    public Usuario(String matricula, String senha, int tipoUsuario) {
        this.matricula = Integer.parseInt(matricula);
        this.senha = Integer.parseInt(senha);
        this.tipoUsuario = tipoUsuario;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public int getSenha() {
        return senha;
    }

    public void setSenha(int senha) {
        this.senha = senha;
    }

    public void setSenha(String senha) {
        this.senha = Integer.parseInt(senha);
    }

    public int getTipoUsuario() { return tipoUsuario; }

    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public boolean isAdministrador(){ return this.tipoUsuario == MicrocontrollerActions.TIPO_USUARIO_ADMIN; }
}
