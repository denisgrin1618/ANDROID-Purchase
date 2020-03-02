package com.account.sunstones.sunstones_purchase;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class FormAssemblyPhotoActivity extends AppCompatActivity {

    DataBase db;
    int assembly_id;
    Assembly assembly;
    ViewPager view_image_pager;
    ArrayList<Product> listProducts = new ArrayList();
    String string_no_filter = "<no filter>";


    String filter_order_id;
    ProductCatalog filter_product_catalog;
    ProductType filter_product_type;
    String filter_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_assembly_photo);

        db = DataBase.getInstance(this);
        assembly_id = getIntent().getIntExtra("assembly_id", 0);
        assembly = db.getAssembly(assembly_id, true);

        listProducts = db.getListProductsForAssembly();
        view_image_pager = (ViewPager) findViewById(R.id.view_image_pager);


        setFilterAdapter(assembly.products_for_assembly);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getSupportActionBar().setTitle("ASSEMBLY #" + String.valueOf(assembly.id));
        return true;
    }

    @Override
    protected void onDestroy() {
        assembly.sync = false;
        db.saveAssembly(assembly);
        super.onDestroy();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        assembly.sync = false;
        db.saveAssembly(assembly);
        Boolean rezalt = MainMenu.onOptionsItemSelected(item, this);
        if(rezalt)
            return true;
        else
            return super.onOptionsItemSelected(item);

    }

    public void onClickButtonSetAside(View view) {

        if(view_image_pager.getAdapter().getCount() > 0) {
            int index = view_image_pager.getCurrentItem();
            Assembly.Assembly_products_for_assembly pp = assembly.products_for_assembly.get(index);
            pp.set_aside = true;
            pp.changed = true;
            pp.quantity_assembly = 0;
            setNextPage();
        }
    }

    public void onClickButtonOutOfStock(View view) {

        if(view_image_pager.getAdapter().getCount() > 0) {
            int index = view_image_pager.getCurrentItem();
            Assembly.Assembly_products_for_assembly pp = assembly.products_for_assembly.get(index);
            pp.out_of_stock = true;
            pp.changed = true;
            pp.quantity_assembly = 0;
            setNextPage();
        }
    }

    public void onClickButtonOpenFilter(View view){
        openDialogFilter();
    }

    public void onClickButtonAddProductToAssembly(View view) {

        if(view_image_pager.getAdapter().getCount() > 0){
            int index = view_image_pager.getCurrentItem();
            Assembly.Assembly_products_for_assembly pp = assembly.products_for_assembly.get(index);
            openDialogAssembly(pp);
        }

    }

    private void setNextPage(){
        int index = view_image_pager.getCurrentItem();
        int next_index = index + 1;
        if(view_image_pager.getAdapter().getCount()-1 < next_index){
            next_index = next_index - 1;
        }
        view_image_pager.setCurrentItem(next_index,true);
    }

    private void initializeFilterSpinner(){

        String[] filters = {"no filter", "show products set aside", "show products out of stock", "show products ready for assembly"};
        Spinner spinner = (Spinner) findViewById(R.id.filter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final Context _context = getBaseContext();

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);

                ArrayList<Assembly.Assembly_products_for_assembly> products_for_assembly = new ArrayList();
                switch(item) {
                    case "no filter":
                        for(Assembly.Assembly_products_for_assembly pp : assembly.products_for_assembly){
                            products_for_assembly.add(pp);
                        }
                        break;

                    case "show products set aside":
                        for(Assembly.Assembly_products_for_assembly pp : assembly.products_for_assembly){
                            if(pp.set_aside == true){
                                products_for_assembly.add(pp);
                            }
                        }
                        break;

                    case "show products out of stock":
                        for(Assembly.Assembly_products_for_assembly pp : assembly.products_for_assembly){
                            if(pp.out_of_stock == true){
                                products_for_assembly.add(pp);
                            }
                        }
                        break;

                    case "show products ready for assembly":
                        for(Assembly.Assembly_products_for_assembly pp : assembly.products_for_assembly){
                            if(pp.out_of_stock == false && pp.set_aside == false && pp.quantity_order > pp.quantity_assembly){
                                products_for_assembly.add(pp);
                            }
                        }
                        break;

                    default:

                        break;
                }


                ProductsForAssemblyAdapter adapterView = new ProductsForAssemblyAdapter(_context, products_for_assembly);
                view_image_pager.setAdapter(adapterView);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

    private void openDialogAssembly(Assembly.Assembly_products_for_assembly pp){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.page_assembly_product_detail);

        EditText dialog_price_vendor_code                   = (EditText) dialog.findViewById(R.id.page_assembly_product_vendor_code);
        EditText page_assembly_product_price                = (EditText) dialog.findViewById(R.id.page_assembly_product_price);
        EditText page_assembly_product_quantity_assembly    = (EditText) dialog.findViewById(R.id.page_assembly_product_quantity_assembly);
        EditText page_assembly_product_quantity_order       = (EditText) dialog.findViewById(R.id.page_assembly_product_quantity_order);
        EditText page_assembly_product_coment               = (EditText) dialog.findViewById(R.id.page_assembly_product_coment);

//        final Double price          = page_assembly_product_price.getText().toString().isEmpty() ? 0 : Double.valueOf(page_assembly_product_price.getText().toString());
//        final int quantity_assembly = page_assembly_product_quantity_assembly.getText().toString().isEmpty() ? 0 : Integer.valueOf(page_assembly_product_quantity_assembly.getText().toString());
//        final String coment         = page_assembly_product_coment.getText().toString();

        dialog_price_vendor_code.setText(String.valueOf(pp.product.vendor_code));
        page_assembly_product_price.setText(String.valueOf(pp.price));

        int quantity = pp.quantity_order - pp.quantity_assembly;
//        quantity     = (quantity < 0 ? 0 : quantity);
        page_assembly_product_quantity_order.setText(String.valueOf(quantity));

        Button button_ok = (Button) dialog.findViewById(R.id.button_add_product);
        button_ok.setOnClickListener(new AddProductOnClickListener(pp, dialog, this));

        dialog.setCancelable(true);
        dialog.setTitle("Product " + String.valueOf(pp.product.vendor_code));
        dialog.show();

    }

    private void openDialogFilter(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_assembly_filter);

        //filter
        Spinner dialog_assembly_filter_filter       = (Spinner) dialog.findViewById(R.id.dialog_assembly_filter_filter);
        String[] array_filter = {string_no_filter, "products set aside", "products out of stock", "products ready for assembly"};
        ArrayAdapter<String> adapter_filter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, array_filter);
        adapter_filter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialog_assembly_filter_filter.setAdapter(adapter_filter);

        int index = 0;
        for(int i=0; i<array_filter.length; i++){
            if(array_filter[i].equals(filter_show)){
                index = i;
            }
        }
        dialog_assembly_filter_filter.setSelection(index);

        //type
        Spinner dialog_assembly_filter_product_type       = (Spinner) dialog.findViewById(R.id.dialog_assembly_filter_product_type);
        ArrayList<ProductType>  list_product_types = db.getProductsType();
        ProductType type_no_filter = new ProductType(0, string_no_filter, string_no_filter);
        ProductType fist_element = list_product_types.get(0);
        list_product_types.add(0, type_no_filter);
        list_product_types.add(fist_element);

        ProductType[] array_types = new ProductType[list_product_types.size()];
        list_product_types.toArray(array_types);

        ArrayAdapter<ProductType> adapter_type = new SpinProductTypeAdapter(this, android.R.layout.simple_spinner_dropdown_item, array_types);
        adapter_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialog_assembly_filter_product_type.setAdapter(adapter_type);

        index = 0;
        for(int i=0; i<array_types.length; i++){
            if(filter_product_type != null && array_types[i].id == filter_product_type.id){
                index = i;
            }
        }
        dialog_assembly_filter_product_type.setSelection(index);


        //catalog
        Spinner dialog_assembly_filter_product_catalog    = (Spinner) dialog.findViewById(R.id.dialog_assembly_filter_product_catalog);
        ArrayList<ProductCatalog>  list_product_catalogs = db.getProductsCatalogs();
        ProductCatalog catalog_no_filter = new ProductCatalog(0, string_no_filter, string_no_filter);
        ProductCatalog fist_element_ = list_product_catalogs.get(0);
        list_product_catalogs.add(0, catalog_no_filter);
        list_product_catalogs.add(fist_element_);

        ProductCatalog[] array_catalogs = new ProductCatalog[list_product_catalogs.size()];
        list_product_catalogs.toArray(array_catalogs);

        ArrayAdapter<ProductCatalog> adapter_catalog = new SpinProductCatalogAdapter(this, android.R.layout.simple_spinner_dropdown_item, array_catalogs);
        adapter_catalog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialog_assembly_filter_product_catalog.setAdapter(adapter_catalog);

        index = 0;
        for(int i=0; i<array_catalogs.length; i++){
            if(filter_product_catalog != null && array_catalogs[i].id == filter_product_catalog.id){
                index = i;
            }
        }
        dialog_assembly_filter_product_catalog.setSelection(index);


        //order
        Spinner dialog_assembly_filter_order = (Spinner) dialog.findViewById(R.id.dialog_assembly_filter_order);
        ArrayList<Integer>  list_order_ids = assembly.getListOrderIdProductsForAssembly();
        String[] array_orders = new String[list_order_ids.size()+1];
        array_orders[0] = string_no_filter;
        for(int i=1; i<=list_order_ids.size(); i++){
            array_orders[i] = String.valueOf(list_order_ids.get(i-1));
        }


        ArrayAdapter<String> adapter_orders = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, array_orders);
        adapter_orders.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialog_assembly_filter_order.setAdapter(adapter_orders);

        index = 0;
        for(int i=0; i<array_orders.length; i++){
            if(array_orders[i].equals(filter_order_id)){
                index = i;
            }
        }
        dialog_assembly_filter_order.setSelection(index);



        //Ok
        Button button_ok = (Button) dialog.findViewById(R.id.dialog_assembly_filter_button_ok);
        button_ok.setOnClickListener(new FilterOnClickListener(dialog, this));

        dialog.setCancelable(true);
        dialog.setTitle("FILTER");
        dialog.show();

    }

    public void setFilterAdapter(ArrayList<Assembly.Assembly_products_for_assembly> products_for_assembly){

        ProductsForAssemblyAdapter adapterView = new ProductsForAssemblyAdapter(this, products_for_assembly);
        view_image_pager.setAdapter(adapterView);

    }

    public class AddProductOnClickListener implements View.OnClickListener {

        Assembly.Assembly_products_for_assembly pp;
        Dialog dialog;
        FormAssemblyPhotoActivity ac;
        public AddProductOnClickListener(Assembly.Assembly_products_for_assembly pp, Dialog dialog, FormAssemblyPhotoActivity ac) {
            this.pp = pp;
            this.dialog = dialog;
            this.ac = ac;
        }

        @Override
        public void onClick(View v)
        {
            EditText dialog_price_vendor_code                   = (EditText) dialog.findViewById(R.id.page_assembly_product_vendor_code);
            EditText page_assembly_product_price                = (EditText) dialog.findViewById(R.id.page_assembly_product_price);
            EditText page_assembly_product_quantity_assembly    = (EditText) dialog.findViewById(R.id.page_assembly_product_quantity_assembly);
            EditText page_assembly_product_quantity_order       = (EditText) dialog.findViewById(R.id.page_assembly_product_quantity_order);
            EditText page_assembly_product_coment               = (EditText) dialog.findViewById(R.id.page_assembly_product_coment);

            final Double price          = page_assembly_product_price.getText().toString().isEmpty() ? 0 : Double.valueOf(page_assembly_product_price.getText().toString());
            final int quantity_assembly = page_assembly_product_quantity_assembly.getText().toString().isEmpty() ? 0 : Integer.valueOf(page_assembly_product_quantity_assembly.getText().toString());
            final String coment         = page_assembly_product_coment.getText().toString();


            pp.quantity_assembly    += quantity_assembly;
            pp.price                = price;
            pp.coment               = coment;
            pp.set_aside            = false;
            pp.out_of_stock         = false;
            pp.changed              = true;
            dialog.dismiss();
            ac.setNextPage();
        }

    };

    public class FilterOnClickListener implements View.OnClickListener {


        Dialog dialog;
        FormAssemblyPhotoActivity ac;

        public FilterOnClickListener(Dialog dialog, FormAssemblyPhotoActivity ac) {
            this.dialog = dialog;
            this.ac = ac;
        }

        @Override
        public void onClick(View v)
        {
            Spinner dialog_assembly_filter_order            = (Spinner) dialog.findViewById(R.id.dialog_assembly_filter_order);
            Spinner dialog_assembly_filter_product_catalog  = (Spinner) dialog.findViewById(R.id.dialog_assembly_filter_product_catalog);
            Spinner dialog_assembly_filter_product_type     = (Spinner) dialog.findViewById(R.id.dialog_assembly_filter_product_type);
            Spinner dialog_assembly_filter_filter           = (Spinner) dialog.findViewById(R.id.dialog_assembly_filter_filter);


            ac.filter_order_id          = (String)dialog_assembly_filter_order.getSelectedItem();;
            ac.filter_product_catalog   = (ProductCatalog)dialog_assembly_filter_product_catalog.getSelectedItem();
            ac.filter_product_type      = (ProductType)dialog_assembly_filter_product_type.getSelectedItem();
            ac.filter_show              = (String) dialog_assembly_filter_filter.getSelectedItem();



            ArrayList<Assembly.Assembly_products_for_assembly> products_for_assembly = new ArrayList<Assembly.Assembly_products_for_assembly>();
            for(Assembly.Assembly_products_for_assembly pp : assembly.products_for_assembly){

                if(     (ac.filter_order_id.equals(string_no_filter) || ac.filter_order_id.equals(String.valueOf(pp.order_id)))
                        && (ac.filter_product_catalog.name_en.equals(string_no_filter) || ac.filter_product_catalog.id == pp.product.catalog.id)
                        && (ac.filter_product_type.name_en.equals(string_no_filter) || ac.filter_product_type.id == pp.product.type.id)
                        && (ac.filter_show.equals(string_no_filter)
                                || (ac.filter_show.equals("products set aside") && pp.set_aside)
                                || (ac.filter_show.equals("products out of stock") && pp.out_of_stock)
                                || (ac.filter_show.equals("products ready for assembly") && !pp.out_of_stock && !pp.out_of_stock))
                ){
                    products_for_assembly.add(pp);
                }
            }
            setFilterAdapter(products_for_assembly);

            dialog.dismiss();
        }

    };

    public class SpinProductTypeAdapter extends ArrayAdapter<ProductType>{

        private Context context;
        private ProductType[] values;

        public SpinProductTypeAdapter(Context context, int textViewResourceId, ProductType[] values) {
            super(context, textViewResourceId, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public int getCount(){
            return values.length;
        }

        @Override
        public ProductType getItem(int position){
            return values[position];
        }

        @Override
        public long getItemId(int position){
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(values[position].name_en);

            return label;
        }

         @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(values[position].name_en);

            return label;
        }
    }

    public class SpinProductCatalogAdapter extends ArrayAdapter<ProductCatalog>{

        private Context context;
        private ProductCatalog[] values;

        public SpinProductCatalogAdapter(Context context, int textViewResourceId, ProductCatalog[] values) {
            super(context, textViewResourceId, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public int getCount(){
            return values.length;
        }

        @Override
        public ProductCatalog getItem(int position){
            return values[position];
        }

        @Override
        public long getItemId(int position){
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(values[position].name_en);

            return label;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(values[position].name_en);

            return label;
        }
    }

}
