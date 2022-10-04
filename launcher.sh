#!/bin/bash

# Change this to your netid
netid=yxz200005

# Root directory of your project
PROJDIR=/home/011/y/yx/yxz200005/myProject

# Directory where the config file is located on your local system
CONFIGLOCAL=$HOME/Documents/GitHub/AOS_Project1/config.txt

# Directory where the config file is located on remote server(dc machine)
CONFIGREMOTE=$PROJDIR/config.txt

# Directory your java classes are in
BINDIR=$PROJDIR/bin

# Your main project class
PROG=Main

n=0

cat $CONFIGLOCAL | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    echo $i
    NumOfNodes=$( echo $i | awk '{ print $1 }' )
    while [[ $n -lt $NumOfNodes ]]
    do
    	read line
    	p=$( echo $line | awk '{ print $1 }' )
        host=$( echo $line | awk '{ print $2 }' )
        echo $p
        echo $host
	
	    osascript -e 'tell app "Terminal"
        do script "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no '$netid@$host' java -cp '$BINDIR' '$PROG' '$n' '$CONFIGREMOTE'; '$SHELL'"
        end tell'
        #gnome-terminal -e "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host java -cp $BINDIR $PROG $n $CONFIGREMOTE; exec bash" &

        n=$(( n + 1 ))
    done
)
