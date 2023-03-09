Run docker-compose up to start the other services

Build docker file
`docker build -t integration-service-demo .`

Run docker container
`docker run -v "/home/robin/work/ohs-api-test (1)/ohs-api-test/bootstrap/integration":/app/files --network=intel_ohs integration-service-demo`

See dockerfile for environment variables needed to run outside a docker container 
