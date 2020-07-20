package com.qupp.client.network.bean.pingan;


import com.contrarywind.interfaces.IPickerViewData;

import java.util.ArrayList;

/**
 *
 */
public class CityBean implements IPickerViewData {


    private String provCode;
    private String provName;
    private ArrayList<CityBean> pubCityList;

    private String areaName;
    private String cityCode;


    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        if(provName==null||provName.equals("")){
            return this.areaName;
        }
        return this.provName;

    }

    public String getProvCode() {
        return provCode;
    }

    public void setProvCode(String provCode) {
        this.provCode = provCode;
    }

    public String getProvName() {
            return provName;

    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    public ArrayList<CityBean> getPubCityList() {
        return pubCityList;
    }

    public void setPubCityList(ArrayList<CityBean> pubCityList) {
        this.pubCityList = pubCityList;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
