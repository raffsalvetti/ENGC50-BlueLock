package br.ufba.engc50.bluelock;

import android.content.Context;
import android.widget.ArrayAdapter;

import br.ufba.engc50.bluelock.model.Usuario;

/**
 * Created by raffaello on 11/02/17.
 */

public class UsuarioArrayAdapter extends ArrayAdapter<Usuario> {
    public UsuarioArrayAdapter(Context context, int resource) {
        super(context, resource);
    }
    
}
