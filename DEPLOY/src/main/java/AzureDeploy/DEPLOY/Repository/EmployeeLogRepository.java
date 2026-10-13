package AzureDeploy.DEPLOY.Repository;

import AzureDeploy.DEPLOY.Model.Employee;
import AzureDeploy.DEPLOY.Model.EmployeeLog;
import com.azure.data.tables.TableClient;
import com.azure.data.tables.models.TableEntity;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public class EmployeeLogRepository {
    private final TableClient table;

    // The TableClient is configured and provided by AzureTableConfig.java in Config package
    public EmployeeLogRepository(TableClient table) {
        this.table = table;
    }

    // Converts an EmployeeLog into an Azure Table entity and inserts it
    public void save(EmployeeLog log) {
        Employee employee = log.getEmployee();

        // Group log entries by employee ID, each entry gets a unique row key
        String partitionKey = String.valueOf(employee.getId());
        String rowKey = UUID.randomUUID().toString();

        // Azure Table Storage stores each log entry as a set of named properties
        TableEntity entity = new TableEntity(partitionKey, rowKey)
                .addProperty("type", log.getType().name())
                .addProperty("employeeId", employee.getId())
                .addProperty("name", employee.getName())
                .addProperty("address", employee.getAddress())
                .addProperty("phoneNumber", employee.getPhoneNumber())
                .addProperty("email", employee.getEmail())
                .addProperty("department", employee.getDepartment())
                .addProperty("salary", employee.getSalary().toPlainString())
                .addProperty("admissionDate", employee.getAdmissionDate().toString());

        // Insert this log entry into the configured Azure Table
        table.createEntity(entity);
    }
}