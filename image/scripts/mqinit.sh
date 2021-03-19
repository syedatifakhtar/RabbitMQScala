#!/bin/sh

# Create Rabbitmq user
(
rabbitmq-plugins enable rabbitmq_management
rabbitmq-plugins enable rabbitmq_amqp1_0
sleep 30 ; \
rabbitmqctl add_user admin admin ; \
rabbitmqctl set_user_tags admin administrator ; \
rabbitmqctl set_permissions -p / admin  ".*" ".*" ".*" ; \
echo "*** User 'user' with password 'password' completed. ***" ; \
echo "*** Log in the WebUI at port 15672 (example: http:/localhost:15672) ***") &

# $@ is used to pass arguments to the rabbitmq-server command.
# For example if you use it like this: docker run -d rabbitmq arg1 arg2,
# it will be as you run in the container rabbitmq-server arg1 arg2
# shellcheck disable=SC2068
rabbitmq-server $@
