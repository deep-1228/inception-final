package com.stanzaliving.inception.http.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanzaliving.inception.dto.*;
import com.stanzaliving.inception.dto.common.ResponseDto;
import com.stanzaliving.inception.util.RestApiManager;
import com.stanzaliving.secure.dto.UserDetailDto;
import liquibase.repackaged.org.apache.commons.collections4.CollectionUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
public class AclServiceClient extends RestApiManager {

    @Autowired
    public void setBaseUrl(@Value("${service.acl.url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private ObjectMapper objectMapper = new ObjectMapper();


    public Map<String, List<String>> getPermission(String userId) {

        try {
            HashMap<String, Object> queryParams = new HashMap<String, Object>() {{
            }};
            String url = "/internal/user" + "/" + userId;
            Map<String, String> overrideHeaders = new HashMap<>();
            ResponseDto responseDto = super.get(url, queryParams, null, true, ResponseDto.class);
            if (Objects.nonNull(responseDto)) {
                return (Map<String, List<String>>) responseDto.getData();
            }
        } catch (Exception e) {
            log.error("Error occurred during HTTP Client call to get user data Data", e);
        }
        return new HashMap<>();
    }

    public Map<String, Object> convertMapStringToObject(Map<String, String> stringMap) {
        Map<String, Object> resMap = new HashMap<>();
        for (Map.Entry<String, String> mapEle: stringMap.entrySet()) {
            resMap.put(mapEle.getKey(), (Object) mapEle.getValue());
        }
        return resMap;
    }

    public List<String> getUsersForPermisisons(ResourcePermissionNameWithAttribute resourcePermissionNameWithAttribute) {
        try {
            Map<String, Object> attributes = new HashMap<>();
            if(Objects.nonNull(resourcePermissionNameWithAttribute.getAttributes())) {
                attributes = convertMapStringToObject(resourcePermissionNameWithAttribute.getAttributes());
            }
            AclPermissionRequestDto aclPermissionRequestDto = new AclPermissionRequestDto(resourcePermissionNameWithAttribute.getResource(), resourcePermissionNameWithAttribute.getPermission(),attributes);


            HashMap<String, Object> queryParams = new HashMap<String, Object>() {{}};
            String url = "/internal/eval/permission/users";
            Map<String, String> overrideHeaders = new HashMap<>();
            overrideHeaders.put(HttpHeaders.CONTENT_TYPE, "application/json");
            ResponseDto object = super.post(url, queryParams, aclPermissionRequestDto, overrideHeaders, true, ResponseDto.class);
            ResponseDto userResponseDto = objectMapper.convertValue(object, ResponseDto.class);
            if(Objects.nonNull(userResponseDto)) {
                return (List<String>) userResponseDto.getData();
            }
        }catch(Exception e) {
            e.printStackTrace();
            log.error("Error occurred during HTTP Client call to get user data Data",e);
        }
        return new ArrayList<>();
    }


    public Map<String,String> getUsersFormWFPermissions(List<AclPermissionRequestDto> aclPermissionRequestDto) {
        try {
            String url = "/internal/eval/permissionsList/users";
            Map<String, String> overrideHeaders = new HashMap<>();
            overrideHeaders.put(HttpHeaders.CONTENT_TYPE, "application/json");
            ResponseDto object = super.post(url, null, aclPermissionRequestDto, overrideHeaders, true, ResponseDto.class);
            ResponseDto userResponseDto = objectMapper.convertValue(object, ResponseDto.class);
            if(Objects.nonNull(userResponseDto)) {
                return (Map<String,String>) userResponseDto.getData();
            }
        } catch(Exception e) {
            log.error("Error occurred during HTTP Client call to get user data Data",e);
        }
        return new HashMap<String, String>();
    }

    public Map<String, UserDetailDto> getUsersDetails(List<String> userIds) {
        try {
            String url = "/internal/redirect/users/list";
            ResponseDto object = super.post(url, null, userIds, null, false, ResponseDto.class);
            List<UserDetailDto> userDetailDtos = objectMapper.convertValue(object.getData(), new TypeReference<List<UserDetailDto>>() {
            });
            Map<String, UserDetailDto> userDetailDtosMap = new HashMap<>();
            if(CollectionUtils.isEmpty(userDetailDtos)) {
                return new HashMap<>();
            }

            for(UserDetailDto userDetailDto: userDetailDtos) {
                userDetailDtosMap.put(userDetailDto.getUserUuid(), userDetailDto);
            }
            return userDetailDtosMap;
        }catch(Exception e) {
            log.error("Error occurred during HTTP Client call to get user details list",e);
            throw e;
        }
    }

    public List<UserDetailDto> getUsersDetailsAsPerUserId(List<String> userIds) {
        try {
            String url = "/internal/redirect/users/list";
            ResponseDto object = super.post(url, null, userIds, null, false, ResponseDto.class);
            List<UserDetailDto> userDetailDtos = objectMapper.convertValue(object.getData(), new TypeReference<List<UserDetailDto>>() {
            });
            if(CollectionUtils.isEmpty(userDetailDtos)) {
                return new ArrayList<>();
            }
            return userDetailDtos;
        }catch(Exception e) {
            log.error("Error occurred during HTTP Client call to get user details list",e);
            throw e;
        }
    }

    public UserDetailDto getUserDetailAsPerUserId(String userId) {
        try {
            String url = "/internal/user/fetch/" + userId;
            ResponseDto object = super.get(url, null, null, true, ResponseDto.class);
            UserDetailDto userDetailDto = objectMapper.convertValue(object.getData(), new TypeReference<UserDetailDto>() {
            });
            if(Objects.isNull(userDetailDto)) {
                return null;
            }
            return userDetailDto;
        }catch(Exception e) {
            log.error("Error occurred during HTTP Client call to get user detail",e);
            throw e;
        }
    }

    public List<UserDto> getUserDetails(List<RolePermissionDto> rolePermissionDtos) {
        try {
            HashMap<String, Object> queryParams = new HashMap<String, Object>() {{}};
            String url = "/permissions/users/list";
            Map<String, String> overrideHeaders = new HashMap<>();
            overrideHeaders.put(HttpHeaders.CONTENT_TYPE, "application/json");
            UserResponseDto userResponseDto = super.post(url, queryParams, rolePermissionDtos, overrideHeaders, true, UserResponseDto.class);
            if(Objects.nonNull(userResponseDto)) {
                return userResponseDto.getData();
            }
        }catch(Exception e) {
            log.error("Error occurred during HTTP Client call to get user data Data",e);
        }
        return new ArrayList<>();
    }

    public Map<String, List<UserAttributeDto>> getUserAttribute(UserPermissionDto userPermissionDto) {
        try {
            HashMap<String, Object> queryParams = new HashMap<String, Object>() {{}};
            String url = "/user/attributes";
            Map<String, String> overrideHeaders = new HashMap<>();
            overrideHeaders.put(HttpHeaders.CONTENT_TYPE, "application/json");
            UserPermissionResponseDto userResponseDto = super.post(url, queryParams, userPermissionDto, overrideHeaders, true, UserPermissionResponseDto.class);
            if(Objects.nonNull(userResponseDto)) {
                return userResponseDto.getData();
            }
        }catch(Exception e) {
            log.error("Error occurred during HTTP Client call to get user data Data",e);
        }
        return new HashMap<>();
    }

}
