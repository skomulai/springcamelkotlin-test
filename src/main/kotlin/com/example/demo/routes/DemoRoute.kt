package com.example.demo.routes

import org.apache.camel.EndpointInject
import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component


@Component
class HelloRoute : RouteBuilder() {

    @EndpointInject(uri = "{{input}}")
    lateinit var fromid: String

    @EndpointInject(uri = "{{output1}}")
    lateinit var toid1: String

    @EndpointInject(uri = "{{output2}}")
    lateinit var toid2: String

    @Throws(Exception::class)
    override fun configure() {
        from(fromid)
            .log("I'm in the Camel Route!")
            .to(toid2)
    }

}