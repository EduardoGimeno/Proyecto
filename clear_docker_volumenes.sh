#!/bin/bash

sudo docker-compose stop
sudo docker stop $(sudo docker ps -aq)
sudo docker rm $(sudo docker ps -aq)

sudo docker volume rm proyecto_postgis-data
