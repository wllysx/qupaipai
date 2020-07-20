package com.qupp.client.ui.view.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.JsonBean;
import com.qupp.client.network.bean.MessageForAddress;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.EdittextUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 收件地址-添加地址
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class MyAddressAddActivity extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    String province = "", city = "", district = "",street="";
    int p1 = 0, p2 = 0, p3 = 0;
    @BindView(R.id.bg_submit)
    TextView bgSubmit;
    @BindView(R.id.ll_default_address)
    LinearLayout llDefaultAddress;
    @BindView(R.id.tv_jiedao)
    TextView tvJiedao;

    //省市区
    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<JsonBean> allcitydata = new ArrayList<>();
    private ArrayList<ArrayList<JsonBean>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<JsonBean>>> options3Items = new ArrayList<>();
    //街道
    private List<JsonBean> optionsStreet = new ArrayList<>();
    String open = "", addressId = "";
    EntityForSimple entity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(MyAddressAddActivity.this), 0, 0);
        open = getIntent().getStringExtra("open");
        entity = ((EntityForSimple) getIntent().getSerializableExtra("entity"));
        allcitydata = Paper.book("jyk").read("citydata", new ArrayList<>());
        if (entity == null) {
        } else {
            open = entity.getTolerant().equals("0") ? "1" : "0";
            etAddress.setText(entity.getAddress());
            etName.setText(entity.getConsignee());
            etPhone.setText(entity.getMobile());
            province = entity.getProvince();
            city = entity.getCity();
            district = entity.getDistrict();
            street = entity.getStreet();
            tvCity.setText(entity.getProvinceText() + "-" + entity.getCityText() + "-" + entity.getDistrictText());
            tvJiedao.setText(entity.getStreetText());
            addressId = entity.getAddressId();

            if (allcitydata != null) {
                for (int i = 0; i < allcitydata.size(); i++) {
                    if (allcitydata.get(i).getId().equals(province)) {
                        p1 = i;
                        for (int j = 0; j < allcitydata.get(i).getSubAddressList().size(); j++) {
                            if (allcitydata.get(i).getSubAddressList().get(j).getId().equals(city)) {
                                p2 = j;
                                for (int k = 0; k < allcitydata.get(i).getSubAddressList().get(j).getSubAddressList().size(); k++) {
                                    if (allcitydata.get(i).getSubAddressList().get(j).getSubAddressList().get(k).getId().equals(district)) {
                                        p3 = k;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        setStateColor(false);
        initView();

    }


    private void initView() {
        if (addressId.equals("")) {
            tvTitle.setText("新增地址");
            bgSubmit.setText("确认添加");
            llDefaultAddress.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setText("编辑地址");
            bgSubmit.setText("确认修改");
            llDefaultAddress.setVisibility(View.GONE);
        }

        checkbox.setChecked(open.equals("0") ? false : true);
        initJsonData(allcitydata);
        EdittextUtil.setEdittextCount(etName, 20);
    }


    /**
     * @param activity
     * @param open     0有数据1无数
     */
    public static void startActivityInstance(Activity activity, String open, EntityForSimple entity) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("entity", entity);
        activity.startActivity(new Intent(activity, MyAddressAddActivity.class)
                .putExtra("open", open)
                .putExtras(bundle)
        );
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick({R.id.back, R.id.ll_default_address, R.id.bg_submit, R.id.tv_city,R.id.tv_jiedao})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_default_address:
                checkbox.setChecked(!checkbox.isChecked());
                break;
            case R.id.bg_submit:
                if (etName.getText().toString().isEmpty()) {
                    showToast("请输入收件人姓名");
                    return;
                }
                if (etPhone.getText().toString().isEmpty()) {
                    showToast("请输入手机号");
                    return;
                }
                if (etPhone.getText().toString().length() < 11) {
                    showToast("请输入正确手机号");
                    return;
                }

                if (etAddress.getText().toString().isEmpty()) {
                    showToast("请输入详细地址");
                    return;
                }
                if (province.isEmpty()) {
                    showToast("请选择省市区");
                    return;
                }
                if (addressId.equals("")) {
                    addAddress();
                } else {
                    editAddress();
                }
                //addAddress();
                break;
            case R.id.tv_city:
                MyApplication.closeKeyboard(MyAddressAddActivity.this);
                showPickerView();
                break;
            case R.id.tv_jiedao:
                if (district.isEmpty()) {
                    showToast("请选择省市区");
                    return;
                }
                initData(district);
                break;
        }
    }

    // 弹出选择器
    private void showPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String opt1tx = options1Items.size() > 0 ? options1Items.get(options1).getPickerViewText() : "";
            String opt2tx = options2Items.size() > 0 && options2Items.get(options1).size() > 0 ? options2Items.get(options1).get(options2).getAreaname() : "";
            String opt3tx = options2Items.size() > 0 && options3Items.get(options1).size() > 0 && options3Items.get(options1).get(options2).size() > 0 ? options3Items.get(options1).get(options2).get(options3).getAreaname() : "";
            String tx = opt1tx + opt2tx + opt3tx;
            tvCity.setText(opt1tx + "-" + opt2tx + "-" + opt3tx);
            province = options1Items.size() > 0 ? options1Items.get(options1).getId() : "";
            city = options2Items.size() > 0 && options2Items.get(options1).size() > 0 ? options2Items.get(options1).get(options2).getId() : "";
            district = options2Items.size() > 0 && options3Items.get(options1).size() > 0 && options3Items.get(options1).get(options2).size() > 0 ? options3Items.get(options1).get(options2).get(options3).getId() : "";
            if(district.equals("")){
                district = city;
            }
            //重新选择省市区清空街道
            street = "";
            tvJiedao.setText("");
        }).setTitleText("")
                .setDividerColor(getResources().getColor(R.color.line))
                .setTextColorCenter(getResources().getColor(R.color.textcolor2)) //设置选中项文字颜色
                .setContentTextSize(16)
                .setSubmitColor(getResources().getColor(R.color.iscur))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.textcolor2))//取消按钮文字颜色
                .build();

        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.setSelectOptions(p1, p2, p3);
        pvOptions.show();
    }

    // 显示街道
    private void showPickerView1() {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String opt1tx = optionsStreet.size() > 0 ? optionsStreet.get(options1).getPickerViewText() : "";
            String tx = opt1tx;
            tvJiedao.setText(opt1tx);

            street = optionsStreet.size() > 0 ? optionsStreet.get(options1).getId() : "";
        }).setTitleText("")
                .setDividerColor(getResources().getColor(R.color.line))
                .setTextColorCenter(getResources().getColor(R.color.textcolor2)) //设置选中项文字颜色
                .setContentTextSize(16)
                .setSubmitColor(getResources().getColor(R.color.iscur))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.textcolor2))//取消按钮文字颜色
                .build();

        pvOptions.setPicker(optionsStreet);//三级选择器
        pvOptions.setSelectOptions(0);
        pvOptions.show();
    }


    private void initJsonData(ArrayList<JsonBean> jsonBean) {//解析数据
        options1Items = jsonBean;
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<JsonBean> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<JsonBean>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            for (int c = 0; c < jsonBean.get(i).getSubAddressList().size(); c++) {//遍历该省份的所有城市
                JsonBean cityName = jsonBean.get(i).getSubAddressList().get(c);
                cityList.add(cityName);//添加城市
                ArrayList<JsonBean> city_AreaList = new ArrayList<>();//该城市的所有地区列表
                city_AreaList.addAll(jsonBean.get(i).getSubAddressList().get(c).getSubAddressList());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }
            //添加城市数据
            options2Items.add(cityList);
            //添加地区数据
            options3Items.add(province_AreaList);
        }
    }


    /**
     * 添加地址
     */
    private void addAddress() {
        ApiUtil.getApiService().addressadd(MyApplication.getToken(), null, etName.getText().toString(), province, city, district, street, etAddress.getText().toString(), etPhone.getText().toString(), checkbox.isChecked() ? "0" : "1").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        showToast("添加成功");
                        finish();
                    } else {
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }

    private void editAddress() {
        ApiUtil.getApiService().addressedit(MyApplication.getToken(), addressId, etName.getText().toString(), province, city, district, street, etAddress.getText().toString(), etPhone.getText().toString(), checkbox.isChecked() ? "0" : "1").enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        showToast("修改成功");
                        finish();
                    } else {
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }

    private void initData(String parentId) {
        ApiUtil.getApiService().addressList(parentId).enqueue(new Callback<MessageForAddress>() {
            @Override
            public void onResponse(Call<MessageForAddress> call, Response<MessageForAddress> response) {
                MessageForAddress entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        optionsStreet = entity.getData();
                        showPickerView1();
                    } else {
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<MessageForAddress> call, Throwable t) {
            }
        });
    }

}
