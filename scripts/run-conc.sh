
#########################
# compile for server side


mkdir -p binServer
javac -d binServer -sourcepath src -cp genclass.jar src/ServerSide/ServerHeistMuseum.java > /dev/null
rm -r src &> /dev/null
sleep 0.2

# start grinformation
java -classpath binServer/ ServerSide.ServerHeistMuseum conc

#javac -d bin -sourcepath src src/registry/ServerRegisterRemoteObject.java 
