#!/bin/bash


# Change this to your netid
netid=yxz200005

#
# Root directory of your project
PROJDIR=$HOME/Documents/GitHub/AOS_Project1

#
# Directory where the config file is located on your local system
CONFIGLOCAL=$HOME/Documents/GitHub/AOS_Project1/config.txt

n=0

cat $CONFIGLOCAL | sed -e "s/#.*//" | sed -e "/^\s*$/d" |
(
    read i
    echo $i
    NumOfNodes=$( echo $i | awk '{ print $1 }' )
    while [[ $n -lt $NumOfNodes ]]
    do
    	read line
        host=$( echo $line | awk '{ print $2 }' )

        echo $host
        osascript -e 'tell app "Terminal"
        do script "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no '$netid@$host' killall -u '$netid'"
        end tell'
        #gnome-terminal -e "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no $netid@$host killall -u $netid" &
        sleep 1

        n=$(( n + 1 ))
    done
   
)


echo "Cleanup complete"
