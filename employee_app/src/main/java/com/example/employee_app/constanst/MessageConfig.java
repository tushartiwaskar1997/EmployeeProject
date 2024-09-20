package com.example.employee_app.constanst;

public class MessageConfig {
    //main Operation
    public static final String SUCCESS_OPERATION = "Process completed successfulLy .";
    //Employee
    public static final String SUCCESS_EMPLOYEE_ADDED = "Employee added successfully .";
    public static final String SUCCESS_EMPLOYEE_DELETED = "Employee deleted successfully .";
    public static final String ERROR_EMPLOYEE_NOT_FOUND = "Employee not found  please check id .";
    public static final String SUCCESS_EMPLOYEE_UPDATED = "Employee updated successfully .";
    public static final String ERROR_EMPLOYEE_IS_ALREADY_ACTIVE = "Employee is already active Please check .";
    public static final String ERROR_DUPLICATE_EMAIL_IS_PRESENT = "This email id is already present please check .";
    public static final String ERROR_EMP_NAME_MISSING = "The employee name is missing please check .";
    public static final String ERROR_DUPLICATE_EMPLOYEE_NAME = " Employee with the same name already present please check .";
    public static final String ERROR_EMPLOYEE_STATUS_CHANGE_TO_ACTIVE = "Employee status changed to active from in active .";
    public static final String ERROR_EMPLOYEE_ALREADY_DELETED = "Employee already deleted please check .";
    //Department
    public static final String SUCCESS_DEPARTMENT_ADDED = "Department added successfully .";
    public static final String SUCCESS_DEPARTMENT_DELETED = "Department deleted successfully .";
    public static final String ERROR_DEPARTMENT_NOT_FOUND = "Department not found  please check id .";
    public static final String SUCCESS_DEPARTMENT_UPDATED = "Department updated successfully .";
    public static final String ERROR_DEPARTMENT_NAME_MISSING = "Department name should be present .";
    public static final String ERROR_DEPARTMENT_ALREADY_EXIST = "Department already exits .";
    public static final String ERROR_DEPARTMENT_IS_MISSING = "Department id is missing please check .";
    public static final String ERROR_DEPARTMENT_STATUS_IS_NOT_ACTIVE = "Department status is not active  employee cannot be allotted .";
    public static final String ERROR_DEPARTMENT_ASSOCIATED_WITH_EMPLOYEE = "Department cannot be deleted as employee associated to it is active .";
    public static final String ERROR_DEPARTMENT_ALREADY_DELETED = "Department is already inactive please check  .";
    public static final String ERROR_DEPARTMENT_ALREADY_ACTIVE = "Department is already active please check .";
    //Designation
    public static final String SUCCESS_DESIGNATION_ADDED = "Designation added successfully .";
    public static final String SUCCESS_DESIGNATION_DELETED = "Designation deleted successfully .";
    public static final String ERROR_DESIGNATION_NOT_FOUND = "Designation not found  please check id .";
    public static final String SUCCESS_DESIGNATION_UPDATED = "Designation updated successfully .";
    public static final String ERROR_DESIGNATION_CANNOT_BE_DELETED = " Designation is referred to some employee hence delete employee first and then try to delete the designation .";
    public static final String ERROR_DEPARTMENT_CANNOT_BE_DELETED = "Department is referred to some designation hence department cannot be deleted please remove designation first .";
    public static final String ERROR_DESIGNATION_NAME_MISSING = "Designation name is missing ,please check .";
    public static final String ERROR_DESIGNATION_NAME_DUPLICATE = "Designation name is already present ,please enter different name .";
    public static final String ERROR_DESIGNATION_IS_MISSING = "Designation id is missing  please check .";
    public static final String ERROR_DESIGNATION_STATUS_IS_NOT_ACTIVE = " Designation status is not active hence employee cannot be allotted .";
    public static final String ERROR_DESIGNATION_ALREADY_DELETED = "Designation  is already delete please check .";
    public static final String ERROR_DESIGNATION_STATUS_ALREADY_ACTIVE ="Designation is already active .";
    //Email
    public static final String ERROR_EMAIL_NOT_FOUND = "Email Not found please check .";
    public static final String ERROR_AUTO_EMAIL_SEND_SUCCESSFULLY = " Auto email has been delivered   successfully .";
    public static final String ERROR_Manual_EMAIL_SEND_SUCCESSFULLY = " Manual email has been delivered  successfully .";
    //
    public static final String ERROR_ID_IS_MISSING = "Id is missing  please check .";
    //image
    public static final String ERROR_IMAGE_IS_MISSING = "please select image .";
    public static final String ERROR_IMAGE_DATA_TYPE_NOT_PROPER = "Please upload the image of jpg , jpeg or png format .";
}
