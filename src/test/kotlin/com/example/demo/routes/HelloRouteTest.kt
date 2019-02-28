package com.example.demo.routes

import org.apache.camel.test.spring.CamelSpringBootRunner
import org.apache.camel.test.spring.DisableJmx
import org.apache.camel.test.spring.MockEndpoints
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.annotation.DirtiesContext
import org.springframework.boot.test.context.SpringBootTest
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.EndpointInject
import org.apache.camel.ProducerTemplate
import org.apache.camel.CamelContext
import org.springframework.beans.factory.annotation.Autowired








@SpringBootTest
@RunWith(CamelSpringBootRunner::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisableJmx(true)
@MockEndpoints
class HelloRouteTest {

    val TEST_MESSAGE_PAYLOAD = "my message"

    @EndpointInject(uri = "{{output2}}")
    lateinit var resultEndpoint: MockEndpoint

    @EndpointInject(uri = "{{input}}")
    lateinit var producer: ProducerTemplate

    @Autowired
    val camelContext: CamelContext? = null

    @Test
    fun testInOut() {
        resultEndpoint!!.expectedMessageCount(1)
        resultEndpoint!!.message(0).equals(TEST_MESSAGE_PAYLOAD)
        producer!!.sendBody(TEST_MESSAGE_PAYLOAD)

        resultEndpoint.assertIsSatisfied()

    }
}