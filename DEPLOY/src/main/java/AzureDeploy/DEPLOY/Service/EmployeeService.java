package AzureDeploy.DEPLOY.Service;

import AzureDeploy.DEPLOY.Model.Employee;
import AzureDeploy.DEPLOY.Model.EmployeeLog;
import AzureDeploy.DEPLOY.Model.enums.ActionType;
import AzureDeploy.DEPLOY.Repository.EmployeeLogRepository;
import AzureDeploy.DEPLOY.Repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private EmployeeRepository employeeRepository;
    private EmployeeLogRepository employeeLogRepository;

    // Spring supplies the repositories when it creates this service
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeLogRepository employeeLogRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeLogRepository = employeeLogRepository;
    }

    // Saves the employee in SQL, then records an INSERT entry in Azure Table Storage
    public Employee createEmployee(Employee employee) {
        Employee savedEmployee = employeeRepository.save(employee);

        employeeLogRepository.save(new EmployeeLog(savedEmployee, ActionType.INSERT));

        return savedEmployee;
    }

    // Returns the employee or throws an exception if the ID does not exist
    public Employee findEmployeeById(int id) {
        return employeeRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Employee not found: " + id));
    }

    // Copies the submitted values onto the existing employee before saving
    public Employee updateEmployee(Employee employee, int id) {
        Employee existingEmployee = findEmployeeById(id);

        existingEmployee.setName(employee.getName());
        existingEmployee.setAddress(employee.getAddress());
        existingEmployee.setPhoneNumber(employee.getPhoneNumber());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setDepartment(employee.getDepartment());
        existingEmployee.setSalary(employee.getSalary());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        // Records the updated employee details as an UPDATE entry
        employeeLogRepository.save(new EmployeeLog(updatedEmployee, ActionType.UPDATE));

        return updatedEmployee;
    }

    // Deletes the employee from SQL and records a DELETE entry
    public void deleteEmployeeById(int id) {
        Employee employee = findEmployeeById(id);
        employeeRepository.delete(employee);
        employeeLogRepository.save(new EmployeeLog(employee, ActionType.DELETE));
    }
}
