# Dotmatrix font generator
LCD matrix display font generator and parser. Suitable for PCD8544 driver. Probably can fit other display types also.

Main features:
- generates empty matrix for creating fonts from 5x8 to 16x16 pixel size
- can draw font, provided as hex string from 5x8 to 16x16 pixel size by using "Draw" button

# Dependencies
- Java 13
- OpenJFX

# Running 
There are several ways to run the application. If you have JDK installed you can use maven wrapper to compile and run application.
```shell script
./mvnw javafx:run
```

Another way is to download binary package and run shell script `run.sh` or `run.bat` from the command line. 
# Example sreenshots
![](https://github.com/vadimkim/dotmatrix/raw/master/images/single_segment.png "single segment")
![](https://github.com/vadimkim/dotmatrix/raw/master/images/multi_segment.png "multi segment")
