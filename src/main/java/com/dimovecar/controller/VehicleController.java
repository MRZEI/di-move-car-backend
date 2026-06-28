package com.dimovecar.controller;

import com.dimovecar.common.Result;
import com.dimovecar.dto.VehicleAddDTO;
import com.dimovecar.dto.VehicleVO;
import com.dimovecar.service.VehicleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "车辆接口")
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @ApiOperation("获取车辆列表")
    @GetMapping("/list")
    public Result<List<VehicleVO>> getVehicleList() {
        List<VehicleVO> list = vehicleService.getVehicleList();
        return Result.success(list);
    }

    @ApiOperation("获取车辆详情")
    @GetMapping("/{id}")
    public Result<VehicleVO> getVehicleById(@PathVariable Long id) {
        VehicleVO vo = vehicleService.getVehicleById(id);
        return Result.success(vo);
    }

    @ApiOperation("添加车辆")
    @PostMapping("/add")
    public Result<VehicleVO> addVehicle(@Validated @RequestBody VehicleAddDTO dto) {
        VehicleVO vo = vehicleService.addVehicle(dto);
        return Result.success(vo);
    }

    @ApiOperation("修改车辆信息")
    @PutMapping("/{id}")
    public Result<Void> updateVehicle(@PathVariable Long id, @RequestBody VehicleAddDTO dto) {
        vehicleService.updateVehicle(id, dto);
        return Result.success();
    }

    @ApiOperation("删除车辆")
    @DeleteMapping("/{id}")
    public Result<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return Result.success();
    }

    @ApiOperation("设置默认车辆")
    @PostMapping("/{id}/default")
    public Result<Void> setDefaultVehicle(@PathVariable Long id) {
        vehicleService.setDefaultVehicle(id);
        return Result.success();
    }

    @ApiOperation("生成挪车二维码")
    @PostMapping("/{id}/qr")
    public Result<Void> generateQrCode(@PathVariable Long id) {
        vehicleService.generateQrCode(id);
        return Result.success();
    }

    @ApiOperation("根据车牌查询车辆")
    @GetMapping("/search")
    public Result<VehicleVO> getVehicleByPlate(@RequestParam String plateNumber) {
        VehicleVO vo = vehicleService.getVehicleByPlate(plateNumber);
        return Result.success(vo);
    }
}
