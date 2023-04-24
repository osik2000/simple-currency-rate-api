# Currency Rate App

## Description

This is backend application that provides three endpoints. It uses API from [NBP](http://api.nbp.pl/).

## Installation / Prepare to use

`way 1 - run it with Java`

1.  [Download release](https://github.com/osik2000/dynatrace.nbp.task/releases/download/v0.0.1-SNAPSHOT/dynatrace.nbp.task.backend-0.0.1-SNAPSHOT.jar)
2.  Run it with:
````
java -jar dynatrace.nbp.task.backend-0.0.1-SNAPSHOT.jar
````

`way 2 - run it with Docker`
1.  Pull my docker image:
````
docker pull osik2000/dynatrace.nbp.task.backend
````
2.  Run it with:
````
docker run -p 8080:8080 -d osik2000/dynatrace.nbp.task.backend
````



## Endpoints description (from task README)

1. Given a date (formatted YYYY-MM-DD) and a currency code (list: https://nbp.pl/en/statistic-and-financial-reporting/rates/table-a/), provide its average exchange rate.
2. Given a currency code and the number of last quotations N (N <= 255), provide the max and min average value (every day has a different average).
3. Given a currency code and the number of last quotations N (N <= 255), provide the major difference between the buy and ask rate (every day has different rates).

## Usage

run app with Java/Docker

### `java -jar build/libs/dynatrace.nbp.task.backend-0.0.1-SNAPSHOT.jar`
or
### `docker run -p 8080:8080 -d osik2000/dynatrace.nbp.task.backend`

`Endpoints can be tested with [Postman](https://www.postman.com/downloads/) app.`

### put 'http://localhost:8080/exchanges/{CURRENCY_CODE}?date=YYYY-MM-DD' to postman (method GET) to get average exchange rate for specific date and currency.

### put 'http://localhost:8080/exchanges/gbp?date=2012-01-02' to postman (method GET) to get average exchange rate for specific date and currency.

### put 'http://localhost:8080/exchanges/gbp?date=2012-01-02' to postman (method GET) to get average exchange rate for specific date and currency.


### `get_max_min_average(currency: str, n: int) -> dict`

Given a currency code and the number of last quotations, returns a dictionary with the max and min average exchange rates for that currency over the last n days.

### `get_major_difference(currency: str, n: int) -> float`

Given a currency code and the number of last quotations, returns the major difference between the buy and sell rates over the last n days.


Examples
--------

Here are some examples which presents uses of endpoints (you can test it with Postman).


# Get the average exchange rate for GBP on January 2, 2012

## http://localhost:8080/exchanges/gbp?date=2012-01-02



# Get the max and minexchange rates for GBP over the last 5 days

## http://localhost:8080/extremum/gbp?quotations=5



# Get the major difference between the buy and ask rate for GBP over the last 5 days

## http://localhost:8080/majordifference/GBP?quotations=5


License
-------

This is an internship task.