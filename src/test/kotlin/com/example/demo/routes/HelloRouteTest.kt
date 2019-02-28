package com.example.demo.routes

import org.apache.camel.CamelContext
import org.apache.camel.Endpoint
import org.apache.camel.EndpointInject
import org.apache.camel.ProducerTemplate
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.test.spring.CamelSpringBootRunner
import org.apache.camel.test.spring.MockEndpoints
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.apache.camel.component.seda.SedaEndpoint
import org.springframework.beans.factory.annotation.Autowired


@SpringBootTest
@RunWith(CamelSpringBootRunner::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints
class HelloRouteTest {
    val inputPayload = "my message"
    val outputPayload = "number 5"

    @Autowired
    lateinit var camelContext: CamelContext

    @EndpointInject(uri = "{{output2}}")
    lateinit var resultEndpoint: MockEndpoint

//    @EndpointInject(uri = "{{output1}}")
//    lateinit var sedaEndpoint: SedaEndpoint

    @EndpointInject(uri = "{{input}}")
    lateinit var producer: ProducerTemplate

    @Test
    fun testInOut() {
        resultEndpoint.expectedMessageCount(1)
        resultEndpoint.expectedBodiesReceived(outputPayload)
        producer.sendBody(inputPayload)

        val sedaEndpoint = camelContext.getEndpoint("{{output1}}", SedaEndpoint::class.java)

        val size = sedaEndpoint.exchanges.size
        assertSame(1, size)

        resultEndpoint.assertIsSatisfied()

    }
}