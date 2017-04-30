
#########################
# compile for server side
mkdir binServer
javac -d binServer -sourcepath src -cp genclass.jar src/ServerSide/ServerHeistMuseum.java 

# start grinformation
gnome-terminal -x sh -c "java -classpath binServer/ ServerSide.ServerHeistMuseum gri; bash"

# start museum
gnome-terminal -x sh -c "java -classpath binServer/ ServerSide.ServerHeistMuseum museum; bash"

# start control
gnome-terminal -x sh -c "java -classpath binServer/ ServerSide.ServerHeistMuseum control; bash"

# start conc
gnome-terminal -x sh -c "java -classpath binServer/ ServerSide.ServerHeistMuseum conc; bash"

# start assault party 1
gnome-terminal -x sh -c "java -classpath binServer/ ServerSide.ServerHeistMuseum agr1; bash"

# start assault party 2
gnome-terminal -x sh -c "java -classpath binServer/ ServerSide.ServerHeistMuseum agr2; bash"

#########################
# compile for client side
mkdir binClient
javac -d binClient -sourcepath src src/ClientSide/ClientHeistMuseum.java 

# start thieves
gnome-terminal -x sh -c "java -classpath binClient/ ClientSide.ClientHeistMuseum thief; bash"

# start master thief
gnome-terminal -x sh -c "java -classpath binClient/ ClientSide.ClientHeistMuseum master; bash"




