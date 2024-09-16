package com.example.employee_app.constanst;

public class MessageConfig {
    //main Operation
    public static final String OPERATION_DONE_SUCCESSFULLY = "Process completed successfulLy .";
    public static final String STATUS_IS_MISSING = "status is missing .";
    //Employee
    public static final String EMPLOYEE_ADDED_SUCCESSFULLY = "Employee added successfully .";
    public static final String EMPLOYEE_DELETED_SUCCESSFULLY = "Employee deleted successfully .";
    public static final String EMPLOYEE_NOT_FOUND = "Employee not found  please check id .";
    public static final String EMPLOYEE_UPDATED_SUCCESSFULLY = "Employee updated successfully .";
    public static final String EMPLOYEE_IS_ALREADY_ACTIVE = "Employee is already active Please check .";
    public static final String DUPLICATE_EMAIL_IS_PRESENT = "This email id is already present please check .";
    public static final String EMP_NAME_MISSING = "The employee name is missing please check .";
    public static final String DUPLICATE_EMPLOYEE_NAME = " Employee with the same name already present please check .";
    public static final String EMPLOYEE_STATUS_CHANGE_TO_ACTIVE = "Employee Status changed to active from in active .";
    public static final String EMPLOYEE_ALREADY_DELETED = "Employee already deleted please check .";
    public static final String EMPLOYEE_STATUS_IS_NOT_ACTIVE_DESIGNATION_NOT_CHANGE = "Employee status is inactive hence designation cannot be change .";
    public static final String EMPLOYEE_STATUS_IS_NOT_ACTIVE_DEPARTMENT_NOT_CHANGE = "Employee status is inactive hence department cannot be change .";
    ///checjk
    //Department
    public static final String DEPARTMENT_ADDED_SUCCESSFULLY = "Department added successfully .";
    public static final String DEPARTMENT_DELETED_SUCCESSFULLY = "Department deleted successfully .";
    public static final String DEPARTMENT_NOT_FOUND = "Department not found  please check id .";
    public static final String DEPARTMENT_UPDATED_SUCCESSFULLY = "Department updated successfully .";
    public static final String DEPARTMENT_NAME_MISSING = "Department name should be present .";
    public static final String DEPARTMENT_STATUS_NOT_PROPER = "Department status between true or false .";
    public static final String DEPARTMENT_ALREADY_EXIST = "Department already exits .";
    public static final String DEPARTMENT_IS_MISSING = "Department id is missing please check .";
    public static final String DEPARTMENT_STATUS_IS_NOT_ACTIVE = "Department status is not active  employee cannot be allotted .";
    public static final String DEPARTMENT_STATUS_CANNOT_SET_inACTIVE = "Not have permission to set the department as inactive ,only you can activate it .";
    public static final String DEPARTMENT_ASSOCIATED_WITH_EMPLOYEE = "Department cannot be deleted as employee associated to it is active .";
    //Designation
    public static final String DESIGNATION_ADDED_SUCCESSFULLY = "Designation added successfully .";
    public static final String DESIGNATION_DELETED_SUCCESSFULLY = "Designation deleted successfully .";
    public static final String DESIGNATION_NOT_FOUND = "Designation not found  please check id .";
    public static final String DESIGNATION_UPDATED_SUCCESSFULLY = "Designation updated successfully .";
    public static final String DESIGNATION_CANNOT_BE_DELETED = " Designation is referred to some employee hence delete employee first and then try to delete the designation .";
    public static final String DEPARTMENT_CANNOT_BE_DELETED = "Department is referred to some designation hence department cannot be deleted please remove designation first .";
    public static final String DESIGNATION_NAME_MISSING = "Designation name is missing ,please check .";
    public static final String DESIGNATION_NAME_DUPLICATE = "Designation name is already present ,please enter different name .";
    public static final String DESIGNATION_IS_MISSING = "Designation id is missing  please check .";
    public static final String DESIGNATION_STATUS_IS_NOT_ACTIVE = " Designation status is not active hence employee cannot be allotted .";
    public static final String DESIGNATION_STATUS_CANNOT_SET_inACTIVE = "Not have permission to set the designation as inactive ,only you can activate it .";
    public static final String DESIGNATION_ALREADY_DELETED = "Designation  is already delete please check .";
    //Email
    public static final String EMAIL_NOT_FOUND = "Email Not found please check .";
    public static final String AUTO_EMAIL_SEND_SUCCESSFULLY = " Auto email has been delivered   successfully .";
    public static final String Manual_EMAIL_SEND_SUCCESSFULLY = " Manual email has been delivered  successfully .";
    //
    public static final String ID_IS_MISSING = "Id is missing  please check .";
    //image
    public static final String IMAGE_IS_MISSING = "please select image .";
    public static final String IMAGE_DATA_TYPE_NOT_PROPER = "Please upload the image of jpg , jpeg or png format .";

}
