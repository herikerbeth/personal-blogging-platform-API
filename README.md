# Personal Blogging Platform API 
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/herikerbeth/personal-blogging-platform-API/blob/edit-readme/LICENSE) 

This is a RESTful API for a personal blogging platform. It allows users to create, read, update, and delete blog posts. The API is built with Java and Spring Boot to create the server, Spring HATEOAS to write hypermedia-driven outputs, and uses PostgreSQL for data storage.

## API Documentation
For detailed API documentation, please visit the [Swagger UI](https://personal-blogging-platform-api.up.railway.app/swagger-ui/index.html).

# 💻 Technologies
## Backend
- Java
- Spring Boot
- Spring Data JPA
- Spring HATEOAS
- Maven
- Lombok
## Testing
- JUnit
- Mockito
## Production Deployment
- Backend: Railway
- Database: PostgreSQL
## Documentation
- Swagger UI

# 🚀 Getting started

What You Need:
- Java 17 or later
- PostgreSQL

## Installing the project

First you must clone the repository.
```bash
# clone repository
$ git clone https://github.com/herikerbeth/personal-blogging-platform-API.git

# enter the project folder
$ cd personal-blogging-platform-API
```
Now, inside IntelliJ, we will install the dependencies with Maven

<img width="300px" src="https://github.com/herikerbeth/assets/blob/main/install-dependencies.png?raw=true">

## Configuring Database Credentials
In the `/src/main/resources/` directory, open `application.properties` and replace the placeholders with your PostgreSQL database details (host, port, database name, user, and password):
```properties
${PGHOST}=your database host
${PGPORT}=your database port
${POSTGRES_DB}=your database name
${POSTGRES_USER}=your database user
${POSTGRES_PASSWORD}=your database password
```
## Starting
Finally, navigate to the Application class file to run the project.

<img width="300px" src="https://github.com/herikerbeth/assets/blob/main/run-application.png?raw=true">


## Test the Service
Now that the service is up, visit Swagger UI to explore the API documentation.
http://localhost:8080/swagger-ui/index.html

# Author

Herik Erbeth

https://www.linkedin.com/in/herik-erbeth

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request.

1. Fork the repository
2. Create a new branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a pull request

## License
This project is licensed under the MIT License - see the [LICENSE](https://github.com/herikerbeth/personal-blogging-platform-API/blob/edit-readme/LICENSE) file for details.
