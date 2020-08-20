package io.chrisdima;

import io.vertx.core.Vertx;

/**
 * My Vertx App!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        System.out.println( "My Vertx App!" );
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new AVerticle());
    }
}
