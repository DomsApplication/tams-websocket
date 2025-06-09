########## linux/86_64 arch ##########
# docker build -t shavika-websocket:latest .
# docker ps OR docker container ls
# docker run -d --name shavika-websocket -p 8999:8999 shavika-websocket:latest

########## linux/amd64,linux/arm64 arch ##########
####QEMU Installation: Ensure QEMU is installed for cross-platform builds. On Docker Desktop, this is typically already set up. On Linux, you can install it using:
#docker run --rm --privileged multiarch/qemu-user-static --reset -p yes
####Enable Buildx and Create a Builder If you haven't set up a Buildx builder, run the following
#docker buildx create --use --name mybuilder || docker buildx use mybuilder
#docker buildx inspect --bootstrap
####Build Multi-Architecture Image Use the docker buildx build command to build the image for multiple architectures:
#docker buildx build --platform linux/amd64,linux/arm64 --tag shavika-websocket:latest --push .
####Local Testing (Optional) If you want to test the built image locally without pushing to a registry, use --load instead of --push. However, --load only works for the architecture of your current system:
#docker buildx build --platform linux/arm64 --tag shavika-websocket:latest --load .
####Run the Container Run the built image:
#docker run -d --name shavika-websocket -p 8999:8999 shavika-websocket:latest
#docker run -d --name shavika-websocket --network=skmc-network -p 8999:8999 shavika-websocket:latest


########## aws ECR tag & push  ##########
###Retrieve an authentication token and authenticate your Docker client to your registry. Use the AWS TOOLS for PowerShell:
#aws ecr get-login-password --region <region> | docker login --username AWS --password-stdin <aws-account>.dkr.ecr.<region>.amazonaws.com
###Build your Docker image using the following command.
#docker buildx build --platform linux/amd64,linux/arm64 --tag <aws-account>.dkr.ecr.<region>.amazonaws.com/skmc:shavika-websocket_2024_12_29_08_45_20 --push .

########## Digital Ocean - Container Repository  ##########
# https://docs.digitalocean.com/products/container-registry/getting-started/quickstart/#push-to-your-registry
#doctl registry login
#docker tag shavika-websocket registry.digitalocean.com/tams-image-repo/shavika-websocket
#docker push registry.digitalocean.com/tams-image-repo/shavika-websocket

FROM amazoncorretto:17-alpine3.15-jdk
RUN apk update && apk upgrade -U -a

RUN mkdir -m777 /etc/shavika-websocket
ADD start-service.sh /etc/shavika-websocket
ADD target/shavika-websocket-standalone-1.0.0.jar /etc/shavika-websocket
RUN chmod +x /etc/shavika-websocket/start-service.sh
WORKDIR /etc/shavika-websocket

ENV JVM_XMX=4096m
EXPOSE 8999
# CMD sh start-service.sh
CMD ["sh", "start-service.sh"]