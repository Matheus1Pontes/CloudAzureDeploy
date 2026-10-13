# Cloud Azure Deploy

A Spring Boot REST API for managing employee records. Employee data is stored in Azure SQL Database, and create, update, and delete actions are logged in Azure Table Storage.

## Features

- CREATE an employee with `POST /employees`.
- FIND an employee with `GET /employees/{id}`.
- UPDATE an employee with `PUT /employees/{id}`.
- DELETE an employee with `DELETE /employees/{id}`.
- Record `INSERT`, `UPDATE`, and `DELETE` audit entries in the `EmployeeLogs` Azure Table.
- Create the Azure Table when the application starts if it does not already exist.

## Project structure

```text
src/main/java/AzureDeploy/DEPLOY/
├── Config/AzureTableConfig.java
├── Controller/EmployeeController.java
├── Model/Employee.java
├── Model/EmployeeLog.java
├── Model/enums/ActionType.java
├── Repository/EmployeeLogRepository.java
├── Repository/EmployeeRepository.java
└── Service/EmployeeService.java
```

`EmployeeController` handles HTTP requests. `EmployeeService` coordinates the operations. `EmployeeRepository` uses Spring Data JPA to access Azure SQL, and `EmployeeLogRepository` writes audit records to Azure Table Storage.

## API examples

### Create an employee

```http
POST /employees
```

```json
{
  "name": "Alex Morgan",
  "address": "123 Main Street",
  "phoneNumber": "123-456-0000",
  "email": "alex@example.com",
  "department": "Engineering",
  "salary": 75000.00
}
```

The API returns `201 Created`, the employee in the response body, and a `Location` header with the new employee’s URL. `admissionDate` is assigned automatically when the employee is first saved.


### Get an employee

```http
GET /employees/1
```

Returns `200 OK` and the employee if the ID exists.


### Update an employee

```http
PUT /employees/1
```

Send the employee fields as JSON. The service updates the existing employee and returns `200 OK`.


### Delete an employee

```http
DELETE /employees/1
```

Returns `204 No Content` when the employee is deleted.

## Requirements

- Maven, or the Maven Wrapper included with the project (`mvnw` / `mvnw.cmd`).
- An Azure SQL Database with a `tab_employee` table that matches the `Employee` entity.
- An Azure Storage account with Table service access.
- An Azure App Service web app to host the Spring Boot application.

Check the Java version in `pom.xml` before creating the App Service. The App Service runtime must support the version used to compile the project.

## Configuration

Keep credentials out of source code and Git. 
Use placeholders like these in `src/main/resources/application.yml`. 
Keep your own SQL server and database names in the JDBC URL.


## Set up Microsoft Azure


### 1. Prepare Azure SQL Database

1. In the Azure portal, create or select a SQL logical server and SQL database.
2. Create the `tab_employee` table with columns compatible with the `Employee` entity. The primary key should be an identity column named `employee_id` if you use `GenerationType.IDENTITY`.
3. Note the SQL server hostname, database name, and SQL login. Put the hostname and database name in the JDBC URL. Add the login and password to the App Service settings.
4. Configure the SQL firewall so the App Service can reach the database. Allow only the needed client or App Service outbound IP addresses.


### 2. Prepare Azure Table Storage

1. Create or select a Storage account.
2. Copy its connection string from the account’s access keys page.
3. Add the connection string to App Service as `AZURE_STORAGE_CONNECTION_STRING`. 
4. The application uses the `EmployeeLogs` table and calls `createTableIfNotExists`, so it can create the table when it starts.


### 3. Create the App Service

1. Create an **App Service** web app using the Java SE stack for an executable Spring Boot JAR.
2. Choose a Java runtime compatible with the project’s configured Java version. Select an available region and pricing plan.
3. In the web app’s **Settings → Environment variables → App settings**, add:

   |                Name               |              Value             |
   | --------------------------------- | ------------------------------ |
   | `AZURE_SQL_USERNAME`              | Your SQL login                 |
   | `AZURE_SQL_PASSWORD`              | Your SQL password              |
   | `AZURE_STORAGE_CONNECTION_STRING` | Your Storage connection string |

4. Select **Apply** to save the settings.


### 4. Build and deploy from VS Code

1. Open the Spring Boot project folder in VS Code.
2. Build the JAR from the project root.

   ```powershell
   .\mvnw.cmd clean package -DskipTests
   ```

3. Install and sign in to the Azure App Service extension in VSCode if needed.
4. In VS Code’s Azure view, expand your subscription and **App Service**. Right-click your web app and choose **Deploy to Web App**.
5. Select the executable JAR in the `target/` folder if prompted, then confirm deployment.
6. When deployment completes, open the web app URL. Check the App Service **Log stream** if the app does not start.
7. Test the API using Swagger, if included, Postman.

## Verify the API

To create an employee, send the JSON from the API examples to:

```text
https://<app-name>.azurewebsites.net/employees
```

After a create, update, or delete request, check the `EmployeeLogs` table in the Storage account to confirm that an audit entry was written.
