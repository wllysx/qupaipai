package com.qupp.client.ui.view.activity.scoreshop;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.ui.view.activity.BaseActivity;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.DoubleClick;
import com.qupp.client.utils.dialog.MiddleDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 实名认证
 * author: MrWang on 2019/8/16
 * email:773630555@qq.com
 * date: on 2019/8/16 14:03
 */


@SuppressLint("all")
public class Authentication extends BaseActivity {

    public Intent intent;
    public Bundle bundle;
    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.et_idcard)
    EditText etIdcard;
    @BindView(R.id.fl_layout)
    FrameLayout flLayout;
    @BindView(R.id.tv_idcardtype)
    TextView tvIdcardtype;
    String idCard = "";
    String name = "";
    @BindView(R.id.tv_submit)
    TextView tvSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_authentication);
        unbinder = ButterKnife.bind(this);
        int stateBarHeight = MyApplication.getStateBar(this);
        flLayout.setPadding(0, stateBarHeight, 0, 0);
        name = getIntent().getStringExtra("name");
        idCard = getIntent().getStringExtra("idCard");
        if (idCard == null || idCard.equals("")) {

        }else{
            tvSubmit.setText("更换");
            etName.setText(name);
            etIdcard.setText(idCard);
        }
        initView();
        getPersonByUserId();
    }

    private void initView() {
        tvTitle.setText("实名认证");
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = etName.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ivDelete.setVisibility(View.GONE);
                } else {
                    ivDelete.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public static void startActivityInstance(Activity activity) {
        activity.startActivity(new Intent(activity, Authentication.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @OnClick({R.id.back, R.id.iv_delete, R.id.ll_idcardtype, R.id.tv_submit, R.id.iv_about})
    public void onViewClicked(View view) {
        if (DoubleClick.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_delete:
                etName.setText("");
                break;
            case R.id.ll_idcardtype:
                break;
            case R.id.tv_submit:
                checkRealPerson();
                break;
            case R.id.iv_about:
                new MiddleDialog(this).setSureListener(() -> {
                }).show("证件号", "支持大陆身份证、台胞证、香港通行证、澳门通行证", "取消", "我知道了", true);
                break;

        }
    }

    /**
     * 认证
     */
    private void checkRealPerson() {
        ApiUtil.getApiService().realPerson(MyApplication.getToken(), etName.getText().toString(), etIdcard.getText().toString()).enqueue(new Callback<EntityForSimpleString>() {
            @Override
            public void onResponse(Call<EntityForSimpleString> call, Response<EntityForSimpleString> response) {
                EntityForSimpleString entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        finish();
                    } else {
                        showToast(entity.getMsg());
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<EntityForSimpleString> call, Throwable t) {
            }
        });
    }

    /**
     * 获取认证信息
     */
    private void getPersonByUserId() {
        ApiUtil.getApiService().getPersonByUserId(MyApplication.getToken()).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        if(entity.getData()!=null){
                            etIdcard.setText(entity.getData().getCardNo());
                            etName.setText(entity.getData().getName());
                            etName.setSelection(etName.getText().toString().length());
                        }
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
}
