/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.plc4x.protocol.df1;

import org.apache.plc4x.plugins.codegenerator.language.mspec.parser.MessageFormatParser;
import org.apache.plc4x.plugins.codegenerator.protocol.Protocol;
import org.apache.plc4x.plugins.codegenerator.types.definitions.ComplexTypeDefinition;
import org.apache.plc4x.plugins.codegenerator.types.exceptions.GenerationException;

import java.io.InputStream;
import java.util.Map;

public class Df1Protocol implements Protocol {

    @Override
    public String getName() {
        return "df1";
    }

    @Override
    public Map<String, ComplexTypeDefinition> getTypeDefinitions() throws GenerationException {
        InputStream schemaInputStream = Df1Protocol.class.getResourceAsStream("/protocols/df1/df1.mspec");
        if(schemaInputStream == null) {
            throw new GenerationException("Error loading message-format schema for protocol '" + getName() + "'");
        }
        return new MessageFormatParser().parse(schemaInputStream);
    }

}
