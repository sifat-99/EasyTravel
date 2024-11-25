# Easy Travel

Easy Travel is a Java-based application designed to help users explore and book travel destinations, including nearby restaurants and other attractions. The application features an admin dashboard for managing bookings and user data.

## Features

- **User Authentication**: Login and logout functionality.
- **Blog Posts**: Display detailed information about travel destinations.
- **Nearby Restaurants**: Show nearby restaurants for each travel destination.
- **Image Carousel**: Display images related to travel destinations.
- **Admin Dashboard**: Manage bookings and user data.

## Technologies Used

- **Java**: Core programming language.
- **Swing**: GUI framework for building the user interface.
- **MySQL**: Database for storing user and booking data.
- **Maven**: Build automation tool for managing dependencies.
- **JavaFX**: Used for some UI components.

## Prerequisites

- **Java 11 or later**: Ensure you have Java Development Kit (JDK) installed.
- **Maven**: Ensure Maven is installed and configured.
- **MySQL**: Ensure MySQL server is running and accessible.

## Setup

1. **Clone the repository**:
    ```sh
    git clone https://github.com/sifat-99/EasyTravel.git
    cd EasyTravel
    ```
   ```css
    Use IntelliJ IDEA to open the project.
   ```   

2. **Configure the database**:
    - Create a MySQL database and import the provided SQL schema.
    - Update the database connection details in `ConnectDB.java`.


# Database Schema Documentation

This document outlines the database schema used in the application. The schema consists of the following tables: `blog_posts`, `bookedUsersPayment`, `Nearby_Restaurants`, and `Users`.

## Tables

### 1. `blog_posts`

```sql
CREATE TABLE blog_posts (
    id INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    description LONGTEXT DEFAULT NULL,
    image1 LONGBLOB DEFAULT NULL,
    image2 LONGBLOB DEFAULT NULL,
    image3 LONGBLOB DEFAULT NULL
);
```

This table stores information about travel blog posts.

| Column Name  | Data Type       | Attributes                   | Description                             |
|--------------|-----------------|------------------------------|-----------------------------------------|
| `id`         | `INT(11)`       | `NOT NULL PRIMARY KEY AUTO_INCREMENT` | Unique identifier for each blog post.   |
| `title`      | `VARCHAR(255)`  | `NOT NULL`                  | Title of the blog post.                |
| `location`   | `VARCHAR(255)`  | `NOT NULL`                  | Location related to the blog post.     |
| `description`| `LONGTEXT`      | `DEFAULT NULL`              | Detailed description of the post.      |
| `image1`     | `LONGBLOB`      | `DEFAULT NULL`              | First image related to the blog post.  |
| `image2`     | `LONGBLOB`      | `DEFAULT NULL`              | Second image related to the blog post. |
| `image3`     | `LONGBLOB`      | `DEFAULT NULL`              | Third image related to the blog post.  |

---

### 2. `bookedUsersPayment`

```sql
CREATE TABLE bookedUsersPayment (
    id INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id INT(11) NOT NULL,
    restaurant_name VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    transaction_id VARCHAR(36) NOT NULL,
    booking_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(id)
);
```

This table stores payment information for users who book restaurants.

| Column Name      | Data Type       | Attributes                           | Description                              |
|------------------|-----------------|--------------------------------------|------------------------------------------|
| `id`             | `INT(11)`       | `NOT NULL PRIMARY KEY AUTO_INCREMENT` | Unique identifier for each payment record. |
| `user_id`        | `INT(11)`       | `NOT NULL`                          | References the `id` column in the `Users` table. |
| `restaurant_name`| `VARCHAR(255)`  | `NOT NULL`                          | Name of the booked restaurant.           |
| `amount`         | `DECIMAL(10, 2)`| `NOT NULL`                          | Amount paid for the booking.             |
| `transaction_id` | `VARCHAR(36)`   | `NOT NULL`                          | Unique identifier for the transaction.   |
| `booking_date`   | `TIMESTAMP`     | `NOT NULL DEFAULT CURRENT_TIMESTAMP` | Date and time of the booking.            |

---

### 3. `Nearby_Restaurants`

```sql
   CREATE TABLE Nearby_Restaurants (
    id INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    blog_post_id INT(11) DEFAULT NULL,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    rating DECIMAL(3, 2) DEFAULT NULL,
    price VARCHAR(50) DEFAULT NULL,
    FOREIGN KEY (blog_post_id) REFERENCES blog_posts(id)
   );
```

This table stores information about restaurants near blog post locations.

| Column Name  | Data Type       | Attributes                           | Description                              |
|--------------|-----------------|--------------------------------------|------------------------------------------|
| `id`         | `INT(11)`       | `NOT NULL PRIMARY KEY AUTO_INCREMENT` | Unique identifier for each restaurant.   |
| `blog_post_id`| `INT(11)`       | `DEFAULT NULL`                      | References the `id` column in the `blog_posts` table. |
| `name`       | `VARCHAR(255)`  | `NOT NULL`                          | Name of the restaurant.                  |
| `location`   | `VARCHAR(255)`  | `NOT NULL`                          | Location of the restaurant.              |
| `rating`     | `DECIMAL(3, 2)` | `DEFAULT NULL`                      | Rating of the restaurant (e.g., 4.5).    |
| `price`      | `VARCHAR(50)`   | `DEFAULT NULL`                      | Price range of the restaurant.           |

---

### 4. `Users`

```sql
CREATE TABLE Users (
    id INT(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);
```

This table stores information about registered users.

| Column Name  | Data Type       | Attributes                   | Description                             |
|--------------|-----------------|------------------------------|-----------------------------------------|
| `id`         | `INT(11)`       | `NOT NULL PRIMARY KEY AUTO_INCREMENT` | Unique identifier for each user.         |
| `name`       | `VARCHAR(255)`  | `NOT NULL`                  | Name of the user.                       |
| `username`   | `VARCHAR(255)`  | `NOT NULL UNIQUE`           | Unique username for login purposes.     |
| `password`   | `VARCHAR(255)`  | `NOT NULL`                  | Encrypted password for the user.        |

---

## Relationships

- The `bookedUsersPayment` table references the `Users` table via the `user_id` column.
- The `Nearby_Restaurants` table references the `blog_posts` table via the `blog_post_id` column.

## Notes

- The `password` column in the `Users` table should store passwords in an encrypted form for security.
- Consider optimizing image storage in the `blog_posts` table if the database grows significantly.
- Maintain referential integrity for all foreign key relationships to ensure data consistency.


## Project Structure

- `src/main/java/dream_team/easy_travel`: Main application code.
- `src/main/java/dream_team/easy_travel/AdminPanel`: Admin dashboard components.
- `src/main/java/dream_team/easy_travel/DatabaseConnection`: Database connection utilities.
- `src/main/resources`: Resource files such as images and fonts.

## Usage

- **User Interface**: The main interface allows users to browse travel destinations and view details.
- **Admin Dashboard**: Accessible to admin users for managing bookings and user data.

## Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Create a new Pull Request.

[//]: # (## License)

[//]: # ()
[//]: # (This project is licensed under the MIT License. See the `LICENSE` file for details.)


## Contact

For any inquiries or issues, please contact [mdabdurrahmansifat@gmail.com].