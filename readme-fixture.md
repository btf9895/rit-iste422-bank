## Bank Testing

#### CSV Tests

From the project's root directory:

Windows: ```gradle runCheckingFixture --args="-f src/test/resources/CheckingAccountTest.csv``` or ```gradle runCheckingFixture --args="-f src/test/resources/SavingsAccountTest.csv```

Mac/Linux: ```./gradle runCheckingFixture --args="-f src/test/resources/CheckingAccountTest.csv``` or ```./gradle runCheckingFixture --args="-f src/test/resources/SavingsAccountTest.csv```

---

#### Fixture Classes

From the project's root directory:

Windows: ```gradle runCheckingFixture``` or ```gradle runSavingsFixture```

Mac/Linux: ```./gradle runCheckingFixture ``` or ```./gradle runSavingsFixture```

###### Arguments

Arguments are specified in the format ```--args="arg0 arg1"```, and the fixture classes are run one of two ways:

- To run tests through the command line arguments, use ```-t``` in place of ```arg0```, and a single-quote-delimited test string in place of ```arg1```. For example: ```--args="-t '10, 20|20, , 40|10, 0'"```
- To run tests through a CSV file, use ```-f``` in place of ```arg0```, and the path to a CSV file containing test scenario strings in place of ```arg1```. For example: ```--args="-f path/to/TestFile.csv"```

---

#### Modified Files

I made a few changes to the existing files for this assignment. I did add an extra feature that is able to be tested to ```CheckingAccountTestFixture``` to fulfill the third requirement. I noticed ```CheckingAccount``` has data on minimum balance, so I specified a sixth column for the checking account tests, minimum balance. Then, I added an additional statement asserting that the ending balance is greater than or equal to the minimum balance.

- I cleaned up the main method of the ```CheckingAccountTestFixture``` class to run the correct set of tests. I left the existing code there, and structured it so that if the user does not specify arguments, the program will use the hardcoded data in its place.
- I added many more tests to ```CheckingAccountTest.csv``` to comprehensively test the features of the class, as the existing tests were barebones and did not account for all scenarios.
- I added was a decimal formatting line to ```SavingsAccount```, since there were some weird rounding errors causing tests to fail. For example, think 200.0 being compared to 200.0000000000005. Those represent the same value to humans, but technically they are different so the compiler flags it as a failed assertion.
- I wrote a very short class called InterestCalculator to help me gather the expected ending balance for all of my tests. I noticed I was doing a lot of repeated calculations, so I wrote a short program that will do it for me. I placed this java file and its respective class file in the src/util/ directory.

---

#### Blockers

I have identified a few features that the existing fixture classes are unable to account for. Scenarios comprising these features include:

1. Tracking check numbers
   * Tracking check numbers is a feature that should be tested, as keeping track of written checks is essential for balancing checkbooks and ensuring correct account balances.
   * A unit test for this could look something like: Open account, write 5 checks, assert that the next check available is number 6.
2. Applying fees for dropping below minimum balance
   * The minimum balance feature and below minimum fee feature are not currently being tested, this is important because overdrafting is something that happens regularly in banking and this application must be able to handle these events in the proper way.
   * A unit test for this would be in the general format of: Open account, make withdrawals to dip account balance below the minimum, assert that the below minimum fee has been applied to the account balance.
3. Continually compounding interest or interest accrued on a non-monthly basis
   * Interest is calculated in a multitude of ways, different types of bank accounts can offer different rates. Most banks apply interest to accounts every month, but the way the amount of interest can differ between banks. For example, some banks may calculate interest quarterly, and pay that out split evenly between three months. Or, they may calculate it monthly (as this program does), adding an increasing amount of interest every month.
   * The program is currently unable to handle different methods of interest calculation. More development would need to be done to allow for this to happen, although it would not be difficult.
   * In ```SavingsAccount```, the ```monthEnd()``` method has a hardcoded instruction to divide the interest rate by 12 and multiply the balance by that to get interest. However, a better implementation would be to accept an argument representing the number of times interest is compounded per year, and use that value instead of 12. For continually compounding interest, use -1 and check for that value at the beginning of the method. Oftentimes the value passed in will still be 12, but this allows for other interest compounding periods to be calculated as well.

---

###### Extra Credit

Should be covered by the arguments section above! Use ```-t``` to run a scenario string rather than a file, and use ```-f``` to specify where ```CheckingAccountTestFixture``` and ```SavingsAccountTestFixture``` should retrieve test scenarios.
