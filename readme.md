# Opening Hours

This project is using **Gradle** to build and run the service.

### Build and run

* Build the project: `./gradlew build`
* Run the project: `./gradlew run`

### Summary

Web server is configured to run on port `8080` and has one route which consumes working hours in defined `JSON` format.
Data is then processed and displayed on a simple `HTML` page.

### API Description

* **Method:** `POST`
* **Path:** `/opening_hours`

###JSON Format

The existing format is a bit overcomplicated.

The format below is simpler, smaller and easier to parse. 
```[
   	{
   		"day": "monday",
   		"opens_at": 36000,
   		"closes_at": 75600,
   	},
   	{
   		"day": "tuesday",
   		"opens_at": 36000,
   		"closes_at": 75600,
   	},
   	{
   		"day": "wednasday",
   		"opens_at": 36000,
   		"closes_at": 75600,
   	},
   	{
   		"day": "thursday",
   		"opens_at": 36000,
   		"closes_at": 75600,
   	},
   	{
   		"day": "friday",
   		"opens_at": 36000,
   		"closes_at": 75600,
   	},
   	{
   		"day": "saturday",
   		"opens_at": 36000,
   		"closes_at": 75600,
   	},
   	{
   		"day": "sunday",
   		"opens_at": 36000,
   		"closes_at": 75600,
   	},
   ]
```
   
For multiple opening and closing hours in one day it can be modified to the following:

```
{
    "day": "monday",
    "opening_hours": [
        {
            "opens_at": 36000,
            "closes_at": 64800
        },
        {
            "opens_at": 75600,
            "closes_at": 86399
        }  
    ]
    ...
}
```