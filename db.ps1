# build.ps1

docker build -t my-postgres ./docker/.
docker rm -f postgres-db 2>$null
docker run -d --name postgres-db -p 5432:5432 my-postgres
