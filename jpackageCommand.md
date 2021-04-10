
# Windows

jpackage --type app-image --temp "D:\all new\android\Eclipse\RosterGeneratorJava-master\temp" --name "Roster" --app-version 1.0.3 --icon "D:\all new\android\Eclipse\RosterGeneratorJava-master\icon.ico" --input "D:\all new\android\Eclipse\RosterGeneratorJava-master\jar" --main-jar RosterGenerator.jar --main-class com.cheng.rostergenerator.ui.Main


# MAC

jpackage --type app-image \

jpackage --type pkg \
 --temp ~/Downloads/temp \
 --name Roster \
 --mac-package-name "Roster @Chandler" \
 --app-version 1.0.3 \
 --input ~/Downloads/jar \
 --main-jar RosterGenerator.jar \
 --main-class com.cheng.rostergenerator.ui.Main \
 --icon "/Users/chandlercheng/Development/EclipseProjects/RosterGeneratorJava/icon.icns"


