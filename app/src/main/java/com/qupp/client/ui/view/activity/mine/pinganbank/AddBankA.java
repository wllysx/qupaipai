package com.qupp.client.ui.view.activity.mine.pinganbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.network.bean.pingan.BankBean;
import com.qupp.client.network.bean.pingan.CityBean;
import com.qupp.client.network.bean.pingan.MessageForBank;
import com.qupp.client.network.bean.pingan.MessageForCity;
import com.qupp.client.network.bean.pingan.MessageForSubBank;
import com.qupp.client.network.bean.pingan.SubBankBean;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.event.ExitEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 添加银行卡1
 * author: MrWang on 2019/7/16
 * email:773630555@qq.com
 * date: on 2019/7/16 15:44
 */

public class AddBankA extends BaseActivity {

    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_bankcard)
    EditText etBankcard;
    @BindView(R.id.tv_bankname)
    TextView tvBankname;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.et_phone)
    EditText etPhone;
    String bankid = "", bankno = "",cityCode="";
    @BindView(R.id.tv_bankname1)
    TextView tvBankname1;

    private List<CityBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<CityBean>> options2Items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_banka);
        ButterKnife.bind(this);
        linear.setPadding(0, MyApplication.getStateBar(AddBankA.this), 0, 0);
        setStateColor(false);
        initView();
    }


    private void initView() {
        tvTitle.setText("添加银行卡");
    }


    /**
     * 启动页面
     *
     * @param activity
     */
    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, AddBankA.class)
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


    @OnClick({R.id.bt_login, R.id.back, R.id.tv_bankname, R.id.tv_city, R.id.tv_wenti, R.id.tv_bankname1})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.bt_login:
                //showToast("下一步");
                submit();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.tv_bankname:
                MyApplication.closeKeyboard(this);
                banks();
                break;
            case R.id.tv_bankname1:
                MyApplication.closeKeyboard(this);
                subbanks();
                break;
            case R.id.tv_city:
                MyApplication.closeKeyboard(this);
                areas();
                break;
            case R.id.tv_wenti:
                showToast("问题");
        }
    }

    private void areas() {
        ApiUtil.getApiService().areas(MyApplication.getToken()).enqueue(new Callback<MessageForCity>() {
            @Override
            public void onResponse(Call<MessageForCity> call, Response<MessageForCity> response) {
                MessageForCity entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                       // showPickerViewForBank(entity.getData());

                        initJsonData(entity.getData());
                        showPickerView();

                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {
                    Log.d("e111111",e.toString());

                }
            }

            @Override
            public void onFailure(Call<MessageForCity> call, Throwable t) {
            }
        });
    }

    // 弹出选择器
    private void showPickerView() {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String opt1tx = options1Items.size() > 0 ? options1Items.get(options1).getPickerViewText() : "";
            String opt2tx = options2Items.size() > 0 && options2Items.get(options1).size() > 0 ? options2Items.get(options1).get(options2).getPickerViewText() : "";
            tvCity.setText(opt1tx + "-" + opt2tx);
            cityCode = options2Items.size() > 0 && options2Items.get(options1).size() > 0 ? options2Items.get(options1).get(options2).getCityCode() : "";
        }).setTitleText("")
                .setDividerColor(getResources().getColor(R.color.line))
                .setTextColorCenter(getResources().getColor(R.color.textcolor2)) //设置选中项文字颜色
                .setContentTextSize(16)
                .setSubmitColor(getResources().getColor(R.color.iscur))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.textcolor2))//取消按钮文字颜色
                .setLineSpacingMultiplier(2f)
                .build();

        pvOptions.setPicker(options1Items, options2Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData(ArrayList<CityBean> cityBean) {//解析数据
        options1Items.clear();
        options2Items.clear();
        options1Items = cityBean;
        for (int i = 0; i < cityBean.size(); i++) {//遍历省份
            ArrayList<CityBean> cityList = new ArrayList<>();//该省的城市列表（第二级）
            for (int c = 0; c < cityBean.get(i).getPubCityList().size(); c++) {//遍历该省份的所有城市
                CityBean cityName = cityBean.get(i).getPubCityList().get(c);
                cityList.add(cityName);//添加城市
            }
            //添加城市数据
            options2Items.add(cityList);
        }
    }

    private void banks() {
        ApiUtil.getApiService().banks(MyApplication.getToken()).enqueue(new Callback<MessageForBank>() {
            @Override
            public void onResponse(Call<MessageForBank> call, Response<MessageForBank> response) {
                MessageForBank entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        showPickerViewForBank(entity.getData());
                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForBank> call, Throwable t) {
            }
        });
    }

    private void subbanks() {
        if (bankid.equals("")) {
            showToast("请选择银行");
            return;
        }
        if (cityCode.equals("")) {
            showToast("请选择地区");
            return;
        }

        ApiUtil.getApiService().subbanks(MyApplication.getToken(), bankid, cityCode).enqueue(new Callback<MessageForSubBank>() {
            @Override
            public void onResponse(Call<MessageForSubBank> call, Response<MessageForSubBank> response) {
                MessageForSubBank entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        showPickerViewForSubBank(entity.getData());
                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForSubBank> call, Throwable t) {
            }
        });
    }

    // 显示銀行
    private void showPickerViewForBank(List<BankBean> optionsbank) {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String opt1tx = optionsbank.size() > 0 ? optionsbank.get(options1).getPickerViewText() : "";
            tvBankname.setText(opt1tx);
            bankid = optionsbank.size() > 0 ? optionsbank.get(options1).getBankCode() : "";
        }).setTitleText("")
                .setDividerColor(getResources().getColor(R.color.line))
                .setTextColorCenter(getResources().getColor(R.color.textcolor2)) //设置选中项文字颜色
                .setContentTextSize(16)
                .setSubmitColor(getResources().getColor(R.color.iscur))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.textcolor2))//取消按钮文字颜色
                .setLineSpacingMultiplier(2f)
                .build();

        pvOptions.setPicker(optionsbank);
        pvOptions.setSelectOptions(0);
        pvOptions.show();
    }

    /**
     * 显示分行
     *
     * @param optionsbank
     */
    private void showPickerViewForSubBank(List<SubBankBean> optionsbank) {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String opt1tx = optionsbank.size() > 0 ? optionsbank.get(options1).getPickerViewText() : "";
            tvBankname1.setText(opt1tx);
            bankno = optionsbank.size() > 0 ? optionsbank.get(options1).getBankNo() : "";
        }).setTitleText("")
                .setDividerColor(getResources().getColor(R.color.line))
                .setTextColorCenter(getResources().getColor(R.color.textcolor2)) //设置选中项文字颜色
                .setContentTextSize(16)
                .setSubmitColor(getResources().getColor(R.color.iscur))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.textcolor2))//取消按钮文字颜色
                .setLineSpacingMultiplier(2f)
                .build();

        pvOptions.setPicker(optionsbank);
        pvOptions.setSelectOptions(0);
        pvOptions.show();
    }

    /**
     * 提交信息
     */
    private void submit() {

        HashMap map = new HashMap();
        map.put("accountName", etName.getText().toString() );
        map.put("bankCardCode",  etBankcard.getText().toString());
        map.put("bankName", tvBankname.getText().toString());
        map.put("openBankName", tvBankname1.getText().toString());
        map.put("openBankCode", bankno);
        map.put("mobilePhone", etPhone.getText().toString());
        JSONObject jasonObject = new JSONObject(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),jasonObject.toString());

        ApiUtil.getApiService().withdrawbind(MyApplication.getToken(),body).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        AddBankB.startActivityInstance(AddBankA.this,etPhone.getText().toString(),entity.getData().getId());
                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exit(ExitEvent event) {
        if (event != null) {
            finish();
        }
    }


}
