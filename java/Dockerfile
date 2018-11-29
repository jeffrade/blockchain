#https://spring.io/guides/gs/spring-boot-docker/
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.github.dvf.blockchain.Blockchain"]

#FROM python:3.6-alpine
#WORKDIR /app
# Install dependencies.
#ADD requirements.txt /app
#RUN cd /app && \
#    pip install -r requirements.txt
# Add actual source code.
#ADD blockchain.py /app
#EXPOSE 5000
#CMD ["python", "blockchain.py", "--port", "5000"]
