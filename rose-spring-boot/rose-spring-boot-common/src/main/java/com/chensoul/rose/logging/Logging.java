/*
 * Copyright © 2025 Chensoul, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chensoul.rose.logging;

import lombok.Data;

@Data
public class Logging {

    private final Logstash logstash = new Logstash();

    private final Loki loki = new Loki();

    private boolean useJsonFormat = false;

    @Data
    public static class Loki {

        private String url = "http://localhost:3100/loki/api/v1/push";

        private String labelPattern = "application=${appName},host=${HOSTNAME},level=%level";

        private String messagePattern = "%level %logger{36} %thread | %msg %ex";
    }

    @Data
    public static class Logstash {

        private boolean enabled = false;

        private String host = "localhost";

        private int port = 5000;

        private int ringBufferSize = 512;
    }
}
