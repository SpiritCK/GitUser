# GitUser

GitUser is a Java application that is used for searching GitHub user, based on either their username or email, and open their repositories.


------------------------------------------------------------------

## JSON

Java Script Object Notation (JSON) is a tet file syntax mainly used for storing and exchanging data between server and client or web application (exmple a web browser). A JSON text file stores data of an object in a human-readable nd easily understood way.

### Storing data

An object's data stored in JSON are enclosed in curly braces. Each set of braces indicates that it's an object. Inside the object, we can declare any number of properties using syntax
```
"name": "value"
```
and seperated by comma. For example :
```
{
    "name": "Kevin",
    "age": "20",
    "gender": "male"
}
```

### Storing data in array

An array of objects in JSON is enclosed by sqare brackets. It is used when we need to send an array of objects as data. For example :
```
[{
    "name": "Kevin",
    "age": "20",
    "gender": "male"
},
{
    "name": "Ivan",
    "age": "22",
    "gender": "male"
}]
```
we would have an array which consist of 2 objects, each has "name", "age", and "gender" properties.

### Nesting objects

Sometimes, an object has properties as another object. For example, a "sibling" object has properties "older" and "younger". In this case, we represent it in JSON as nested object like this example below.
```
{
    "older": {
        "name": "Kevin",
        "age": "20",
        "gender": "male"
    },
    "younger": {
        "name": "Ivan",
        "age": "22",
        "gender": "male"
    }
}
```


------------------------------------------------------------------

## REST

Representational State Transfer (REST) is a style of architecture that describe how networked resources are defined and addressed. REST can refer different networked resources uniquely using Universal Resource Identifier (URI) like URL in Web Service. Resources are accessed using REST in a simple interface over HTTP method. REST is a style of architecture, not an implementation specification. REST can support any type of media. The most frequently used media is JSON and XML.

RESTful Web Service is a Web Service that implements REST in data transfer. RESTful Web Service request resource by URI and get a response in specified format, such as JSON, HTML or XML. Request is made by using HTTP verbs/method, like GET, PUT, DELETE, POST and OPTIONS.


------------------------------------------------------------------

## GitHub API

Application Program Interface (API) is a set of protocols, routines, and tools for building software applications. An API specifies how software components should interact. An API is used when a programmer wants to use a service or subprogram that already exist without remaking it. For example, when we want to make a web application that uses Google Maps, we  don't need to recreate the code for Google Maps. Instead, we just need to use some method that are provided by Google Maps API.

The current version for GitHub API is v3 with specification :

### Schema
All API access is over HTTPS and accessed from the `https://api.github.com`. All data is sent and received as JSON. Blank fields are included as `null` instead of being omitted. All timestamps return in ISO 8601 format:
```
YYYY-MM-DDTHH:MM:SSZ
```

### Parameter
For GET requests, any parameters not specified as a segment in the path can be passed as an HTTP query string parameter:
```
curl -i "https://api.github.com/repos/vmg/redcarpet/issues?state=closed"
```
In this example, the 'vmg' and 'redcarpet' values are provided for the :owner and :repo parameters in the path while :state is passed in the query string.

For POST, PATCH, PUT, and DELETE requests, parameters not included in the URL should be encoded as JSON with a Content-Type of 'application/json':
```
curl -i -u username -d '{"scopes":["public_repo"]}' https://api.github.com/authorizations
```

### Root Endpoint
You can issue a GET request to the root endpoint to get all the endpoint categories that the API supports:
```
curl https://api.github.com
```

### Client Error
There are three possible types of client errors on API calls that receive request bodies:

- Sending invalid JSON will result in a 400 Bad Request response.
- Sending the wrong type of JSON values will result in a 400 Bad Request response.
- Sending invalid fields will result in a 422 Unprocessable Entity response.

All error objects have resource and field properties so that your client can tell what the problem is. There's also an error code to let you know what is wrong with the field.

### HTTP Redirect
API v3 uses HTTP redirection where appropriate. Clients should assume that any request may result in a redirection. Receiving an HTTP redirection is not an error and clients should follow that redirect. Redirect responses will have a `Location` header field which contains the URI of the resource to which the client should repeat the requests.

|Status Code | Description |
| ---------- | ----------- |
| 301 | Permanent redirection. The URI you used to make the request has been superseded by the one specified in the Location header field. This and all future requests to this resource should be directed to the new URI. |
| 302, 307 | Temporary redirection. The request should be repeated verbatim to the URI specified in the Location header field but clients should continue to use the original URI for future requests. |

### HTTP Verbs
Where possible, API v3 strives to use appropriate HTTP verbs for each action.

| Verb | Description |
| ---- | ----------- |
| HEAD | Can be issued against any resource to get just the HTTP header info. |
| GET | Used for retrieving resources. |
| POST | Used for creating resources. |
| PATCH | Used for updating resources with partial JSON data. For instance, an Issue resource has title and body attributes. A PATCH request may accept one or more of the attributes to update the resource. PATCH is a relatively new and uncommon HTTP verb, so resource endpoints also accept POST requests. |
| PUT | Used for replacing resources or collections. For PUT requests with no body attribute, be sure to set the Content-Length header to zero. |
| DELETE | Used for deleting resources. |
