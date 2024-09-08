package com.example.employee_app.dto;

import com.example.employee_app.constanst.AppConstansts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class HandleRequest {

    public static ResponseEntity<Object> createResponse(String message , HttpStatus status ,Object response){
        Map<String ,Object>  map =  new HashMap<>();
        map.put(AppConstansts.Message, message);
        map.put(AppConstansts.Http_Status, status.value());
        map.put(AppConstansts.Data , response);
        return ResponseEntity.ok(map);
    }
}
