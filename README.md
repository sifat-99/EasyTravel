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
    git clone https://github.com/your-username/easy-travel.git
    cd easy-travel
    ```

2. **Configure the database**:
    - Create a MySQL database and import the provided SQL schema.
    - Update the database connection details in `ConnectDB.java`.

3. **Install dependencies**:
    ```sh
    mvn clean install
    ```

4. **Run the application**:
    ```sh
    mvn javafx:run
    ```

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