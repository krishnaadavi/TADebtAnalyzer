# TADebtAnalyzer
TrueAccord Debt Analyzer (Implementation of TA take home assignment)

Analyzes debt, payment plan and payment info retrieved from web services and outputs the debt summaries.

# Specification: https://gist.github.com/jeffling/2dd661ff8398726883cff09839dc316c

# Objectives 
- Compute and display the debt summaries analyzing the payments, payment plans and debt data provided by the services.
- Implementation should address all the scenarios laid out in the spec.
- Design should be modular and extendable for enhancements.
- Test coverage for all the scenarios in the spec. 

# Design
Followed object-oriented design approach by dividing the app into following model, services and business logic components.  

Components:

services - Backend services are implemented using an interface and 3 methods for each of the services. This can be extended further when implementing backend. For now, they call the HTTP service endpoints provided.

model - Data objects Payment, PaymentPlan, Debt and the info object DebtPaymentInfo make up the schema. We can add more objects to this as we extend the application.

business logic - Logic implemented per specification is in TADebtAnalyzer class.

tests - Unit tests implemented provide the test coverage for all the cases.

#Key aspects of the application
- TADebtAnalyzerApp java class is the entry point
- TAService abstracts the backend service functionality
- data and info objects are in model package
- TADebtAnalyzerTest has all the unit tests providing coverage for core functionality 

#Assumptions
- Data from HTTP services is valid and is always available
- Data schema from HTTP services is assumed to be in correct format

#Enhancements (if there is more time..)
- Configure the environment specific properties (service urls etc)
- Dependency injections for TAService etc.
- build server backend services layer, fetch data from other TA or 3rd party services
- Instead of stdout, output can be written to db or a file for further analytics.

#Instructions to compile and run 
Terminal: 
- run.sh //compiles and runs TADebtAnalyzerApp 
- please ensure java is installed

IDE: 
- You may use any of the IDEs (I used IntelliJ)
- pom.xml is included in the project
- Open the project in IDE and run TADebtAnalyzerApp class
- run TADebtAnalyzerTest for unit tests 



