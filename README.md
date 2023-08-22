# DBSchemaManager

Project was developed in 2020. 

This project consists of a system to fetch database metadata that includes:
- Tables
- Columns
- Views (including its creation script)
- Procedures (including its creation script)

To achieve this, I developed a microservice system with three services:
- Frontend
- Backend (Responsible for storing all the fetched data)
- Fetcher (Responsible for running jobs to fetch the data from the databases and compare it with the existing data in the Backend service)
