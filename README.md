# online_bookstore

Spring Boot application simulating online bookstore.

I decided to do three separate controllers, Book, Customer and Order Controller. For logic to work it is needed to create at least one book using endpoint /api/books with valid request body and also create customer using endpoint /api/customer with valid request body for customer. After that you can use endpoint in Order controller to create orders. There is in place loyalty bonus logic for giving one book free(regular/old type) after buying 10 books of any type.

I used Spring Data JPA as it creates database itself. I was thinking about adding some initial data for books, but I covered some examples in tests.
There are some required validation and constraints on creating customer(it is impossible to have customer with same OIB).

Other things are quite common, I separated my logic to be handled in services and also have repositories for fetching and storage. There is a lot of room for improvement this application, such as creating REST documentation using Swagger or some other tool, improving exception handling and error messages. Also I could add entity User for login to aplication which could have roles, so I could forbid non wanted users to add/update books and add customers.
