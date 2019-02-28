package com.example.demo.routes

import org.apache.camel.Endpoint
import org.apache.camel.EndpointInject
import org.apache.camel.Exchange
import org.apache.camel.ExchangePattern
import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component


@Component
class HelloRoute : RouteBuilder() {

    @EndpointInject(uri = "{{input}}")
    lateinit var fromid: Endpoint

    @EndpointInject(uri = "{{output1}}")
    lateinit var toid1: Endpoint

    @EndpointInject(uri = "{{output2}}")
    lateinit var toid2: Endpoint

    @Throws(Exception::class)
    override fun configure() {
        from(fromid)
            .log("I'm in the Camel Route!\nMy body is: \${body}")
            .to(ExchangePattern.InOnly, toid1)
            .process { e -> e.getIn().body = "number 5" }
            .to("log:after?showBody=true")
            .to(toid2)

        from(toid1)
            .log("Luettu jonosta \${body}")
    }

}