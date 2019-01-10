#!/bin/bash
tmux new-session -d -s scouting '/home/pi/rscout2018/serverScripts/node_modules/forever/bin/forever start /home/pi/rscout2018/serverScripts/index.js'
