package com.dimovecar.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dimovecar.common.ResultCode;
import com.dimovecar.common.UserContext;
import com.dimovecar.dto.VehicleAddDTO;
import com.dimovecar.dto.VehicleVO;
import com.dimovecar.entity.UserVehicle;
import com.dimovecar.exception.BusinessException;
import com.dimovecar.mapper.UserVehicleMapper;
import com.dimovecar.service.VehicleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private UserVehicleMapper userVehicleMapper;

    @Override
    public List<VehicleVO> getVehicleList() {
        Long userId = UserContext.getUserId();

        List<UserVehicle> vehicles = userVehicleMapper.selectList(
                new LambdaQueryWrapper<UserVehicle>()
                        .eq(UserVehicle::getUserId, userId)
                        .orderByDesc(UserVehicle::getIsDefault)
                        .orderByAsc(UserVehicle::getCreateTime)
        );

        return vehicles.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleVO getVehicleById(Long id) {
        Long userId = UserContext.getUserId();

        UserVehicle vehicle = userVehicleMapper.selectOne(
                new LambdaQueryWrapper<UserVehicle>()
                        .eq(UserVehicle::getId, id)
                        .eq(UserVehicle::getUserId, userId)
        );

        if (vehicle == null) {
            throw new BusinessException(ResultCode.VEHICLE_NOT_EXIST);
        }

        return convertToVO(vehicle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VehicleVO addVehicle(VehicleAddDTO dto) {
        Long userId = UserContext.getUserId();

        UserVehicle existing = userVehicleMapper.selectOne(
                new LambdaQueryWrapper<UserVehicle>()
                        .eq(UserVehicle::getPlateNumber, dto.getPlateNumber())
        );

        if (existing != null) {
            throw new BusinessException(ResultCode.VEHICLE_ALREADY_EXIST);
        }

        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            UserVehicle updateDefault = new UserVehicle();
            updateDefault.setIsDefault(0);
            userVehicleMapper.update(updateDefault,
                    new LambdaQueryWrapper<UserVehicle>()
                            .eq(UserVehicle::getUserId, userId)
                            .eq(UserVehicle::getIsDefault, 1)
            );
        }

        UserVehicle vehicle = new UserVehicle();
        BeanUtils.copyProperties(dto, vehicle);
        vehicle.setUserId(userId);
        vehicle.setStatus(1);
        if (vehicle.getIsDefault() == null) {
            vehicle.setIsDefault(0);
        }

        userVehicleMapper.insert(vehicle);

        return convertToVO(vehicle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVehicle(Long id, VehicleAddDTO dto) {
        Long userId = UserContext.getUserId();

        UserVehicle vehicle = userVehicleMapper.selectOne(
                new LambdaQueryWrapper<UserVehicle>()
                        .eq(UserVehicle::getId, id)
                        .eq(UserVehicle::getUserId, userId)
        );

        if (vehicle == null) {
            throw new BusinessException(ResultCode.VEHICLE_NOT_EXIST);
        }

        if (!dto.getPlateNumber().equals(vehicle.getPlateNumber())) {
            UserVehicle existing = userVehicleMapper.selectOne(
                    new LambdaQueryWrapper<UserVehicle>()
                            .eq(UserVehicle::getPlateNumber, dto.getPlateNumber())
            );
            if (existing != null && !existing.getId().equals(id)) {
                throw new BusinessException(ResultCode.VEHICLE_ALREADY_EXIST);
            }
        }

        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            UserVehicle updateDefault = new UserVehicle();
            updateDefault.setIsDefault(0);
            userVehicleMapper.update(updateDefault,
                    new LambdaQueryWrapper<UserVehicle>()
                            .eq(UserVehicle::getUserId, userId)
                            .eq(UserVehicle::getIsDefault, 1)
            );
        }

        UserVehicle update = new UserVehicle();
        BeanUtils.copyProperties(dto, update);
        update.setId(id);
        userVehicleMapper.updateById(update);
    }

    @Override
    public void deleteVehicle(Long id) {
        Long userId = UserContext.getUserId();

        UserVehicle vehicle = userVehicleMapper.selectOne(
                new LambdaQueryWrapper<UserVehicle>()
                        .eq(UserVehicle::getId, id)
                        .eq(UserVehicle::getUserId, userId)
        );

        if (vehicle == null) {
            throw new BusinessException(ResultCode.VEHICLE_NOT_EXIST);
        }

        userVehicleMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultVehicle(Long id) {
        Long userId = UserContext.getUserId();

        UserVehicle vehicle = userVehicleMapper.selectOne(
                new LambdaQueryWrapper<UserVehicle>()
                        .eq(UserVehicle::getId, id)
                        .eq(UserVehicle::getUserId, userId)
        );

        if (vehicle == null) {
            throw new BusinessException(ResultCode.VEHICLE_NOT_EXIST);
        }

        UserVehicle updateDefault = new UserVehicle();
        updateDefault.setIsDefault(0);
        userVehicleMapper.update(updateDefault,
                new LambdaQueryWrapper<UserVehicle>()
                        .eq(UserVehicle::getUserId, userId)
                        .eq(UserVehicle::getIsDefault, 1)
        );

        UserVehicle update = new UserVehicle();
        update.setId(id);
        update.setIsDefault(1);
        userVehicleMapper.updateById(update);
    }

    @Override
    public VehicleVO getVehicleByPlate(String plateNumber) {
        UserVehicle vehicle = userVehicleMapper.selectOne(
                new LambdaQueryWrapper<UserVehicle>()
                        .eq(UserVehicle::getPlateNumber, plateNumber)
        );

        if (vehicle == null) {
            return null;
        }

        return convertToVO(vehicle);
    }

    @Override
    public void generateQrCode(Long vehicleId) {
        Long userId = UserContext.getUserId();

        UserVehicle vehicle = userVehicleMapper.selectOne(
                new LambdaQueryWrapper<UserVehicle>()
                        .eq(UserVehicle::getId, vehicleId)
                        .eq(UserVehicle::getUserId, userId)
        );

        if (vehicle == null) {
            throw new BusinessException(ResultCode.VEHICLE_NOT_EXIST);
        }

        String qrCodeUrl = "/api/qr/" + vehicle.getId();

        UserVehicle update = new UserVehicle();
        update.setId(vehicleId);
        update.setQrCodeUrl(qrCodeUrl);
        userVehicleMapper.updateById(update);
    }

    private VehicleVO convertToVO(UserVehicle vehicle) {
        VehicleVO vo = new VehicleVO();
        BeanUtils.copyProperties(vehicle, vo);

        String typeDesc = "其他";
        if (vehicle.getVehicleType() != null) {
            switch (vehicle.getVehicleType()) {
                case 1:
                    typeDesc = "小型汽车 · 蓝牌";
                    break;
                case 2:
                    typeDesc = "小型汽车 · 新能源";
                    break;
                case 3:
                    typeDesc = "大型汽车";
                    break;
                default:
                    typeDesc = "其他";
            }
        }
        vo.setVehicleTypeDesc(typeDesc);

        return vo;
    }
}
