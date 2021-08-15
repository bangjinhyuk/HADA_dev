package com.hada.fridgeapplication;

import android.content.Context;
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

    @Override
    public int getItemCount() { // 아이템의 개수 반환
        return data.size() ;
    }

    @Override
    public void onBindViewHolder(SensorAdapter.ViewHolder holder, int position) {
        holder.cardView.setMinimumWidth(width);
        holder.cardView.setMinimumHeight(height/8);

        // ViewHolder에 정의된 텍스트뷰에 데이터의 텍스트를 출력
        holder.sensor_title.setText(data.get(position).getSensorName());
        holder.temperature.setText(data.get(position).getTemperature());
        holder.humidity.setText(data.get(position).getHumidity());
        if(data.get(position).getStatus() == 0) holder.sensor_color.setImageResource(R.drawable.yellow);
        else if(data.get(position).getStatus() == 1) holder.sensor_color.setImageResource(R.drawable.green);
        else holder.sensor_color.setImageResource(R.drawable.red);
        // ViewHolder에 정의된 이미지뷰에 데이터의 이미지 경로의 이미지 출력
//        Glide.with(context).load(data.get(position).get()).override(1024).into(holder.rv_image);
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

