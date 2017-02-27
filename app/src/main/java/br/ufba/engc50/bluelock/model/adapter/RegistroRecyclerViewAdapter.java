package br.ufba.engc50.bluelock.model.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.ufba.engc50.bluelock.R;
import br.ufba.engc50.bluelock.model.Registro;
import br.ufba.engc50.bluelock.view.fragment.RegistroFragment;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Registro} and makes a call to the
 * specified {@link br.ufba.engc50.bluelock.view.fragment.RegistroFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RegistroRecyclerViewAdapter extends RecyclerView.Adapter<RegistroRecyclerViewAdapter.ViewHolder> {

    private final List<Registro> mValues;
    private final RegistroFragment.OnListFragmentInteractionListener mListener;

    public RegistroRecyclerViewAdapter(List<Registro> items, RegistroFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_registro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mMatriculaView.setText(String.valueOf(mValues.get(position).getMatricula()));
        holder.mDataView.setText(mValues.get(position).getData_ddMMyyyyHHmmss());
        holder.mAcaoView.setText(mValues.get(position).getAcao());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mMatriculaView;
        public final TextView mDataView;
        public final TextView mAcaoView;
        public Registro mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mMatriculaView = (TextView) view.findViewById(R.id.registroMatricula);
            mDataView = (TextView) view.findViewById(R.id.registroData);
            mAcaoView = (TextView) view.findViewById(R.id.registroAcao);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDataView.getText() + "'";
        }
    }
}
