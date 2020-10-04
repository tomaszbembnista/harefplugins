### Thermostat
Basing on provided current temperature and configured plan, provides delta between current and required temperature.

#### Input
```javascript
{
    "temperature" : "number" 
}
```

#### Output
```javascript
{
    "delta" : "number"
}
```

#### Configuration
Allows to define desired temperature for day of week and hours.
```javascript
{
    "defaultTemp" : "number";
    "days" : [
        {
            "name" : "string" //enum values [MO, TU, WE, TH, FR, SA, SU]
            "defaultTemp" : "number"
        }
    ] 
}
```