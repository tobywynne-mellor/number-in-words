# Convert a number into words coding challenge

## Summary
This project is a coding challenge for the Emerging Technology Talent Graduate Scheme at Ninety One.

### Problem
Write a function that converts a given number into words. For example, given the number “1234” as input, return the
output “one thousand, two hundred and thirty-four”.

The input to your application will consist of lines of text containing random digits. You are expected to handle invalid
numbers appropriately.

### Installation
[Install Java 9](https://www.java.com/en/download/help/download_options.html)

[Install Maven](https://maven.apache.org/install.html)

### How to run
```bash
# Place input.txt in resources/fixtures

mvn clean install
mvn exec:java

# to run test cases
mvn test
```

### Approach
1. Use RegEx to find the number in the input line.
2. Split the number into groups of 3 digits
3. Turn each group into words using integer division and modulo 
4. Combine each group into one sentence

### Design Choices
#### Data type for the number
I initially used integers for the whole number but soon realised that numbers were reaching the limit of around two billion (max 2147483647). I switched to using long as it can hold numbers in the order of quintilian's (max 9223372036854775807). I think longs should be sufficient for this problem as numbers larger than the limit are rarely represented in words.

#### Iteration vs Recursion
I chose an iterative approach rather than recursion for a few reasons. I think the iterative approach is more readable and easier to grasp especially when debugging. Secondly, recursion is generally slower due to the overhead maintaining the stack and uses more memory. 

#### Maven
I set up a java maven project so that dependencies can be managed if this code were to be extended.
Maven conveniently runs the tests during the install step so works well in a CI/CD environment such as Jenkins.

#### File Input
I chose to use an input stream instead of reading the whole file at once to reduce memory usage

#### Maintenance & Evolution
I implemented a test class that tests my convert function with several assertions. This enables me or future developers to quickly and conveniently test to make sure any code change does not affect the desired functionality.
The NumberToWordsConverter class can be easily imported into any java project that needs it.

### Assumptions
- input is provided as input.txt in ```main/resources/fixtures```
- each line can have one number otherwise it will be considered invalid
- number cannot be prefixed or suffixed with '#' for example otherwise it is considered invalid
- each line of input will have one line of output
- numbers do not contain decimal places
- numbers cannot be negative
- the largest values are in trillions
- words should be in american english as per the test cases 
