
#########################
# compile for server side


mkdir binServer &> /dev/null
javac -d binServer -sourcepath src -cp genclass.jar src/ServerSide/ServerHeistMuseum.java > /dev/null
rm -r src &> /dev/null
sleep 0.2

# start grinformation
java -classpath binServer/ ServerSide.ServerHeistMuseum control

