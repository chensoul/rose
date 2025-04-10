package com.chensoul.core;

/**
 * 缓存的key 常量
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
public interface CacheConstants {

    /**
     * 菜单信息缓存的key
     */
    String MENU_DETAIL = "chensoul:menu_detail";

    /**
     * 用户信息缓存的key
     */
    String USER_DETAIL = "chensoul:user_detail";

    /**
     * 字典信息缓存的key
     */
    String DICT_DETAIL = "chensoul:dict_detail";

    /**
     * oauth 客户端缓存的key，值为hash
     */
    String OAUTH_CLIENT_DETAIL = "chensoul:oauth_client_detail";

    /**
     * tokens缓存的key
     */
    String OAUTH_TOKENS = "chensoul:oauth_tokens";

    /**
     * oauth token store 存储前缀
     */
    String OAUTH_TOKEN_STORE_PREFIX = "chensoul:oauth_token_store:";

    String SHOP = "chensoul:shop";

    String TENANT_APP_CONFIG = "chensoul:tenant_app_config";

    String CLIENT = "chensoul:client";

    String LOGIN_FAIL_COUNT_CACHE = "chensoul:loginFailCount:";
}
