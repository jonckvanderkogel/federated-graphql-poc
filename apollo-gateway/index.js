const {ApolloServer, gql} = require('apollo-server');
const {ApolloGateway} = require('@apollo/gateway');

const gateway = new ApolloGateway();


const server = new ApolloServer({
    gateway,
    subscriptions: false,
    tracing: false,
    introspection: true,
    playground: true,
    cors: {
        origin: '*',
    }
});
server.listen();
