package com.User_Service.User_Service.service;

import com.User_Service.User_Service.exception.InvalidRequestException;
import com.User_Service.User_Service.request.*;
import com.User_Service.User_Service.response.*;
import com.User_Service.User_Service.rri.Permission;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

public class UserServiceTest {
    @InjectMocks
    UserServiceImpl service = new UserServiceImpl();

    @Test
    @DisplayName("If_ManagePermissionsRequest_Is_Null")
    public void managePermissionsNullRequest() {
        Assertions.assertThrows(InvalidRequestException.class, () -> service.managePermissions(null));
    }

    @Test
    @DisplayName("If_Both_ManagePermissionsRequest_Attributes_Are_Null")
    public void managePermissionsRequestNullAttribs() {
        ManagePermissionsRequest request = new ManagePermissionsRequest(null, null);
        Assertions.assertThrows(InvalidRequestException.class, () -> service.managePermissions(request));
    }

    @Test
    @DisplayName("If_ManagePermissionsRequest_Username_Field_Is_Null")
    public void managePermissionsRequestNullUsernameField() {
        ManagePermissionsRequest request = new ManagePermissionsRequest(null, Permission.VIEWING);
        Assertions.assertThrows(InvalidRequestException.class, () -> service.managePermissions(request));
    }

    @Test
    @DisplayName("If_ManagePermissionsRequest_Permission_Field_Is_Null")
    public void managePermissionsRequestNullPermissionField() {
        ManagePermissionsRequest request = new ManagePermissionsRequest("exampleUser", null);
        Assertions.assertThrows(InvalidRequestException.class, () -> service.managePermissions(request));
    }









}
