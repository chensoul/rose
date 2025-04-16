package com.chensoul.rose.upms.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chensoul.rose.upms.domain.entity.OAuth2Client;

/**
 * <p>
 * 客户端 服务类
 * </p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public interface OAuth2ClientService extends IService<OAuth2Client> {

    OAuth2Client getByClientId(String clientId);

    Boolean removeClientDetailsById(Long id);

    Boolean updateClientDetailsById(OAuth2Client OAuth2Client);

    boolean saveClientDetails(OAuth2Client OAuth2Client);
}
