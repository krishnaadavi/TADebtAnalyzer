javac -d target/classes -cp lib/javatuples-1.2.jar:lib/json-20210307.jar src/main/java/com/ta/**/*.java src/main/java/com/ta/TADebtAnalyzerApp.java
java  -Dfile.encoding=UTF-8 -classpath target/classes:lib/javatuples-1.2.jar:lib/json-20210307.jar com.ta.TADebtAnalyzerApp

