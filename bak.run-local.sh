


# compile all 
javac -d engineHeist/serverSide -sourcepath src src/ServerSide/*.java 
javac -d engineHeist/registry -sourcepath src src/registry/*.java
javac -d engineHeist/clientSide -sourcepath src src/ClientSide/*.java 

#########################
# registry side

rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false

java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/registry/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy registry.ServerRegisterRemoteObject

#########################
# server side

# start grinformation
java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ServerSide.GRInformationServer
	
	
# start museum
java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ServerSide.MuseumServer
	
# start control
java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ServerSide.ControlCollectionServer

# start conc
java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ServerSide.ConcentrationSiteServer

# start assault party 1
java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ServerSide.AssaultPartyServer1


# start assault party 2
java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/serverSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ServerSide.AssaultPartyServer2



#########################
# client side
java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/clientSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ClientSide.ThiefClient


java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/clientSide/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy ClientSide.MasterThiefClient


























