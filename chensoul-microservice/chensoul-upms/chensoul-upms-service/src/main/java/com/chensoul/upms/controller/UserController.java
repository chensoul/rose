package com.chensoul.upms.controller;

import static com.chensoul.upms.ControllerConstants.TENANT_ID;
import static com.chensoul.upms.ControllerConstants.USER_ID;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chensoul.core.util.RestResponse;
import com.chensoul.security.util.TokenPair;
import com.chensoul.spring.boot.syslog.annotation.SysLog;
import com.chensoul.upms.entity.User;
import com.chensoul.upms.model.param.UserRegisterRequest;
import com.chensoul.upms.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 注册用户
     *
     * @param userRegisterRequest 用户
     * @return 添加的用户
     */
    @SysLog("注册用户")
    @PostMapping("/register")
    public RestResponse<User> registerUser(@RequestBody UserRegisterRequest userRegisterRequest) {
        return RestResponse.ok(userService.registerUser(userRegisterRequest, true));
    }

    @SysLog("删除用户")
    @DeleteMapping(value = "/{userId}")
    public RestResponse<Void> deleteUser(@PathVariable(USER_ID) String userId) throws Exception {
        userService.deleteUser(userService.getById(userId));
        return RestResponse.ok();
    }

    @PostMapping("/{userId}/userCredential/enabled")
    public RestResponse<User> setUserCredentialEnabled(
            @PathVariable(USER_ID) Long userId,
            @RequestParam(required = false, defaultValue = "true") boolean userCredentialEnabled)
            throws Exception {
        return RestResponse.ok(userService.setUserCredentialEnabled(userId, userCredentialEnabled));
    }

    @GetMapping("/list")
    public RestResponse<List<User>> listUsers() {
        return RestResponse.ok(userService.list());
    }

    @GetMapping("/{userId}")
    public RestResponse<User> findUserById(@PathVariable(USER_ID) Long userId) {
        return RestResponse.ok(userService.getById(userId));
    }

    // @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @GetMapping("/{userId}/token")
    public RestResponse<TokenPair> getUserToken(@PathVariable(USER_ID) Long userId) {
        return RestResponse.ok(userService.getUserToken(userId));
    }

    // @PreAuthorize("hasAuthority('SYS_ADMIN')")
    @GetMapping(value = "/tenant/{tenantId}/users")
    public Page<User> findUsersByTenantId(Page page, @PathVariable(TENANT_ID) String tenantId) {
        return userService.findUserByTenantId(page, tenantId);
    }
}
