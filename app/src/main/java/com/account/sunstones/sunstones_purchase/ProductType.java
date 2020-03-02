package com.account.sunstones.sunstones_purchase;

public class ProductType {

    public int id = 0;
    public String name_en = "";
    public String name_chn = "";

    public ProductType(){}

    public ProductType(int id, String name_en, String name_chn){
        this.id         = id;
        this.name_en    = name_en;
        this.name_chn   = name_chn;
    }

}
