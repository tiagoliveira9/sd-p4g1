
# compile all 
javac -d engineHeist/serverSide -sourcepath src src/ServerSide/*.java 
javac -d engineHeist/registry -sourcepath src src/registry/*.java
javac -d engineHeist/clientSide -sourcepath src src/ClientSide/*.java 

#########################
# registry side

gnome-terminal -x sh -c "rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false 22407" 


gnome-terminal -x sh -c "cd engineHeist/registry/;
	java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/registry/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy registry.ServerRegisterRemoteObject"

sleep 0.5

#########################
# server side

# start grinformation
gnome-terminal -x sh -c "cd engineHeist/serverSide/;
	java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ServerSide.GRInformationServer"
	
sleep 0.5
	
# start museum
gnome-terminal -x sh -c "cd engineHeist/serverSide/;
	java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ServerSide.MuseumServer"
	
sleep 0.5
	
# start control
gnome-terminal -x sh -c "cd engineHeist/serverSide/;
	java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ServerSide.ControlCollectionServer"

sleep 0.5

# start conc
gnome-terminal -x sh -c "cd engineHeist/serverSide/;
	java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ServerSide.ConcentrationSiteServer"

sleep 0.5

# start assault party 1
gnome-terminal -x sh -c "cd engineHeist/serverSide/;
	java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ServerSide.AssaultPartyServer1"

sleep 0.5

# start assault party 2
gnome-terminal -x sh -c "cd engineHeist/serverSide/;
	java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ServerSide.AssaultPartyServer2"

sleep 0.5

#########################
# client side
gnome-terminal -x sh -c "cd engineHeist/clientSide/;
	java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/clientSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ClientSide.ThiefClient"

sleep 0.8

gnome-terminal -x sh -c "cd engineHeist/clientSide/;
	java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/clientSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ClientSide.MasterThiefClient"



























