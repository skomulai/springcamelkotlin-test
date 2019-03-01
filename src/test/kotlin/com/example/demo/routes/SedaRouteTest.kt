package com.example.demo.routes

import org.apache.camel.CamelContext
import org.apache.camel.EndpointInject
import org.apache.camel.ProducerTemplate
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.component.seda.SedaEndpoint
import org.apache.camel.test.spring.CamelSpringBootRunner
import org.apache.camel.test.spring.MockEndpoints
import org.junit.Assert.assertSame
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext


@SpringBootTest
@RunWith(CamelSpringBootRunner::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints
class SedaRouteTest {
    val inputPayload = "my message"
    val outputPayload = "number 5"

    @Autowired
    lateinit var camelContext: CamelContext

    @EndpointInject(uri = "{{seda.route.output2}}")
    lateinit var resultEndpoint: MockEndpoint

    @EndpointInject(uri = "{{seda.route.input}}")
    lateinit var producer: ProducerTemplate

    @Test
    fun testInOut() {
        resultEndpoint.expectedMessageCount(1)
        resultEndpoint.expectedBodiesReceived(outputPayload)
        producer.sendBody(inputPayload)

        val sedaEndpoint = camelContext.getEndpoint("{{seda.route.output1}}", SedaEndpoint::class.java)

        val size = sedaEndpoint.exchanges.size
        assertSame(1, size)

        resultEndpoint.assertIsSatisfied()

    }
}