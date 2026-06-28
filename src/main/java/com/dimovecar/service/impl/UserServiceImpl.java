package com.dimovecar.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dimovecar.common.ResultCode;
import com.dimovecar.common.UserContext;
import com.dimovecar.dto.UserSettingVO;
import com.dimovecar.dto.UserVO;
import com.dimovecar.dto.VehicleVO;
import com.dimovecar.entity.SysUser;
import com.dimovecar.entity.UserSetting;
import com.dimovecar.exception.BusinessException;
import com.dimovecar.mapper.SysUserMapper;
import com.dimovecar.mapper.UserSettingMapper;
import com.dimovecar.service.UserService;
import com.dimovecar.service.VehicleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private UserSettingMapper userSettingMapper;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String SMS_CODE_PREFIX = "sms:code:";
    private static final String SMS_SEND_PREFIX = "sms:send:";

    @Override
    public UserVO getUserInfo() {
        Long userId = UserContext.getUserId();

        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    @Override
    public void updateUserInfo(UserVO userVO) {
        Long userId = UserContext.getUserId();

        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        SysUser update = new SysUser();
        update.setId(userId);

        if (userVO.getNickname() != null) {
            update.setNickname(userVO.getNickname());
        }
        if (userVO.getAvatarUrl() != null) {
            update.setAvatarUrl(userVO.getAvatarUrl());
        }

        sysUserMapper.updateById(update);
    }

    @Override
    public UserSettingVO getUserSetting() {
        Long userId = UserContext.getUserId();

        UserSetting setting = userSettingMapper.selectOne(
                new LambdaQueryWrapper<UserSetting>()
                        .eq(UserSetting::getUserId, userId)
        );

        if (setting == null) {
            setting = new UserSetting();
            setting.setUserId(userId);
            setting.setNotifyEnable(1);
            setting.setSoundEnable(1);
            setting.setVibrateEnable(1);
            setting.setQuietEnable(0);
            setting.setQuietStart("22:00");
            setting.setQuietEnd("08:00");
            userSettingMapper.insert(setting);
        }

        UserSettingVO vo = new UserSettingVO();
        BeanUtils.copyProperties(setting, vo);
        return vo;
    }

    @Override
    public void updateUserSetting(UserSettingVO settingVO) {
        Long userId = UserContext.getUserId();

        UserSetting setting = userSettingMapper.selectOne(
                new LambdaQueryWrapper<UserSetting>()
                        .eq(UserSetting::getUserId, userId)
        );

        if (setting == null) {
            setting = new UserSetting();
            setting.setUserId(userId);
            BeanUtils.copyProperties(settingVO, setting);
            userSettingMapper.insert(setting);
        } else {
            UserSetting update = new UserSetting();
            update.setId(setting.getId());
            if (settingVO.getNotifyEnable() != null) {
                update.setNotifyEnable(settingVO.getNotifyEnable());
            }
            if (settingVO.getSoundEnable() != null) {
                update.setSoundEnable(settingVO.getSoundEnable());
            }
            if (settingVO.getVibrateEnable() != null) {
                update.setVibrateEnable(settingVO.getVibrateEnable());
            }
            if (settingVO.getQuietEnable() != null) {
                update.setQuietEnable(settingVO.getQuietEnable());
            }
            if (settingVO.getQuietStart() != null) {
                update.setQuietStart(settingVO.getQuietStart());
            }
            if (settingVO.getQuietEnd() != null) {
                update.setQuietEnd(settingVO.getQuietEnd());
            }
            userSettingMapper.updateById(update);
        }
    }

    @Override
    public void sendSmsCode(String phone, Integer type) {
        if (phone == null || phone.length() != 11) {
            throw new BusinessException("手机号格式不正确");
        }

        String sendKey = SMS_SEND_PREFIX + phone;
        Boolean hasKey = redisTemplate.hasKey(sendKey);
        if (Boolean.TRUE.equals(hasKey)) {
            throw new BusinessException(ResultCode.SMS_SEND_TOO_FREQUENT);
        }

        String code = RandomUtil.randomNumbers(6);

        String codeKey = SMS_CODE_PREFIX + phone;
        redisTemplate.opsForValue().set(codeKey, code, 5, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(sendKey, "1", 60, TimeUnit.SECONDS);

        System.out.println("发送短信验证码：phone=" + phone + ", code=" + code);
    }

    @Override
    public Map<String, Object> getProfileData() {
        Long userId = UserContext.getUserId();

        Map<String, Object> result = new HashMap<>();

        UserVO userInfo = getUserInfo();
        result.put("userInfo", userInfo);

        List<VehicleVO> vehicles = vehicleService.getVehicleList();
        result.put("vehicles", vehicles);

        UserSettingVO setting = getUserSetting();
        result.put("setting", setting);

        return result;
    }
}
