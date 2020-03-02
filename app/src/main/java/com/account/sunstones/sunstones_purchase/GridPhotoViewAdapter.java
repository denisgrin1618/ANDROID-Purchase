package com.account.sunstones.sunstones_purchase;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridPhotoViewAdapter extends BaseAdapter{


    private int m_offer_current_filter = 1;
    private ArrayList<Photo> m_list_photo;
    private ArrayList<Photo> m_list_photo_delete = new ArrayList<Photo>();
    private Context m_context;
    private Offer m_offer;
    private static LayoutInflater inflater = null;
    private Boolean m_check_box_delete_vivsible = false;

    public GridPhotoViewAdapter(FormOfferActivity mainActivity, Offer offer) {
        this.m_context              = mainActivity;
        this.m_offer                = offer;
        this.m_list_photo           = offer.getFilteredListPhotos();
        this.m_offer_current_filter = offer.curentFilter;
        this.inflater               = (LayoutInflater)m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return m_list_photo.size(); //result.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView os_text;
        ImageView os_img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.grid_photo_item, null);
        holder.os_text =(TextView) rowView.findViewById(R.id.grid_photo_item_title);
        holder.os_img  =(ImageView) rowView.findViewById(R.id.grid_photo_item_image);

        CheckBox grid_photo_item_check_box_delete = (CheckBox) rowView.findViewById(R.id.grid_photo_item_check_box_delete);
        grid_photo_item_check_box_delete.setVisibility(m_check_box_delete_vivsible == true ? View.VISIBLE : View.INVISIBLE);

        int quantityProducts = m_list_photo.get(position).getQuantityProducts();
        String title         = "products:" + quantityProducts;

        if(quantityProducts == 0)
            holder.os_img.setBackgroundResource(R.drawable.image_border_red);
        else
            holder.os_img.setBackgroundResource(R.drawable.image_border_green);


        holder.os_text.setText(title);
        holder.os_img.setImageBitmap(m_list_photo.get(position).photo_original);

        rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

//                Toast toast = Toast.makeText(context, "You Clicked "+m_list_photo.get(position).title, Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();

                Intent intent = new Intent(m_context, PhotoActivity.class);
                intent.putExtra("photo_id", m_list_photo.get(position).photo_id);
                intent.putExtra("offer_id", m_list_photo.get(position).offer_id);
                intent.putExtra("offer_current_filter", m_offer_current_filter);
                m_context.startActivity(intent);

            }
        });

        grid_photo_item_check_box_delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked == true){
                    m_list_photo_delete.add(m_list_photo.get(position));
                }else{
                    m_list_photo_delete.remove(m_list_photo.get(position));
                }

            }
        });

        return rowView;
    }

    public void setVisibleCheckBoxDelete(Boolean visible){
        m_check_box_delete_vivsible = visible;

    }

    public ArrayList<Photo> getSelectedPhotosForDelete(){
        return m_list_photo_delete;
    }
}
