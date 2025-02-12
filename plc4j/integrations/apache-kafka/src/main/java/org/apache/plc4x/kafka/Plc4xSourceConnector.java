/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/
package org.apache.plc4x.kafka;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.apache.plc4x.kafka.config.Job;
import org.apache.plc4x.kafka.config.JobReference;
import org.apache.plc4x.kafka.config.Source;
import org.apache.plc4x.kafka.config.SourceConfig;
import org.apache.plc4x.kafka.exceptions.ConfigurationException;
import org.apache.plc4x.kafka.util.VersionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Plc4xSourceConnector extends SourceConnector {

    private static final Logger log = LoggerFactory.getLogger(Plc4xSourceConnector.class);

    private static final ConfigDef CONFIG_DEF = new ConfigDef();

    private SourceConfig sourceConfig;

    @Override
    public void start(Map<String, String> props) {
        try {
            sourceConfig = SourceConfig.fromPropertyMap(props);
        } catch (ConfigurationException e) {
            log.error("Error processing source configuration", e);
        }
    }

    @Override
    public void stop() {
        sourceConfig = null;
    }

    @Override
    public Class<? extends Task> taskClass() {
        return Plc4xSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        // Initially we planned to have the simple assumption that one task maps to one PLC connection.
        // But we could easily say that one scraper instance maps to a task and one scraper task can
        // process multiple PLC connections. But I guess this would be an optimization as we have to
        // balance the load manually.
        if(sourceConfig.getJobs().size() > maxTasks) {
            // not enough tasks
            log.warn("NOT ENOUGH TASKS!");
            return Collections.emptyList();
        }

        // For each configured source we'll start a dedicated scraper instance collecting
        // all the scraper jobs enabled for this source.
        List<Map<String, String>> configs = new LinkedList<>();
        for (Source source : sourceConfig.getSources()) {
            // Build a list of job configurations only containing the ones referenced from
            // the current source.
            StringBuilder query = new StringBuilder();
            for (JobReference jobReference : source.getJobReferences()) {
                Job job = sourceConfig.getJob(jobReference.getName());
                if(job == null) {
                    log.warn(String.format("Couldn't find referenced job '%s'", jobReference.getName()));
                } else if(jobReference.isEnabled()) {
                    query.append(",").append(jobReference.getName()).append("|").append(jobReference.getTopic());
                    query.append("|").append(job.getInterval());
                    for (Map.Entry<String, String> field : job.getFields().entrySet()) {
                        String fieldName = field.getKey();
                        String fieldAddress = field.getValue();
                        query.append("|").append(fieldName).append("#").append(fieldAddress);
                    }
                }
            }

            // Create a new task configuration.
            Map<String, String> taskConfig = new HashMap<>();
            taskConfig.put(Plc4xSourceTask.CONNECTION_NAME_CONFIG, source.getName());
            taskConfig.put(Plc4xSourceTask.PLC4X_CONNECTION_STRING_CONFIG, source.getConnectionString());
            taskConfig.put(Plc4xSourceTask.QUERIES_CONFIG, query.toString().substring(1));
            configs.add(taskConfig);
        }
        return configs;
    }

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }

    @Override
    public String version() {
        return VersionUtil.getVersion();
    }

}
