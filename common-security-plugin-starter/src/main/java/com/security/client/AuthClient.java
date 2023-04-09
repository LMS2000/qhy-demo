package com.security.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 大忽悠
 * @create 2023/2/14 15:02
 */
@FeignClient("qhy-auth")
public interface AuthClient {
    @PostMapping("/auth/authentication/getAuthorties")
    UserAuthorityDto getUserAuthorities(@RequestParam("username") String username, @RequestParam("serviceName") String serviceName);

    @PostMapping("/auth/authentication/registerAuthorities")
    UserAuthorityDto registerAuthorities(@RequestParam("name") String name, @RequestParam("desc") String desc);

    @PostMapping("/auth/authentication/register")
    void registerUser(@RequestParam("username") String username, @RequestParam("serviceName") String serviceName,
                      @RequestParam("role") String role);

    @DeleteMapping("/auth/authentication/delete")
    void deleteUser(@RequestParam("username") String username, @RequestParam("serviceName") String serviceName);
}
