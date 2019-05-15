# otm-ui
Open Transportation Models - User Interface

## Requirements ##

+ Java 8
+ Apache Maven

## Installation ##

If you are interested in running OTM and not in development, you can download the jar file [here](https://mymavenrepo.com/repo/XtcMAROnIu3PyiMCmbdY/otm/otm-sim/1.0-SNAPSHOT/)

Otherwise:
1. move “settings.xml” to your .m2 folder
2. mvn install -DskipTests. Run the jar file (with dependencies) in the target folder. 
3. Import to IntelliJ: 
  + Import Project
  + Import from external model > Maven
  + “Next” through everything
