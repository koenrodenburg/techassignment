# Tech Assignment

This application exposes two http endpoints that accept base64 encoded JSON, and a third that provides insight in the differences between the two.

The application is built using Java 14, Spring Boot, Spring Web, Spring Data JPA, Actuator, Lombok, H2 database, Maven, JUnit, Mockito, and JaCoCo.

## Usage

Running the application requires Java 14 and maven.

- In a command line window, navigate to the root folder of this project.
- Run `mvn clean install`
- Navigate to the generated subfolder `target`
- Run `java -jar techassignment-0.0.1-SNAPSHOT.jar`
- To see if the application started up successfully, visit `http://localhost:8080/actuator/health`. The expected output is: `{ "status": "UP" }` 

## Endpoints
The application has three endpoints:

#### [POST] /v1/diff/{id}/left
Accepts base64 encoded JSON for the left side of the diff. The diff is identified by {id}. Example request body:
```
eyAidGVzdCI6ICIxMjMiIH0=
```

#### [POST] /v1/diff/{id}/right
Accepts base64 encoded JSON for the right side of the diff. The diff is identified by {id}. Expample request body:
```
eyAidGV4dCI6ICIxNDQiIH0=
```

#### [GET] /v1/diff/{id}
Returns the diff comparison identified by {id}. There are six possible result types:
- `ID_UNKNOWN` - The ID does not exist in the database, meaning that neither a left side or a right side has been received
- `LEFT_SIDE_MISSING` - For this ID, only a right side has been received
- `RIGHT_SIDE_MISSING` - For this ID, only a left side has been received
- `SIZE_NOT_EQUAL` - The two sides have different sizes
- `EQUAL` - The two sides are exactly the same
- `DIFFERENT` - The two sides are of equal size, but there are differences, which are given in `differences`.

Example response bodies:
```
{
    "id": "2135",
    "resultType": "ID_UNKNOWN",
    "leftSide": null,
    "rightSide": null,
    "differences": []
}
```

```
{
    "id": "2135",
    "resultType": "DIFFERENT",
    "leftSide": "eyAidGVzdCI6ICIxMjMiIH0=",
    "rightSide": "eyAidGV4dCI6ICIxNDQiIH0=",
    "differences": [
        {
            "fromIndex": 7,
            "toIndex": 8
        },
        {
            "fromIndex": 16,
            "toIndex": 19
        }
    ]
}
```

## Considerations
#### Placement of business logic
The actual business logic (comparing the two base64 encoded JSON strings) is executed when a request for the diff result is received. This only makes sense as long as the diff result for the same ID is only requested once or a few times, and calculating of the diff does not take too long, as this is a synchronous process (i.e. the user is waiting). 

When there are many diff requests for the same ID, it makes more sense to calculate the diff once both 'left' and 'right' have been received, and store the result for immediate retrieval upon request. The same holds for the case where the diff calculation takes a long time; when it is executed on receiving both inputs (especially when made asynchronous) the user won't notice a delay.   

#### Database structure
I have chosen to store all information relating to an ID in a single record in the database. This adds an additional read operation to the processing of input, but it reduces the complexity of calculating the result. Depending on whether there is more load on the input or output side of the application, the decision could be made to split the information over multiple records (e.g. separate 'left' and 'right' records) to increase processing speed on the input side of the application.

#### Comments
I have been sparse with comments in the code, because I'm a firm believer that code should be as self-explanatory as possible, and that comments should only be used for extra clarification where necessary. When all methods and variables have clear names, comments generally add little value. However, they clutter the code, they will inevitably fall out of sync with the code,  and they teach developers to ignore comments alltogether (meaning they will also not read that one comment that actually *is* important).

The only exception to this should be public APIs or libraries, where outside developers will interact with the code without having access to the sources. In that case, JavaDoc documentation should be generated from comments in the code that explain the workings of all public methods and their signatures. 

## Automated tests
#### Unit tests
All classes and all their methods are under unit tests, using JUnit and Mockito. 

#### Integration tests
The functionality of the application is also integration tested, using JUnit and MockMvc. 

#### Code Coverage
JaCoCo is incluced in the Maven configuration to determine the coverage of the automated tests. Lombok is configured to add the `@Generated` annotation to its generated methods, such that these will not be considered by JaCoCo.

Current coverage is 97% of instructions. The missing three percent is located in the main method `TechAssignmentApplication.main(String[])`, and in two JaCoCo oversights in `DiffInputService.input(Side, String, String)`: the generated code from the `@NonNull` annotations, and an expected `default` case for the switch expression (which is unnecessary because the switch expression is exhaustive for the Enum).   

## Suggestions for improvement

#### Functionality
- The application currently compares the base64 encoded JSON strings with each other. It would be an interesting next step to compare the decoded JSON strings, or even the two JSON tree structures with each other. That way, the differences between the two inputs can be shown visually (i.e. in a web-based frontend) to the user.

#### CI/CD
- Running the automated tests and generating the coverage reports could be outsourced to Jenkins. A minimum requirement of code coverage could be added to the configuration such that builds fail if they do not meet this requirement. 

- To facilitate easier startup and deployment, the application could be containerized by adding a Dockerfile. Building and deploying the container can also be outsourced to Jenkins.

#### Tech stack
- The in-memory H2 database should be replaced by a database that persists data when the application is shut down.

- The endpoints of the application are currently unsecured. Spring Security could be added to authenticate users.







