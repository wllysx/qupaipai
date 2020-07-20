package com.qupp.client.network;


import com.qupp.client.network.bean.EntityForSimpleB;
import com.qupp.client.network.bean.EntityForSimpleString;
import com.qupp.client.network.bean.MessageForAddress;
import com.qupp.client.network.bean.MessageForList;
import com.qupp.client.network.bean.MessageForListString;
import com.qupp.client.network.bean.MessageForSimple;
import com.qupp.client.network.bean.pingan.MessageForBank;
import com.qupp.client.network.bean.pingan.MessageForCity;
import com.qupp.client.network.bean.pingan.MessageForSubBank;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface ApiService{


    /**
     * 通用接口
     * @param body {'username':'thinkgem','password':'jeesite'}
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("{api}")
    Observable<String> getDatas1(@Path("api") String api, @Body RequestBody body);//传入的参数为RequestBody

    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("{api}")
    Call<String> getDatas(@Path("api") String api, @Body RequestBody body);//传入的参数为RequestBody

    /**
     * 注册融云
     * @param id
     * @param name
     * @param portrait
     * @return
     */
    @POST("app/rongyun/register")
    Call<MessageForSimple> register(@Header("Authorization") String token);

    /**
     * 绑定手机
     * @param phone 手机号
     * @param verifyCode 验证码
     * @param openId
     * @param unionId
     * @param nickName 昵称
     * @param avatar 头像
     * @param gender 值为1时是男性，值为2时是女性，值为0时是未知
     * @return
     */
    @POST("app/noauth/loginWithWeixin")
    Call<MessageForSimple> mobile(@Query("phone") String phone, @Query("verifyCode") String verifyCode,@Query("openId") String openId,@Query("unionId") String unionId,@Query("nickName") String nickName,@Query("avatar") String avatar,@Query("gender") String gender,@Query("registerSource") String registerSource,@Query("registerChannel") String registerChannel);



    /**
     * 加入群聊
     * @param userId
     * @return
     */
    @POST("app/rongyun/groupJoin")
    Call<MessageForSimple> groupJoin(@Query("userId") String userId);

    /**
     * 发送消息
     * @return
     */
    @POST("app/rongyun/sendGroup")
    Call<MessageForSimple> sendGroup();

    //正式接口

    //登录注册模块
    /**
     * 验证码登录
     * @param phone 手机号码11位
     * @param verifyCode 验证码 6位参数  前期不做校验
     * @return
     */
    @POST("app/noauth/loginWithCode")
    Call<MessageForSimple> loginWithCode(@Query("phone") String phone,@Query("verifyCode") String verifyCode,@Query("registerSource") String registerSource,@Query("registerChannel") String registerChannel);


    /**
     * app密码登录
     * @param phone
     * @param password
     * @return
     */
    @POST(" app/noauth/login")
    Call<MessageForSimple> login(@Query("phone") String phone,@Query("password") String password);


    /**
     * 获取验证码
     * @param mobile
     * @return
     */
    @POST("code/sms")
    Call<MessageForSimple> codesms(@Query("mobile") String mobile);


    /**
     * 获取验证码
     * @param mobile
     * @param use use  : changeLoginPassWord修改密码
     * @return
     */
    @POST("code/sms")
    Call<MessageForSimple> codesms(@Query("mobile") String mobile,@Query("use") String use);



    //我的模块


    /**
     * 邀请码填写
     * @param parentInvitationCode
     * @return
     */
    @POST("app/memberUserList/inviteCode")
    Call<MessageForSimple> inviteCode(@Header("Authorization") String token, @Query("parentInvitationCode") String parentInvitationCode);


    /**
     * 我的积分明细列表
     * @param current
     * @param size
     * @return
     */
    @POST("app/memberUserList/myIntegrate")
    Call<MessageForSimple> myIntegrate(@Header("Authorization") String token,@Query("current") String current,@Query("size") String size);
    /**
     * 会员提现纪录列表
     * @param current 页面
     * @param current 数量
     * @return
     */
    @POST("app/member/withdraw/list")
    Call<MessageForSimple> withdrawlist(@Header("Authorization") String token,@Query("current") String current,@Query("size") String size);

    /**
     * 邀请码(未完成)
     * @return
     */
    @POST("app/memberUserList/getQRcode")
    Call<MessageForSimple> getQRcode(@Header("Authorization") String token);

    /**
     * 会员提现纪录列表
     * @return
     */
    @POST("app/member/withdraw/lis")
    Call<MessageForSimple> withdrawlis(@Header("Authorization") String token);

    /**
     * 充值记录
     * @param id 分页最后一条记录id
     * @param size 条数
     * @return
     */
    @POST("app/member/rechargeLog/list")
    Call<MessageForList> rechargeLoglist(@Header("Authorization") String token,@Query("id") String id,@Query("size") String size,@Query("status") String status);

    /**
     * 余额充值
     * @param amount 充值金额
     * @param payType 支付方式 1.支付宝，2.微信
     * @return
     */
    @POST("app/account/recharge")
    Call<EntityForSimpleString> recharge(@Header("Authorization") String token,@Query("amount") String amount,@Query("payType") String payType);

    @POST("app/account/recharge")
    Call<MessageForSimple> recharge1(@Header("Authorization") String token,@Query("amount") String amount,@Query("payType") String payType,@Query("bankCardId") String bankCardId );

    /**
     * 余额充值
     * @param amount 充值金额
     * @param payType 支付方式 1.支付宝，2.微信
     * @return
     */
    @POST("app/account/recharge")
    Call<MessageForSimple> recharge(@Header("Authorization") String token,@Query("amount") String amount,@Query("payType") String payType,@Query("t") String t);


    /**
     * 实名认证
     * @param userName 姓名
     * @param idCard 身份证
     * @return
     */
    @POST("app/memberRealperson/realPerson")
    Call<EntityForSimpleString> realPerson(@Header("Authorization") String token,@Query("userName") String userName, @Query("idCard") String idCard);

    /**
     * 实名认证---是否已实名认证
     * @return
     */
    @POST("app/memberRealperson/checkRealPerson")
    Call<EntityForSimpleString> checkRealPerson(@Header("Authorization") String token);

    /**
     * 统计订单
     * @return
     */
    @POST("app/order/order/statistics")
    Call<MessageForSimple> statistics(@Header("Authorization") String token);

    /**
     * 我的粉丝、积分、余额统计
     * @return
     */
    @POST("app/memberUserList/balanceAndIntegral")
    Call<MessageForSimple> balanceAndIntegralAndFans(@Header("Authorization") String token);


    /**
     * 余额充值
     * @param amount 充值金额
     * @return
     */
    @POST("app/account/recharge")
    Call<MessageForSimple> recharge(@Header("Authorization") String token,@Query("amount") String amount);

    /**
     * 获取账户信息
     * @return
     */
    @POST("app/account/detail")
    Call<MessageForSimple> accountdetail(@Header("Authorization") String token);

    /**
     * 我的竞拍
     * @return
     */
    @POST("app/order/auctionRecordInfo/myList")
    Call<MessageForSimple> myList(@Header("Authorization") String token,@Query("current") String current,@Query("size") String size);

    /**
     * 我的上级信息
     * @return
     */
    @POST("app/member/superiorInfo")
    Call<MessageForSimple> superiorInfo(@Header("Authorization") String token);

    /**
     * 我的粉丝-总个数
     * @return
     */
    @POST("app/memberUserList/getAllMyFansNum")
    Call<MessageForSimple> getAllMyFansNum(@Header("Authorization") String token);

    /**
     * 我的粉丝-全部
     * @return
     */
    @POST("app/member/getAllMyFans")
    Call<MessageForSimple> getAllMyFans(@Header("Authorization") String token,@Query("current") String current,@Query("size") String size);

    /**
     * 我的粉丝-直属粉丝
     * @return
     */
    @POST("app/member/getMyFans")
    Call<MessageForSimple> getMyFans(@Header("Authorization") String token,@Query("current") String current,@Query("size") String size);

    /**
     * 订单列表
     * @param size 条数
     * @param current 页码
     * @param orderStatus 订单状态 0待付款1待发货2待收货3交易完成4交易失败
     * @return
     */
    @POST("app/order/order/list")
    Call<MessageForSimple> orderlist(@Header("Authorization") String token,@Query("size") String size,@Query("current") String current,@Query("orderStatus") String orderStatus);

    /**
     * 取消订单
     * @param orderId 订单id
     * @return
     */
    @POST("app/order/order/closeOrder")
    Call<MessageForSimple> ordercloseOrder(@Header("Authorization") String token,@Query("orderId") String orderId);

    /**
     * 预支付接口
     * @param orderId 订单id
     * @return
     */
    @POST("app/order/order/prePayOrder")
    Call<MessageForSimple> orderprePayOrder(@Header("Authorization") String token,@Query("orderId") String orderId);

    /**
     * 确认收货
     * @param orderId 订单id
     * @return
     */
    @POST("app/order/order/receipt")
    Call<MessageForSimple> receipt(@Header("Authorization") String token,@Query("orderId") String orderId);



    /**
     * 订单支付
     * @param orderId 订单id
     * @param addressId 地址id
     * @param payType 付款类型 1余额2微信3支付宝4.积分
     * @param payPassWord 支付密码
     * @return
     */
    @POST("app/order/order/orderPay")
    Call<MessageForSimple> orderorderPay(@Header("Authorization") String token,@Query("orderId") String orderId,@Query("addressId") String addressId,@Query("payType") String payType,@Query("payPassWord") String payPassWord,@Query("auctionMealId") String auctionMealId,@Query("memberCouponId") String memberCouponId);

    @POST("app/order/order/orderPay")
    Call<MessageForSimple> orderorderPay1(@Header("Authorization") String token,@Query("orderId") String orderId,@Query("addressId") String addressId,@Query("payType") String payType,@Query("payPassWord") String payPassWord,@Query("auctionMealId") String auctionMealId,@Query("memberCouponId") String memberCouponId,@Query("bankCardId") String bankCardId);

    /**
     * 订单支付
     * @param orderId
     * @param addressId
     * @param payType
     * @param payPassWord
     * @param t
     * @return
     */
    @POST("app/order/order/orderPay")
    Call<EntityForSimpleString> orderorderPay(@Header("Authorization") String token,@Query("orderId") String orderId,@Query("addressId") String addressId,@Query("payType") String payType,@Query("payPassWord") String payPassWord,@Query("t") String t,@Query("auctionMealId") String auctionMealId,@Query("memberCouponId") String memberCouponId);

    /**
     * 订单详情接口
     * @param orderId 订单id
     * @return
     */
    @POST("app/order/order/detail")
    Call<MessageForSimple> orderorderdetail(@Header("Authorization") String token,@Query("orderId") String orderId);


    /**
     * 快递查询
     * @param orderId 订单id
     * @return
     */
    @POST("app/order/order/kuaiDiQuery")
    Call<EntityForSimpleString> orderkuaiDiQuery(@Header("Authorization") String token,@Query("orderId") String orderId);

    /**
     * 确认收货
     * @param orderId 订单id
     * @return
     */
    @POST("app/order/order/receipt")
    Call<MessageForSimple> orderorderreceipt(@Header("Authorization") String token,@Query("orderId") String orderId);

    /**
     * 个人信息
     * @return
     */
    @POST("app/memberUserList/personInfo")
    Call<MessageForSimple> memberpersonInfo(@Header("Authorization") String token);

    /**
     * 我的总积分
     * @return
     */
    @POST("app/memberUserList/myTotalIntegrateo")
    Call<MessageForSimple> myTotalIntegrateo(@Header("Authorization") String token);

    /**
     * 我的积分明细列表
     * @return
     */
    @POST("app/memberUserList/myIntegrate")
    Call<MessageForSimple> myIntegrate(@Header("Authorization") String token);

    /**
     * 我的余额明细列表
     * @return
     */
    @POST("app/memberUserList/myAmountList")
    Call<MessageForList> myAmount(@Header("Authorization") String token,@Query("id") String id,@Query("size") String size,@Query("tradeType") String tradeType);

    /**
     * 查看资金明细更多
     * @param token
     * @param id
     * @param size
     * @param tradeType
     * @return
     */
    @POST("app/memberUserList/myOldAmountList")
    Call<MessageForList> myOldAmountList(@Header("Authorization") String token,@Query("id") String id,@Query("size") String size,@Query("tradeType") String tradeType);




    /**
     * 地址列表
     * @param current
     * @param size
     * @return
     */
    @POST("app/address/list")
    Call<MessageForSimple> addresslist(@Header("Authorization") String token,@Query("current") String current,@Query("size") String size);

    /**
     * 地址修改添加
     * @param consignee 用户真是姓名
     * @param province 省
     * @param city 市
     * @param district 县/区
     * @param street 镇/街道
     * @param address 详细地址
     * @param mobile 联系电话
     * @param tolerant 是否为默认地址 0默认1不默认
     * @return
     */
    @POST("app/address/add")
    Call<MessageForSimple> addressadd(@Header("Authorization") String token,@Query("addressId") String addressId,@Query("consignee") String consignee,@Query("province") String province,@Query("city") String city,@Query("district") String district,@Query("street") String street,@Query("address") String address,@Query("mobile") String mobile,@Query("tolerant") String tolerant);

    /**
     * 编辑地址
     * @param addressId
     * @param consignee
     * @param province
     * @param city
     * @param district
     * @param street
     * @param address
     * @param mobile
     * @param tolerant
     * @return
     */
    @POST("app/address/edit")
    Call<MessageForSimple> addressedit(@Header("Authorization") String token,@Query("addressId") String addressId,@Query("consignee") String consignee,@Query("province") String province,@Query("city") String city,@Query("district") String district,@Query("street") String street,@Query("address") String address,@Query("mobile") String mobile,@Query("tolerant") String tolerant);

    /**
     * 全部地址
     * @return
     */
    @POST("app/noauth/allAddressList")
    Call<MessageForAddress> allAddressList();

    /**
     * 获取区
     * @return
     */
    @POST("app/noauth/addressList")
    Call<MessageForAddress> addressList(@Query("parentId") String parentId);



    /**
     * 删除地址
     * @param addressId
     * @return
     */
    @POST("app/address/del")
    Call<MessageForSimple> addressdel(@Header("Authorization") String token,@Query("addressId") String addressId);

    /**
     * 修改默认地址
     * @param addressId
     * @return
     */
    @POST("app/address/settingTolerant")
    Call<MessageForSimple> settingTolerant(@Header("Authorization") String token,@Query("addressId") String addressId);

    /**
     * 设置支付密码
     * @param payPass 支付密码（需要加密）
     * @param smsCode 短信验证码
     * @param mobile
     * @return
     */
    @POST("app/member/setPassword")
    Call<MessageForSimple> setPassword(@Header("Authorization") String token,@Query("payPass") String payPass,@Query("smsCode") String smsCode,@Query("mobile") String mobile);

    /**
     * 是否设置支付密码
     * @return
     */
    @POST("app/member/isSetPassword")
    Call<EntityForSimpleString> isSetPassword(@Header("Authorization") String token);



    /**
     * 设置/修改密码
     * @param phone 手机号码
     * @param verifyCode 短信验证码
     * @param newLoginCode 密码
     * @return
     */
    @POST("app/member/changeLoginCode")
    Call<MessageForSimple> changeLoginCode(@Header("Authorization") String token,@Query("phone") String phone,@Query("verifyCode") String verifyCode,@Query("newLoginPassWord") String newLoginCode);


    /**
     * 添加绑定(绑定支付宝和微信)
     * @param account 账号
     * @param name 姓名
     * @param accountType 0.微信，1.支付宝
     * @return
     */
    @POST("app/member/withdrawAccount/add")
    Call<MessageForSimple> withdrawAccountadd(@Header("Authorization") String token,@Query("account") String account,@Query("name") String name,@Query("accountType") String accountType);


    /**
     *  添加绑定(绑定支付宝)
     * @param token
     * @param accountType
     * @return
     */
    @POST("app/member/withdrawAccount/add")
    Call<MessageForSimple> withdrawAccountadd1(@Header("Authorization") String token,@Query("aliPayUserCode") String aliPayUserCode,@Query("aliType") String aliType,@Query("accountType") String accountType);


    /**
     * 账号绑定列表
     * @return
     */
    @POST("app/member/withdrawAccount/list")
    Call<MessageForList> withdrawAccountlist(@Header("Authorization") String token);


    /**
     * 提现
     * @param payPassword 支付密码不能为空
     * @param amount 提现金额不能为空
     * @param type 提现账号：0.微信，1.支付宝
     * @return
     */
    @POST("app/member/withdraw/add")
    Call<MessageForSimple> withdrawadd(@Header("Authorization") String token,@Query("payPassword") String payPassword,@Query("amount") String amount,@Query("type") String type,@Query("source") String source,@Query("isIntegral") String isIntegral,@Query("integralType") String integralType,@Query("bankCardId") String bankCardId);

    //历史模块

    /**
     * 拍卖历史记录列表
     * @return
     */
    @POST("app/noauth/order/auctionRecordInfo/listHistory")
    Call<MessageForSimple> listHistory(@Query("current") String current,@Query("size") String size);

    //积分商城

    /**
     * 积分商城商品列表
     * @param categoryId 分类id
     * @param sortNum 0.默认排序，权重+ 时间，积分排序 1.升序，2.降序 价格排序 3.升序，4.降序
     * @param size 条数
     * @param current 页码
     * @param goodsType 商品类型  1 寄拍  2换购
     * @return
     */
    @POST("app/noauth/mall/goodsInfo/list")
    Call<MessageForSimple> goodsInfolist(@Query("goodsType") String goodsType,@Query("categoryId") String categoryId,@Query("sortNum") String sortNum,@Query("size") String size,@Query("current") String current);

    /**
     * 商品列表+搜索
     * @param goodsName 商品名称，可模糊，搜索时为必须
     * @param queryType 查询类型  1 列表展示或列表搜索 2其他
     * @return
     */
    @POST("app/noauth/mall/goodsInfo/list")
    Call<MessageForSimple> goodsInfolist(@Query("goodsName") String goodsName,@Query("queryType") String queryType,@Query("size") String size,@Query("current") String current);


    /**
     * 积分商城商品详情
     * @param goodsId 商品id
     * @return
     */
    @POST("app/noauth/mall/goodsInfo/detail")
    Call<MessageForSimple> goodsInfodetail(@Header("Authorization") String token,@Query("goodsId") String goodsId);

    /**
     * 积分商城订单列表
     * @param orderStatusQuery 订单状态 0待付款1待发货2待收货3已完成4.全部
     * @param orderTypeQuery 寄拍状态 1.待接拍，2.寄拍中，3.寄拍成功，4.全部
     * @param size 条数
     * @param current 页码
     * @return
     */
    @POST("app/mall/order/list")
    Call<MessageForSimple> mallorderlist(@Header("Authorization") String token,@Query("orderStatusQuery") String orderStatusQuery,@Query("orderTypeQuery") String orderTypeQuery,@Query("size") String size,@Query("current") String current);


    /**
     * 积分商城预支付订单
     * @param goodsId 商品id
     * @return
     */
    @POST("app/mall/order/prePayOrder")
    Call<MessageForSimple> prePayOrder(@Header("Authorization") String token,@Query("goodsId") String goodsId);


    /**
     *  积分商城订单支付
     * @param goodsId 商品id
     * @param addressId 地址id
     * @param payType 付款类型 1余额2微信3支付宝4.积分
     * @param integralType 1：普通积分，2，超值积分
     * @param payPassWord 支付密码
     * @return
     */
    @POST("app/mall/order/orderPay")
    Call<MessageForSimple> orderPay1(@Header("Authorization") String token,@Query("goodsId") String goodsId,@Query("addressId") String addressId,@Query("payType") String payType,@Query("payPassWord") String payPassWord,@Query("num") String num,@Query("integralType") String integralType,@Query("remark") String remark,@Query("bankCardId") String bankCardId);

    /**
     *  积分商城订单支付(orderId)
     * @param orderId 商品id
     * @param addressId 地址id
     * @param payType 付款类型 1余额2微信3支付宝4.积分
     * @param payPassWord 支付密码
     * @return
     */
    @POST("app/mall/order/orderPayByOrderId")
    Call<MessageForSimple> orderPay1order(@Header("Authorization") String token,@Query("orderId") String orderId,@Query("addressId") String addressId,@Query("payType") String payType,@Query("payPassWord") String payPassWord,@Query("bankCardId") String bankCardId);

    /**
     *  积分商城订单支付(积分)
     * @param goodsId 商品id
     * @param addressId 地址id
     * @param payType 付款类型 1余额2微信3支付宝4.积分
     * @param integralType 1：普通积分，2，超值积分
     * @return
     */
    @POST("app/mall/order/orderPay")
    Call<EntityForSimpleString> orderPay2(@Header("Authorization") String token,@Query("goodsId") String goodsId,@Query("addressId") String addressId,@Query("payType") String payType,@Query("payPassWord") String payPassWord,@Query("num") String num,@Query("integralType") String integralType,@Query("remark") String remark);

    /**
     *  积分商城订单支付(积分)
     * @param orderId 商品id
     * @param addressId 地址id
     * @param payType 付款类型 1余额2微信3支付宝4.积分
     * @return
     */
    @POST("app/mall/order/orderPayByOrderId")
    Call<EntityForSimpleString> orderPay2order(@Header("Authorization") String token,@Query("orderId") String orderId,@Query("addressId") String addressId,@Query("payType") String payType,@Query("payPassWord") String payPassWord);

    /**
     *  积分商城订单支付(积分)
     * @param goodsId 商品id
     * @param addressId 地址id
     * @param payType 付款类型 1余额2微信3支付宝4.积分
     * @param integralType 1：普通积分，2，超值积分
     * @return
     */
    @POST("app/mall/order/orderPay")
    Call<EntityForSimpleString> orderPay(@Header("Authorization") String token,@Query("goodsId") String goodsId,@Query("addressId") String addressId,@Query("payType") String payType,@Query("num") String num,@Query("integralType") String integralType,@Query("remark") String remark);




    /**
     * 积分商城订单详情
     * @param orderId 订单id
     * @return
     */
    @POST("app/mall/order/detail")
    Call<MessageForSimple> orderdetail(@Header("Authorization") String token,@Query("orderId") String orderId);

    /**
     * 快递查询
     * @param orderId 订单id
     * @return
     */
    @POST("app/mall/order/kuaiDiQuery")
    Call<EntityForSimpleString> kuaiDiQuery(@Header("Authorization") String token,@Query("orderId") String orderId);

    /**
     * 积分商城确认收货
     * @param orderId
     * @return
     */
    @POST("app/mall/order/receipt")
    Call<MessageForSimple> orderreceipt(@Header("Authorization") String token,@Query("orderId") String orderId);

    /**
     * 积分商城取消订单
     * @param orderId
     * @return
     */
    @POST("app/mall/order/closeOrder")
    Call<MessageForSimple> closeOrder(@Header("Authorization") String token,@Query("orderId") String orderId);

    /**
     * 积分商城申请寄拍
     * @param orderId
     * @return
     */
    @POST("app/mall/order/applyMailing")
    Call<EntityForSimpleString> applyMailing(@Header("Authorization") String token,@Query("orderId") String orderId);

    /**
     * 待支付订单预支付接口
     * @param orderId
     * @return
     */
    @POST("app/mall/order/prePayOrderByOrderId")
    Call<MessageForSimple> prePayOrderByOrderId(@Header("Authorization") String token,@Query("orderId") String orderId);


    //首页接口

    /**
     * 出价
     * @param auctionId 商品id
     * @param topPrice 当前最高价
     * @return
     */
    @POST("app/order/auctionRecordDetail/add")
    Call<MessageForSimple> auctionRecordDetailadd(@Header("Authorization") String token,@Query("auctionId") String auctionId,@Query("topPrice") String topPrice);

    /**
     * 加价纪录列表
     * @param current 下标页面
     * @param size 条数
     * @param auctionId 拍卖纪录id
     * @return
     */
    @POST("app/noauth/order/auctionRecordInfo/listByAuctionIdWx")
    Call<MessageForSimple> listByAuctionIdWx(@Query("current") String current,@Query("size") String size,@Query("auctionId") String auctionId);



    /**
     * 今日热拍
     * @param current
     * @param size
     * @return
     */
    @POST("app/noauth/order/auctionRecordInfo/listToday")
    Call<MessageForSimple> listToday(@Query("current") String current,@Query("size") String size);


    /**
     * 拍卖纪录列表---查询近三天的预展
     * @param current
     * @param size
     * @param type  1今日2明日3后日
     * @return
     */
    @POST("app/noauth/order/auctionRecordInfo/listDate")
    Call<MessageForSimple> listDate(@Query("current") String current,@Query("size") String size,@Query("type") String type);


    /**
     * 小程序广告列表接口
     * @param positionId 广告位id
     * @return
     */
    @POST("app/noauth/banner/pageList")
    Call<MessageForList> pageList(@Query("positionId") String positionId);

    /**
     * 运营分类
     * @param positionId
     * @param type 1.拍卖,2.寄拍，3.换购
     * @return
     */
    @POST("app/noauth/category/list")
    Call<MessageForList> categorylist(@Query("parentId") String positionId,@Query("type") String type,@Query("isRecommend") String isRecommend);


    /**
     * 分类商品列表
     * @param current
     * @param size
     * @param fatherCategoryId 分类id
     * @return
     */
    @POST("app/noauth/order/auctionRecordInfo/listFatherCategory")
    Call<MessageForSimple> listFatherCategory(@Query("current") String current,@Query("size") String size,@Query("fatherCategoryId") String fatherCategoryId);

    /**
     * 分类商品列表二期改（超值拍 限价拍）
     * @param current
     * @param size
     * @param isGiveIntegral 是否赠送积分 0不赠送 1赠送
     * @param isLimitPrice 是否限价 0不限价 1限价
     * @return
     */
    @POST("app/noauth/order/auctionRecordInfo/listFatherCategory")
    Call<MessageForSimple> listFatherCategory(@Query("current") String current,@Query("size") String size,@Query("displayArea") String displayArea,@Query("isGiveIntegral") String isGiveIntegral,@Query("isLimitPrice") String isLimitPrice);


    /**
     * 拍卖商品详情
     * @param auctionId
     * @return
     */
    @POST("app/noauth/order/auctionRecordInfo/detail")
    Call<MessageForSimple> auctionRecordInfodetail(@Query("auctionId") String auctionId);

    /**
     * 收益排行榜
     * @param auctionId
     * @return
     */
    @POST("app/noauth/order/auctionRecordInfo/rankingList")
    Call<MessageForList> rankingList(@Query("auctionId") String auctionId);


    /**
     * 消息类型（0：通知，1：公告）
     * @param token
     * @param msgType
     * @return
     */
    @POST("app/messagePush/list")
    Call<MessageForSimple> messagePushlist(@Header("Authorization") String token,@Query("msgType") String msgType,@Query("current") String current,@Query("size") String size,@Query("status") String status);


    //vip商城

    /**
     * vip店铺总数
     * @return
     */
    @POST("app/noauth/vipRoom/total")
    Call<EntityForSimpleString> vipRoomtotal();

    /**
     * vip店铺总数(新)
     * @return
     */
    @POST("app/noauth/vipRoom/totalOpen")
    Call<EntityForSimpleString> vipRoomtotalOpen();

    /**
     * vip店铺列表
     * @param number 店铺编号
     * @param previousRoomId 上次查询最后一条店铺id
     * @param size 默认20 查询条数
     * @return
     */
    @POST("app/noauth/vipRoom/list")
    Call<MessageForList> vipRoomlist(@Query("number") String number,@Query("previousRoomId") String previousRoomId,@Query("size") String size);


    /**
     * vip店铺列表(vip类型)
     * @param number 店铺编号
     * @param previousRoomId 上次查询最后一条店铺id
     * @param size 默认20 查询条数
     * @return
     */
    @POST("app/noauth/vipRoom/listCustom")
    Call<MessageForList> vipRoomlistCustom(@Query("number") String number,@Query("previousRoomId") String previousRoomId,@Query("size") String size);

    /**
     * 根据编号搜索vip店铺
     * @param number 店铺编号
     * @return
     */
    @POST("app/noauth/vipRoom/getOneByNumber")
    Call<MessageForSimple> getOneByNumbe(@Query("number") String number);


    /**
     * 校验店铺密码
     * @param token
     * @param roomId
     * @param password
     * @return
     */
    @POST("app/vipRoom/checkPassword")
    Call<MessageForSimple> checkPassword(@Header("Authorization") String token,@Query("roomId") String roomId,@Query("password") String password);

    /**
     * 今日场次
     * @param token
     * @param displayArea 1 默认区  2 新人  3  vip 默认为1
     * @param roomId 店铺id
     * @return
     */
    @POST("app/order/auctionRecordInfo/listToday")
    Call<MessageForSimple> listToday(@Header("Authorization") String token,@Query("displayArea") String displayArea,@Query("roomId") String roomId,@Query("current") String current,@Query("size") String size);

    /**
     * 近期预告
     * @param token
     * @param displayArea 1 默认区  2 新人  3  vip 默认为1
     * @param roomId 店铺id
     * @return
     */
    @POST("app/order/auctionRecordInfo/listFuture")
    Call<MessageForList> listFuture(@Header("Authorization") String token,@Query("displayArea") String displayArea,@Query("roomId") String roomId,@Query("current") String current,@Query("size") String size);

    /**
     * 須知 和規則
     * @param ruleKey userKnow 須知 tabRuleKey
     * @return
     */
    @POST("app/noauth/sys/ruleByKey")
    Call<MessageForSimple> ruleByKey(@Query("ruleKey") String ruleKey);

    /**
     * 根據openId登錄
     * @param openId
     * @return
     */
    @POST("app/noauth/loginWithOpenId")
    Call<EntityForSimpleString> loginWithOpneId(@Query("openId") String openId);

    /**
     * 订单未读个数
     * @param token
     * @return
     */
    @POST("app/order/order/statistics")
    Call<MessageForSimple> orderstatistics(@Header("Authorization") String token);

    /**
     * 系统参数
     * @return
     */
    @POST("app/noauth/sys/key")
    Call<EntityForSimpleString> syskey(@Query("sysConfigKey") String sysConfigKey);


    /**
     * app版本检测接口 查询最新版本
     * @param verType
     * @param verCode
     * @return
     */
    @POST("app/noauth/versioninfo/edition")
    Call<MessageForSimple> versioninfoedition(@Query("verType") String verType,@Query("verCode") String verCode);


    //二期修改


    /**
     * 热门分类
     * @param size 分类展示长度
     * @return
     */
    @POST("app/noauth/category/popular")
    Call<MessageForList> categorypopular(@Query("size") String size);

    /**
     * App端收藏商品
     * @param token
     * @param goodsId
     * @return
     */
    @POST("app/mall/collectGoodsInfo/collect")
    Call<MessageForSimple> collectGoodsInfocollect(@Header("Authorization") String token,@Query("goodsId") String goodsId);


    /**
     * App端取消收藏
     * @param token
     * @param goodsId
     * @return
     */
    @POST(" app/mall/collectGoodsInfo/cancel")
    Call<MessageForSimple> collectGoodsInfocancel(@Header("Authorization") String token,@Query("goodsId") String goodsId);

    /**
     * 我的收藏
     * @param token
     * @param current
     * @param size
     * @return
     */
    @POST("app/mall/collectGoodsInfo/list")
    Call<MessageForSimple> collectGoodsInfolist(@Header("Authorization") String token,@Query("current") String current,@Query("size") String size);

    /**
     * 上传图片
     * @param base64
     * @return
     */
    @FormUrlEncoded
    @POST("oss/upload")
    Call<MessageForSimple> upload(@Header("Authorization") String token,@Field("base64Pic") String base64);

    /**
     * 上传图片
     * @return
     */
    @Multipart
    @POST("oss/uploadMobileFileList")
    Call<MessageForListString> uploadMobileFileList(@Header("Authorization") String token, @PartMap Map<String, RequestBody> params);

    /**
     * 修改头像昵称
     * @param token
     * @param avatar
     * @param newName
     * @return
     */
    @POST("app/member/updateAvatar")
    Call<MessageForSimple> memberupdateAvatar(@Header("Authorization") String token,@Query("avatar") String avatar,@Query("newName") String newName);

    /**
     * 获取消息列表
     * @param size
     * @param msgType
     * @return
     */
    @POST("app/pushMessage/list")
    Call<MessageForList> pushMessagelist(@Header("Authorization") String token,@Query("size") String size,@Query("msgType") String msgType,@Query("pushId") String pushId);


    /**
     * 是否有未读红点
     * @param token
     * @param sendTime 该参数传入上次浏览（点击）消息的时间
     * @return
     */
    @POST("app/pushMessage/isRead")
    Call<EntityForSimpleB> pushMessageisRead(@Header("Authorization") String token, @Query("sendTime") String sendTime);

    /**
     * 安卓定制版用户当天是否签到
     * @param token
     * @return
     */
    @POST("app/signIn/checkSign")
    Call<EntityForSimpleB> checkSign(@Header("Authorization") String token);

    /**
     * 安卓定制版签到
     * @param token
     * @return
     */
    @POST("app/signIn/add")
    Call<MessageForSimple> signInAdd(@Header("Authorization") String token);


    /**
     * 获取当日整点列表（定制）
     * @return
     */
    @POST("app/noauth/order/auctionRecordInfo/hoursToday")
    Call<MessageForList> hoursToday();

    /**
     * 是否有开拍提醒（定制版）
     * @param token
     * @param auctionId
     * @return (判断是否有开拍提醒，0：没有；1：有)
     */
    @POST("app/pushMessage/isHaveRemind")
    Call<EntityForSimpleString> isHaveRemind(@Header("Authorization") String token, @Query("auctionId") String auctionId);

    /**
     * 开拍提醒
     * @param token
     * @param title 标题不能为空
     * @param content 内容不能为空
     * @param linkId auctionId(跳转链接)
     * @param auctionTime 开拍时间
     * @param isRemind 0：取消提醒；1：开拍提醒
     * @return
     */
    @POST("app/pushMessage/isRemind")
    Call<MessageForSimple> pushMessageisRemind(@Header("Authorization") String token, @Query("title") String title, @Query("content") String content, @Query("linkId") String linkId, @Query("auctionTime") String auctionTime, @Query("isRemind") String isRemind);

    /**
     * 今日整点 拍卖
     * @param current
     * @param size
     * @param startTime
     * @param endTime
     * @return
     */
    @POST("app/noauth/order/auctionRecordInfo/listTodayHours")
    Call<MessageForSimple> listTodayHours(@Query("current") String current,@Query("size") String size,@Query("startTime") String startTime,@Query("endTime") String endTime);

    /**
     * 提现指定金额所需积分
     * @param token
     * @param money
     * @return
     */
    @POST("app/member/withdraw/withdrawNeedIntegral")
    Call<MessageForSimple> withdrawNeedIntegral(@Header("Authorization") String token,@Query("money") String money);

    /**
     * 分页查询中奖记录
     * @param token
     * @param activityLogId 最后一条数据的id
     * @param size
     * @return
     */
    @POST("app/activity/pageList")
    Call<MessageForList> activitypageList(@Header("Authorization") String token,@Query("activityLogId") String activityLogId,@Query("size") String size);

    /**
     * 查看中奖记录详情
     * @param token
     * @param activityLogId 抽奖记录id
     * @return
     */
    @POST("app/activity/detail")
    Call<MessageForSimple> activitydetail(@Header("Authorization") String token,@Query("activityLogId") String activityLogId);

    /**
     * 设置收货地址
     * @param token
     * @param activityLogId
     * @param addressId
     * @return
     */
    @POST("app/activity/settingReceivingAddress")
    Call<MessageForSimple> settingReceivingAddress(@Header("Authorization") String token,@Query("activityLogId") String activityLogId,@Query("addressId") String addressId);

    /**
     * 查询物流（抽奖活动）
     * @param token
     * @param orderId
     * @return
     */
    @POST("app/activity/kuaiDiQuery")
    Call<EntityForSimpleString> activitykuaiDiQuery(@Header("Authorization") String token,@Query("activityLogId") String orderId);

    /**
     * 确认收货
     * @param token
     * @param orderId
     * @return
     */
    @POST("app/activity/receipt")
    Call<MessageForSimple> activityreceipt(@Header("Authorization") String token,@Query("activityLogId") String orderId);


    //冲话费

    /**
     * 获取充值面板
     * @param token
     * @return
     */
    @POST("app/oilCardPrice/list")
    Call<MessageForList> oilCardPricelist(@Header("Authorization") String token,@Query("oilCardType") String oilCardType);

    /**
     * 获取支付方式
     * @param token
     * @return
     */
    @POST("app/oilCardPrice/getPaymentSettings")
    Call<MessageForSimple> getPaymentSettings(@Header("Authorization") String token,@Query("paymentType") String paymentType);

    /**
     * 话费充值记录
     * @param token
     * @return
     */
    @POST("app/prepaidRefillLog/list")
    Call<MessageForList> prepaidRefillLoglist(@Header("Authorization") String token,@Query("id") String id,@Query("size") String size);

    /**
     * 手机话费充值
     * @param token
     * @param mobile 手机号
     * @param type 支付类型：1积分 2余额 3微信 4支付宝
     * @param oilCardId 充值模板id
     * @param payPassWord 支付密码（余额支付）
     * @return
     */
    @POST("app/prepaidRefillLog/juheRecharge")
    Call<MessageForSimple> juheRecharge(@Header("Authorization") String token,@Query("mobile") String mobile,@Query("type") String type,@Query("oilCardId") String oilCardId);

    @POST("app/prepaidRefillLog/juheRecharge")
    Call<EntityForSimpleString> juheRecharge(@Header("Authorization") String token,@Query("mobile") String mobile,@Query("type") String type,@Query("oilCardId") String oilCardId,@Query("payPassWord") String payPassWord,@Query("integralType") String integralType);


    /**
     * 根据商品id查询近三天拍卖纪录
     * @param current
     * @param size
     * @return
     */
    @POST("app/noauth/order/auctionRecordInfo/newListDate")
    Call<MessageForSimple> newListDate(@Query("current") String current,@Query("size") String size,@Query("type") String type,@Query("goodsId") String goodsId);

    /**
     * 寄拍明细列表
     * @param token
     * @param goodsId
     * @return
     */
    @POST("app/mallAuctionDetail/list")
    Call<MessageForSimple> mallAuctionDetail(@Header("Authorization") String token,@Query("mallOrderId") String goodsId);


    //新加
    /**
     * 判断是否新人
     * @param token
     * @return
     */
    @POST("app/member/isNewPeople")
    Call<EntityForSimpleString> isNewPeople(@Header("Authorization") String token);



    /**
     * 实名认证---是否已实名认证(新)
     * @return
     */
    @POST("app/memberRealperson/checkRealPersonNum")
    Call<EntityForSimpleString> checkRealPersonNum(@Header("Authorization") String token);

    /**
     * 获取实名认证个人信息
     * @param token
     * @return
     */
    @POST("app/memberRealperson/getPersonByUserId")
    Call<MessageForSimple> getPersonByUserId(@Header("Authorization") String token);

    /**
     * 提现撤回
     * @param token
     * @return
     */
    @POST("app/member/withdraw/cancel")
    Call<EntityForSimpleString> withdrawcancel(@Header("Authorization") String token,@Query("id") String id);


    /**
     * 拍卖商品套餐列表
     * @param token
     * @param auctionId 拍卖id
     * @return
     */
    @POST("app/auctionMeal/listByAuctionId")
    Call<MessageForList> listByAuctionId(@Header("Authorization") String token,@Query("auctionId") String auctionId);


    /**
     * 超值积分列表
     * @param token
     * @param current
     * @param size
     * @return
     */
    @POST("app/memberUserList/mySuperIntegrate")
    Call<MessageForSimple> mySuperIntegrate(@Header("Authorization") String token,@Query("current") String current,@Query("size") String size);

    /**
     * 我的积分
     * @param token
     * @return
     */
    @POST("app/memberUserList/myAllIntegrate")
    Call<MessageForSimple> myAllIntegrate(@Header("Authorization") String token);

    /**
     * 获取第三方支付配置
     * @param id 1.APP-iOS，2.APP-Android，3.小程序
     * @return
     */
    @POST("app/noauth/sys/getTerminalPayment")
    Call<MessageForSimple> getTerminalPayment(@Query("id") String id);

    /**
     * 一键登录
     * @param token
     * @param registerSource
     * @param registerChannel
     * @return
     */
    @POST("app/noauth/oneClick")
    Call<MessageForSimple> oneClick(@Query("token") String token,@Query("registerSource") String registerSource,@Query("registerChannel") String registerChannel);

    /**
     * 我的优惠券
     * @param token
     * @param type
     * @param size
     * @param current
     * @return
     */
    @POST("app/coupon/getMyPage")
    Call<MessageForSimple> getMyPage(@Header("Authorization") String token,@Query("type") String type,@Query("size") String size,@Query("current") String current);

    /**
     * 获取H5详情
     * @param token
     * @param id
     * @return
     */
    @POST("app/noauth/sys/getH5Manage")
    Call<MessageForSimple> getH5Manage(@Header("Authorization") String token,@Query("id") String id);


    /**
     * 优惠券指定商品列表
     * @param current
     * @param size
     * @param fatherCategoryIds 1000,1005
     * @return
     */
    @POST("app/order/auctionRecordInfo/listFatherCategoryArray")
    Call<MessageForSimple> listFatherCategoryArray(@Header("Authorization") String token,@Query("current") String current,@Query("size") String size,@Query("fatherCategoryIds") String fatherCategoryIds);


    /**
     * 订单获取可用张数
     * @param token
     * @param orderId
     * @return
     */
    @POST("app/coupon/getUseCount")
    Call<EntityForSimpleString> getUseCount(@Header("Authorization") String token,@Query("orderId") String orderId);

    /**
     * 订单获取优惠券
     * @param token
     * @param orderId
     * @param size
     * @param current
     * @return
     */
    @POST("app/coupon/getUseList")
    Call<MessageForList> getUseList(@Header("Authorization") String token,@Query("orderId") String orderId,@Query("size") String size,@Query("current") String current);

    /**
     * 我的钱包明细列表
     * @param token
     * @param current
     * @param size
     * @return
     */
    @POST("app/memberWalletDetail/list")
    Call<MessageForSimple> memberWalletDetaillist(@Header("Authorization") String token,@Query("current") String current,@Query("size") String size);

    /**
     * 钱包余额详情
     * @param token
     * @return
     */
    @POST("app/memberWallet/detail")
    Call<MessageForSimple> memberWalletdetail(@Header("Authorization") String token);



    //银行卡

    /**
     * 查询银行列表
     * @return
     */
    @GET("app/sys/pub/banks")
    Call<MessageForBank> banks(@Header("Authorization") String token);

    /**
     * 查询支行列表
     * @param token
     * @param bankCode
     * @param cityCode
     * @return
     */
    @GET("app/sys/pub/subBanks")
    Call<MessageForSubBank> subbanks(@Header("Authorization") String token, @Query("bankCode") String bankCode, @Query("cityCode") String cityCode);

    /**
     * 查询省份区域列表
     * @param token
     * @return
     */
    @GET("app/sys/pub/areas")
    Call<MessageForCity> areas(@Header("Authorization") String token);

    /**
     *  绑定提现银行卡-银行卡信息鉴权
     * @param token
     * @param body
     *      * @param token
     *      * @param accountName 户名
     *      * @param bankCardCode 银行卡号
     *      * @param bankName 银行名称
     *      * @param openBankName 开户行名称
     *      * @param openBankCode 开户行号
     *      * @param mobilePhone 手机号
     *      * @return
     *      */
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("app/member/bankCard/withdraw/bind")
    Call<MessageForSimple> withdrawbind(@Header("Authorization") String token,@Body RequestBody body);

    /**
     * 绑定提现银行卡-发送短信验证码
     * @param token
     * @param bankCardId
     * @return
     */
    @POST("app/member/bankCard/withdraw/bind/{bankCardId}")
    Call<MessageForSimple> codebind(@Header("Authorization") String token,@Path("bankCardId") String bankCardId);

    /**
     * 绑定提现银行卡-短信验证码鉴权
     * @param token
     * @param bankCardId
     * @param messageCode
     * @return
     */
    @POST("app/member/bankCard/withdraw/bind/messageCode/verification")
    Call<MessageForSimple> verification(@Header("Authorization") String token,@Query("bankCardId") String bankCardId,@Query("messageCode") String messageCode);

    /**
     * 解绑提现银行卡
     * @param token
     * @param bankCardId
     * @return
     */
    @POST("app/member/bankCard/withdraw/unBind/{bankCardId}")
    Call<MessageForSimple> unBind(@Header("Authorization") String token,@Path("bankCardId") String bankCardId);

    /**
     * 查询用户银行卡列表
     * @param token
     * @return
     */
    @GET("app/member/bankCards")
    Call<MessageForList> bankCards(@Header("Authorization") String token);


    /**
     * 发送支付验证码
     * @param token
     * @param payScene 支付场景 1-余额充值 2-尾款交割 3-商城支付
     * @param orderNo 支付订单号
     * @param bankCardId 支付银行卡ID
     * @return
     */
    @POST("app/member/bankCard/payment/messageCode")
    Call<MessageForSimple> messageCode(@Header("Authorization") String token,@Body RequestBody body);

    /**
     * 确认支付
     * @param token
     * @param orderNo 订单号
     * @param bankCardId 支付银行卡ID
     * @param messageCode 短信验证码
     * @param payScene 支付场景
     * @param payTime 支付时间
     * @return
     */
    @POST("app/member/bankCard/payment/confirm")
    Call<MessageForSimple> paymentconfirm(@Header("Authorization") String token,@Body RequestBody body);

    /**
     * 支付密码校验
     * @param token
     * @param payPassword
     * @return
     */
    @POST("app/memberUserList/payPassword/verification")
    Call<MessageForSimple> verification(@Header("Authorization") String token,@Query("payPassword") String payPassword);

    /**
     * 解绑提现银行卡
     * @param token
     * @param bankCardId
     * @return
     */
    @GET("app/member/bankCards/{bankCardId}")
    Call<MessageForSimple> bankCards(@Header("Authorization") String token,@Path("bankCardId") String bankCardId);


}
