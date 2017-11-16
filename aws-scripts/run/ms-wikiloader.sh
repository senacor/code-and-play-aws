#!/bin/bash

screen -dm -S ms-wikiloader bash -c 'java -jar -Dspring.profiles.active=lokal -Dserver.port=8080 /home/ec2-user/ms-wikiloader.jar; exec bash'
