package com.qupp.client.network.bean;


import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.qupp.client.MyApplication;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 二层modelbean
 */


public class EntityForSimple implements Serializable, MultiItemEntity {

    private boolean canUse;
    private String cantUseRemark;
    private boolean isCustomMarkUp;
    private boolean checked;
    private String endTimes;
    private String id;
    private String token;
    private String userId;
    private String total;
    public int type;
    public static final int TYPE_TOP =0;
    public static final int TYPE_OTHER = 1;
    public static final int TYPE_SELF = 2;
    private String startTimes;
    //快递信息
    private String time;
    private String context;
    private ArrayList<EntityForSimple> data;

    //收益排行榜
    private String num;
    private String totalPrice;

    private String content;
    private String acceptTime;
    private String soldNum;

    private String pushId;
    private String displayArea;


    /**
     * banner图片地址
     */
    private String image;

    /**
     * 头像
     */
    private String avatar;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 性别  0未知1男2女
     */
    private String gender;
    /**
     * 昵称
     */
    private String nickname;
    private String nickName;
    /**
     * 电话
     */
    private String phone;
    /**
     * 邀请码
     */
    private String invitationCode;
    /**
     * 余额
     */
    private String amount;
    /**
     * 粉丝
     */
    private String fanNums;
    /**
     * 积分
     */
    private String integralAmount;

    /**
     * 锁定资金
     */
    private String lockAmount;
    /**
     * 总收益
     */
    private String totalIncome;
    /**
     * 今日收益
     */
    private String todayIncome;

    /**
     * 列表容器
     */
    private ArrayList<EntityForSimple> records;

    /**
     * 1 申请中 2  审核通过  3 审核拒绝  （2预展中 3进行中 4 5已结束 ）
     */
    private String status;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 商品展示图
     */
    private String logo;
    /**
     * 商品名
     */
    private String goodsName;
    /**
     * 商品id
     */
    private String goodsId;
    /**
     * 积分
     */
    private String integral;
    private String totalIntegral;
    /**
     * 价格
     */
    private String price;
    /**
     * 估值
     */
    private String assessPrice;

    /**
     * 价格类型  1.积分  2.积分+金额，3.金额'
     */
    private String priceType;
    /**
     * 商品详情（长图）
     */
    private String goodsDetail;
    /**
     * 头部轮播图（手动转数据）
     */
    private String headImage;
    /**
     * 结拍时间
     */
    private String endTime;
    /**
     * 成交价格
     */
    private String transactionPrice;
    /**
     * 起拍价格
     */
    private String startPrice;
    /**
     * 当前价
     */
    private String topPrice;
    /**
     * 出价次数
     */
    private String markupNum;
    /**
     * 开拍时间
     */
    private String startTime;
    /**
     * 商品分类id
     */
    private String categoryId;
    /**
     * 商品分类名字
     */
    private String name;
    /**
     * 分类图标
     */
    private String pic;

    /**
     * 商品类型  1 寄拍  2换购'3特卖
     */
    private String goodsType;
    /**
     * type 1 h5url 2商品id 3商品分类列表 5app内部
     */
    private String linkId ;
    /**
     * 拍卖id 商品id
     */
    private String auctionId;
    /**
     * 红包
     */
    private String brokerage;
    /**
     * 趣拍拍价格
     */
    private String markupPrice;
    /**
     * 保证金
     */
    private Double nextCashDeposit;
    /**
     * 出价时间
     */
    private String markupTimeString;
    private String markupTime;
    /**
     * 出价金额
     */
    private String payPrice;
    /**
     * 用户名
     */
    private String userName;

    private String createTime;
    private String sendTime;

    private String title;
    /**
     * 已推人数
     */
    private String indirectNum;

    /**
     * 收货人
     */
    private String consignee;
    /**
     * 收货手机
     */
    private String mobile;
    //省市区街道
    private String province;
    private String city;
    private String district;
    private String street;

    private String provinceText;
    private String cityText;
    private String districtText;
    private String streetText;
    private String address;
    //0 默认地址  1非默认
    private String tolerant;
    private String addressId;
    private String remark;

    private String account;
    private String accountType;
    //微信支付字段
    private String appId;
    private String noncestr;
    private String outTradeNo;
    private String prepay_id;
    private String sign;
    private String timestamp;

    private String payType;
    private String payTime;
    private String money;
    private String orderType;
    private String applyTime;
    private String orderStatus;
    private String orderId;
    //保证金
    private String pledgePrice;
    //订单金额
    private String orderPrice;
    private String actualPayment;
    private String deliverTime;
    //默认地址
    private EntityForSimple memberAddress;
    private EntityForSimple orderViewVo;
    private EntityForSimple goodsInfo;
    private EntityForSimple order;
    private String viewCount;
    private String count;

    private String orderStr;
    private String contractUrl;

    //vip店铺
    private String number;
    private String roomId;
    private String vipNickname;
    private String vipPhone;
    //昨日收益
    private String yesterdayProfit;

    private String waitPay;
    private String waitDeliverGoods;
    private String waitReceivingGoods;
    private String tradeTypeName;

    private String tradeType;

    private String logisticsName;
    private String logisticsNo;
    private String withdrawName;
    private String withdrawAccount;

    private String verCode;
    private String verName;

    //版本更新
    private String fileUrl;
    /**
     * 是否强制更新 0否 1是
     */
    private String isForce;
    /**
     * 是否收藏，0收藏，1未收藏
     */
    private String collected;

    /**
     * 库存
     */
    private String inventory;

    private String isGiveIntegral;
    private String isLimitPrice;
    private String limitPrice;

    private String msgType;
    private String openPassword;

    private int signIntegral;
    private String signName;
    private int signCountDay;
    private String signTime;

    private String currentStatus;
    private String currentStatusName;
    private String hour;
    private String cardNo;

    /**
     * 1 定制 0普通
     */
    private String openCustom;


    private String activityName;
    /** 奖品类型:1.实物，2.红包，3.积分，4.无，5.默认1积分 */
    private String prizeType;
    /** 奖品图片 */
    private String imageUrl;

    private String activityId;



    private String realityIntegral;

    private String needIntegral;

    private boolean isIntegral;

    private String serviceCharge;

    private String prizeName;

    private String activityLogId;

    /** 支付方式 1.积分，2.余额，3.第三方 */
    private String paymentMethod;

    private String panelAmount;

    private String rechargeAmount;

    private String typeName;
    private String startDay;

    private String cashDepositScale;

    private int giveIntegral;
    private boolean selected;
    private String paymentAmount;

    private ArrayList<EntityForSimple> signList;
    private String isViewMallAuctionDetail;

    private String alert;
    /**
     * 积分类型 0 无积分 1全额 2大礼包
     */
    private String integralType;

    private String showSymbol;
    private String showPrice;
    private String showRemark;
    private String couponShowName;
    private String useTime;
    private String useLimit;
    /**
     * 小banner
     */
    private String minImage;
    /**
     * banner详情
     */
    private String prompt;
    /**
     * h5key
     */
    private String h5Id;

    private String h5Name;
    private String h5Url;
    /**
     * 带参分享：0否，1.是
     */
    private String parameterShare;
    /**
     * 分享配置：1.微信好友，2.微信朋友圈，3.微博
     */
    private String shareConfig;
    /**
     * 分享标题
     */
    private String shareTitle;
    /**
     * 分享内容
     */
    private String shareContent;
    /**
     * 分享链接
     */
    private String shareUrl;
    /**
     * 分享图标
     */
    private String shareImg;

    /**
     * 是否支持小程序：0.否，1.是
     */
    private String isXcx;
    /**
     * 小程序分享标题
     */
    private String xcxShareTitle;
    /**
     * 小程序分享链接
     */
    private String xcxShareUrl;
    /**
     * 小程序分享图标
     */
    private String xcxShareImg;

    private String useLimitType;

    private ArrayList<String> categoryIdList;

    private String useLimitMaxPrice;

    private String discountAmount;

    /**
     *预支付订单已绑定优惠券
     */
    private EntityForSimple memberCoupon;


    /**
     * 大礼包套餐
     */
    private ArrayList<EntityForSimple> auctionMealViewVoList;
    /**
     * 礼包商品详情
     */
    private ArrayList<EntityForSimple> auctionMealGoodsVoList;
    /**
     * 礼包商品id
     */
    private String mallGoodsId;
    /**
     * 礼包商品名称
     */
    private String mallGoodsName;
    /**
     * 礼包积分比例 （手动*100）
     */
    private String integralRatio;
    /**
     * 礼包超值积分比例
     */
    private String worthRatio;

    /**
     * 大礼包套餐（订单列表）
     */
    private ArrayList<EntityForSimple> mallOrderList;

    private String worthIntegral;
    /**
     * 总积分
     */
    private String totalIntegralAmount;
    /**
     * 超值积分
     */
    private String superIntegralAmount;
    /**
     * 是否有库存
     */
    private boolean flag;

    private String firstCategoryId;

    private EntityForSimple auctionMeal;

    /**
     *预支付订单已绑定优惠券
     */

    private String auctionMealId;

    //银行卡相关
    private String bankName;

    private String bindDateTime;

    private String shortCardCode;

    private String useType;

    private String logoUrl;

    private String accountName;

    private String payDate;

    private String orderNo;

    private String bankCardType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhone() {
        return phone;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public String getAmount() {
        return amount;
    }

    public String getFanNums() {
        return fanNums;
    }

    public String getIntegralAmount() {
        return integralAmount;
    }

    public String getLockAmount() {
        if(lockAmount==null){
            return "0.00";
        }
        return lockAmount;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public String getTodayIncome() {
        return todayIncome;
    }

    public ArrayList<EntityForSimple> getRecords() {
        return records;
    }

    public String getStatus() {
        return status;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getLogo() {
        return logo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getIntegral() {
        if(integral==null){
            return "0";
        }
        return integral;
    }

    public String getPrice() {
        if(price==null){
            return "";
        }
        return price.replace(".00","");
    }

    public String getGoodsId() {
        return goodsId;
    }

    public String getAssessPrice() {
        return assessPrice.replace(".00","");
    }

    public String getPriceType() {
        if(priceType==null){
            return "";
        }
        return priceType;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public String getHeadImage() {
        return headImage;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTransactionPrice() {
        if(transactionPrice==null){
            return "";
        }
        return transactionPrice.replace(".00","");
    }

    public String getMarkupNum() {
        return markupNum;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getImage() {
        return image;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getPic() {
        return pic;
    }

    public String getLinkId() {
        return linkId;
    }

    public String getStartPrice() {
        if(startPrice==null){
            return "";
        }
        return startPrice.replace(".00","");
    }

    public String getTopPrice() {
        if(topPrice==null){
            return "";
        }
        return topPrice.replace(".00","");
    }

    public String getAuctionId() {
        return auctionId;
    }

    public String getBrokerage() {
        if(brokerage==null){
            return "";
        }
        return MyApplication.rvZeroAndDot(brokerage);
    }

    public String getMarkupPrice() {
        if(markupPrice==null){
            return "";
        }

        return markupPrice.replace(".00","");
    }

    public Double getNextCashDeposit() {
        return nextCashDeposit;
    }

    public String getUserId() {
        return userId;
    }

    public String getMarkupTimeString() {
        return markupTimeString;
    }

    public String getMarkupTime() {
        return markupTime;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public String getUserName() {
        return userName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getTitle() {
        return title;
    }

    public String getIndirectNum() {
        return indirectNum;
    }

    public String getConsignee() {
        return consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public String getProvinceText() {
        return provinceText;
    }

    public String getCityText() {
        return cityText;
    }

    public String getDistrictText() {
        return districtText;
    }

    public String getStreetText() {
        return streetText;
    }

    public String getAddress() {
        return address;
    }

    public String getTolerant() {
        return tolerant;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setTolerant(String tolerant) {
        this.tolerant = tolerant;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getStreet() {
        return street;
    }

    public String getRemark() {
        return remark;
    }

    public String getAccount() {
        return account;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getAppId() {
        return appId;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public String getSign() {
        return sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPayType() {
        return payType;
    }

    public String getPayTime() {
        return payTime;
    }

    public String getMoney() {
        return money;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPledgePrice() {
        return pledgePrice;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public String getDeliverTime() {
        return deliverTime;
    }

    public EntityForSimple getMemberAddress() {
        return memberAddress;
    }

    public EntityForSimple getOrderViewVo() {
        return orderViewVo;
    }

    public EntityForSimple getGoodsInfo() {
        return goodsInfo;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public String getViewCount() {
        return viewCount;
    }

    public String getStartTimes() {
        return startTimes;
    }

    public String getOrderStr() {
        return orderStr;
    }

    public String getTime() {
        return time;
    }

    public String getContext() {
        return context;
    }

    public ArrayList<EntityForSimple> getData() {
        return data;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getEndTimes() {
        return endTimes;
    }

    public String getNum() {
        return num;
    }

    public String getTotalPrice() {
        if(totalPrice==null){
            return "0";
        }else {
            return totalPrice.replace(".000","").replace(".00","");
        }
    }

    public String getTotal() {
        return total;
    }

    public String getContent() {
        return content;
    }

    public String getNumber() {
        return number;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public String getVipNickname() {
        return vipNickname;
    }

    public String getVipPhone() {
        return vipPhone;
    }

    public String getCount() {
        return count;
    }

    public String getYesterdayProfit() {
        return yesterdayProfit;
    }

    public String getWaitPay() {
        return waitPay;
    }

    public String getWaitDeliverGoods() {
        return waitDeliverGoods;
    }

    public String getWaitReceivingGoods() {
        return waitReceivingGoods;
    }

    public String getTradeTypeName() {
        return tradeTypeName;
    }

    public String getTradeType() {
        if(tradeType==null){
            return "";
        }
        return tradeType;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public String getWithdrawName() {
        return withdrawName;
    }

    public String getWithdrawAccount() {
        return withdrawAccount;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getIsForce() {
        return isForce;
    }

    public String getVerCode() {
        return verCode;
    }

    public String getVerName() {
        return verName;
    }

    public String getCollected() {
        if(collected==null){
            return "1";
        }
        return collected;
    }

    public String getInventory() {
        return inventory;
    }

    public String getIsGiveIntegral() {
        if(isGiveIntegral == null){
            return "";
        }
        return isGiveIntegral;
    }

    public String getIsLimitPrice() {
        if(isLimitPrice==null){
            return "";
        }
        return isLimitPrice;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getLimitPrice() {
        return limitPrice;
    }

    public String getMsgType() {
        return msgType;
    }

    public String getOpenPassword() {
        return openPassword;
    }

    public String getTotalIntegral() {
        return totalIntegral;
    }

    public EntityForSimple getOrder() {
        return order;
    }

    public String getSoldNum() {
        return soldNum;
    }

    public String getPushId() {
        return pushId;
    }

    public int getSignIntegral() {
        return signIntegral;
    }

    public int getSignCountDay() {
        return signCountDay;
    }

    public String getSignTime() {
        return signTime;
    }

    public ArrayList<EntityForSimple> getSignList() {
        return signList;
    }

    public void setSignIntegral(int signIntegral) {
        this.signIntegral = signIntegral;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getOpenCustom() {
        if(openCustom==null){
            return "0";
        }else{
            return openCustom;
        }

    }

    public String getCurrentStatus() {
        if(currentStatus==null){
            return "3";
        }else {
            return currentStatus;
        }
    }

    public String getCurrentStatusName() {
        return currentStatusName;
    }

    public String getHour() {
        return hour;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getPrizeType() {
        return prizeType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getRealityIntegral() {
        return realityIntegral;
    }

    public String getNeedIntegral() {
        return needIntegral;
    }

    public boolean isIntegral() {
        return isIntegral;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public String getActivityLogId() {
        return activityLogId;
    }

    public String getPaymentMethod() {
        if(paymentMethod==null){
            return "";
        }
        return paymentMethod;
    }

    public String getPanelAmount() {
        return panelAmount;
    }

    public String getRechargeAmount() {
        return rechargeAmount;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getStartDay() {
        return startDay;
    }

    public String getCashDepositScale() {
        return cashDepositScale;
    }

    public int getGiveIntegral() {
        return giveIntegral;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public String getIsViewMallAuctionDetail() {
        if(isViewMallAuctionDetail==null){
            return "";
        }
        return isViewMallAuctionDetail;
    }

    public String getSendTime() {
        return sendTime;
    }
    public String getAlert() {
        if(alert==null){
            return "";
        }
        return alert;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getDisplayArea() {
        return displayArea;
    }

    public String getIntegralType() {
        if(integralType==null){
            return "";
        }
        return integralType;
    }

    public ArrayList<EntityForSimple> getAuctionMealViewVoList() {
        return auctionMealViewVoList;
    }

    public ArrayList<EntityForSimple> getAuctionMealGoodsVoList() {
        if(auctionMealGoodsVoList==null){
            return new ArrayList<>();
        }
        return auctionMealGoodsVoList;
    }

    public String getMallGoodsId() {
        return mallGoodsId;
    }

    public String getMallGoodsName() {
        return mallGoodsName;
    }

    public String getIntegralRatio() {
        if(integralRatio==null){
            return "0";
        }else if(integralRatio.equals("0.000")||integralRatio.equals("0.00")||integralRatio.equals("0.0")){
            return "0";
        }
        return integralRatio;
    }

    public String getWorthRatio() {
        if(worthRatio==null){
            return "0";
        }else if(worthRatio.equals("0.000")||worthRatio.equals("0.00")||worthRatio.equals("0.0")){
            return "0";
        }
        return worthRatio;
    }

    public ArrayList<EntityForSimple> getMallOrderList() {
        return mallOrderList;
    }

    public String getWorthIntegral() {
        if(worthIntegral==null){
            return "0";
        }
        return worthIntegral;
    }

    public String getTotalIntegralAmount() {
        return totalIntegralAmount;
    }

    public String getSuperIntegralAmount() {
        return superIntegralAmount;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setMallGoodsId(String mallGoodsId) {
        this.mallGoodsId = mallGoodsId;
    }

    public void setMallGoodsName(String mallGoodsName) {
        this.mallGoodsName = mallGoodsName;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }


    public boolean isCustomMarkUp() {
        return isCustomMarkUp;
    }

    public void setCustomMarkUp(boolean customMarkUp) {
        isCustomMarkUp = customMarkUp;
    }

    public String getShowSymbol() {
        return showSymbol;
    }

    public String getShowPrice() {
        if(showPrice==null){
            return "0";
        }else{
            return showPrice;
        }
    }

    public String getShowRemark() {
        return showRemark;
    }

    public String getCouponShowName() {
        return couponShowName;
    }

    public String getUseTime() {
        return useTime;
    }

    public String getUseLimit() {
        return useLimit;
    }

    public String getMinImage() {
        return minImage;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getH5Id() {
        return h5Id;
    }

    public String getH5Name() {
        return h5Name;
    }

    public String getH5Url() {
        return h5Url;
    }

    public String getParameterShare() {
        return parameterShare;
    }

    public String getShareConfig() {
        if(shareConfig==null) {
            return "";
        }
        return shareConfig;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public String getShareImg() {
        return shareImg;
    }

    public String getIsXcx() {
        return isXcx;
    }

    public String getXcxShareTitle() {
        return xcxShareTitle;
    }

    public String getXcxShareUrl() {
        return xcxShareUrl;
    }

    public String getXcxShareImg() {
        return xcxShareImg;
    }

    public String getUseLimitType() {
        if(useLimitType==null){
            return "1";
        }
        return useLimitType;
    }

    public ArrayList<String> getCategoryIdList() {
        return categoryIdList;
    }

    public String getUseLimitMaxPrice() {
        return useLimitMaxPrice;
    }

    public EntityForSimple getMemberCoupon() {
        return memberCoupon;
    }

    public String getDiscountAmount() {
        if(discountAmount==null){
            return "0";
        }
        return discountAmount;
    }

    public boolean isCanUse() {
        return canUse;
    }

    public String getCantUseRemark() {
        return cantUseRemark;
    }

    public String getFirstCategoryId() {
        if(firstCategoryId==null){
            return "0";
        }else{
            return firstCategoryId;
        }

    }

    public String getAuctionMealId() {
        if(auctionMealId==null){
            return "0";
        }
        return auctionMealId;
    }

    public EntityForSimple getAuctionMeal() {
        return auctionMeal;
    }

    public String getActualPayment() {
        return actualPayment;
    }


    public String getBankName() {
        return bankName;
    }

    public String getBindDateTime() {
        return bindDateTime;
    }

    public String getShortCardCode() {
        return shortCardCode;
    }

    public String getUseType() {
        if(useType==null){
            return "";
        }
        return useType;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getPayDate() {
        return payDate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getBankCardType() {
        if(bankCardType==null){
            return "1";
        }
        return bankCardType;
    }
}

