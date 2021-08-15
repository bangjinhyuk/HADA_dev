package com.hada.fridgeapplication;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.ViewHolder> {

    private ArrayList<SensorModel> data; // 모델화된 데이터들을 리스트로 받아옴
    private Context context;
    private int width,height;

    private String TAG = "SensorAdapter click event";

    SensorAdapter(ArrayList<SensorModel> data, Context context, int width, int height) { // 생성자
        this.data = data;
        this.context = context;
        this.width = width;
        this.height = height;
    }

    @Override
    public SensorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Adapter 내부에 정의된 ViewHolder에 정의된 레이아웃을 inflate해서 반환
        return new SensorAdapter.ViewHolder(inflater.inflate(R.layout.sensor_list, parent, false));
    }
    // 리스너 객체 참조를 저장하는 변수
    private ItemClickListener mListener = null;

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(ItemClickListener listener)
    {
        this.mListener = listener;
    }

    @Override
    public int getItemCount() { // 아이템의 개수 반환
        return data.size() ;
    }

    @Override
    public void onBindViewHolder(SensorAdapter.ViewHolder holder, int position) {
        // ViewHolder에 정의된 텍스트뷰에 데이터의 텍스트를 출력
        holder.sensor_title.setText(data.get(position).getSensorName());
        holder.temperature.setText(data.get(position).getTemperature());
        holder.humidity.setText(data.get(position).getHumidity());
        if(data.get(position).getStatus() == 0) holder.sensor_color.setImageResource(R.drawable.yellow);
        else if(data.get(position).getStatus() == 1) holder.sensor_color.setImageResource(R.drawable.green);
        else holder.sensor_color.setImageResource(R.drawable.red);

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               int pos = holder.getAdapterPosition();
               if (pos != RecyclerView.NO_POSITION)
               {
                   Intent intent = new Intent(holder.itemView.getContext(),DetailActivity.class);
                   intent.putExtra("title",data.get(pos).getSensorName());
                   intent.putExtra("id",Integer.toString(data.get(pos).getId()));
                   holder.itemView.getContext().startActivity(intent);
                   Log.d(TAG, data.get(pos).getSensorName());
                   Log.d(TAG, Integer.toString(data.get(pos).getId()));

               }
           }
       });
    }

    // ViewHolder 클래스 정의를 통해 Adapter에서 사용할 뷰들을 연결
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sensor_title;
        TextView temperature;
        TextView humidity;
        ImageView sensor_color;
        CardView cardView;
        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.sensor_cardview);
            sensor_title = itemView.findViewById(R.id.fridge_name);
            temperature = itemView.findViewById(R.id.temperature);
            humidity = itemView.findViewById(R.id.humidity);
            sensor_color = itemView.findViewById(R.id.img_color);

        }
    }
}

