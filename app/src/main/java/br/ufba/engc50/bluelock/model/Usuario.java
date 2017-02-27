package br.ufba.engc50.bluelock.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufba.engc50.bluelock.remote.MicrocontrollerActions;
import br.ufba.engc50.bluelock.remote.TipoUsuario;

/**
 * Created by raffaello.salvetti on 10/02/2017.
 */
public class Usuario implements Parcelable {
    private int codigo;
    private int matricula;
    private int senha;
    private int tipoUsuario;
    private Date dataExclusao;

    public Usuario(){}

    public Usuario(String jsonString){
        try {
            String dt;
            JSONObject jObject = new JSONObject(jsonString);
            this.codigo = jObject.getInt("codigo");
            this.matricula = jObject.getInt("matricula");
            this.senha = jObject.getInt("senha");
            this.tipoUsuario = jObject.getInt("cod_tipo_usuario");
            dt = jObject.getString("data_exclusao");
            if(!dt.equals("null"))
                this.dataExclusao = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dt);
        }catch (Exception ex) {
            Log.e("USUARIO", "Erro convertendo jsonString: " + ex.getMessage());
        }
    }

    public Usuario(JSONObject jObject) {
        try {
            String dt;
            this.codigo = jObject.getInt("codigo");
            this.matricula = jObject.getInt("matricula");
            this.senha = jObject.getInt("senha");
            this.tipoUsuario = jObject.getInt("cod_tipo_usuario");
            dt = jObject.getString("data_exclusao");
            if(!dt.equals("null"))
                this.dataExclusao = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dt);
        }catch (Exception ex) {
            Log.e("USUARIO", "Erro convertendo jsonString: " + ex.getMessage());
        }
    }

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

    protected Usuario(Parcel in) {
        codigo = in.readInt();
        matricula = in.readInt();
        senha = in.readInt();
        tipoUsuario = in.readInt();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
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

    public Date getDataExclusao() {
        return dataExclusao;
    }

    public void setDataExclusao(Date dataExclusao) {
        this.dataExclusao = dataExclusao;
    }

    public boolean isAdministrador(){ return this.tipoUsuario == TipoUsuario.ADMINISTRADOR.getCodigo(); }

    public UrlEncodedFormEntity getPostParameters() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Usuario[codigo]", String.valueOf(this.getCodigo())));
        nameValuePairs.add(new BasicNameValuePair("Usuario[matricula]", String.valueOf(this.getMatricula())));
        nameValuePairs.add(new BasicNameValuePair("Usuario[senha]", String.valueOf(this.getSenha())));
        nameValuePairs.add(new BasicNameValuePair("Usuario[cod_tipo_usuario]", String.valueOf(this.getTipoUsuario())));
        try {
            return new UrlEncodedFormEntity(nameValuePairs);
        } catch (Exception ex){
            Log.e("Usuario", "erro criando parametros post: " + ex.getMessage());
            return null;
        }
    }

    public static ArrayList<Usuario> buildListUsuario(String json){
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
        try {
            JSONArray jArray = new JSONArray(json);
            for (int i = 0 ; i < jArray.length() ; i++) {
                usuarios.add(new Usuario(jArray.getJSONObject(i)));
            }
        }catch (Exception ex) {
            Log.e("USUARIO", "Erro convertendo json: " + ex.getMessage());
        }
        return usuarios;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(codigo);
        parcel.writeInt(matricula);
        parcel.writeInt(senha);
        parcel.writeInt(tipoUsuario);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;

        if (codigo != usuario.codigo) return false;
        if (matricula != usuario.matricula) return false;
        if (senha != usuario.senha) return false;
        if (tipoUsuario != usuario.tipoUsuario) return false;
        return dataExclusao != null ? dataExclusao.equals(usuario.dataExclusao) : usuario.dataExclusao == null;

    }

    @Override
    public int hashCode() {
        int result = codigo;
        result = 31 * result + matricula;
        result = 31 * result + senha;
        result = 31 * result + tipoUsuario;
        result = 31 * result + (dataExclusao != null ? dataExclusao.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Código: " + codigo + "; Matrícula: " + matricula + "; Senha: " + senha + "; " + (isAdministrador() ? "É " : "Não é ") + "administrador.";
    }
}

