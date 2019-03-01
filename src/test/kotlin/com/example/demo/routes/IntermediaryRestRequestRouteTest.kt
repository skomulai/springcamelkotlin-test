package com.example.demo.routes

 import org.apache.camel.CamelContext
import org.apache.camel.EndpointInject
import org.apache.camel.ProducerTemplate
import org.apache.camel.builder.AdviceWithRouteBuilder
import org.apache.camel.component.mock.MockEndpoint
import org.apache.camel.test.spring.CamelSpringBootRunner
import org.apache.camel.test.spring.MockEndpoints
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.apache.camel.builder.PredicateBuilder.isEqualTo
import org.apache.camel.builder.RouteBuilder
 import org.apache.camel.test.spring.CamelSpringTestSupport
 import org.springframework.context.annotation.Configuration
 import org.springframework.context.support.AbstractApplicationContext
import org.junit.Before




@SpringBootTest
@RunWith(CamelSpringBootRunner::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints
class IntermediaryRestRequestRouteTest {
    val inputPayload = "my message"
    val expectedRequestPayload = "number 5"
    val expectedOutputPayload = "number 5 and 6"

    @Autowired
    lateinit var camelContext: CamelContext

    @EndpointInject(uri = "{{rest.route.output2}}")
    lateinit var resultEndpoint: MockEndpoint

    @EndpointInject(uri = "{{rest.route.input}}")
    lateinit var producer: ProducerTemplate

    @Test
    fun testInOut() {
        camelContext.addRoutes(object : RouteBuilder() {
            @Throws(Exception::class)
            override fun configure() {
                from("{{rest.route.output1}}")
                        .to("mock:inputRequest")
                        .setBody(constant("number 5"))
                        .to("mock:testResponse")
            }
        })
        val intermediaryEndpoint1 = camelContext.getEndpoint("mock:inputRequest", MockEndpoint::class.java)
        val intermediaryEndpoint2 = camelContext.getEndpoint("mock:testResponse", MockEndpoint::class.java)


        camelContext.start()

        resultEndpoint.expectedMessageCount(1)
        resultEndpoint.expectedBodiesReceived(expectedOutputPayload)

        intermediaryEndpoint1.expectedMessageCount(1)
        intermediaryEndpoint1.expectedBodiesReceived(expectedRequestPayload)

        intermediaryEndpoint2.expectedMessageCount(1)
        intermediaryEndpoint2.expectedBodiesReceived(expectedOutputPayload)

        producer.sendBody(inputPayload)

        resultEndpoint.assertIsSatisfied()

    }
}