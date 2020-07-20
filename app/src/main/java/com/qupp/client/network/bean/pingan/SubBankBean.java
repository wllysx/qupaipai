package com.qupp.client.network.bean.pingan;


import com.contrarywind.interfaces.IPickerViewData;

/**
 *
 */
public class SubBankBean implements IPickerViewData {


    private String bankNo;
    private String bankName;


    public String getBankNo() {
        return bankNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return this.bankName;
    }

}
