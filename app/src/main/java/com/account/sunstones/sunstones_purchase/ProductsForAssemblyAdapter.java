package com.account.sunstones.sunstones_purchase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;

public class ProductsForAssemblyAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<Assembly.Assembly_products_for_assembly> mListProducts = new ArrayList();
    private DataBase mData;
    private int mTextSize;

    ProductsForAssemblyAdapter(Context context, ArrayList<Assembly.Assembly_products_for_assembly> listProducts) {
        this.mContext       = context;
        this.mListProducts  = listProducts;
        this.mData          = DataBase.getInstance(context);
        this.mTextSize      = mData.getPhontSize();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.page_assembly_product, null);
        ImageView form_order_photo_image = (ImageView) view.findViewById(R.id.form_order_photo_image);
        TextView text_view_page          = (TextView) view.findViewById(R.id.text_view_page);

        String pager = String.format("Ordered product %s/%s", position+1, mListProducts.size());
        text_view_page.setText(pager);

        Product product = mListProducts.get(position).product;
        Bitmap image    = makePhotoWithVendorCode(product);
        form_order_photo_image.setImageBitmap(image);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    private Bitmap makePhotoWithVendorCode(Product product){

        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(mTextSize);

        Bitmap bitmap = Bitmap.createBitmap(mData.getPhotoOriginal(product.photo_id));
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(mutableBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawCircle(product.x, product.y, 10, p);

        String text = Integer.toString(product.vendor_code);
        float width_text = p.measureText(text);
        float X = (bitmap.getWidth() < product.x + width_text ?   bitmap.getWidth() - width_text : product.x);
        float Y = product.y;
        canvas.drawText(text, X, Y, p);

        return mutableBitmap;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

    @Override
    public int getCount() {
        return mListProducts.size();
    }

}
