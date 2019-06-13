package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author colaz
 * @date 2019/5/2
 **/
public class Const {

    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String TOKEN_PREFIX = "token_";

    public interface Role {
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN = 1;
    }

    public interface RedisCacheExTime {
        int REDIS_SESSION_EXTIME = 60 * 30; //30分钟
    }

    public interface Cart {
        int CHECKED = 1;
        int UNCHECKED = 0;

        String LIMIT_COUNT_SUCCESS = "LIMIT_COUNT_SUCCESS";
        String LIMIT_COUNT_FAIL = "LIMIT_COUNT_FAIL";

    }

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_asc", "price_desc");
    }

    public enum ProductStatusEnum {
        ON_SALE(1, "在线");

        private int code;
        private String value;

        ProductStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public enum OrderStatusEnum {
        CANCELED(0, "已取消"),
        NO_PAY(10, "未付款"),
        PAID(20, "已付款"),
        SHIPPED(40, "已发货"),
        OEDER_SUCCESS(50, "订单完成"),
        ORDER_CLOSED(60, "订单关闭");

        private int code;
        private String value;
        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static OrderStatusEnum codeOf(int code) {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("没有找到对应枚举");
        }
    }

    public interface AlipayCallback {
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";
    }

    public enum PayPlatformEnum{
        ALIPAY(1, "支付宝");

        private int code;
        private String value;
        PayPlatformEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public enum PaymentTypeEnum {
        ONLINE_PAY(1, "在线支付");

        private int code;
        private String value;
        PaymentTypeEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        public static PaymentTypeEnum codeOf(int code) {
            for (PaymentTypeEnum paymentTypeEnum : values()) {
                if (paymentTypeEnum.getCode() == code) {
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应枚举");
        }
    }




}
