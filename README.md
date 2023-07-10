# AOS_Project1

## Group Members:  
Yisheng Zhang, Yifan Lin

## How to Compile

<p> Under project diretory, run command <br>
'javac *.java' or 'javac --release 8 *.java' </p>

## How to Run

<p> Main class is the entry point. <br>
For each node i (each terminal), run command: <br>
 'java Main <$node_id> <$path_to_config_file>' <br>
 where $node_id is id of current node, $path_to_config_file is path to config.txt on dc machine. <br>
 Note: config file must be a txt file</p>

 ## Output
 <p>For each node, it will output a '.out' file with each line corresponding to its local state when snapshot happended<br>
 The output file would be created at the same folder as config file</p>

 ## launcher.sh and cleanup.sh
 <p>Provided launcher script and clean up script are runned on MacOS<br>
 Modification may be needed to test on other system<br>
 Variables like $PROJDIR, $CONFIGLOCAL, $CONFIGREMOTE may need to be modified based on where files are located</p>
