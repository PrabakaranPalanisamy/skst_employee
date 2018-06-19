package com.mazenet.mzs119.skst.Model;

/**
 * Created by PRABAKARAN on 12/12/2017.
 */

public class CollectModel {

    public String Name, Id, Emp_Code;

    public void setId(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String collectAgentName) {
        this.Name = collectAgentName;
    }

    public String getEmp_Code() {
        return Emp_Code;
    }

    public void setEmp_Code(String emp_Code) {
        Emp_Code = emp_Code;
    }
}
