FROM confluentinc/cp-kafka-connect:6.2.0
USER root

RUN yum install -y unzip curl
RUN curl -LJO https://d1i4a15mxbxib1.cloudfront.net/api/plugins/confluentinc/kafka-connect-jdbc/versions/10.3.x/confluentinc-kafka-connect-jdbc-10.3.x.zip
RUN unzip -o /home/appuser/confluentinc-kafka-connect-jdbc-10.3.x.zip -d /etc/kafka-connect/plugins