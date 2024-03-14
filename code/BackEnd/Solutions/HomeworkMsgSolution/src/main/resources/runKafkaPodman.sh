#Detached
podman run -dt --name kafka-podman -p 9092:9092 -e LOG_DIR=/tmp/logs docker.io/bitnami/kafka:3.6 /bin/sh -c 'export CLUSTER_ID=$(/opt/bitnami/kafka/bin/kafka-storage.sh random-uuid) && /opt/bitnami/kafka/bin/kafka-storage.sh format -t $CLUSTER_ID -c /opt/bitnami/kafka/config/kraft/server.properties && /opt/bitnami/kafka/bin/kafka-server-start.sh /opt/bitnami/kafka/config/kraft/server.properties'

#Interactive
#podman run -it --name kafka-podman -p 9092:9092 -e LOG_DIR=/tmp/logs docker.io/bitnami/kafka:3.6 /bin/sh -c 'export CLUSTER_ID=$(/opt/bitnami/kafka/bin/kafka-storage.sh random-uuid) && /opt/bitnami/kafka/bin/kafka-storage.sh format -t $CLUSTER_ID -c /opt/bitnami/kafka/config/kraft/server.properties && /opt/bitnami/kafka/bin/kafka-server-start.sh /opt/bitnami/kafka/config/kraft/server.properties'
