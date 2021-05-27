## First steps
Run the project:
```
mvn spring-boot:run
```

Go to http://localhost:8081/graphiql

This service adds reviews to a Show. As you can see in the schema it defines a "stub" of Show where it only has the
id field of Show and it adds reviews to it. 

### N+1 problem
Very important to take into consideration is the N+1 problem. There are two showcases to demonstrate how this works.
The first one is how this service adds reviews to a Show. The RequestLoggingFilterConfig logs all the incoming requests
and as you can see from there, when you do a request as follows:
```
{
  shows{
    id
    title
    averageRating
    reviews {
      id
      description
    }
  }
}
```
You see the Reviews service gets just one call, with all the show id's in the request:
```
{"query":"query($representations:[_Any!]!){_entities(representations:$representations)
    {...on Show{reviews{id description}}}}","variables":{"representations":[
            {"__typename":"Show","id":"1"},
            {"__typename":"Show","id":"2"},
            {"__typename":"Show","id":"3"},
            {"__typename":"Show","id":"4"},
            {"__typename":"Show","id":"5"}
        ]
    }}]
```

The DGS framework then takes care of calling the @DgsEntityFetcher(name = "Show") for every show. So even though now
the service only receives one call, you have to make sure you don't run into the N+1 problem further on in the chain.

To resolve that you use the DataLoader pattern as follows:
```
@DgsData(parentType = "Show", field = "reviews")
public CompletableFuture<List<Review>> reviews(DgsDataFetchingEnvironment dfe) {
    DataLoader<Long, List<Review>> dataLoader = dfe.getDataLoader(ReviewsDataLoader.class);
    Show show = dfe.getSource();
    return dataLoader.load(show.getId());
}
```

A more simple example is the Foo/Bar/Beer example. Here the Shows service exposes a foo query which serves Foo objects.
Foo has a Bar field, which in turn has a Beer field. The Shows service knows there is a Bar object but it doesn't know
much about it. The Bar object itself is "hydrated" by the Reviews service. What the Shows service must do is create 
"stub" Bar objects which then get hydrated by the Reviews service. Again you can see the Reviews service receives one
call from the Apollo gateway with all the Bar objects for which it wants to know which Beer is served there. The
Reviews service then uses a DataLoader to fetch the hydrated Bar objects in batch to prevent propagating the N+1 problem
down the chain.