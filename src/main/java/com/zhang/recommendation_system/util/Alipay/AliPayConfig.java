package com.zhang.recommendation_system.util.Alipay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝支付设置
 * @author jitwxs
 * @since 2018/6/4 19:04
 */
@Configuration
public class AliPayConfig {
    private String NAT_APP_URL = "http://vasqk6.natappfree.cc";

    private String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
    private String appId = "2021000119602023";//2016092500594263
    private String merchantPrivateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCqKaYW7melmztXxE+GWd8Mxao3Jf1IUhRGEs7sWr2B+s0LUokXIsVoLksgaHZ4W8DB9MAS7TaD7mPDT4dePFbgddTizkaWBM6uu1USZRT0QNnkto56NAfpybWQg9BirvydfaW9hsuhxQz/So4l1tV/E3cizIN9cJmFGEhWNH1B75MEo63cpePYueZx5VvHpLKDSEUmL9UcWN5t6RlaEb6yv1TLCSDhIrsB/D5R4lRt+gH984TKfyiTaTh6+MJLEoPAm2V/LKdZ5A49rwMrdjmDEsZ607Qy0OEFaNz+GegSoNc0T8EW45EZ3W0PRez/ANkAquBblQo4JmMTNYl2iDv3AgMBAAECggEAd52BYHtEJrjhVUftMx832S2WAuBUlKMGF2AfTyxWLOmVe5EQR1As/pgUPXrRVLgC3xTidCuF9+X7AQ+Fs+uskmqiNLEWE1zbwR50pdKikTSJNupRa3vp2CDv3d+a+9PQRhFOEdWQjW4MPeU12UmUmJihVbf38pTVhibUqqyJJHBUMppZVgq8bYIQKcnY+QlXVfftAaicHeZfPo7x8q6bz4OvFz6NsamBMTZSlKSZowHH/qwk8Lpv9ooWSCZ4wfHjNCLTnujgCeVkl01uhC6V986TkbeSOPGnAcxhEXU1qWnm2PQ2ljIdEBog2u6q0pXRbYsw44iWrAjrIufUk9hIOQKBgQDSKxVQUhWD4zpoCCAo9wrMnOWSR9VKTUXfHxcqcsmBNTMUdM431gyHEiFyYJpdSDvp63h0kfvGLg4x06IcoJVHeYtj/dChluWD2JlMmuUnGgyYkO+KfCL3C1hN+tZSeS/B+bNXzaJfAPMjJ/jwzVS8IzbyUk4Eonn7ImRrOspoTQKBgQDPRTN0hE0LiCtQfe48xf80GDwg5xda7vqm3ACxCm6TCyJsNXUllCsY+fzRKR2b+8CfVdW8tm+FLTP3uJPI0nH1sDnx7N8K/197txwdnRo53hOrW8569qUzeDnaQmfCTnzEgULGmFStOyfXrnTbbHdT7oaU9gDl/Bml1vus8m+XUwKBgAehDr4CrMtD1VvvZ5N1HEw/Kco8l06CLZYi38lnr07kan1EnDPErmG807/+J7xZIeuk45N/tR8FjvhaEVlurtyQWfIARbGH877sfuwb91OLC4gCQt/JD8/d9o+mGl+WDJjDArKeQVWniCvviIbdPUUzFgyH1lMuvrTSE8kcsjpFAoGAXrsRDmLdV7TvnwU/WnVf825UJPGZogzxcEvC4SeUbA49uFkiZn92xx6SZwI0rzL70ZcIbgyvoZ2MT98DZFiSjyjxAtoxArS+7is9ZWeFMgeG093M3DRYQtLjFhcdlxgKPEGbxGqg4N/VkOUCMbs8Opcc2X4p4PM8waglNOFFEVMCgYAPS2yp8ZmAFif8arjJIDjtVH36vu2Mj5BV9MJHqMIPUNpsyMfBZhskEsR1vHXph1j26kQQ6oawvTesjTrzQe+lW2TPLSsJrRVVza5epitoVD8SCc/TyihKkRiFAC6TRvEwySWDF3IqyppbYsP2HTlHlWbPMIDTBfAlm/O1g93IqQ==";
    private String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvL+OA1j/zvqQvLj0aaYor0/N7lDLD3JQXjiGPGrj3IC7z2UjYSprDvq6123b1QQBaqFg/Q8royFLPNeNAwEMa4vrHlTLP9HixC1r1M99EM7EMBkc35LefFMSJlX+xrGXjbpFydzVjABlZsoJgu0GMOuzZBqF91rzHPG20PGrRk2l1qr4K1pUq1C3cL3I+BoJREBvYrqPENJcMJGvXgPvaeSixloN1zn/YhvlylUSqRomtO4ZtOPt2xLGVDeL9rfS4T51HHuU+GRUHWXWxEhXfsWe6IJIxjHhE14zEp3N05jbRwWkQRUX1Q9ksa7STbM9T8UKOBuo3RsJwgMhkrvzzwIDAQAB";
    private String signType = "RSA2";
    private String sellerId = "2088102177227915";
    private String notifyUrl = NAT_APP_URL + "/order/alipay/notify";
    private String returnUrl = NAT_APP_URL + "/order/alipay/return";

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMerchantPrivateKey() {
        return merchantPrivateKey;
    }

    public void setMerchantPrivateKey(String merchantPrivateKey) {
        this.merchantPrivateKey = merchantPrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    @Bean
    AlipayClient alipayClient() {
        return new DefaultAlipayClient
                (gatewayUrl, appId, merchantPrivateKey, "json", "utf-8", alipayPublicKey, signType);
    }
}
