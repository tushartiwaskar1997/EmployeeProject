package com.example.employee_app.constanst;

public class MessageConfig {
    //main Operation
    public static final String  OPERATION_DONE_SUCCESSFULLY ="Process completed successfulLy . ";
    public static final String  OPERATION_FAIL ="Process failed please check .";
    //Employee
    public static final String  EMPLOYEE_ADDED_SUCCESSFULLY = "Employee added successfully .";
    public static final String  EMPLOYEE_DELETED_SUCCESSFULLY ="Employee deleted successfully .";
    public static final String  EMPLOYEE_NOT_FOUND ="Employee not found  please check id ." ;
    public static final String  EMPLOYEE_UPDATED_SUCCESSFULLY = "Employee updated successfully .";
    public static final String EMP_DEPARTMENT_ID_MISSSING ="Department id should be present  please check .";
    public static final String EMP_DESIGNATION_ID_MISSING ="Employee designation id is missing  please check";
    public static final String DUPLICATE_EMAIL_IS_PRESENT ="This email id is already present please check ";
    public static final String EMP_NAME_MISSING ="The employee name is missing please check ";
    public static final String DUPLICATE_EMPLOYEE_NAME =" Employee with the same name already present please check .";
    public static final String EMPLOYEE_STATUS_CHANGE_TO_ACTIVE= "Employee Status changed to active from in active ";

    //Department
    public static final String  DEPARTMENT_ADDED_SUCCESSFULLY = "Department added successfully .";
    public static final String  DEPARTMENT_DELETED_SUCCESSFULLY ="Department deleted successfully .";
    public static final String  DEPARTMENT_NOT_FOUND ="Department not found  please check id ." ;
    public static final String  DEPARTMENT_UPDATED_SUCCESSFULLY = "Department updated successfully .";
    public static final String DEPARTMENT_NAME_MISSING    ="Department name should be present .";
    public static final String DEPARTMENT_STATUIS_MISSING ="Department status is missing  ";
    public static final String DEPARTMENT_STATUS_NOT_PROPER ="Department status between true or false";
    public static final String DEPARTMENT_ALREADY_EXIST  ="Department already exits .";
    //Designation
    public static final String  DESIGNATION_ADDED_SUCCESSFULLY = "Designation added successfully .";
    public static final String  DESIGNATION_DELETED_SUCCESSFULLY ="Designation deleted successfully .";
    public static final String  DESIGNATION_NOT_FOUND ="Designation not found  please check id ." ;
    public static final String  DESIGNATION_UPDATED_SUCCESSFULLY = "Designation updated successfully .";
    public static final  String DEPARTMENT_CANNOT_BE_DELETED = "Department is referred to some designation hence department cannot be deleted .please remove designation first ";
    public static final  String DESIGNATION_CANNOT_BE_DELETED =" Designation is referred to some employee hence delete employee first and then try to delete the designation .";
    public static final  String DESIGNATION_NAME_MISSING     ="Designation name is missing ,please check !!.";
    public static final   String DESIGNATION_NAME_DUPLICATE  ="Designation name is already present ,please enter differnet name . ";
    //Email
    public static final  String AUTO_EMAIL_SEND_SUCCESSFULLY =" Auto email has been delivered   successfully";
    public static final  String Manual_EMAIL_SEND_SUCCESSFULLY =" Manual email has been delivered  successfully";
    //
    public static final String ID_IS_MISSING ="Id is missing  please check .";
}
