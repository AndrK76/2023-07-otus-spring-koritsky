docker-compose -p hw16 -fbuild-containers.yml  down
docker volume rm hw16-pg-data
docker network rm hw16-net
docker rmi hw16-front:0.0.1
docker rmi hw16-back:0.0.1

