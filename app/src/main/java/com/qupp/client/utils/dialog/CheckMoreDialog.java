package com.qupp.client.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.qupp.client.MyApplication;
import com.qupp.client.R;
import com.qupp.client.network.ApiUtil;
import com.qupp.client.network.bean.EntityForSimple;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.utils.adapter.CheckMoreAdapter;
import com.qupp.client.utils.glide.GlideRoundTransform;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 中间弹出dialog
 */

public class CheckMoreDialog {

    private Context context;
    private Dialog dialog;
    CheckMoreDialog middleDialog;

    public CheckMoreDialog(Context context) {
        super();
        this.context = context;
        middleDialog = this;
        // TODO Auto-generated constructor stub
    }

    public void show(String url, String goodsname, String goodsid) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_check_more, null);
        ViewHolder viewHolder = new ViewHolder(view);
        initData(goodsid,viewHolder.listview);

        viewHolder.tvGoodsName.setText(goodsname);
        RequestOptions options1 = new RequestOptions()
                .centerCrop()
                .priority(Priority.HIGH)//优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE)//缓存策略
                .transform(new GlideRoundTransform(4));
        Glide.with(context).load(url).apply(options1).into(viewHolder.ivImg);
        viewHolder.ivClose.setOnClickListener(v -> dismiss());


        dialog = new Dialog(context, R.style.ActionSheetDialogTopStyle);
        dialog.setContentView(view);

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        dialog.show();


    }

    private void initData(String id,ListView listView){

        ApiUtil.getApiService().mallAuctionDetail(MyApplication.getToken(),id).enqueue(new Callback<MessageForSimple>() {
            @Override
            public void onResponse(Call<MessageForSimple> call, Response<MessageForSimple> response) {
                MessageForSimple entity = response.body();
                try {
                    if (entity.getCode().equals("0")) {
                        ArrayList<EntityForSimple> mlist =entity.getData().getRecords();
                        listView.setAdapter(new CheckMoreAdapter(context,mlist));
                    } else {
                        Toast.makeText(context,entity.getMsg(),Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<MessageForSimple> call, Throwable t) {
            }
        });
    }


    public void dismiss() {
        dialog.dismiss();
    }

    public static interface Sure {
        void onSure();
    }

    public Sure sure;

    /**
     * 点击确定回调的监听
     *
     * @param sure
     */
    public CheckMoreDialog setSureListener(Sure sure) {
        this.sure = sure;
        return middleDialog;
    }

    public static interface MCancel {
        void onCancel();
    }


    public MCancel mCancel;

    /**
     * 点击取消回调的监听
     *
     * @param
     */
    public void setCancelListener(MCancel mCancel) {
        this.mCancel = mCancel;
    }

    static class ViewHolder {
        @BindView(R.id.iv_img)
        ImageView ivImg;
        @BindView(R.id.tv_goodsName)
        TextView tvGoodsName;
        @BindView(R.id.listview)
        ListView listview;
        @BindView(R.id.iv_close)
        ImageView ivClose;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
