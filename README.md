# HowTo

1. Download an xls from the verification list ~~and convert it to xlsx. (Would appreciate if someone would look at why it can't load the xls as it is)~~
   ~~\* For example, you can upload it to Google Drive and download as xlsx.~~
2. Compile with `mvn clean install`
3. Run `java -jar target/target/verifyIT-1.0-SNAPSHOT-jar-with-dependencies.jar [input] [output]` (input is a valid ~~xlsx~~ xls file, output is a directory)
4. Now your output directory should be filled with verifications in pdf format!
