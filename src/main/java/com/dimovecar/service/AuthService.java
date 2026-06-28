package com.dimovecar.service;

import com.dimovecar.dto.LoginDTO;
import com.dimovecar.dto.LoginVO;
import com.dimovecar.dto.UserVO;

public interface AuthService {

    LoginVO loginByPhone(LoginDTO loginDTO);

    LoginVO wxLogin(String code, String userInfo);

    void logout();

    UserVO getCurrentUser();
}
