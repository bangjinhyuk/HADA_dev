package com.hada.pillmanagement;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class PillListViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Item> items = new ArrayList<Item>();

    public PillListViewAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // item.xml 레이아웃을 inflate해서 참조획득
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item, parent, false);
        }

        // item.xml 의 참조 획득
        TextView txt_name = (TextView)convertView.findViewById(R.id.pill_name);
        TextView txt_date = (TextView)convertView.findViewById(R.id.pill_date);
        TextView txt_day = (TextView)convertView.findViewById(R.id.pill_day);
        ImageView btn_delete = (ImageView)convertView.findViewById(R.id.btn_delete);

        Item item = items.get(position);

        StringTokenizer st = new StringTokenizer(item.getDay(),"-");
        StringBuilder renameDay = new StringBuilder();
        String tmp;
        int index = st.countTokens();
        for(int i=0;i<index;i++){
            tmp = st.nextToken();
            if(tmp.equals("0")){
                renameDay.append("월");
            }else if(tmp.equals("1")){
                renameDay.append("화");
            }else if(tmp.equals("2")){
                renameDay.append("수");
            }else if(tmp.equals("3")){
                renameDay.append("목");
            }else if(tmp.equals("4")){
                renameDay.append("금");
            }else if(tmp.equals("5")){
                renameDay.append("토");
            }else renameDay.append("일");

            if(i!=index-1 && index!=1) renameDay.append("-");

        }
        // 가져온 데이터를 텍스트뷰에 입력
        txt_name.setText(item.getName());
        txt_date.setText(item.getdate()+"~"+item.getLastDate());
        txt_day.setText(renameDay.toString());

        Database database;
        SQLiteDatabase db;
        database = new Database(convertView.getContext(), "pill.db", null, 1);
        db = database.getWritableDatabase();
        database.onCreate(db);

        // 리스트 아이템 삭제
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(item.getId().toString());
                db.delete("mytable","_id=?",new String[]{item.getId().toString()});
                items.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void addItem(Long id, String name, String day, String date, String lastDate){
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDate(date);
        item.setDay(day);
        item.setLastDate(lastDate);

        items.add(item);
    }
}