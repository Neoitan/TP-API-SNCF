package com.mobile.tp_api_sncf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    Context context;
//    ArrayList<String> alDataStored;
    String[] data1;
    String[] data2;

    public RecyclerAdapter(Context c, String[] data1, String[] data2){
        this.context = c;
        this.data1 = data1;
        this.data2 = data2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View v = inflater.inflate(R.layout.row_object, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvData1.setText(this.data1[position]);
        holder.tvData2.setText(this.data2[position]);
        position++;
        holder.tvId.setText(""+position);
    }

    @Override
    public int getItemCount() {
        if(this.data1 != null)
            return this.data1.length;
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvId, tvData1, tvData2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tvId    = itemView.findViewById(R.id.tvId);
            this.tvData1 = itemView.findViewById(R.id.tvData1);
            this.tvData2 = itemView.findViewById(R.id.tvData2);
        }
    }
}
