#  Licensed to the Apache Software Foundation (ASF) under one or more
#  contributor license agreements.  See the NOTICE file distributed with
#  this work for additional information regarding copyright ownership.
#  The ASF licenses this file to You under the Apache License, Version 2.0
#  (the "License"); you may not use this file except in compliance with
#  the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

FROM azul/zulu-openjdk:latest

RUN apt update -y
RUN apt install -y python-setuptools git 
RUN apt install -y bison flex gcc g++ make libpcap-dev libc-dev

COPY . /ws/

WORKDIR /ws

# Install all dependencies first so they can get cached
# FIXME: this currently breaks for PLC4J: Examples: Hello-Webapp: Spring-Boot WAR -- hence || :
RUN ./mvnw -P with-java,with-cpp,with-boost,with-dotnet,with-python,with-proxies,with-sandbox dependency:go-offline || :

RUN apt install -y python

RUN ./mvnw -P with-java,with-cpp,with-boost,with-python,with-proxies,with-sandbox install
# the following fails just the same
# RUN ./mvnw install -P with-java  -DskipTests
