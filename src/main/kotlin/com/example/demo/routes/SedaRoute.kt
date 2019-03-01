package com.example.demo.routes

import org.apache.camel.Endpoint
import org.apache.camel.EndpointInject
import org.apache.camel.Exchange
import org.apache.camel.ExchangePattern
import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component


@Component
class SedaRoute : RouteBuilder() {

    @EndpointInject(uri = "{{seda.route.input}}")
    lateinit var fromid: Endpoint

    @EndpointInject(uri = "{{seda.route.output1}}")
    lateinit var toid1: Endpoint

    @EndpointInject(uri = "{{seda.route.output2}}")
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