package com.qupp.client.utils.dialog;




import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qupp.client.R;
import com.qupp.client.network.bean.EntityForSimple;

import java.util.List;

/**
 * 选择优惠券
 */

public class SelectCouponAdapter extends BaseMultiItemQuickAdapter<EntityForSimple, BaseViewHolder> {


    private List<EntityForSimple> data;

    public SelectCouponAdapter(List<EntityForSimple> data) {
        super(data);
        this.data = data;
        addItemType(EntityForSimple.TYPE_TOP, R.layout.item_coupon_select);
        addItemType(EntityForSimple.TYPE_OTHER, R.layout.item_coupon1_select);
    }

    @Override
    protected void convert(BaseViewHolder helper, EntityForSimple item) {

        helper.setText(R.id.tv_showSymbol, item.getShowSymbol());
        helper.setText(R.id.tv_showPrice, item.getShowPrice());
        helper.setText(R.id.tv_showRemark, item.getShowRemark());
        helper.setText(R.id.tv_couponShowName, item.getCouponShowName());
        helper.setText(R.id.tv_useTime, item.getUseTime());
        helper.setText(R.id.tv_useLimit, item.getUseLimit());

        switch (helper.getItemViewType()) {
            case EntityForSimple.TYPE_TOP:
                if(item.isSelected()){
                    helper.setVisible(R.id.iv_select,true);
                }else{
                    helper.setVisible(R.id.iv_select,false);
                }
                break;
            case EntityForSimple.TYPE_OTHER:

                break;
            default:
                break;
        }

    }


}
