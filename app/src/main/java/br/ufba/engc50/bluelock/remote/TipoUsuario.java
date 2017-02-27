package br.ufba.engc50.bluelock.remote;

/**
 * Created by raffaello on 26/02/17.
 */

public enum TipoUsuario {
    ADMINISTRADOR(1, "Administrador"),
    USUARIO(2, "Usuário"),
    DESCONHECIDO(0, "Desconhecido");

    private int codigo;
    private String descricao;

    private TipoUsuario(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public static TipoUsuario getByCodigo(int codigo) {
        for (TipoUsuario tu: TipoUsuario.values()) {
            if(tu.getCodigo() == codigo)
                return tu;
        }
        return TipoUsuario.DESCONHECIDO;
    }
}
