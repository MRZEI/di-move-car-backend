package com.dimovecar.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dimovecar.common.ResultCode;
import com.dimovecar.common.UserContext;
import com.dimovecar.dto.LoginDTO;
import com.dimovecar.dto.LoginVO;
import com.dimovecar.dto.UserVO;
import com.dimovecar.entity.SysUser;
import com.dimovecar.entity.UserSetting;
import com.dimovecar.exception.BusinessException;
import com.dimovecar.mapper.SysUserMapper;
import com.dimovecar.mapper.UserSettingMapper;
import com.dimovecar.service.AuthService;
import com.dimovecar.utils.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private UserSettingMapper userSettingMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String SMS_CODE_PREFIX = "sms:code:";

    @Override
    public LoginVO loginByPhone(LoginDTO loginDTO) {
        String phone = loginDTO.getPhone();
        String code = loginDTO.getCode();

        String redisKey = SMS_CODE_PREFIX + phone;
        String savedCode = redisTemplate.opsForValue().get(redisKey);

        if (savedCode == null) {
            throw new BusinessException(ResultCode.SMS_CODE_ERROR);
        }

        if (!savedCode.equals(code)) {
            throw new BusinessException(ResultCode.SMS_CODE_ERROR);
        }

        redisTemplate.delete(redisKey);

        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getPhone, phone)
        );

        if (user == null) {
            user = new SysUser();
            user.setPhone(phone);
            user.setNickname("用户" + phone.substring(phone.length() - 4));
            user.setIsVerified(0);
            user.setStatus(1);
            user.setNotifyCount(0);
            sysUserMapper.insert(user);

            UserSetting setting = new UserSetting();
            setting.setUserId(user.getId());
            setting.setNotifyEnable(1);
            setting.setSoundEnable(1);
            setting.setVibrateEnable(1);
            setting.setQuietEnable(0);
            setting.setQuietStart("22:00");
            setting.setQuietEnd("08:00");
            userSettingMapper.insert(setting);
        }

        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        user.setLastLoginTime(LocalDateTime.now());
        sysUserMapper.updateById(user);

        String token = jwtUtil.generateToken(user.getId());

        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserId(user.getId());
        loginVO.setUserInfo(convertToUserVO(user));

        return loginVO;
    }

    @Override
    public LoginVO wxLogin(String code, String userInfo) {
        String mockOpenId = "wx_openid_" + Math.abs(code.hashCode() % 1000000);

        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getOpenid, mockOpenId)
        );

        if (user == null) {
            user = new SysUser();
            user.setOpenid(mockOpenId);
            user.setNickname("微信用户" + code.substring(code.length() - 4));
            user.setIsVerified(0);
            user.setStatus(1);
            user.setNotifyCount(0);
            sysUserMapper.insert(user);

            UserSetting setting = new UserSetting();
            setting.setUserId(user.getId());
            setting.setNotifyEnable(1);
            setting.setSoundEnable(1);
            setting.setVibrateEnable(1);
            setting.setQuietEnable(0);
            setting.setQuietStart("22:00");
            setting.setQuietEnd("08:00");
            userSettingMapper.insert(setting);
        }

        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        user.setLastLoginTime(LocalDateTime.now());
        sysUserMapper.updateById(user);

        String token = jwtUtil.generateToken(user.getId());

        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserId(user.getId());
        loginVO.setUserInfo(convertToUserVO(user));

        return loginVO;
    }

    @Override
    public void logout() {
        Long userId = UserContext.getUserId();
        if (userId != null) {
            try {
                redisTemplate.opsForValue().set("token:blacklist:" + userId, "1", 7, TimeUnit.DAYS);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public UserVO getCurrentUser() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED);
        }

        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        return convertToUserVO(user);
    }

    private UserVO convertToUserVO(SysUser user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}
