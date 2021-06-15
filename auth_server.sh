fnc=$1
red=`tput setaf 1`
reset=`tput sgr0`
green=`tput setaf 2`
project="dpa_authorization_server.jar"
if [ "$fnc" = "status" ]
then
        process=$(ps axf | grep $project | grep -v grep | wc -l)
        if [ $process -gt 0 ]
        then
                echo "$project is ${green}UP${reset}"
        else
                echo "$project is ${red}DOWN${reset}"
        fi
        exit
fi
if [ "$fnc" = "start" ]
then
        echo -n "Starting..."
  
        process=$(ps axf | grep $project | grep -v grep | wc -l)
         
        if [ $process -gt 0 ]
        then
            echo "$project is already ${green}UP${reset} and ${green}running${reset}"
        else
            nohup java -jar $project >/dev/null 2>&1 &
	    process=$(ps axf | grep $project | grep -v grep | awk '{print $1}')
	    echo "$project was ${green}started${reset} with the following ${green}PID $process${reset}"
        fi
        exit
fi
if [ "$fnc" = "restart" ]
then
        echo "Restarting"
	echo -n "Stopping..."
        ps axf | grep $project | grep -v grep | awk '{print "kill -15 " $1}' | sh
	echo "${green}OK${reset}"
	echo -n "Starting..."
        nohup java -jar $project >/dev/null 2>&1 &
	process=$(ps axf | grep $project | grep -v grep | awk '{print $1}')
        echo "$project was ${green}started${reset} with the following PID $process"
        exit
fi
if [ "$fnc" = "stop" ]
then
        echo -n "Stopping..."
        ps axf | grep $project | grep -v grep | awk '{print "kill -15 " $1}' | sh
	echo "${green}OK${reset}"
	echo "$project is ${red}DOWN${reset}"
        exit
fi
