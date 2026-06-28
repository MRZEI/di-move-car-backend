package com.dimovecar.service;

import com.dimovecar.dto.UserSettingVO;
import com.dimovecar.dto.UserVO;

import java.util.Map;

public interface UserService {

    UserVO getUserInfo();

    void updateUserInfo(UserVO userVO);

    UserSettingVO getUserSetting();

    void updateUserSetting(UserSettingVO settingVO);

    void sendSmsCode(String phone, Integer type);

    Map<String, Object> getProfileData();
}
