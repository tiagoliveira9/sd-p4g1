
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
     -Djava.security.policy=java.policy ClientSide.ThiefClient;"

sleep 0.8

gnome-terminal -x sh -c "cd engineHeist/clientSide/;
	java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/clientSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ClientSide.MasterThiefClient; "



























