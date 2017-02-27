package br.ufba.engc50.bluelock.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by raffaello on 27/02/17.
 */

public class Registro implements Parcelable {
    private int matricula;
    private Date data;
    private String acao;

    public Registro(JSONObject jsonObject) {
        try {
            String dt;
            matricula = jsonObject.getInt("matricula");
            acao = jsonObject.getString("acao");
            dt = jsonObject.getString("data");
            if(!dt.equals("null"))
                this.data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dt);
        } catch (Exception ex) {
            Log.e("Registro", "erro montando o objeto usando json");
        }
    }

    public Registro(String json) {
        try {
            String dt;
            JSONObject jsonObject = new JSONObject(json);
            matricula = jsonObject.getInt("matricula");
            acao = jsonObject.getString("acao");
            dt = jsonObject.getString("data");
            if(!dt.equals("null"))
                this.data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dt);
        } catch (Exception ex) {
            Log.e("Registro", "erro montando o objeto usando json");
        }
    }

    public Registro() { }

    public Registro(int matricula, Date data, String acao) {
        this.matricula = matricula;
        this.data = data;
        this.acao = acao;
    }

    protected Registro(Parcel in) {
        matricula = in.readInt();
        acao = in.readString();
    }

    public static final Creator<Registro> CREATOR = new Creator<Registro>() {
        @Override
        public Registro createFromParcel(Parcel in) {
            return new Registro(in);
        }

        @Override
        public Registro[] newArray(int size) {
            return new Registro[size];
        }
    };

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Registro registro = (Registro) o;

        if (matricula != registro.matricula) return false;
        if (data != null ? !data.equals(registro.data) : registro.data != null) return false;
        return acao != null ? acao.equals(registro.acao) : registro.acao == null;

    }

    @Override
    public int hashCode() {
        int result = matricula;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (acao != null ? acao.hashCode() : 0);
        return result;
    }

    public static ArrayList<Registro> buildListRegistro(String json){
        ArrayList<Registro> registros = new ArrayList<Registro>();
        try {
            JSONArray jArray = new JSONArray(json);
            for (int i = 0 ; i < jArray.length() ; i++) {
                registros.add(new Registro(jArray.getJSONObject(i)));
            }
        }catch (Exception ex) {
            Log.e("Registro", "Erro convertendo json: " + ex.getMessage());
        }
        return registros;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(matricula);
        parcel.writeString(acao);
    }
}
