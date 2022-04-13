# Welcome to Wood Industry Ecommerce 👋
[![Documentation](https://img.shields.io/badge/documentation-yes-brightgreen.svg)](https://github.com/matravolgyiadrian/wood-industry-ecommerce/blob/master/documentation/Software_Requirement_Specification.md)

> Web application to meet a wood industry owner's need to sell products online efficient. The application stores products that can be purchased through it; the user can send email to the admin regarding custom products; the admin can create promotions that offers percentage off based coupons through this platform.

### 🏠 [Homepage](https://github.com/matravolgyiadrian/wood-industry-ecommerce)

### ✨ [Demo](https://matravolgyi-adrian-thesis.herokuapp.com/)

## Prerequisites
* [Cloudinary](https://cloudinary.com/) account
* Email account to send emails from
* PostgreSQL database
* [Heroku](https://www.heroku.com/home) account (if you deploy it through Heroku)

## Usage
### 1. Run locally

**With Heroku**

1. Get Heroku CLI [Here](https://devcenter.heroku.com/articles/heroku-cli)
1. Change the corresponding environment variables in the "`src/resources/application-prod.properties`" file OR change the variable in the "`src/resources/application.properties`" file to 'dev' from 'prod'
1. In the terminal run: 
    ```bash 
    $ mvn clean install
    $ heroku local web
    ```
1. The web application will be available at [here](http://localhost:3000/)

**Without Heroku**

1. Change the corresponding environment variables in the "`src/resources/application-prod.properties`" file OR change the variable in the "`src/resources/application.properties`" file to 'dev' from 'prod'
1. In the terminal run:
    ```bash
    $ mvn clean install
    $ mvn spring-boot:run
    ```

The admin account credentials are: 

    username: admin
    password: admin 
## Run tests

```sh
$ mvn test
```
