package munch.catalyst;

import catalyst.CatalystEngine;
import catalyst.CatalystModule;
import catalyst.data.DataModule;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.Test;

/**
 * Created By: Fuxing Loh
 * Date: 13/6/2017
 * Time: 6:24 PM
 * Project: munch-core
 */
class MunchCatalystTest {

    public static void main(String[] args) {
        System.setProperty("services.catalyst.data.url", "http://edge.corpus.munch.space:8200");
        // Token is injected with env

        Injector injector = Guice.createInjector(new TestModule());
        injector.getInstance(MunchCatalyst.class).run();
    }

    static class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            install(new CatalystModule());
            install(new DataModule());
            install(new EmptyClientModule());
            bind(CatalystEngine.class).to(MunchCatalyst.class);
        }
    }

    @Test
    void printer() throws Exception {
        String text = "com.fasterxml.jackson.core.JsonParseException: Unexpected character ('<' (code 60)): expected a valid value (number, String, array, object, 'true', 'false' or 'null')\n at [Source: java.io.ByteArrayInputStream@630662f2; line: 1, column: 2]\n\tat com.fasterxml.jackson.core.JsonParser._constructError(JsonParser.java:1702)\n\tat com.fasterxml.jackson.core.base.ParserMinimalBase._reportError(ParserMinimalBase.java:558)\n\tat com.fasterxml.jackson.core.base.ParserMinimalBase._reportUnexpectedChar(ParserMinimalBase.java:456)\n\tat com.fasterxml.jackson.core.json.UTF8StreamJsonParser._handleUnexpectedValue(UTF8StreamJsonParser.java:2689)\n\tat com.fasterxml.jackson.core.json.UTF8StreamJsonParser._nextTokenNotInObject(UTF8StreamJsonParser.java:878)\n\tat com.fasterxml.jackson.core.json.UTF8StreamJsonParser.nextToken(UTF8StreamJsonParser.java:772)\n\tat com.fasterxml.jackson.databind.ObjectMapper._initForReading(ObjectMapper.java:3834)\n\tat com.fasterxml.jackson.databind.ObjectMapper._readMapAndClose(ObjectMapper.java:3783)\n\tat com.fasterxml.jackson.databind.ObjectMapper.readTree(ObjectMapper.java:2321)\n\tat munch.restful.client.RestfulResponse.<init>(RestfulResponse.java:50)\n\tat munch.restful.client.RestfulRequest.executeResponse(RestfulRequest.java:227)\n\tat munch.restful.client.RestfulRequest.asResponse(RestfulRequest.java:217)\n\tat munch.restful.client.RestfulRequest.asResponse(RestfulRequest.java:205)\n\tat corpus.data.proxy.ProxyClient.forward(ProxyClient.java:49)\n\tat munch.restful.server.JsonRoute.handle(JsonRoute.java:30)\n\tat spark.ResponseTransformerRouteImpl$1.handle(ResponseTransformerRouteImpl.java:47)\n\tat spark.http.matching.Routes.execute(Routes.java:61)\n\tat spark.http.matching.MatcherFilter.doFilter(MatcherFilter.java:130)\n\tat spark.embeddedserver.jetty.JettyHandler.doHandle(JettyHandler.java:50)\n\tat org.eclipse.jetty.server.session.SessionHandler.doScope(SessionHandler.java:189)\n\tat org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:141)\n\tat org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:119)\n\tat org.eclipse.jetty.server.Server.handle(Server.java:517)\n\tat org.eclipse.jetty.server.HttpChannel.handle(HttpChannel.java:308)\n\tat org.eclipse.jetty.server.HttpConnection.onFillable(HttpConnection.java:242)\n\tat org.eclipse.jetty.io.AbstractConnection$ReadCallback.succeeded(AbstractConnection.java:261)\n\tat org.eclipse.jetty.io.FillInterest.fillable(FillInterest.java:95)\n\tat org.eclipse.jetty.io.SelectChannelEndPoint$2.run(SelectChannelEndPoint.java:75)\n\tat org.eclipse.jetty.util.thread.strategy.ExecuteProduceConsume.produceAndRun(ExecuteProduceConsume.java:213)\n\tat org.eclipse.jetty.util.thread.strategy.ExecuteProduceConsume.run(ExecuteProduceConsume.java:147)\n\tat org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:654)\n\tat org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:572)\n\tat java.lang.Thread.run(Thread.java:745)\n";

        System.out.println(text);
    }
}