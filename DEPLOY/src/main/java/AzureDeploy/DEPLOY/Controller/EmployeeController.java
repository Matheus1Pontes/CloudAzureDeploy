package AzureDeploy.DEPLOY.Controller;

import AzureDeploy.DEPLOY.Model.Employee;
import AzureDeploy.DEPLOY.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // POST /employees: creates an employee and returns its URL in the Location header
    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEmployee.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdEmployee);
    }

    // GET /employees/{id}: returns one employee
    @GetMapping("/{id}")
    public ResponseEntity<Employee> findByid(@PathVariable("id") int id) {
        Employee employee = employeeService.findEmployeeById(id);
        return ResponseEntity.ok().body(employee);
    }

    // PUT /employees/{id}: updates the employee with the submitted values
    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable("id") int id, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(employee, id);
        return ResponseEntity.ok().body(updatedEmployee);
    }

    // DELETE /employees/{id}: deletes the employee and returns HTTP 204
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.noContent().build();
    }

}
