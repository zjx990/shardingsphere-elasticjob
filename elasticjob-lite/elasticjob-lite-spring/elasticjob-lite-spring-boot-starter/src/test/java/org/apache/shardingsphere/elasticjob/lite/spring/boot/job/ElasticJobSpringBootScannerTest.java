/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.elasticjob.lite.spring.boot.job;

import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.lite.spring.boot.job.fixture.EmbedTestingServer;
import org.apache.shardingsphere.elasticjob.lite.spring.boot.job.fixture.job.impl.AnnotationCustomJob;
import org.apache.shardingsphere.elasticjob.lite.spring.core.scanner.ElasticJobScan;
import org.awaitility.Awaitility;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@ActiveProfiles("elasticjob")
@ElasticJobScan(basePackages = "org.apache.shardingsphere.elasticjob.lite.spring.boot.job.fixture.job.impl")
public class ElasticJobSpringBootScannerTest extends AbstractJUnit4SpringContextTests {
    
    @BeforeClass
    public static void init() {
        EmbedTestingServer.start();
        AnnotationCustomJob.reset();
    }
    
    @Test
    public void assertDefaultBeanNameWithTypeJob() {
        Awaitility.await().atMost(1L, TimeUnit.MINUTES).untilAsserted(() ->
                assertThat(AnnotationCustomJob.isCompleted(), is(true))
        );
        assertTrue(AnnotationCustomJob.isCompleted());
        assertNotNull(applicationContext);
        assertNotNull(applicationContext.getBean("annotationCustomJobSchedule", ScheduleJobBootstrap.class));
    }
}
