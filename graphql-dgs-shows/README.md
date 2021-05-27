## First steps
Run the project:
```
mvn spring-boot:run
```

Go to http://localhost:8080/graphiql

### Get all shows:
```
{
  shows {
    id
    title
    director
    releaseYear
    genre
    averageRating
    actors {
      name
    }
  }
}
```

### With filters (which can be mixed and matched)
```
{
  shows(titleFilter: "Ozark", showFilter: {director: "Bateman", showGenre: THRILLER}) {
    id
    title
    director
    releaseYear
    genre
    averageRating
    actors {
      name
    }
  }
}
```

### Perform mutation
```
mutation {
  addRating(rating: {showId: 3, stars: 3}) {
    id
    title
    averageRating
  }
}
```
