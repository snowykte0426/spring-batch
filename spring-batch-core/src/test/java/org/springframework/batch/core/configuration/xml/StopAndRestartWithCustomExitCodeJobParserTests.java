/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.batch.core.configuration.xml;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Henning Pöttker
 */
@SpringJUnitConfig
class StopAndRestartWithCustomExitCodeJobParserTests extends AbstractJobParserTests {

	@Test
	void testStopIncomplete() throws Exception {

		//
		// First Launch
		//
		JobExecution jobExecution = createJobExecution();
		job.execute(jobExecution);
		assertEquals(1, stepNamesList.size());
		assertEquals("[s1]", stepNamesList.toString());

		assertEquals(BatchStatus.STOPPED, jobExecution.getStatus());
		assertEquals("CUSTOM", jobExecution.getExitStatus().getExitCode());

		StepExecution stepExecution1 = getStepExecution(jobExecution, "s1");
		assertEquals(BatchStatus.COMPLETED, stepExecution1.getStatus());
		assertEquals(ExitStatus.COMPLETED.getExitCode(), stepExecution1.getExitStatus().getExitCode());

		//
		// Second Launch
		//
		stepNamesList.clear();
		jobExecution = createJobExecution();
		job.execute(jobExecution);
		assertEquals(1, stepNamesList.size()); // step1 is not executed
		assertEquals("[s2]", stepNamesList.toString());

		assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
		assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());

		StepExecution stepExecution2 = getStepExecution(jobExecution, "s2");
		assertEquals(BatchStatus.COMPLETED, stepExecution2.getStatus());
		assertEquals(ExitStatus.COMPLETED, stepExecution2.getExitStatus());

	}

}
