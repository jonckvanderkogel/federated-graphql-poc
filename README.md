# Quickstart
First build the "graphql-utils" library as both the graphql-dgs-* projects use it.
So from the root of the project:
```
cd graphql-utils
mvn clean install
```

Start the two graphql-dgs-* projects (from the root of the project):
```
mvn spring-boot:run
```

Start the Apollo Gateway (from the root of the project):
```
node index.js
```

Note that below is described how to run the project if you are running behind the corporate proxy.

Navigate to the [GraphQL Playground](http://localhost:4000). From there you can do your queries, view the docs, schema,
query planner, tracing, etc...

To see a federated query in action (shows are coming from 1 service, reviews from another):
```
{
  shows (titleFilter: "t") {
    id
    title
    averageRating
    actors {
      name
    }
    reviews {
      description
    }
  }
}
```

Or the Foo/Bar/Beer example, where the Shows service exposes the foo query and stub Bar objects, the Bar objects
are hydrated from the Reviews service:
```
{
  foo {
    id
    bar {
      id
      beer
    }
  }
}
```

## Apollo Studio
[Here](https://www.apollographql.com/docs/federation/managed-federation/setup/) is the reference documentation for
setting up managed federation in Apollo Studio.

In every terminal where you run one of the projects you first need to set the following environment variable to
integrate with Apollo Studio:
```
export APOLLO_KEY="the_graph_key_available_in_apollo_studio"
export APOLLO_GRAPH_ID="the_graph_id"
export APOLLO_GRAPH_VARIANT="current"
```

Now you need to register every one of your implementing services with Apollo Studio. You use the Rover CLI for this.
(hint: npm install)

```
rover subgraph publish $graph_id@$graph_variant \
    --name shows \
    --routing-url http://localhost:8080/graphql/
    --schema src/main/resources/schema/schema.graphqls
    --convert
```
```
rover subgraph publish $graph_id@$graph_variant \
    --name shows \
    --routing-url http://localhost:8081/graphql
    --schema src/main/resources/schema/schema.graphqls
```


## Tracing
We can see full traces of all requests in Apollo Studio. Apollo Server sends the traces by default. For the Spring Boot
apps to send their traces they need to import this dependency:
```
<dependency>
    <groupId>com.apollographql.federation</groupId>
    <artifactId>federation-graphql-java-support</artifactId>
    <version>0.6.4</version>
</dependency>
```

And then expose an instance of this class as a bean:
```
com.apollographql.federation.graphqljava.tracing.FederatedTracingInstrumentation
```