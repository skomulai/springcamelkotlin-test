package com.example.demo.routes

import org.apache.camel.Endpoint
import org.apache.camel.EndpointInject
import org.apache.camel.Exchange
import org.apache.camel.ExchangePattern
import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component


@Component
class IntermediaryRestRequestRoute : RouteBuilder() {

    @EndpointInject(uri = "{{rest.route.input}}")
    lateinit var fromid: Endpoint

    @EndpointInject(uri = "{{rest.route.output1}}")
    lateinit var toid1: Endpoint

    @EndpointInject(uri = "{{rest.route.output2}}")
    lateinit var toid2: Endpoint

    @Throws(Exception::class)
    override fun configure() {
        from(fromid)
            .id("reqres")
            .log("I'm in the Camel Route!\nMy body is: \${body}")
            .to(ExchangePattern.InOut, toid1)
            .to("log:after?showBody=true")
            .transform(body().append(" and 6"))
            .to("log:after?showBody=true")
            .to(toid2)
    }

}