# E-Commerce Shop API

This project provides a backend API for managing products and shopping carts. 

### Technologies Used:

    Spring Boot
    Spring Data JPA
    H2 Database
    Gradle
### Features:
  
*Product Management*

    List all products
        Endpoint: GET /products
        Retrieves a list of all products in the system.

    Get a product
        Endpoint: GET /products/{id}
        Retrieves details of a single product by its ID.

    Create a new product
        Endpoint: POST /products
        Creates a new product in the system.

    Delete a product
        Endpoint: DELETE /products/{id}
        Deletes an existing product from the system.

*Cart Management*

    Create a cart
        Endpoint: POST /carts
        Creates an empty shopping cart.

    List all carts
        Endpoint: GET /carts
        Retrieves a list of all shopping carts.

    Modify a cart
        Endpoint: PUT /carts/{id}
        Adds or removes products from a shopping cart.

    Checkout a cart
        Endpoint: POST /carts/{id}/checkout
        Finalizes the shopping cart and displays the total cost.

### Notes:
- The application uses an in-memory H2 database, so data resets on restart.
