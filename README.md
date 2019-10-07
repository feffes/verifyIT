# HowTo

1. Download an xls from the verification list on Fortnox
2. Compile with `mvn clean install`
3. Run `java -jar target/target/verifyIT-1.0-SNAPSHOT-jar-with-dependencies.jar [input] [output] [-A]` (input is a valid xls file, output is a directory, -A will merge the PDFs into one big file)
4. Now your output directory should be filled with verifications in pdf format!
