# Country and city list

## Task statement

Create an enterprise-grade "Country and city list" application (it will stay there for years, will be extended and
maintained) which allows the user to do the following:

* Browse through the paginated list of cities with the logos
* Display unique cities names
* Get all cities by country name
* Search by the city name
* Edit the city (both name and logos) - Editing the city should be only allowed for users with role EDITOR

Technical clarification

- Spring Boot
- Build system: Maven, Gradle any database

Expected outcome

- Git repository

Should you need any further information, please do not hesitate to contact the sender of this task.

## Application installation

1) Prepare empty PostgreSql DB
2) Prepare folder to storing logo image files
3) Set up system environments:
    * PORT - server port for app, default 8100
    * DB_URL - prepared PostgreSql DB url (like "jdbc:postgresql://localhost:5432/postgres")
    * DB_USERNAME - username for access to PostgreSql server
    * DB_PASSWORD - password for access to PostgreSql server
    * IMAGE_STORAGE_PATH - path to prepared on step 2 folder (for windows like "D:/image_storage")
4) Start CountrycityApplication

## Application using

Swagger UI is available by the link: http://localhost:8100/swagger-ui/index.html  
If you change port - change it in url also

Add "localhost" profile for enabling additional hibernate/sql logging

## Application development notes

For starting integration tests you should have Docker engine because of using TestContainer with PostgreSql
