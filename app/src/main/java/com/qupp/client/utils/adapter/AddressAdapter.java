package com.qupp.client.utils.adapter;



import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 我的地址
 */

public class AddressAdapter extends BaseQuickAdapter<EntityForSimple, BaseViewHolder> {


    public AddressAdapter(List<EntityForSimple> data) {
        super(R.layout.item_address, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {

        helper.setText(R.id.tv_consignee,item.getConsignee());
        helper.setText(R.id.tv_mobile, MyApplication.getPhonePass(item.getMobile()));
        helper.setText(R.id.tv_address,item.getProvinceText()+"-"+item.getCityText()+"-"+item.getDistrictText()+" "+item.getStreetText()+" "+item.getAddress());

        helper.addOnClickListener(R.id.tv_delete);
        helper.addOnClickListener(R.id.tv_edit);
        helper.addOnClickListener(R.id.ll_default_address);
        ((CheckBox) helper.getView(R.id.checkbox)).setChecked(item.getTolerant().equals("0")?true:false);

    }

}
