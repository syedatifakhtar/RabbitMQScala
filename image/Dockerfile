FROM rabbitmq:latest

ADD image/scripts/mqinit.sh /init.sh

RUN ["chmod", "+x", "init.sh"]

EXPOSE 15672

# Define default command
CMD ["/init.sh"]