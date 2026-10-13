package AzureDeploy.DEPLOY.Model;

import AzureDeploy.DEPLOY.Model.enums.ActionType;

// Holds the employee data and the kind of operation being logged
public class EmployeeLog {
    private ActionType type;

    private Employee employee;

    public EmployeeLog(Employee employee, ActionType type) {
        this.employee = employee;
        this.type = type;
    }

    // Returns whether this log represents an INSERT, UPDATE, or DELETE
    public ActionType getType() {
        return type;
    }

    // Returns the employee details to include in the log entry
    public Employee getEmployee() {
        return employee;
    }

}
