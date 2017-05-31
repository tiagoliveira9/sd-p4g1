
# compile all 
javac -d engineHeist/serverSide -sourcepath src src/ServerSide/*.java 
javac -d engineHeist/registry -sourcepath src src/registry/*.java
javac -d engineHeist/clientSide -sourcepath src src/ClientSide/*.java 

#########################
# registry side

gnome-terminal -x sh -c " echo RMI Registry Started... ; rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false 22407; bash" 


gnome-terminal -x sh -c "cd engineHeist/registry/;
	java -Djava.rmi.server.codebase="file:///home/endla/Documents/SD/sd2017-p4g1-b/engineHeist/registry/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy registry.ServerRegisterRemoteObject"

sleep 0.5
