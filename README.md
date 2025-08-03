# Internship-Assignment
**Running Elasticsearch 🐳** <br/>
This project uses Docker to run a local, single-node Elasticsearch cluster for development.

**Prerequisites** <br/>
You must have Docker and Docker Compose installed on your machine.
The docker-compose.yml file required to run the container is included in the root of this project.

**How to Start the Service** <br/>
Open a terminal and navigate to the root directory of this project (where the docker-compose.yml file is located). <br/>
Run the following command to start the Elasticsearch container in the background: <br/>
docker-compose up -d <br/>
To Verify It's Running wait a few moments for the container to initialize.<br/>
To confirm that Elasticsearch is running correctly, execute this command in postman : http://localhost:9200<br/>
You should receive a JSON response with details about the cluster, similar to this:

<img width="1920" height="1080" alt="Screenshot (7)" src="https://github.com/user-attachments/assets/f673c45d-2d3e-451b-b36f-6651a2b44255" />

**Application Configuration ⚙️** <br/>
The application is configured with defaults that work out-of-the-box with the provided Docker setup.

**All configuration properties are located in the following file:**
src/main/resources/application.properties

<img width="1920" height="1080" alt="Screenshot (8)" src="https://github.com/user-attachments/assets/4e683007-d9c0-4491-b826-fb8896f7639f" />

**Data Ingestion & Verification 💾** <br/>

**Triggering Ingestion** <br/>
The sample data, located at src/main/resources/sample-courses.json, is automatically ingested into Elasticsearch. This process is handled by the DataInitializer component and runs only the first time the Spring Boot application starts with an empty Elasticsearch instance.<br/>

If the courses index already exists, the data ingestion is skipped to prevent creating duplicate documents on subsequent application restarts.

**Verifying Ingestion** <br/>
You can verify that the data has been successfully loaded by querying Elasticsearch in PostMan.

**1. Check the Document Count**<br/>
To get a quick count of all documents in the courses index, run the following command:
we wil run the command : localhost:9200/courses/_count

<img width="1920" height="1080" alt="Screenshot (9)" src="https://github.com/user-attachments/assets/97930075-6018-423f-926e-57f5858a27a0" />

We will run the command localhost:9200/courses/_search?pretty to view 10 course documents from the index and confirm that the total hits is 50.

<img width="1920" height="1080" alt="Screenshot (10)" src="https://github.com/user-attachments/assets/316a9922-0133-461e-97c1-9a996628ab79" />

**API Usage Examples (Postman) 🧪**<br/>

Basic Setup:
Open Postman and create a new request.
Set the request type to GET.
Set the URL to: http://localhost:8080/api/search
Use the "Params" tab (located below the URL bar) to add the keys and values for each example.

Example 1: Full-Text Search for "Java"
Action: In the "Params" tab, add one row:
Key: q
Value: Java
Expected Behavior: The response will include courses with "Java" in their title or description, such as "Advanced Java Concepts"

<img width="1920" height="1080" alt="Screenshot (84)" src="https://github.com/user-attachments/assets/a43f557c-11ac-4475-8b47-8ec6e06ff774" />

Example 2 : Age Range Filters
This example finds courses suitable for students between the ages of 10 and 14.
Parameters: minAge=10, maxAge=14

<img width="1920" height="1080" alt="Screenshot (86)" src="https://github.com/user-attachments/assets/84387056-c4ed-4220-aaf1-ee8ddc4694f1" />

**Example 3 : Exact Filters**
Here are example URLs for the exact filters.

**Filter by Category**
This example finds all courses in the Technology category.
Parameter: category=Technology

<img width="1920" height="1080" alt="Screenshot (87)" src="https://github.com/user-attachments/assets/0f935ee3-e4c3-4e72-b794-6b68c4e55bf4" />

**Filter by Type**
This example finds all courses that are a CLUB.
Parameter: type=CLUB

<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/f352a6c4-8f5d-45a5-89b0-d3546bfd2c52" />

**Example 4 :Date Filter**

This example finds all courses that start on or after September 20, 2025.
Note : Here the default sorting also implemented. 
Parameter: startDate=2025-09-20

<img width="1920" height="1080" alt="Screenshot (89)" src="https://github.com/user-attachments/assets/367ce61e-0d65-475c-a96a-b5217b466b61" />

**Example 5 :Sorting**
**Sort by Price (Ascending) 📈**
To sort by price from low to high, add the sort=priceAsc parameter.

<img width="1920" height="1080" alt="Screenshot (90)" src="https://github.com/user-attachments/assets/3a733642-a337-403a-8929-5312763ec50a" />

** Sort by Price (Descending) 📉**
To sort by price from high to low, add the sort=priceDesc parameter.

<img width="1920" height="1080" alt="Screenshot (91)" src="https://github.com/user-attachments/assets/2dfc806c-4d9c-4966-86e2-3b62acbad0e2" />

**Example 5 :Pagination**
Getting the First Page
This test shows how to get the first page of results.
Expected Behavior: The response will contain the first 5 courses from the full result set.

<img width="1920" height="1080" alt="Screenshot (92)" src="https://github.com/user-attachments/assets/0b1b41c7-b54e-42ff-9f32-14202f369f80" />












