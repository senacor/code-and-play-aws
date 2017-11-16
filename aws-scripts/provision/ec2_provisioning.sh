#!/bin/bash

yum install java-1.8.0-openjdk -y
yum erase java-1.7.0-openjdk -y

aws s3 cp s3://code-and-play-aws /home/ec2-user/ --recursive
chmod +x /home/ec2-user/*.sh

echo "@reboot sh /home/ec2-user/ms-wikiloader.sh" > crontab.txt && crontab crontab.txt

/home/ec2-user/ms-wikiloader.sh