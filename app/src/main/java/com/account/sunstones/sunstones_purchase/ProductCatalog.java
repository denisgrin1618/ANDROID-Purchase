package com.account.sunstones.sunstones_purchase;

public class ProductCatalog {

    public int id = 0;
    public String name_en = "";
    public String name_chn = "";

    public ProductCatalog(){}

    public ProductCatalog(int id, String name_en, String name_chn){
        this.id         = id;
        this.name_en    = name_en;
        this.name_chn   = name_chn;
    }
}
