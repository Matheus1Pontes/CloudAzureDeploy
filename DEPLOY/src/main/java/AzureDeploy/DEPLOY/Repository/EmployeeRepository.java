package AzureDeploy.DEPLOY.Repository;

import AzureDeploy.DEPLOY.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/* JpaRepository provides common database operations for Employee records
   Employee is the entity type, and Integer is the type of its ID.*/
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
  
}
