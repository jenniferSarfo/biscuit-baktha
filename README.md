# Biscuit
Is a free command line interface tool for software project management. This project gives you the ability to follow Agaile approach. Current version (0.1.0) is Beta, and it has only the implementation for Scrum so far. Commands are designed to be intuitive and easy to remember. Your work will be saved as json files under your home directory (`$HOME$/biscuit/`).


# Features
* Autocompelete ability
* Coloured console output
* Dashboard view
* Adding, editing, and removing projects
* Adding and editing releases
* Adding and editing sprints
* Adding and editing user stories
* Adding and editing tasks
* Backlog view
* List releases, sprints, user stories, and tasks with filtering and sorting features
* Changing status of releases, sprints, user stories, and tasks (Status: created, open, planned, unplanned, in_progress, in_testing, done, overdue, removed)
* Planner view
* Print plan in short form as a tree, or in details in a table
* Moving user stories to sprints
* Moving sprints to releases
* Unplan sprints, user stories, or unplan all



# Requirements
Java 1.8


# Installation
## Linux
```
cd ./biscuit/binaries
chmod +x ./setup.sh
./setup.sh
```
Following files will be created
```
/usr/local/bin/biscuit.jar
/usr/local/bin/biscuit
```

## Windows
no installation file yet  
run manually:  
```
cd ./biscuit/binaries
java -jar biscuit.jar
```



# Documentation
* [Wiki](https://github.com/hamadmarri/Biscuit/wiki)
* [Usage Example](https://github.com/hamadmarri/Biscuit/wiki/Usage-Example)
* [Commands](https://github.com/hamadmarri/Biscuit/wiki/Commands)
* [Future work](https://github.com/hamadmarri/Biscuit/wiki/Future-work)


# Credits
The following libraries are used in this project  
* [jline 2.12](https://github.com/jline/jline2)
* [asciitable 0.2.5](https://github.com/vdmeer/asciitable)
* [gson 2.8.0](https://github.com/google/gson)
* [ColorCodes.java](https://gist.github.com/nathan-fiscaletti/9dc252d30b51df7d710a)


# Donate
[![paypal](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=RZC5CWSCTMB8E)

Testing commit on slack.
Testing commit for YT video.

**Instructions to run the project:**
1. Import the project.
2. Download the biscuit-baktha.jar which is the artifact of the project with it's dependencies.(In the Final Release version of the project
https://github.com/jenniferSarfo/biscuit-baktha/releases/tag/4.0)
3. Add the downloaded biscuit-baktha.jar as an external JAR file to the class path in java build path.
4. Export the project as a java Runnable JAR file.
5. In in the CLI navigate to folder (where the jar file is saved) and run the command "java -jar <<filename>>.jar"

