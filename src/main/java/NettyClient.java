import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by A-Lugovskoy
 * <p>
 * Client side.
 */
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {

        // 4 threads enough for > 1000000 client.
        ChannelFactory channelFactory = new NioClientSocketChannelFactory(
                //Boss executors thread pool
                new OrderedMemoryAwareThreadPoolExecutor(1, 400000000,
                        2000000000, 60, TimeUnit.SECONDS),
                //Handler executors thread pool
                new OrderedMemoryAwareThreadPoolExecutor(1, 400000000,
                        2000000000, 60, TimeUnit.SECONDS));

        //Standard class in netty for simple open connect.
        ClientBootstrap bootstrap = new ClientBootstrap(channelFactory);

        //Set new ChannelPipelines
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new NettyClientHandler());
            }
        });

        //The result of an asynchronous Channel I/O operation.
        //connect to localhost:11111
        ChannelFuture future = bootstrap.connect(new InetSocketAddress( 11111));

        //.sync() - wait connect and getting channel if method .sync() won't throw exception(if connection will be open).
        Channel channel = future.sync().getChannel();

        //write our data to channel
        channel.write("Hello World");

        Thread.sleep(1000);

        //method .close() is async and we need await close connect and clear our resources.
        channel.close().await();
        //clear resources
        channelFactory.releaseExternalResources();
    }
}
