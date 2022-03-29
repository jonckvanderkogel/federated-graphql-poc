##GraphQL application using Spring boot and PostgresQL

- Remove any incompatible docker volume (if exists) using `docker volume rm motor-maintenance_database-data`
- Start postgres database using docker-compose `docker-compose -f docker-compose.yml up`
- Start application from your IDE or using `mvn spring-boot:run`
- Use a tool like GraphQL playground to query application on `http://localhost:8080/graphql`

- For more information on graphql-java see [documentation](https://www.graphql-java.com/documentation/) 

##Example queries

```
query {
    motorcycleById(id: 1) {
        brand,
        name,
        engineSize
    }
}
```

