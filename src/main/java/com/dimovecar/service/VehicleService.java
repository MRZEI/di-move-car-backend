package com.dimovecar.service;

import com.dimovecar.common.Result;
import com.dimovecar.dto.VehicleAddDTO;
import com.dimovecar.dto.VehicleVO;

import java.util.List;

public interface VehicleService {

    List<VehicleVO> getVehicleList();

    VehicleVO getVehicleById(Long id);

    VehicleVO addVehicle(VehicleAddDTO dto);

    void updateVehicle(Long id, VehicleAddDTO dto);

    void deleteVehicle(Long id);

    void setDefaultVehicle(Long id);

    VehicleVO getVehicleByPlate(String plateNumber);

    void generateQrCode(Long vehicleId);
}
