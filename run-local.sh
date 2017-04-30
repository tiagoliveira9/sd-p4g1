
#########################
# compile for server side
mkdir binServer
javac -d binServer -sourcepath src -cp genclass.jar src/ServerSide/ServerHeistMuseum.java 

# start grinformation
gnome-terminal -x sh -c "java -classpath binServer/ ServerSide.ServerHeistMuseum gri; bash"
sleep 0.3
# start museum
gnome-terminal -x sh -c "java -classpath binServer/ ServerSide.ServerHeistMuseum museum; bash"
sleep 0.3
# start control
gnome-terminal -x sh -c "java -classpath binServer/ ServerSide.ServerHeistMuseum control; bash"
sleep 0.3
# start conc
gnome-terminal -x sh -c "java -classpath binServer/ ServerSide.ServerHeistMuseum conc; bash"
sleep 0.3
# start assault party 1
gnome-terminal -x sh -c "java -classpath binServer/ ServerSide.ServerHeistMuseum agr1; bash"
sleep 0.3
# start assault party 2
gnome-terminal -x sh -c "java -classpath binServer/ ServerSide.ServerHeistMuseum agr2; bash"
sleep 0.3
#########################
# compile for client side
mkdir binClient
javac -d binClient -sourcepath src src/ClientSide/ClientHeistMuseum.java 
sleep 0.3
# start thieves
gnome-terminal -x sh -c "java -classpath binClient/ ClientSide.ClientHeistMuseum thief; bash"
sleep 0.3
# start master thief
gnome-terminal -x sh -c "java -classpath binClient/ ClientSide.ClientHeistMuseum master; bash"




